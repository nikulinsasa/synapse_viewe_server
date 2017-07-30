package sasa.synapse.parser.loader;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.w3c.dom.Document;

public class FileLoaderTest {

	@Test
	public void test() throws LoaderException {

		FileLoader fl = new FileLoader(new File("tests/default/proxy-services/proxy1.xml"));

		Document proxy = fl.load();

		assertTrue(proxy.getElementsByTagName("proxy") != null);

		assertEquals("StockQuoteProxy1",
				proxy.getFirstChild().getAttributes().getNamedItem("name").getNodeValue());

	}

}
