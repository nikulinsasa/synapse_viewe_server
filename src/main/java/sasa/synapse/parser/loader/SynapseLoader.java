package sasa.synapse.parser.loader;

import java.io.File;
import java.util.Iterator;

import org.w3c.dom.Document;

import sasa.synapse.parser.AbstractSynapseParser;


public class SynapseLoader {

	public static final int PROXIES = 0;
	public static final int SEQUENCES = 1;
	public static final int ENDPOINTS = 2;
	public static final int API = 3;
	public static final int TASKS = 4;

	private String[] folders = { "proxy-services", "sequences", "endpoints", "api", "tasks" };
	private String[] synapseElements = { "proxy", "sequence", "endpoint", "api", "task" };
	

	private SynapseContainer[] containers = new SynapseContainer[5];

	public void load(String path) throws LoaderException {
		File file = new File(path);
		if (!file.isDirectory()) {
			throw new LoaderException(file.getPath() + " is not a directory");
		}

		for (int i = 0; i < 5; i++) {
			containers[i] = new SynapseContainer(new AbstractSynapseParser(synapseElements[i]));
			Iterator<File> _conetntFiles = (new DirectoryLoader(path + "/" + folders[i])).loadDirectory()
					.getFilesInDirecory();
			while (_conetntFiles.hasNext()) {
				try{
					Document doc = (new FileLoader(_conetntFiles.next())).load();
					if(doc!=null){
						containers[i].add(doc);
					}
				}catch(LoaderException le){
					le.printStackTrace();
				}
			}
		}
	}

	public SynapseContainer getProxy() {
		return containers[PROXIES];
	}

	public SynapseContainer getSequencies() {
		return containers[SEQUENCES];
	}

	public SynapseContainer getApi() {
		return containers[API];
	}

	public SynapseContainer getEndpoints() {
		return containers[ENDPOINTS];
	}

	public SynapseContainer getTasks() {
		return containers[TASKS];
	}

}
