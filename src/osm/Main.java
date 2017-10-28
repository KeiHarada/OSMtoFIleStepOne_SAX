package osm;

import java.io.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.sun.jmx.snmp.Timestamp;

import parser.XMLParse;

public class Main {

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
		
		/* start */
		System.out.println(new Timestamp(System.currentTimeMillis()).toString());
        
        /* preparing XML parser */
		File input = new File(args[0]+".osm");
		SAXParserFactory spfactory = SAXParserFactory.newInstance();
		SAXParser parser = spfactory.newSAXParser();
		XMLParse xmlParse = new XMLParse(args[0]);
		parser.parse(input, xmlParse);
        System.out.println("all finished!!");
        
        /* finished */
		System.out.println(new Timestamp(System.currentTimeMillis()).toString());
	}
}
	
