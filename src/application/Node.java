/*
Alonso Martinez
*/

package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Circle;

//class for node. A node is a point on the grid that contains an x and y position
public class Node{
	private String nodeName; //every node has a name
    private Node parent; //node has a parent. the parent is where the current node came from
    private double gValue; //gValue is cost from current block
    private double hValue; //hValue is distance
    private double fValue; //fValue is overall cost
    private int xPos; //every Node has an X pos
    private int yPos; // y position of node
	private int zPos;
    private Boolean isObstacle; //variable to see if the node is an obstacle
    private String description;
	private List<Node> neighbors = new ArrayList<Node>();
	private String map;
	private boolean isTransitionNode = false;
	private String type;
    //constructor for node
    public Node(String nodeName, double hValue,double gValue,double fValue, Boolean isObstacle, int xPos, int yPos,String description){
            this.nodeName = nodeName;
    		this.hValue = hValue;
            this.gValue = gValue;
            this.isObstacle = isObstacle;
            this.xPos = xPos;
            this.yPos = yPos;
            this.description = description;
            this.map = map;
            this.zPos = zPos;
    }
    public Node(String nodeName,int xPos, int yPos,int zpos, String map,String description,String type)
    {
    	this.nodeName = nodeName;
    	this.hValue = 0;
        this.gValue = 0;
        this.isObstacle = false;
        this.xPos = xPos;
        this.yPos = yPos;
        this.map = map;
        this.description=  description;
        this.type = type;
    }

    //function to turn stringName into an actual string name.
    //Why do we have to do this? Because Java is stupid. JK, Java Master Race
    public String toString(){
            return description;
    }
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Node> getNeighbors() {
		return neighbors;
	}
	public void setNeighbors(List<Node> neighbors) {
		this.neighbors = neighbors;
	}
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		this.map = map;
	}
	public boolean isTransitionNode() {
		return isTransitionNode;
	}
	public void setTransitionNode(boolean isTransitionNode) {
		this.isTransitionNode = isTransitionNode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getzPos() {
		return zPos;
	}
	public void setzPos(int zPos) {
		this.zPos = zPos;
	}
    
  
    
    

}
