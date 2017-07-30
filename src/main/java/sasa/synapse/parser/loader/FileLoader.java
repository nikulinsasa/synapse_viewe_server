package sasa.synapse.parser.loader;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FileLoader {

	private File file;

	public FileLoader(File file) {
		setFile(file);
	}
	
	public Document load() throws LoaderException {
		
		if(!file.exists()){
			throw new LoaderException("a file "+file.getAbsolutePath()+" was not found");
		}
		if(file.getName().substring(file.getName().length()-4).equals(".xml")){
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				return dBuilder.parse(file);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				throw new LoaderException("File "+file.getName()+" is not xml");
			}
		}else{
			return null;
		}
		
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	
}
