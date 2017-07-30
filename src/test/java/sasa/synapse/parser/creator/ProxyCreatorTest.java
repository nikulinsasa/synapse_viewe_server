package sasa.synapse.parser.creator;

import static org.junit.Assert.*;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sasa.synapse.parser.loader.LoaderException;
import sasa.synapse.parser.loader.SynapseLoader;

public class ProxyCreatorTest {

	@Test
	public void test() throws LoaderException, TransformerException, XPathExpressionException {
		SynapseLoader sl = new SynapseLoader();
		sl.load("tests/default");
		
		ProxyCreator creator = new ProxyCreator(sl.getProxy().get("StockQuoteProxy2"));
		Node node = creator.create(sl.getSequencies());
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList list = (NodeList)xPath.evaluate("//sequence[@key='main']", 
				node.getOwnerDocument().getDocumentElement(), XPathConstants.NODESET);
		
		assertEquals(1, list.getLength());
		int countNode = 0;
		for(int i=0,count=list.item(0).getChildNodes().getLength();i<count;i++){
			if(list.item(0).getChildNodes().item(i).getNodeType()==Node.ELEMENT_NODE){
				countNode++;
			}
		}
		assertEquals("in",list.item(0).getChildNodes().item(1).getNodeName());
		assertEquals(2,countNode);
		assertEquals("out",list.item(0).getChildNodes().item(3).getNodeName());
		
		list = (NodeList)xPath.evaluate("//sequence[@key='stockquote']", 
				node.getOwnerDocument().getDocumentElement(), XPathConstants.NODESET);
		
		assertEquals(1, list.getLength());
		assertEquals("log",list.item(0).getChildNodes().item(3).getNodeName());
	}

}
