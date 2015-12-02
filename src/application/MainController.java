package application;
/**
Alonso
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;


import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.transform.Scale;

public class MainController implements Initializable {

	static List<Node> mapNodes = new ArrayList<Node>();
	static List<Node> mapNodes2 = new ArrayList<Node>();

	static List<Node> map1TransitionNodes = new ArrayList<Node>();
	static List<Node> map2TransitionNodes = new ArrayList<Node>();
	static List<Node> edgeNodes = new ArrayList<Node>();


	@FXML
	private Pane pane = new Pane();

	private BufferedImage img;

	private Image image;
	@FXML
	private ImageView mapView = new ImageView();

	@FXML
	private Label name = new Label();

	@FXML
	private Label description = new Label();


	@FXML
	private TextField nodeName = new TextField();

	@FXML
	private TextField nodeDescription = new TextField();

	StackPane stack = new StackPane();
	@FXML
	private Canvas imageCanvas = new Canvas();

	@FXML
	private Button genSupermap = new Button();

	@FXML
	private  Button loadMap1 = new Button();

	@FXML
	private ComboBox<Node> map1Dropdown = new ComboBox();

	private MenuItem addEdge = new MenuItem("Add Edge");

	private MenuItem deleteEdge = new MenuItem("Delete Edge");

	private MenuItem showEdges = new MenuItem("Show Edges");

	private MenuItem addNode = new MenuItem("Add Node");

	private MenuItem deleteNode = new MenuItem("Delete Node");
	@FXML
	private Button loadMap2 = new Button();

		@FXML
	    private ScrollPane scrollImage = new ScrollPane();

	@FXML
	private ComboBox<Node> map2Dropdown = new ComboBox();

	@FXML
	private CheckBox isTransitionCheckbox = new CheckBox();

	@FXML
	private Button makeTransButton = new Button();

	@FXML
	private MenuButton nodeOptions = new MenuButton();

	private MenuItem modifyNode = new MenuItem("Modify Node");

	@FXML
	private MenuButton edgeOptions = new MenuButton();

	private int imageWidth = 0;
	private int imageHeight = 0;
	private int duplicateNode = 1;
	private int firstNodeLoc = -1;
	private int secondNodeLoc =  -1;
	public static boolean mapLoaded = false;
	private volatile int numberClicks = 0;
	private boolean doOnce = true;
	private int currentNodeLoc = -1;
	private String nodeMapName;
	private static String path;
	private static boolean shouldAddNode = false;
	private static boolean shouldDeleteNode = false;
	private static boolean shouldAddEdge = false;
	private static boolean shouldDeleteEdge = false;
	private static boolean shouldModifyNode = false;
	private static boolean shouldShowEdges = false;
	private static boolean shouldMakeEdge = false;
	private static boolean shouldRemoveEdge = false;
	static boolean oneSelected = false;
	static boolean twoSelected = false;
	Node currentNode;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		pane.setStyle("-fx-background-color: #205080;");


		nodeOptions.getItems().remove(0);
		nodeOptions.getItems().remove(0);
		edgeOptions.getItems().remove(0);
		edgeOptions.getItems().remove(0);

		map1Dropdown.setOnAction(new EventHandler(){

			@Override
			public void handle(Event arg0) {

			}

		});

		addNode.setOnAction(new EventHandler(){

			@Override
			public void handle(Event event) {
				
				shouldAddEdge = false;
				shouldDeleteEdge = false;
				shouldShowEdges = false;
				shouldAddNode = true;
				shouldDeleteNode = false;
				nodeOptions.setText("Adding Node");
				edgeOptions.setText("Edge Options");
				if(shouldAddNode){
					imageCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							if(shouldAddNode){
								System.out.println("In Add");
								System.out.println(mapNodes);
								shouldDeleteNode = false;
								System.out.println("GetSceneCoords");
								System.out.println(event.getY());
								System.out.println(event.getX());
								System.out.println("GetSceneCoords");



								GraphicsContext gc = imageCanvas.getGraphicsContext2D();
								//imageCanvas.getGraphicsContext2D().clearRect(0, 0, imageCanvas.getWidth(), imageCanvas.getHeight());
								if(doOnce){
									if(mapNodes.size() ==0){
										System.out.println("Creating new node at " +event.getX()+" "+event.getY());
										Node node = new Node("node",(int)event.getX()-5,(int)event.getY()-5,0,nodeMapName,"");
										mapNodes.add(node);
										clearCanvas();
										renderEverything();
										System.out.println(mapNodes);

										doOnce = false;
										System.out.println("Zero map Node");
									}
								} else{

									for(int i = 0; i < mapNodes.size();i++){
										if(mapNodes.get(i).xPos >=event.getX()-5-30&& mapNodes.get(i).xPos <=event.getX()-5+30 && mapNodes.get(i).yPos>=event.getY()-5-30 && mapNodes.get(i).yPos<=event.getY()-5+30){
											System.out.println("Node already exists here");
											System.out.println("********1**********");
											System.out.println(mapNodes.get(i).xPos);
											System.out.println(mapNodes.get(i).yPos);
											System.out.println("Counter is: "+i);
											System.out.println(event.getX()-5);
											System.out.println(event.getY()-5);
											System.out.println("********1**********");
											duplicateNode++;
										}
									}
								}
								if(duplicateNode == 0){
									System.out.println("In duplicate node");
									Node node = new Node("node",(int)event.getX()-5,(int)event.getY()-5,0,nodeMapName,"");
									mapNodes.add(node);
									renderEverything();
									System.out.println(mapNodes);
								} else {
									duplicateNode = 0;
								}





							}



						}
					});		
				} else {
					System.out.println("Not clicking");
				}
			}
		});

		deleteNode.setOnAction(new EventHandler(){

			@Override
			public void handle(Event event) {

				shouldAddEdge = false;
				shouldDeleteEdge = false;
				shouldDeleteNode = true;
				shouldAddNode = false;


				nodeOptions.setText("Deleting Node");

				if(shouldDeleteNode){

					imageCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							if(shouldDeleteNode){
								System.out.println("In Delete");





								System.out.println("Trying to delete");
								for(int i = 0; i < mapNodes.size();i++){
									if(mapNodes.get(i).xPos >=event.getX()-5-30&& mapNodes.get(i).xPos <=event.getX()-5+30 && mapNodes.get(i).yPos>=event.getY()-5-30 && mapNodes.get(i).yPos<=event.getY()-5+30){
										System.out.println("Node already exists here");
										fixEdges(mapNodes.get(i));
										mapNodes.remove(i);
										clearCanvas();
										renderEverything();
										System.out.println(mapNodes);
									} else {
										System.out.println("Missed bitch");
									}
								}


							}

						}
					});		
				} else {
					System.out.println("Not clicking");
				}
			}
		});



		nodeOptions.getItems().add(addNode);
		nodeOptions.getItems().add(deleteNode);
		nodeOptions.getItems().add(modifyNode);

		edgeOptions.getItems().add(addEdge);
		edgeOptions.getItems().add(deleteEdge);
		edgeOptions.getItems().add(showEdges);


		isTransitionCheckbox.setOnAction(new EventHandler(){

			@Override
			public void handle(Event arg0) {
				mapNodes.get(currentNodeLoc).isTransitionNode = !mapNodes.get(currentNodeLoc).isTransitionNode;
				renderTransitionNodes();
			}

		});


		nodeDescription.setOnAction(new EventHandler(){

			@Override
			public void handle(Event event) {
				if(currentNodeLoc !=-1){
					mapNodes.get(currentNodeLoc).description = nodeDescription.getText();
					nodeDescription.setStyle("-fx-text-fill: green;-fx-font-weight: bold;");

				} else {
					nodeDescription.setStyle("-fx-text-fill: red;-fx-font-weight: bold;");
				}
			}

		});


		nodeName.setOnAction(new EventHandler(){

			@Override
			public void handle(Event event) {
				if(currentNodeLoc !=-1){
					mapNodes.get(currentNodeLoc).nodeName = nodeName.getText();
					nodeName.setStyle("-fx-text-fill: green;-fx-font-weight: bold;");

				} else {
					nodeName.setStyle("-fx-text-fill: red;-fx-font-weight: bold;");
				}
			}

		});

		modifyNode.setOnAction(new EventHandler(){

			@Override
			public void handle(Event event) {

				shouldAddEdge = false;
				shouldAddNode = false;
				shouldDeleteNode = false;
				shouldDeleteEdge = false;
				shouldShowEdges = false;
				shouldModifyNode = true;
				nodeOptions.setText("Modifying Node");
				imageCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {

						clearCanvas();
						renderEverything();
						nodeName.setStyle(null);
						nodeDescription.setStyle(null);
						for(int i = 0; i < mapNodes.size();i++){
							if(mapNodes.get(i).xPos >=event.getX()-5-30&& mapNodes.get(i).xPos <=event.getX()-5+30 && mapNodes.get(i).yPos>=event.getY()-5-30 && mapNodes.get(i).yPos<=event.getY()-5+30){
								System.out.println("Clicked a node");
								System.out.println(mapNodes.get(i).nodeName);
								showNodeData();
								currentNodeLoc = i;
								nodeName.setText(mapNodes.get(i).nodeName);
								nodeDescription.setText(mapNodes.get(i).description);
								if(mapNodes.get(i).isTransitionNode){
									isTransitionCheckbox.setSelected(true);
								} else {
									isTransitionCheckbox.setSelected(false);
								}
								GraphicsContext gc = imageCanvas.getGraphicsContext2D();
								gc.setFill(Color.GOLD);
								gc.fillOval((double)mapNodes.get(i).xPos,(double)mapNodes.get(i).yPos, 30, 30);
								gc.setFill(Color.RED);
								gc.fillOval((double)mapNodes.get(i).xPos+1.5,(double)mapNodes.get(i).yPos+1.5, 27, 27);
							}
						}
					}
				});		

			}
		});



		loadMap1.setOnAction(new EventHandler() {

			@Override
			public void handle(Event e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					path = selectedFile.getAbsolutePath();
					path = path+"\\";
					nodeMapName = selectedFile.getName();
					try {
						clearCanvas();
						img = ImageIO.read(new File(Paths.get(path+"map.png").toString()));
						image = SwingFXUtils.toFXImage(img, null);
						
						mapView.setImage(image);
						

						
						loadMap1.setText(selectedFile.getName());
						imageWidth = (int) image.getWidth();
						imageHeight = (int) image.getHeight();
						//mapView.setFitHeight(440);
					///	mapView.setFitWidth(1000);
					//	mapView.setImage(image);
						
						System.out.println(imageWidth);
						nodeOptions.setLayoutX(1020);
						edgeOptions.setLayoutX(1020);
						nodeName.setLayoutY(550);
						nodeDescription.setLayoutY(550);
						mapNodes.clear();
						name.setLayoutY(nodeName.getLayoutY()-17);
						

						description.setLayoutY(nodeDescription.getLayoutY()-17);
						isTransitionCheckbox.setLayoutY(nodeName.getLayoutY()+4);						
						genSupermap.setLayoutX(1020);
						genSupermap.setLayoutY(nodeName.getLayoutY()-21);
						getNodesFromFile1(Paths.get(path+"mapNodes.csv").toString());
						checkForTransNodes(mapNodes,map1TransitionNodes);
						connectEdgesFromFile(mapNodes,Paths.get(path+"mapEdges.csv").toString());
						map1Dropdown.setItems(FXCollections.observableArrayList(map1TransitionNodes));
						imageCanvas.setHeight(img.getHeight());
						imageCanvas.setWidth(img.getWidth());

						stack.getChildren().addAll(mapView,imageCanvas);
						//stack.getChildren().addAll(imageCanvas,mapView);
						scrollImage.getTransforms().add(new Scale(.5,.5));

						scrollImage.setHbarPolicy(ScrollBarPolicy.ALWAYS);
						scrollImage.setVbarPolicy(ScrollBarPolicy.ALWAYS);
						scrollImage.setContent(stack);
						scrollImage.setPrefHeight(800);
						scrollImage.setPrefWidth(1500);


						scrollImage.autosize();

						
						



						mapLoaded = true;
						Main.primaryStage.setWidth(1175);
						Main.primaryStage.setHeight(650);
						Main.primaryStage.centerOnScreen();
						renderEverything();

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}   
		});



		loadMap2.setOnAction(new EventHandler(){

			@Override
			public void handle(Event e) {
				String path;
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					path = selectedFile.getAbsolutePath();
					path = path+"\\";
					try {
						img = ImageIO.read(new File(Paths.get(path+"map.png").toString()));
						image = SwingFXUtils.toFXImage(img, null);
						loadMap2.setText(selectedFile.getName());
						getNodesFromFile2(Paths.get(path+"mapNodes.csv").toString());
						checkForTransNodes(mapNodes2,map2TransitionNodes);
						map2Dropdown.setItems(FXCollections.observableArrayList(map2TransitionNodes));

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}

		});



		addEdge.setOnAction(new EventHandler(){

			@Override
			public void handle(Event arg0) {
				shouldDeleteEdge = false;
				shouldAddEdge = true;
				shouldAddNode = false;
				shouldDeleteNode = false;
				shouldDeleteEdge = false;
				oneSelected = false;
				twoSelected = false;
				shouldShowEdges = false;
				edgeOptions.setText("Adding Edge");
				nodeOptions.setText("Node Options");
				numberClicks = 0;
				if(shouldAddEdge){
					imageCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {


							if(shouldAddEdge){
								System.out.println("***********");
								System.out.println(numberClicks);
								System.out.println(firstNodeLoc);
								System.out.println(secondNodeLoc);
								System.out.println("***********");

								System.out.println("Trying to add edge");
								if(numberClicks == 0){
									for(int i = 0; i < mapNodes.size();i++){
										if(mapNodes.get(i).xPos >=event.getX()-5-30&& mapNodes.get(i).xPos <=event.getX()-5+30 && mapNodes.get(i).yPos>=event.getY()-5-30 && mapNodes.get(i).yPos<=event.getY()-5+30){
											System.out.println("CLICKED THE FIRST NODE");
											firstNodeLoc = i;
											numberClicks += 1;
											GraphicsContext gc = imageCanvas.getGraphicsContext2D();
											//gc.clearRect(event.getX()-5, event.getY()-5, 30, 30);
											//clearCanvas();
											renderEverything();
											gc.setFill(Color.GOLD);
											gc.fillOval((double)mapNodes.get(i).xPos,(double)mapNodes.get(i).yPos, 30, 30);
											gc.setFill(Color.RED);
											gc.fillOval((double)mapNodes.get(i).xPos+1.5,(double)mapNodes.get(i).yPos+1.5, 27, 27);

										} 
									}
								} else if(numberClicks != 0){
									for(int i = 0; i < mapNodes.size();i++){
										if(mapNodes.get(i).xPos >=event.getX()-5-30&& mapNodes.get(i).xPos <=event.getX()-5+30 && mapNodes.get(i).yPos>=event.getY()-5-30 && mapNodes.get(i).yPos<=event.getY()-5+30){
											System.out.println("CLICKED THE SECOND NODE");
											secondNodeLoc = i;
											numberClicks = 2;
											shouldMakeEdge = true;
											GraphicsContext gc = imageCanvas.getGraphicsContext2D();
											//clearCanvas();
											renderEverything();
											gc.setFill(Color.GOLD);
											gc.fillOval((double)mapNodes.get(i).xPos,(double)mapNodes.get(i).yPos, 10, 10);
											gc.setFill(Color.RED);
											gc.fillOval((double)mapNodes.get(i).xPos+1.5,(double)mapNodes.get(i).yPos+1.5, 7, 7);
										} 
									}
								}
							}

							if(shouldMakeEdge && firstNodeLoc != secondNodeLoc &&firstNodeLoc!=-1 && secondNodeLoc!=-1 &&secondNodeLoc!=firstNodeLoc){
								shouldMakeEdge = false;

								System.out.println("Making an edge at " +firstNodeLoc + " "+secondNodeLoc);

								if(!mapNodes.get(firstNodeLoc).neighbors.contains(mapNodes.get(secondNodeLoc))){
									mapNodes.get(firstNodeLoc).neighbors.add(mapNodes.get(secondNodeLoc));
									mapNodes.get(secondNodeLoc).neighbors.add(mapNodes.get(firstNodeLoc));
									edgeNodes.add(mapNodes.get(firstNodeLoc));
									edgeNodes.add(mapNodes.get(secondNodeLoc));
									System.out.println(mapNodes.get(firstNodeLoc).neighbors);
									GraphicsContext gc = imageCanvas.getGraphicsContext2D();
									shouldDeleteEdge = false;
									renderEverything();
									event.consume();


								}
								firstNodeLoc = -1;
								secondNodeLoc = -1;
								numberClicks = 0;
								shouldMakeEdge = false;
								renderEverything();
								shouldDeleteEdge = false;
								oneSelected = false;
								twoSelected = false;
								System.out.println("RESTARTING");
								System.out.println("RESTARTING");
								System.out.println("RESTARTING");
								System.out.println("RESTARTING");
								System.out.println("RESTARTING");
								imageCanvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);


							} else {

								if(firstNodeLoc == -1){
									numberClicks = 0;
									//firstNodeLoc = 0;
									shouldMakeEdge = false;
									shouldDeleteEdge = false;
								} else {
									numberClicks = 1;
									secondNodeLoc = 0;
									renderEverything();
									shouldMakeEdge = false;
									shouldDeleteEdge = false;
								}

							}



						}
					});		
				} else {
					System.out.println("Not clicking");
					shouldDeleteEdge = false;
				}
				oneSelected = false;
			}    

		});


		genSupermap.setOnAction(new EventHandler(){

			@Override
			public void handle(Event event) {

				saveMapNodes(Paths.get(path).toString()+"\\mapNodes.csv");
				saveMapEdges(Paths.get(path).toString()+"\\mapEdges.csv");
				genSupermap.setText("Map Saved!");
				genSupermap.setTextFill(Color.BLUE);

			}

		});



		makeTransButton.setOnAction(new EventHandler(){

			@Override
			public void handle(Event event) {

				if(map1Dropdown.getSelectionModel().getSelectedItem()!=null &&map2Dropdown.getSelectionModel().getSelectedItem()!=null){
					map1Dropdown.getSelectionModel().getSelectedItem().neighbors.add(map2Dropdown.getSelectionModel().getSelectedItem());
					map2Dropdown.getSelectionModel().getSelectedItem().neighbors.add(map1Dropdown.getSelectionModel().getSelectedItem());

					System.out.println(map1Dropdown.getSelectionModel().getSelectedItem().neighbors);
					System.out.println(map2Dropdown.getSelectionModel().getSelectedItem().neighbors);

				}
			}

		});

		deleteEdge.setOnAction(new EventHandler(){
			@Override
			public void handle(Event arg0){

				shouldDeleteEdge = true;
				System.out.println("Starting event handler for delete");
				firstNodeLoc = -1;
				secondNodeLoc = -1;
				System.out.println(oneSelected);
				System.out.println(twoSelected);
				oneSelected = false;
				twoSelected = false;
				clearTrue();
				shouldAddEdge = false;
				shouldAddNode = false;
				shouldDeleteNode = false;
				shouldShowEdges = false;
				clearTrue();
				edgeOptions.setText("Deleting Edge");
				nodeOptions.setText("Node Options");
				System.out.println(oneSelected);
				clearTrue();
				System.out.println(twoSelected);

				System.out.println("Starting");


				if(shouldDeleteEdge == true && shouldAddEdge == false){
					oneSelected = false;
					System.out.println("*************");
					System.out.println(oneSelected);
					System.out.println("*************");

					imageCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							System.out.println("******Handle*******");
							System.out.println(oneSelected);
							System.out.println("******Handle*******");


							if(oneSelected == false){
								for(int i = 0; i < mapNodes.size();i++){
									if(mapNodes.get(i).xPos >=event.getX()-5-30&& mapNodes.get(i).xPos <=event.getX()-5+30 && mapNodes.get(i).yPos>=event.getY()-5-30 && mapNodes.get(i).yPos<=event.getY()-5+30){
										System.out.println("CLICKED THE FIRST NODE");
										oneSelected = true;
										firstNodeLoc = i;
										GraphicsContext gc = imageCanvas.getGraphicsContext2D();
										gc.setFill(Color.GOLD);
										gc.fillOval((double)mapNodes.get(i).xPos,(double)mapNodes.get(i).yPos, 30, 30);
										gc.setFill(Color.RED);
										gc.fillOval((double)mapNodes.get(i).xPos+1.5,(double)mapNodes.get(i).yPos+1.5, 27, 27);
										break;
										//									firstNodeLoc = i;
										//									numberClicks =2;
										//									GraphicsContext gc = imageCanvas.getGraphicsContext2D();
										//									gc.setFill(Color.GOLD);
										//									gc.fillOval((double)mapNodes.get(i).xPos,(double)mapNodes.get(i).yPos, 10, 10);
										//									gc.setFill(Color.RED);
										//									gc.fillOval((double)mapNodes.get(i).xPos+1.5,(double)mapNodes.get(i).yPos+1.5, 7, 7);
										//									event.consume();	
									} 
								}
								System.out.println(oneSelected);
								System.out.println(twoSelected);
							} else if(oneSelected){
								for(int i = 0; i < mapNodes.size();i++){
									if(mapNodes.get(i).xPos >=event.getX()-5-30&& mapNodes.get(i).xPos <=event.getX()-5+30 && mapNodes.get(i).yPos>=event.getY()-5-30 && mapNodes.get(i).yPos<=event.getY()-5+30){
										System.out.println("CLICKED THE SECOND NODE");
										twoSelected = true;
										GraphicsContext gc = imageCanvas.getGraphicsContext2D();
										gc.setFill(Color.GOLD);
										gc.fillOval((double)mapNodes.get(i).xPos,(double)mapNodes.get(i).yPos, 10, 10);
										gc.setFill(Color.RED);
										gc.fillOval((double)mapNodes.get(i).xPos+1.5,(double)mapNodes.get(i).yPos+1.5, 7, 7);
										secondNodeLoc = i;
										break;
									}
								}
							}

							if(oneSelected && twoSelected){
								System.out.println("Selected two nodes");
								oneSelected = false;
								twoSelected = false;
								if(mapNodes.get(firstNodeLoc).neighbors.contains(mapNodes.get(secondNodeLoc)) && mapNodes.get(secondNodeLoc).neighbors.contains(mapNodes.get(firstNodeLoc))){
									mapNodes.get(firstNodeLoc).neighbors.remove(mapNodes.get(secondNodeLoc));
									mapNodes.get(secondNodeLoc).neighbors.remove(mapNodes.get(firstNodeLoc));
									clearCanvas();
									renderEverything();

									event.consume();
									System.out.println(mapNodes.get(firstNodeLoc).neighbors);
								}
								firstNodeLoc = -1;
								secondNodeLoc = -1;
								renderEverything();
								imageCanvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);

							} else {
								twoSelected =false;
							}


						}

					});	

				} 


			}


		});




















		showEdges.setOnAction(new EventHandler(){

			@Override
			public void handle(Event arg0) {
				clearCanvas();
				renderEverything();

			}

		});

	}


	protected void clearCanvas(){
		GraphicsContext gc = imageCanvas.getGraphicsContext2D();
		gc.clearRect(0,0,imageCanvas.getWidth(),imageCanvas.getHeight());	
	}

	protected void renderNodes(){
		for(Node n: mapNodes){
			GraphicsContext gc = imageCanvas.getGraphicsContext2D();
			gc.setFill(Color.RED);
			gc.fillOval(n.xPos, n.yPos, 30, 30);
		}


	}

	protected void fixEdges(Node node){
		for (Node n: mapNodes){
			if(n.neighbors.contains(node)){
				n.neighbors.remove(node);
				System.out.println("Removed");
			}
		}
	}


	protected void showNodeData(){
		System.out.println("*************************");

		for(int i = 0; i < mapNodes.size();i++){
			System.out.println(mapNodes.get(i).nodeName);
			System.out.println(mapNodes.get(i).description);
			System.out.println(mapNodes.get(i).isTransitionNode);
		}

		System.out.println("*************************");

	}





	private static void saveMapNodes(String fileName){

		try
		{
			FileWriter writer = new FileWriter(fileName);


			for (int i = 0; i<mapNodes.size();i++){
				writer.append(mapNodes.get(i).nodeName);
				writer.append(',');
				writer.append(Integer.toString(mapNodes.get(i).xPos));
				writer.append(',');
				writer.append(Integer.toString(mapNodes.get(i).yPos));
				writer.append(',');
				writer.append(Integer.toString(mapNodes.get(i).zPos));
				writer.append(',');
				writer.append(mapNodes.get(i).map);
				writer.append(",");
				writer.append(mapNodes.get(i).description);
				writer.append(",");
				writer.append(String.valueOf(mapNodes.get(i).isTransitionNode));
				writer.append("\n");
				System.out.println(mapNodes.get(i).map);
			}
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 
	}

	protected void renderEdges(){
		List<Node> edgeNodes = new ArrayList<Node>();




		GraphicsContext gc = imageCanvas.getGraphicsContext2D();



		for(int i = 0; i < mapNodes.size()-1;i++){
			for(int j = 0; j <mapNodes.get(i).neighbors.size();j++){

				int x1 = mapNodes.get(i).xPos+5;
				int y1 = mapNodes.get(i).yPos+5;
				int x2 = mapNodes.get(i).neighbors.get(j).xPos+5;
				int y2 = mapNodes.get(i).neighbors.get(j).yPos+5;
				gc.setFill(Color.WHITE);
				gc.strokeLine(x1, y1, x2, y2);
			}

		}
	}

	private static void connectEdgesFromFile(List<Node> nodeList, String filePath)
	{
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";
		int edgeX1Index = 0;
		int edgeY1Index = 1;
		int edgeX2Index = 4;
		int edgeY2Index = 5;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] edgeData = line.split(delimiter);
				int x1 = Integer.parseInt(edgeData[edgeX1Index]);
				int y1 = Integer.parseInt(edgeData[edgeY1Index]);
				int x2 = Integer.parseInt(edgeData[edgeX2Index]);
				int y2 = Integer.parseInt(edgeData[edgeY2Index]);
				System.out.println(x1+" "+y1+" "+x2+" "+y2);
				Node n1 = findNodeByXY(nodeList, x1, y1);
				Node n2 = findNodeByXY(nodeList, x2, y2);
				System.out.println("********************");
				//System.out.println(n1.map);
				//System.out.println(n2.map);
				System.out.println("********************");


				if(n1 != null&& n2 !=null){
					if (n1.neighbors == null)
					{
						n1.neighbors.add(n2);


					}else
					{
						n1.neighbors.add(n2);
					}

				} else {
					System.out.println("Transition");
				}
			}



		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {e.printStackTrace();}
			}
		}
	}

	public static Node findNodeByXY(List<Node> nodeList, int x, int y)//Want to change this to throwing an exception when the node is not found
	{
		for(Node n : nodeList){
			if(n.xPos == x && n.yPos == y)
			{
				return n;
			}
		}
		return null;
	}


	private void getNodesFromFile1(String filePath)
	{
		Boolean isTrans = null;
		List<Node> nodeList = new ArrayList<Node>();
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";
		int nodeNameIndex = 0;
		int nodeXIndex = 1;
		int nodeYIndex = 2;
		int nodeZIndex = 3;
		int nodeMapIndex = 4;
		int nodeDescIndex = 5;
		int nodeTransIndex = 6;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] nodeData = line.split(delimiter);
				String name = nodeData[nodeNameIndex];
				int x = Integer.parseInt(nodeData[nodeXIndex]);
				int y = Integer.parseInt(nodeData[nodeYIndex]);
				int z = Integer.parseInt(nodeData[nodeZIndex]);
				String map =nodeData[nodeMapIndex];
				String description = nodeData[nodeDescIndex];
				if(nodeData[nodeTransIndex]!=null){
					isTrans = Boolean.valueOf(nodeData[nodeTransIndex]);
				}
				Node newNode = new Node(name,x,y,z,map, description);
				newNode.isTransitionNode = isTrans;
				nodeList.add(newNode);
			}

		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {e.printStackTrace();}
			}
		}
		System.out.println(nodeList);
		mapNodes = nodeList;
	}







	private void getNodesFromFile2(String filePath)
	{
		Boolean isTrans = null;
		List<Node> nodeList = new ArrayList<Node>();
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";
		int nodeNameIndex = 0;
		int nodeXIndex = 1;
		int nodeYIndex = 2;
		int nodeZIndex = 3;
		int nodeMapIndex = 4;
		int nodeDescIndex = 5;
		int nodeTransIndex = 6;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] nodeData = line.split(delimiter);
				String name = nodeData[nodeNameIndex];
				int x = Integer.parseInt(nodeData[nodeXIndex]);
				int y = Integer.parseInt(nodeData[nodeYIndex]);
				int z = Integer.parseInt(nodeData[nodeZIndex]);
				String map =nodeData[nodeMapIndex];
				String description = nodeData[nodeDescIndex];
				if(nodeData[nodeTransIndex]!=null){
					isTrans = Boolean.valueOf(nodeData[nodeTransIndex]);
				}
				Node newNode = new Node(name,x,y,z,map, description);
				newNode.isTransitionNode = isTrans;
				nodeList.add(newNode);
			}

		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {e.printStackTrace();}
			}
		}
		System.out.println(nodeList);
		mapNodes2 = nodeList;
	}





	private void checkForTransNodes(List<Node> mapNodesNumber,List<Node>transNodeStore){
		for(Node n: mapNodesNumber){
			if(n.isTransitionNode == true){
				transNodeStore.add(n);

			}
		}
	}


	private void clearTrue(){
		oneSelected = false;
		twoSelected = false;
	}

	private Node findTransNode(String name,List<Node> mapTransNodes){
		for(Node n: mapTransNodes){
			if(n.nodeName == name){
				return n;
			}
		}
		return null;
	}



	private static void saveMapEdges(String fileName){

		try
		{
			FileWriter writer = new FileWriter(fileName);


			for (int i = 0; i<mapNodes.size();i++){
				for (int j = 0; j<mapNodes.get(i).neighbors.size();j++){
					writer.append(Integer.toString(mapNodes.get(i).xPos));
					writer.append(',');
					writer.append(Integer.toString(mapNodes.get(i).yPos));
					writer.append(',');
					writer.append(Integer.toString(mapNodes.get(i).zPos));
					writer.append(',');
					writer.append(mapNodes.get(i).map);
					writer.append(',');
					writer.append(Integer.toString(mapNodes.get(i).neighbors.get(j).xPos));
					writer.append(',');
					writer.append(Integer.toString(mapNodes.get(i).neighbors.get(j).yPos));
					writer.append(',');
					writer.append(Integer.toString(mapNodes.get(i).neighbors.get(j).zPos));
					writer.append(',');
					writer.append(mapNodes.get(i).neighbors.get(j).map);

					writer.append("\n");
				}
			}
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 
	}


	protected void renderTransitionNodes(){
		for(Node n: mapNodes){
			if(n.isTransitionNode == true){
				GraphicsContext gc = imageCanvas.getGraphicsContext2D();
				gc.setFill(Color.BLUE);
				gc.fillRect(n.xPos-.75,n.yPos-.75,12,12);
				gc.setFill(Color.WHITE);
				gc.fillOval(n.xPos+1.5, n.yPos+1.5, 7.5, 7.5);
			}
		}


	}

	public void renderEverything(){
		renderEdges();
		renderNodes();
		System.out.println("Hi");
		renderTransitionNodes();
	}

}
