package MappingTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import junit.framework.TestCase;

public class TestAStar extends TestCase{
	@Test
	public void testConstructorOfAStarWithNoParameters(){
		AStar astar = new AStar();
		assertEquals(astar.getSettings(), null);
	}
	@Test
	public void testConstructorOfAStarWithParameters(){
		AStar astar = new AStar();
		List<Node> map = new ArrayList<Node>();
		Node n1 = new Node(0, 0, "A");
		Node n2 = new Node(1, 0, "B");
		Node n3 = new Node(2, 0, "C");
		map.add(n1);
		map.add(n2);
		map.add(n3);
		Settings defaultSettings = new Settings(false, false, false);
		astar.setSettings(defaultSettings);
		AStar astar_new = new AStar(map,defaultSettings);
		assertEquals(astar.getSettings(), astar_new.getSettings());
	}
	@Test
	public void testCreatingEdgesWhenRunningAStar(){
		AStar astar = new AStar();
		List<Node> map = new ArrayList<Node>();
		Node n1 = new Node(0, 0, "A");
		Node n2 = new Node(1, 0, "B");
		Node n3 = new Node(2, 0, "C");
		Edge[] edges1={new Edge(n2,1)};
		Edge[] edges2={new Edge(n1,1), new Edge(n3,1)};
		Edge[] edges3={new Edge(n2,1)};
		n1.neighbors=edges1;
		n2.neighbors=edges2;
		n3.neighbors=edges3;
		map.add(n1);
		map.add(n2);
		map.add(n3);
		Settings defaultSettings = new Settings(false, false, false);
		astar.setSettings(defaultSettings);
		AStar astar_new = new AStar(map,defaultSettings);
		
		List<Node>bestPath=astar.findPath(n1, n3);
		List<Node>testPath=Arrays.asList(n1,n2,n3);
		
		assertEquals(testPath,bestPath);
	}
	
	
}
