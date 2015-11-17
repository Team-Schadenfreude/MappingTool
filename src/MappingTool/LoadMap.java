package MappingTool;
import java.awt.Color;
/**
Alonso
 */
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LoadMap extends JLabel{


	private static final long serialVersionUID = 1L;
	private static final int CELLSX = 60;
	private static final int CELLSY = 40;
	boolean shouldRedraw = false;
	int mouseX;
	int mouseY;
	int adjustedMouseX;
	int adjustedMouseY;

	BufferedImage img;
	int nodeWidth;
	int nodeHeight;
	int imageWidth;
	int imageHeight;
	boolean doOnce= true;
	static int numberClicks;
	int[][] grid2;
	static boolean addingEdge = false;
	static boolean addingNode = true;
	static List<Node> mapNodes = new ArrayList<Node>();
	List<Node> tempNodes = new ArrayList<Node>();
	static List<Node> bestPath = new ArrayList<Node>();
	static List<Node> testPath = new ArrayList<Node>();
	static List<Node> finalNodes = new ArrayList<Node>();
	static List<Node> edgeNodes = new ArrayList<Node>();


	List<Node> map = new ArrayList<Node>();
	static Node n1 = new Node(0, 0, "A");
	static Node n2 = new Node(1, 0, "B");
	static Node n3 = new Node(2, 0, "C");
	static boolean drawPath = false;
	static boolean calcPath = false;
	int firstNodeLoc = 0;
	int secondNodeLoc = 0;
	int scaleX;
	int scaleY;
	int nodeNumber;



	public LoadMap() {
		try {
			img = ImageIO.read(new File("C://Users/Alonso/Desktop/StrattonHall2.png"));
			setIcon(new ImageIcon(img));
			grid2 = new int[img.getWidth()][img.getHeight()];

		} catch (IOException ex) {
			Logger.getLogger(LoadMap.class.getName()).log(Level.SEVERE, null, ex);
		}

		addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) { 

				if(calcPath){
					testPath.add(n1);
					testPath.add(n2);
					testPath.add(n3);

					System.out.println("Here");
					System.out.println(mapNodes);
					Settings defaultSettings = new Settings(false, false, false);
					AStar astar = new AStar(mapNodes, defaultSettings);
					int last = mapNodes.size()-1;

					bestPath = astar.findPath(mapNodes.get(0), mapNodes.get(mapNodes.size()-1));
					System.out.println(bestPath);
					drawPath = true;
					repaint();

				}



				if(addingNode){
					//System.out.println("Location  X: "+ me.getX()+ " Location Y: " + me.getY());
					mouseX = (int)me.getX();
					mouseY = (int) me.getY();
					System.out.println("ADDING NODE");
					adjustedMouseX = setAlignedMouseX(mouseX,imageWidth,CELLSX);
					adjustedMouseY = setAlignedMouseY(mouseY,imageHeight,CELLSY);
					mapNodes.add(new Node(("node"+nodeNumber),0,0,0,false,adjustedMouseX,adjustedMouseY,"Node" +nodeNumber));				
					nodeNumber++;
					shouldRedraw = true;
					//System.out.println(mapNodes.get(i).xPos);
					System.out.println("***");

					System.out.println(imageWidth);
					System.out.println(imageHeight);
					System.out.println("***");

					repaint();
				}
				else if(addingEdge){
					boolean shouldMakeEdge = false;
					//numberClicks = 0;
					int mouseX1 = 0;
					int mouseY1 = 0;
					int mouseX2 = 0;
					int mouseY2 = 0;


					if(numberClicks ==0){

						mouseX1 = (int)me.getX();
						mouseY1 = (int) me.getY();
						mouseX1 = setAlignedMouseX(mouseX1,imageWidth,CELLSX);
						mouseY1 = setAlignedMouseY(mouseY1,imageHeight,CELLSY);
						for(int i =0; i<mapNodes.size();i++){
							if(mapNodes.get(i).xPos == mouseX1 && mapNodes.get(i).yPos == mouseY1){
								System.out.println("Worked 1");
								System.out.println("Found at: " +i);
								tempNodes.add(mapNodes.get(i));
								firstNodeLoc = i;

							} else {
								System.out.println("Didnt Work");
							}
						}
						numberClicks = 1;

					} else if(numberClicks == 1) {
						System.out.println(tempNodes.size());

						mouseX2 = (int)me.getX();
						mouseY2 = (int) me.getY();
						mouseX2 = setAlignedMouseX(mouseX2,imageWidth,CELLSX);
						mouseY2 = setAlignedMouseY(mouseY2,imageHeight,CELLSY);
						numberClicks = 3;
						for(int i =0; i<mapNodes.size();i++){
							if(mapNodes.get(i).xPos == mouseX2 && mapNodes.get(i).yPos == mouseY2){
								System.out.println("Worked 2");
								System.out.println("Found at: " +i);

								tempNodes.add(mapNodes.get(i));
								secondNodeLoc = i;
							} else {
								System.out.println("Didnt Work");
							}
						}
					}

					System.out.println("***1****");
					System.out.println(firstNodeLoc);
					System.out.println(secondNodeLoc);
					System.out.println("***1****");

					if(tempNodes.size() == 2){
						tempNodes.get(0).neighbors = new Edge[]{
								new Edge(tempNodes.get(1),Edge.getDistance(tempNodes.get(0),tempNodes.get(1))),
						};
						edgeNodes.add(tempNodes.get(0));
						edgeNodes.add(tempNodes.get(1));


						tempNodes.get(1).neighbors = new Edge[]{
								new Edge(tempNodes.get(0),Edge.getDistance(tempNodes.get(1),tempNodes.get(0))),

						};
						System.out.println("*******");
						System.out.println(firstNodeLoc);
						System.out.println(secondNodeLoc);
						System.out.println("*******");

						mapNodes.remove(firstNodeLoc);
						mapNodes.add(firstNodeLoc, tempNodes.get(0));
						mapNodes.remove(secondNodeLoc);
						mapNodes.add(secondNodeLoc, tempNodes.get(1));
						tempNodes.clear();
						System.out.println("Edges generated");


					}







				}
			} 
		}); 


	}


	private static void saveMapNodes(String fileName){

		try
		{
			FileWriter writer = new FileWriter(fileName);


			for (int i = 0; i<mapNodes.size();i++){
				System.out.println(mapNodes.get(i).nodeName);
				writer.append(mapNodes.get(i).nodeName);
				writer.append(',');
				writer.append(Integer.toString(mapNodes.get(i).xPos));
				writer.append(',');
				writer.append(Integer.toString(mapNodes.get(i).yPos));
				writer.append(',');
				writer.append(mapNodes.get(i).description);
				writer.append("\n");
			}
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 
	}

	private static void saveMapEdges(String fileName){

		try
		{
			FileWriter writer = new FileWriter(fileName);


			for (int i = 0; i<edgeNodes.size();i+=2){
				writer.append(Integer.toString(edgeNodes.get(i).xPos));
				writer.append(',');
				writer.append(Integer.toString(edgeNodes.get(i).yPos));
				writer.append(',');
				writer.append(Integer.toString(edgeNodes.get(i+1).xPos));
				writer.append(',');
				writer.append(Integer.toString(edgeNodes.get(i+1).yPos));
				writer.append("\n");
			}
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		imageWidth = img.getWidth();
		imageHeight = img.getHeight();
		if (img != null) {
			int cellHeight = (int) (getHeight() / CELLSY); 
			int cellWidth = (int) (getWidth() / CELLSX);
			scaleX = cellWidth;
			scaleY = cellHeight;
			Rectangle[][] grid = new Rectangle[getWidth()][getHeight()];


			if(doOnce == true){
				for (int x = 0; x < imageWidth-100; x++) {
					for (int y = 0; y < imageHeight-100; y++) {
						grid2[x][y] = 0;
					}	
				}
			}

			doOnce = false;


			if(shouldRedraw){
				g.setColor(Color.red);
				grid2[adjustedMouseX*cellWidth][adjustedMouseY*cellHeight] = 1;
				//System.out.println(grid2[adjustedMouseX*cellWidth][adjustedMouseY*cellHeight]);
				grid[adjustedMouseX*cellWidth][adjustedMouseY*cellHeight] = new Rectangle(adjustedMouseX*cellWidth, adjustedMouseY*cellHeight, cellWidth, cellHeight);
				g.fillRect(grid[adjustedMouseX*cellWidth][adjustedMouseY*cellHeight].x,grid[adjustedMouseX*cellWidth][adjustedMouseY*cellHeight].y, cellWidth, cellHeight);
				shouldRedraw = false;
				//System.out.println(adjustedMouseX*cellWidth);
				//System.out.println(adjustedMouseY*cellHeight);

			}	
			for (int y = 0; y < getHeight(); y+=cellHeight) {
				for (int x = 0; x < getWidth(); x+=cellWidth) {

					if(grid2[x][y] !=0){
						g.setColor(Color.red);
						g.fillRect(x,y, cellWidth, cellHeight);

					} else {
						grid[x][y] = new Rectangle(x,y,cellWidth,cellHeight);
						g.setColor(Color.black);
						g.drawRect(grid[x][y].x,grid[x][y].y , cellWidth, cellHeight);
					}
				}
			}

			if(drawPath){
				System.out.println("Recoloring");
				g.setColor(Color.blue);




				for(int i =0; i <bestPath.size()-1;i++){
					int x1 = bestPath.get(i).xPos *scaleX;
					int y1 = bestPath.get(i).yPos *scaleY;
					int x2 = bestPath.get(i+1).xPos *scaleX;
					int y2 = bestPath.get(i+1).yPos *scaleY;
					g.drawLine(x1+cellWidth/2,y1+cellHeight/2,x2+cellWidth/2,y2+cellHeight/2 );
				}



			}
		}
	}



	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			



				JButton addEdge = new JButton("Add Edge");
				


				JButton save = new JButton("Save");


				save.setActionCommand("SAVE");

				save.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent arg0) {

						saveMapNodes("C://Users/Alonso/Desktop/mapNodes.csv");
						saveMapEdges("C://Users/Alonso/Desktop/mapEdges.csv");


					}          
				});
				JButton delete = new JButton("Delete Node");
				delete.setActionCommand("DELETE");
				delete.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent arg0) {

						calcPath = true;
						addingEdge = false;
						addingNode = false;
						System.out.println("Printing path");


					}          
				});
				JFrame frame = new JFrame();
				JPanel wrapperPanel = new JPanel(new GridBagLayout());
				wrapperPanel.add(new LoadMap());
				wrapperPanel.add(delete);
				wrapperPanel.add(addEdge);
				wrapperPanel.add(save);
				frame.add(wrapperPanel);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	public int setAlignedMouseX(int mousex,int imageWidth,int cells){
		int scale = imageWidth/cells;

		float mousexholder = mousex/scale;
		int actualMouseX = (int)Math.floor(mousexholder);
		return actualMouseX;

	}

	public int setAlignedMouseY(int mousey,int imageHeight,int cells){
		int scale = imageHeight/cells;

		float mousexholder = mousey/scale;
		int actualMouseY = (int)Math.floor(mousexholder);
		return actualMouseY;

	}


}
