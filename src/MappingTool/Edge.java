/*
Alonso Martinez
*/
package MappingTool;


//an edge is the connection between two nodes
public class Edge{
	public double cost; //an edge has a cost (the distance) between the two nodes
	public Node connection; //the connection node is what the node connects to  


	//constructor for edge 
	public Edge(Node connection, double cost){
		this.connection = connection;
		this.cost = cost;
	}
	
	public static double getDistance(Node one, Node two){

		double dx = one.xPos - two.xPos;

		double dy = one.yPos - two.yPos;

		double distance = Math.sqrt(dx*dx + dy*dy);


		return distance;
	}
	
	
	public Node returnConnection(){
		return connection;
	}
}
