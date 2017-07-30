package sasa.synapse.parser;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import sasa.synapse.parser.loader.LoaderException;
import sasa.synapse.parser.loader.SynapseLoader;

public class BinderSynapseElementsStorage extends AbstractBinder {

	private String path = "tests/default";
	
	public BinderSynapseElementsStorage(){}
	
	public BinderSynapseElementsStorage(String path){
		this.path = path;
	}
	
	@Override
	protected void configure() {
		SynapseLoader sl = new SynapseLoader();
		try {
			sl.load(path);
			bind(sl).to(SynapseLoader.class);
		} catch (LoaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
