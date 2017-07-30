package sasa.synapse.parser.creator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sasa.synapse.parser.loader.SynapseContainer;

public class ProxyCreator {

	private Node proxy;

	public ProxyCreator(Node proxy) {
		this.proxy = proxy;
	}

	public Node create(SynapseContainer sequenceContainer) {
		Document doc = proxy.getOwnerDocument();
		
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

}
