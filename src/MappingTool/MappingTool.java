package MappingTool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;
import java.awt.Graphics;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

/**
Alonso
 */
public class MappingTool extends JLabel {






	//list of nodes
	static List<Node> mapNodes = new ArrayList<Node>();
	static List<Node> tempNodes = new ArrayList<Node>();
	static List<Node> bestPath = new ArrayList<Node>();
	static List<Node> finalNodes = new ArrayList<Node>();
	static List<Node> edgeNodes = new ArrayList<Node>();






	//ints
	int mouseX = 0;
	int mouseY = 0;
	int mouseX1 = 0;
	int mouseY1 = 0;
	int mouseX2 = 0;
	int mouseY2 = 0;
	int firstNodeLoc = 0;
	int secondNodeLoc = 0;
	static int scaleX;
	static int scaleY;
	int nodeNumber;
	int cellHeight;
	int cellWidth;
	int adjustedMouseX;
	int adjustedMouseY;
	int imageWidth;
	int imageHeight;
	int CELLSX = 60;
	int CELLSY = 40;
	int[][] grid2;
	static int numberClicks;

	//strings
	String path;



	//booleans
	static boolean shouldShowGrid;
	static boolean drawPath = false;
	static boolean calcPath = false;
	static boolean addingEdge = false;
	static boolean addingNode = false;
	static boolean shouldRedraw = false;
	static boolean renderEdges = false;
	static boolean showNodeEdges = false;
	static boolean renderEdge = false;
	boolean doOnce= true;


	//map components
	static BufferedImage img;
	static JFrame frame = new JFrame();
	static private JPanel contentPane;
	static boolean imageLoaded = false;
	static JButton btnLoadMap = new JButton("Load Map");
	private final static  JButton btnAddNode = new JButton("Add Node");
	private final  static JButton btnAddEdge = new JButton("Add Edge");
	private final static JButton btnSave = new JButton("Save");
	private final static JButton btnDeleteNode = new JButton("Delete Node");
	private final static JButton btnShowNodeEdges = new JButton("Show Edges");
	private final static JButton btnRunButton = new JButton("Run A*");
	private final  static JButton btnShowGrid = new JButton("Show Grid");
	private final static JLabel lblMap = new JLabel();
	private static final long serialVersionUID = 1L;






	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(imageLoaded){

			imageWidth = img.getWidth();
			imageHeight = img.getHeight();


			if (img != null) {
				System.out.println("Setting cell width and height");
				int cellHeight = (int) (getHeight() / CELLSY); 
				int cellWidth = (int) (getWidth() / CELLSX);
				scaleX = cellWidth;
				scaleY = cellHeight;
				Rectangle[][] grid = new Rectangle[getWidth()][getHeight()];


				if(doOnce == true){
					for (int x = 0; x < imageWidth; x++) {
						for (int y = 0; y < imageHeight; y++) {
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
							if(shouldShowGrid){
								grid[x][y] = new Rectangle(x,y,cellWidth,cellHeight);
								g.setColor(Color.black);
								g.drawRect(grid[x][y].x,grid[x][y].y , cellWidth, cellHeight);


							}
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
					drawPath = false;
					calcPath = false;
				}



				if(renderEdge){
						int x1 = mapNodes.get(firstNodeLoc).xPos*scaleX+cellWidth/2;
						int y1 = mapNodes.get(firstNodeLoc).yPos*scaleY+cellHeight/2;
						int x2 = mapNodes.get(secondNodeLoc).xPos*scaleX+cellWidth/2;
						int y2 = mapNodes.get(secondNodeLoc).yPos*scaleY+cellHeight/2;
						System.out.println(x1);
						System.out.println(y1);
						System.out.println(x2);
						System.out.println(y2);

						g.setColor(Color.ORANGE);
						g.drawLine(x1,y1,x2,y2);
					}
					renderEdge = false;
				}



				if(renderEdges){
					System.out.println("Rendering Edges");

					System.out.println(firstNodeLoc);
					System.out.println(mapNodes.get(firstNodeLoc).connectioNodes);
					for(int i = 0; i < mapNodes.get(firstNodeLoc).connectioNodes.size();i++){

						System.out.println(cellWidth);
						System.out.println(cellHeight);


						int x1 = (mapNodes.get(firstNodeLoc).xPos*scaleX)+(cellWidth/2);
						int y1 = (mapNodes.get(firstNodeLoc).yPos*scaleY)+(cellHeight/2);
						int x2 = (mapNodes.get(firstNodeLoc).connectioNodes.get(i).xPos*scaleX)+(cellWidth/2);
						int y2 = (mapNodes.get(firstNodeLoc).connectioNodes.get(i).yPos*scaleY)+(cellHeight/2);
			
						System.out.println(x1);
						System.out.println(y1);
						System.out.println(x2);
						System.out.println(y2);

						g.setColor(Color.GREEN);
						g.drawLine(x1,y1,x2,y2);

					}

					renderEdges = false;
					showNodeEdges = false;
				}





			}
		}
	













	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

					frame.getContentPane().add(new MappingTool());
					frame.getContentPane().add(contentPane);
					frame.setSize(800, 328);

					frame.setLocationRelativeTo(null);
					//frame.add(btnLoadMap);
					//frame.pack();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setTitle("Randy Advanced Mapping Peripheral");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}







	/**
	 * Create the frame.
	 */
	public MappingTool() {











		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,328);
		//setLocationRelativeTo(null);
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		//	setContentPane(contentPane);
		contentPane.setLayout(null);
		System.out.println("Second");



		btnLoadMap.setBounds(661, 11, 113, 23);

		contentPane.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) { 
				if(imageLoaded){






					if(showNodeEdges){

						mouseX1 = (int)me.getX();
						mouseY1 = (int) me.getY();
						mouseX1 = setAlignedMouseX(mouseX1,imageWidth,CELLSX);
						mouseY1 = setAlignedMouseY(mouseY1,imageHeight,CELLSY);

						for(int i =0; i<mapNodes.size();i++){
							if(mapNodes.get(i).xPos == mouseX1 && mapNodes.get(i).yPos == mouseY1){

								System.out.println("Worked");
								renderEdges = true;
								firstNodeLoc = i;
								repaint();
							} else {
								System.out.println("Didnt Work");
								;

							}
						}





					}





					if(calcPath){




						if(numberClicks ==0){

							mouseX1 = (int)me.getX();
							mouseY1 = (int) me.getY();
							mouseX1 = setAlignedMouseX(mouseX1,imageWidth,CELLSX);
							mouseY1 = setAlignedMouseY(mouseY1,imageHeight,CELLSY);

							for(int i =0; i<mapNodes.size();i++){
								if(mapNodes.get(i).xPos == mouseX1 && mapNodes.get(i).yPos == mouseY1){
									firstNodeLoc = i;
									numberClicks += 1;	
									System.out.println("Worked");

								} else {
									System.out.println("*******************");
									System.out.println(mapNodes.get(i).xPos);
									System.out.println(mapNodes.get(i).yPos);
									System.out.println(mouseX1);
									System.out.println(mouseY1);
									System.out.println("*******************");


								}
							}

						} else if(numberClicks == 1) {
							mouseX2 = (int)me.getX();
							mouseY2 = (int) me.getY();
							mouseX2 = setAlignedMouseX(mouseX2,imageWidth,CELLSX);
							mouseY2 = setAlignedMouseY(mouseY2,imageHeight,CELLSY);
							for(int i =0; i<mapNodes.size();i++){
								if(mapNodes.get(i).xPos == mouseX2 && mapNodes.get(i).yPos == mouseY2){
									secondNodeLoc = i;

									numberClicks = 2;
									System.out.println("Worked");

								} else {
									System.out.println("*******************");
									System.out.println(mapNodes.get(i).xPos);
									System.out.println(mapNodes.get(i).yPos);
									System.out.println(mouseX1);
									System.out.println(mouseY1);
									System.out.println("*******************");
								}
							}
						}












						if(numberClicks > 1){

							Settings defaultSettings = new Settings(false, false, false);
							AStar astar = new AStar(mapNodes, defaultSettings);

							bestPath = astar.findPath(mapNodes.get(firstNodeLoc), mapNodes.get(secondNodeLoc));
							System.out.println(bestPath);
							drawPath = true;
							repaint();
						}

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
						System.out.println(mapNodes);
						//System.out.println(mapNodes.get(i).xPos);

						System.out.println(imageWidth);
						System.out.println(imageHeight);
						repaint();
					}
					else if(addingEdge){
						boolean shouldMakeEdge = false;
						addingNode = false;
						showNodeEdges = false;
						//numberClicks = 0;



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

							tempNodes.get(0).connectioNodes.add(tempNodes.get(1));
							edgeNodes.add(tempNodes.get(0));
							edgeNodes.add(tempNodes.get(1));


							tempNodes.get(1).neighbors = new Edge[]{
									new Edge(tempNodes.get(0),Edge.getDistance(tempNodes.get(1),tempNodes.get(0))),

							};
							tempNodes.get(1).connectioNodes.add(tempNodes.get(0));

							System.out.println("*******");
							System.out.println(firstNodeLoc);
							System.out.println(secondNodeLoc);
							System.out.println("*******");
							
							mapNodes.remove(firstNodeLoc);
							mapNodes.add(firstNodeLoc, tempNodes.get(0));
							mapNodes.remove(secondNodeLoc);
							mapNodes.add(secondNodeLoc, tempNodes.get(1));
							renderEdge = true;
							repaint();
							tempNodes.clear();
							System.out.println("Edges generated");
							numberClicks = 0;
							//firstNodeLoc = 0;
							//secondNodeLoc = 0;


						}






					}
				}
			} 
		}); 


		btnLoadMap.setFocusable(false);
		btnLoadMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int offset = 50;
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					path = selectedFile.getAbsolutePath();
					path = path+"\\";
					try {
						img = ImageIO.read(new File(Paths.get(path+"map.png").toString()));
						int width = img.getWidth();
						int height = img.getHeight();
						grid2 = new int[img.getWidth()][img.getHeight()];

						File csvNodeFile =(new File(path+"node.csv")); 
						File csvEdgeFile =(new File(path+"edge.csv")); 
						System.out.println(path);
						setIcon(new ImageIcon(img));
						setBounds(0,0,img.getWidth(),img.getHeight());
						//contentPane.add(lblMap);
						frame.setSize(img.getWidth()+200,img.getHeight());
						btnLoadMap.setBounds(img.getWidth()+10, 11, 113+offset, 23);
						btnAddNode.setBounds(img.getWidth()+10, 45, 113+offset, 23);
						btnAddEdge.setBounds(img.getWidth()+10, 79, 113+offset, 23);
						btnDeleteNode.setBounds(img.getWidth()+10, 113, 113+offset, 23);
						btnShowNodeEdges.setBounds(img.getWidth()+10, 147, 113+offset, 23);
						btnRunButton.setBounds(img.getWidth()+10, 181, 113+offset, 23);
						btnShowGrid.setBounds(img.getWidth()+10, 215, 113+offset, 23);
						btnSave.setBounds(img.getWidth()+10, 249, 113+offset, 23);

						frame.setLocationRelativeTo(null);


						frame.revalidate();
						repaint();
						revalidate();
						frame.repaint();
						imageLoaded = true;
					} catch (IOException ex) {
						Logger.getLogger(LoadMap.class.getName()).log(Level.SEVERE, null, ex);

					}
				}
			}
		});



		contentPane.add(btnLoadMap);

		btnAddNode.setBounds(661, 45, 113, 23);
		btnAddNode.setFocusable(false);
		btnAddNode.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				addingEdge = false;
				addingNode= true;
				calcPath = false;
			}
		});
		contentPane.add(btnAddNode);

		btnAddEdge.setBounds(661, 79, 113, 23);
		btnAddEdge.setFocusable(false);
		btnAddEdge.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {

				addingEdge = true;
				addingNode= false;
				numberClicks = 0;
				calcPath = false;
			}          
		});

		JButton add = new JButton("Add Nodes");
		add.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				addingEdge = false;
				addingNode= true;
				calcPath = false;
			}          
		});
		contentPane.add(btnAddEdge);

		btnDeleteNode.setFocusable(false);
		btnDeleteNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});


		btnDeleteNode.setBounds(661, 113, 113, 23);
		btnDeleteNode.setFocusable(false);
		contentPane.add(btnDeleteNode);

		btnShowNodeEdges.setBounds(661, 147, 113, 23);
		btnShowNodeEdges.setFocusable(false);
		contentPane.add(btnShowNodeEdges);
		btnShowNodeEdges.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				calcPath = false;
				addingNode = false;
				addingEdge = false;
				showNodeEdges = true;

			}          
		});

		btnRunButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				calcPath = true;
				addingEdge = false;
				addingNode = false;
				System.out.println("Printing path");



			}
		});
		btnRunButton.setBounds(661, 181, 113, 23);
		btnRunButton.setFocusable(false);
		contentPane.add(btnRunButton);
		btnSave.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {

				saveMapNodes(Paths.get(path).toString()+"\\mapNodes.csv");
				saveMapEdges(Paths.get(path).toString()+"\\mapEdges.csv");
				saveMapScale(Paths.get(path).toString()+"\\mapScale.csv");


			}          
		});
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(0, 0, 4, 22);
		contentPane.add(textArea);

		btnSave.setBounds(661, 249, 113, 23);
		btnSave.setFocusable(false);
		contentPane.add(btnSave);


		btnShowGrid.setBounds(661, 215, 113, 23);
		btnShowGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				shouldShowGrid= !shouldShowGrid;
				frame.repaint();
			}
		});
		btnShowGrid.setFocusable(false);
		contentPane.add(btnShowGrid);



		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e)
		{}





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
	
	private static void saveMapScale(String fileName){

		try
		{
			FileWriter writer = new FileWriter(fileName);


				writer.append(Integer.toString(scaleX));
				writer.append(",");
				writer.append(Integer.toString(scaleY));

			
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 
	}
}
