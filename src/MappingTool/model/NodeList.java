package MappingTool.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import MappingTool.Node;

public class NodeList {
	private HashMap<String, Node> nodes;

	public NodeList() {
		nodes = new HashMap<String, Node>();
	}
	
	public HashMap<String, Node> getListOfNodes(){
		return nodes;
	}
	public Collection<String> getNames(){
		return nodes.keySet();
	}
	
	public boolean delete(String s){
		return nodes.remove(s)!=null;
	}
	
	public int getSize() {
		return nodes.size();
	}

	// right now very inefficient. We could use some sorting algorithm...
	public boolean addNode(String s, Node n) {
		return (nodes.put(s,n)==n);		
	}
	public void addNode(Node n){
		String name = n.nodeName;
		addNode(name,n);
	}

	public boolean deleteNode(String name) {
		return (nodes.remove(name) != null);
	}
	
	public Node findNode(String name){
		return nodes.get(name);
	}

	public Node get(String s) {
		return nodes.get(s);
	}
	
	public Set<String> getKeys(){
		return nodes.keySet();
	}
	
/*	public Node findNode (int x, int y){	
		return null;
	}

	public boolean addNodes(Node... nodes) {
		return false;
	}

	public boolean deleteNodes(String... names) {
		return false;
	}
*/
}