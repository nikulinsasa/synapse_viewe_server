package sasa.synapse.parser.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class DirectoryLoader {

	private File file;
	
	private ArrayList<File> filesInDirecory;
	
	public DirectoryLoader(String path) throws LoaderException{
		file = new File(path);
		
		filesInDirecory = new ArrayList<File>();
	}
	
	public DirectoryLoader loadDirectory() {
		if(file.isDirectory()){
			loadDirectory(file);
		}
		return this;
	}
	
	public void loadDirectory(File _file) {
		File[] files = _file.listFiles();
		for(File item: files){
			if(item.isDirectory()){
				loadDirectory(item);
			}else{
				filesInDirecory.add(item);
			}
		}
	}

	public Iterator<File> getFilesInDirecory() {
		return filesInDirecory.iterator();
	}

}
