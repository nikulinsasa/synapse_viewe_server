package sasa.synapse.parser.loader;

import static org.junit.Assert.*;

import org.junit.Test;

public class SynapseLoaderTest {

	@Test
	public void test() throws LoaderException {
		SynapseLoader sl = new SynapseLoader();
		sl.load("tests/default");
		assertTrue(sl.getProxy().get("StockQuoteProxy1") != null);
		assertTrue(sl.getProxy().get("StockQuoteProxy2").getOwnerDocument().getElementsByTagName("target")!=null);
		
		assertTrue(sl.getProxy().get("StockQuoteProxy2").getOwnerDocument().getElementsByTagName("in")!=null);
		
		assertTrue(sl.getProxy().get("StockQuoteProxy3").getOwnerDocument().getElementsByTagName("in")!=null);
		
	}

}
