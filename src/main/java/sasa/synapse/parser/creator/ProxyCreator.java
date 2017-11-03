package sasa.synapse.parser.creator;

import java.util.HashMap;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sasa.synapse.parser.loader.SynapseContainer;

public class ProxyCreator {

	private Node proxy;

	private HashMap<String, Integer> usedSequencies = new HashMap<>();
	
	public ProxyCreator(Node proxy) {
		this.proxy = proxy;
	}

	/**
	 * Создаем используемые зависимости в proxy
	 * @param sequenceContainer
	 * @return
	 */
	public Node create(SynapseContainer sequenceContainer) {
		Document doc = proxy.getOwnerDocument();
		
		Node inSequncy = proxy.getAttributes().getNamedItem("inSequence");
		Node outSequncy = proxy.getAttributes().getNamedItem("outSequence");
		Node faultSequence = proxy.getAttributes().getNamedItem("faultSequence");
		if(inSequncy!=null) {
			appendSequency(doc, "inSequence", inSequncy);
		}
		if(outSequncy!=null) {
			appendSequency(doc, "outSequncy", outSequncy);
		}
		if(faultSequence!=null) {
			appendSequency(doc, "faultSequence", faultSequence);
		}
		
		boolean repeat;
		do {
			repeat = false;
			NodeList list = doc.getElementsByTagName("sequence");
			for (int i = 0, count = list.getLength(); i < count; i++) {
				Node item = list.item(i);
				Node key = item.getAttributes().getNamedItem("key");
				if (key != null && item.getFirstChild() == null) {
					repeat = true;
					String name = key.getNodeValue();
					addSqeuency(name);
					if(sequenceContainer.get(name)!=null){
						NodeList innerSequence = sequenceContainer.get(name).getChildNodes();
						for (int j = 0, countTags = innerSequence.getLength(); j < countTags; j++) {
							item.appendChild(doc.importNode(innerSequence.item(j), true));
						}
					}else{
						Node emptyNode = doc.createTextNode("CONCAT SEQUENCE");
						item.appendChild(emptyNode);
					}
				}
			}
		} while (repeat);

		return proxy;
	}

	private void appendSequency(Document doc, String name, Node source) {
		Node inSequencyForProxy = doc.createElement(name);
		Node sequency = doc.createElement("sequency");
		Attr attr = doc.createAttribute("key");
		attr.setValue(source.getNodeValue());
		sequency.appendChild(attr);
		inSequencyForProxy.appendChild(sequency);
	}
	
	private void addSqeuency(String name) {
		
		if(usedSequencies.get(name)==null){
			usedSequencies.put(name, new Integer(0));
		}
		usedSequencies.put(name,usedSequencies.get(name)+1);
	}
	
	/**
	 * используемые последовательности
	 * @return
	 */
	public HashMap<String, Integer> getUsedSequencies() {
		return usedSequencies;
	}
	
}
