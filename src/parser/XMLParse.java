package parser;

import java.io.*;
import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParse extends DefaultHandler {
	
	String filename;
	public long currentNode = 0;
	
	/* constructor */
	public XMLParse(String filename){
		this.filename = filename;
	}
	
	/* @over ride */
	public void startDocument() {
        System.out.println("start parsing the osm file ...");
    }
	
	/* @over ride */
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
    	try {
			BufferedWriter nodeBW1 = new BufferedWriter(new FileWriter(new File(filename+"_nodes.txt"),true));
	        BufferedWriter nodeBW2 = new BufferedWriter(new FileWriter(new File("temp_nodes.txt"),true));
	        BufferedWriter edgeBW = new BufferedWriter(new FileWriter(new File("temp_edges.txt"),true));
        
	        /* 1.case of node*/
	        if(qName.equals("node")) {
	        	nodeBW1.write(Long.toString(currentNode)+"\t"+Double.parseDouble(attributes.getValue("lat"))+"\t"+Double.parseDouble(attributes.getValue("lon"))+System.getProperty("line.separator"));
	        	nodeBW2.write(Long.toString(currentNode)+"\t"+Long.parseLong(attributes.getValue("id"))+"\t"+Double.parseDouble(attributes.getValue("lat"))+"\t"+Double.parseDouble(attributes.getValue("lon"))+System.getProperty("line.separator"));
	        	currentNode++;
	        }
	        
	        /* 2.case of way */
	        if(qName.equals("way")){
	        	edgeBW.write("-"+System.getProperty("line.separator"));
	        }
	        
	        /* 3.case of way node */
	        if(qName.equals("nd")){
	        	edgeBW.write(Long.parseLong(attributes.getValue("ref"))+System.getProperty("line.separator"));
	        }
	        
	        nodeBW1.close();
	        nodeBW2.close();
	        edgeBW.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
    }
    
    /* @over ride */
    public void endDocument(){
    	System.out.println("complete parsing the osm file!!");
    	System.out.println("exporting to output files ...");
    	try{
			exportingFile();
		}catch (IOException e){
			e.printStackTrace();
		}
    }
    
    public void exportingFile() throws IOException{
    	BufferedReader edgeBR = new BufferedReader(new FileReader(new File("temp_edges.txt")));  
        BufferedWriter edgeBW = new BufferedWriter(new FileWriter(new File(filename+"_edges.txt")));
        
        String searchString, line;
        String[] tokens = new String[4];
        String[] prev = new String[4];
        long count = 0;
        
        while((searchString = edgeBR.readLine()) != null){
        	if(searchString.equals("-")){
        		count = 0;
        	}else{
            	BufferedReader nodeBR = new BufferedReader(new FileReader(new File("temp_nodes.txt")));
        		while((line = nodeBR.readLine()) != null){
            		StringTokenizer st = new StringTokenizer(line,"\t");
            		for(int i=0;i<4;i++){
            			tokens[i] = st.nextToken();
            		}
            		if(searchString.equals(tokens[1])){
            			if(count > 0){ edgeBW.write(prev[0]+"\t"+tokens[0]+"\t"+dist(prev[2],tokens[2],prev[3],tokens[3])+System.getProperty("line.separator")); }
            			System.arraycopy(tokens,0,prev,0,4);
            			break;
            		}
        		}
        		count++;
                nodeBR.close();
        	}
        }
        
        edgeBR.close();
        edgeBW.close();
    }
    
	/* Calculates distance from node to node using longitude and latitude. */
    public String dist(String x1, String x2, String y1, String y2){
    	double lat1 = Double.parseDouble(x1);
    	double lat2 = Double.parseDouble(x2);
    	double lon1 = Double.parseDouble(y1);
    	double lon2 = Double.parseDouble(y2);
    	
    	double theta = lon1-lon2;
		double dist = Math.sin(deg2rad(lat1))*Math.sin(deg2rad(lat2))+Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return Double.toString(dist * 1000.0);
    }
    
	/* Converts Decimal degrees to Radians */
	public static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	
	/* Converts Radians to Decimal degrees */
	public static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
}

