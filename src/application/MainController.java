package application;
/**
Alonso
 */

// AWT Imports
import java.awt.image.BufferedImage;

// IO Imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// UTIL Imports
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
// JavaFX Imports
import javafx.collections.FXCollections;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.transform.Scale;

// Other Imports
import java.net.URL;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class MainController implements Initializable {
	DoubleProperty zoomProperty = new SimpleDoubleProperty(200);

	static List<Node> mapNodes = new ArrayList<Node>();
	static List<Node> mapNodes2 = new ArrayList<Node>();
	static List<String> typeList = new ArrayList<String>();
	static List<Node> map1TransitionNodes = new ArrayList<Node>();
	static List<Node> map2TransitionNodes = new ArrayList<Node>();
	static List<Node> tempMapTransitionNodes = new ArrayList<Node>();

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
	private Label map1 = new Label();

	@FXML
	private CheckBox snap;

	@FXML
	private Label map2 = new Label();

	@FXML
	private Label node1 = new Label();

	@FXML
	private Label node2 = new Label();

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
	private ComboBox<String> typeBox = new ComboBox<String>();

	@FXML
	private Label type;

	@FXML
	private Button loadMap1 = new Button();

	@FXML
	private ComboBox<Node> map1Dropdown = new ComboBox<Node>();

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
	private ComboBox<Node> map2Dropdown = new ComboBox<Node>();

	@FXML
	private CheckBox isTransitionCheckbox = new CheckBox();

	@FXML
	private Button makeTransButton = new Button();

	@FXML
	private MenuButton nodeOptions = new MenuButton();

	private MenuItem modifyNode = new MenuItem("Modify Node");

	@FXML
	private MenuButton edgeOptions = new MenuButton();

	private int duplicateNode = 1;
	private int firstNodeLoc = -1;
	private int secondNodeLoc = -1;
	private int nodeSizeReg =30;
	private int nodeSizeRegY;
	private int nodeSizeCampus = nodeSizeReg/4;
	private int nodeSizeCampusY;


	public static boolean mapLoaded = false;
	private volatile int numberClicks = 0;
	private boolean doOnce = true;
	private int currentNodeLoc = -1;
	private String nodeMapName;
	private static String path;
	private boolean isCampus = false;
	private static boolean shouldAddNode = false;
	private static boolean shouldDeleteNode = false;
	private static boolean shouldAddEdge = false;
	private static boolean shouldDeleteEdge = false;
	private static boolean shouldModifyNode = false;
	private static boolean shouldMakeEdge = false;
	static boolean oneSelected = false;
	static boolean twoSelected = false;
	Node currentNode;
	public final static int SNAP_LENIENCY = 40;
	public final static int SNAP_RANGE = 100;
	static boolean isDeleteDone = false;
	static boolean isAddDone = false;
	private static boolean snapToNodes = false;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		pane.setStyle("-fx-background-color: #545352;");

		nodeOptions.getItems().remove(0);
		nodeOptions.getItems().remove(0);
		edgeOptions.getItems().remove(0);
		edgeOptions.getItems().remove(0);

		map1Dropdown.setOnAction(new EventHandler() {

			@Override
			public void handle(Event arg0) {
			}

		});

		addNode.setOnAction(new EventHandler() {

			@Override
			public void handle(Event event) {

				resetAllVariables();
				shouldAddNode = true;
				nodeOptions.setText("Adding Node");


				imageCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {

						double eventXPos = event.getX();
						double eventYPos = event.getY();
						boolean changedX = false;
						boolean changedY = false;
						int refNodeX = 0;
						int refNodeY = 0;

						// System.out.println("GetSceneCoords");
						// System.out.println(event.getY());
						// System.out.println(event.getX());

						// imageCanvas.getGraphicsContext2D().clearRect(0,
						// 0, imageCanvas.getWidth(),
						// imageCanvas.getHeight());


						if (snapToNodes){
							System.out.println("Snapping");
							double eventXTemp = event.getX() + 5 + nodeSizeReg + SNAP_LENIENCY + 1;
							double eventYTemp = event.getY() + 5 + nodeSizeReg + SNAP_LENIENCY + 1;
							for (int i = 0; i < mapNodes.size(); i++) {
								if (mapNodes.get(i).xPos >= eventXPos - 5 - nodeSizeReg - SNAP_LENIENCY
										&& mapNodes.get(i).xPos <= eventXPos - 5 + nodeSizeReg + SNAP_LENIENCY
										&& mapNodes.get(i).yPos >= eventYPos - 5 - nodeSizeReg - SNAP_RANGE
										&& mapNodes.get(i).yPos <= eventYPos - 5 + nodeSizeReg + SNAP_RANGE) {
									// Makes sure your using the closest connection
									if ( Math.abs(event.getX() - mapNodes.get(i).xPos) < 
											Math.abs(event.getX() - eventXTemp) ) {
										eventXTemp = mapNodes.get(i).xPos + nodeSizeReg/2;
										changedX = true;
										refNodeX = i;
									}

								}
								if (mapNodes.get(i).yPos >= eventYPos - 5 - nodeSizeReg - SNAP_LENIENCY
										&& mapNodes.get(i).yPos <= eventYPos - 5 + nodeSizeReg + SNAP_LENIENCY
										&& mapNodes.get(i).xPos >= eventXPos - 5 - nodeSizeReg - SNAP_RANGE
										&& mapNodes.get(i).xPos <= eventXPos - 5 + nodeSizeReg + SNAP_RANGE) {
									if ( Math.abs(event.getY() - mapNodes.get(i).yPos) < 
											Math.abs(event.getY() - eventYTemp) ) { 
										eventYTemp = mapNodes.get(i).yPos + nodeSizeReg/2;
										changedY = true;
										refNodeY = i;

									}
								}
							}
							if (changedX) {
								eventXPos = eventXTemp;
								System.out.println("Snapping to X: " + eventXPos);
							}
							if (changedY) {
								eventYPos = eventYTemp;
								System.out.println("Snapping to Y: " + eventYPos);
							}
						}


































						if (doOnce) {
							if (mapNodes.size() == 0) {
								// System.out.println("Creating new node
								// at " + event.getX() + " " +
								// event.getY());
								Node node = new Node("node", (int) eventXPos - nodeSizeReg/2, (int)eventYPos - nodeSizeReg/2, 0,
										nodeMapName, "","None");
								mapNodes.add(node);
								clearCanvas();
								renderEverything();
								// System.out.println(mapNodes);

								doOnce = false;
								// System.out.println("Zero map Node");
							}
						} else {

							for (int i = 0; i < mapNodes.size(); i++) {
								if (mapNodes.get(i).xPos >= eventXPos - 5 - nodeSizeReg
										&& mapNodes.get(i).xPos <= eventXPos - 5 + nodeSizeReg
										&& mapNodes.get(i).yPos >= eventYPos - 5 - nodeSizeReg
										&& mapNodes.get(i).yPos <= eventYPos - 5 + nodeSizeReg) {
									// System.out.println("Node already
									// exists here");
									// System.out.println(mapNodes.get(i).xPos);
									// System.out.println(mapNodes.get(i).yPos);
									// System.out.println("Counter is: "
									// + i);
									// System.out.println(event.getX() -
									// 5);
									// System.out.println(event.getY() -
									// 5);
									duplicateNode++;
								}
							}
						}

						if (!shouldAddNode) {
							imageCanvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
						} else if (duplicateNode == 0) {
							// System.out.println("In duplicate node");
							Node node = new Node("node", (int) eventXPos - nodeSizeReg/2, (int) eventYPos - nodeSizeReg/2, 0,
									nodeMapName, "","None");
							mapNodes.add(node);
							renderEverything();
							if (changedX) {
								System.out.println("Snapping to X: " + eventXPos);
								highlightNode(refNodeX);
							}
							if (changedY) {
								System.out.println("Snapping to Y: " + eventYPos);
								highlightNode(refNodeY);
							}
							// System.out.println(mapNodes);
						} else {
							duplicateNode = 0;
						}

					}
				});
			}
		});

		deleteNode.setOnAction(new EventHandler() {

			@Override
			public void handle(Event event) {

				resetAllVariables();
				shouldDeleteNode = true;
				nodeOptions.setText("Deleting Node");

				if (!shouldDeleteNode) {
					imageCanvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
				}

				imageCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (shouldDeleteNode) {
							for (int i = 0; i < mapNodes.size(); i++) {
								if (mapNodes.get(i).xPos >= event.getX() - 5 - nodeSizeReg
										&& mapNodes.get(i).xPos <= event.getX() - 5 + nodeSizeReg
										&& mapNodes.get(i).yPos >= event.getY() - 5 - nodeSizeReg
										&& mapNodes.get(i).yPos <= event.getY() - 5 + nodeSizeReg) {
									fixEdges(mapNodes.get(i));
									mapNodes.remove(i);
									clearCanvas();
									renderEverything();
									System.out.println(mapNodes);
								} else {
									// System.out.println("Missed bitch");
								}
							}

						}

					}
				});

			}
		});

		nodeOptions.getItems().add(addNode);
		nodeOptions.getItems().add(deleteNode);
		nodeOptions.getItems().add(modifyNode);

		edgeOptions.getItems().add(addEdge);
		edgeOptions.getItems().add(deleteEdge);
		edgeOptions.getItems().add(showEdges);

		isTransitionCheckbox.setOnAction(new EventHandler() {

			@Override
			public void handle(Event arg0) {

				mapNodes.get(currentNodeLoc).isTransitionNode = !mapNodes.get(currentNodeLoc).isTransitionNode;
				renderTransitionNodes();
			}

		});



		nodeDescription.setOnAction(new EventHandler() {

			@Override
			public void handle(Event event) {

				resetSaveButton();

				if (currentNodeLoc != -1) {
					mapNodes.get(currentNodeLoc).description = nodeDescription.getText();
					nodeDescription.setStyle("-fx-text-fill: green;-fx-font-weight: bold;");

				} else {
					nodeDescription.setStyle("-fx-text-fill: red;-fx-font-weight: bold;");
				}
			}

		});

		nodeName.setOnAction(new EventHandler() {

			@Override
			public void handle(Event event) {

				resetSaveButton();

				if (currentNodeLoc != -1) {
					mapNodes.get(currentNodeLoc).nodeName = nodeName.getText();
					nodeName.setStyle("-fx-text-fill: green;-fx-font-weight: bold;");

				} else {
					nodeName.setStyle("-fx-text-fill: red;-fx-font-weight: bold;");
				}
			}

		});

		modifyNode.setOnAction(new EventHandler() {

			@Override
			public void handle(Event event) {

				resetAllVariables();
				shouldModifyNode = true;

				if(currentNodeLoc!=-1){
					typeBox.setPromptText("Node Type");
				}
				nodeOptions.setText("Modifying Node");

				imageCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {


						if (!shouldModifyNode || shouldAddNode) {
							imageCanvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
						}

						//System.out.println("Restarting Event Handler");
						typeBox.setPromptText("Node Type");
						clearCanvas();
						renderEverything();
						nodeName.setStyle(null);
						nodeDescription.setStyle(null);
						for (int i = 0; i < mapNodes.size(); i++) {



							if (mapNodes.get(i).xPos >= event.getX() - 5 - nodeSizeReg
									&& mapNodes.get(i).xPos <= event.getX() - 5 + nodeSizeReg
									&& mapNodes.get(i).yPos >= event.getY() - 5 - nodeSizeReg
									&& mapNodes.get(i).yPos <= event.getY() - 5 + nodeSizeReg) {
								showNodeData();
								currentNodeLoc = i;
								nodeName.setText(mapNodes.get(i).nodeName);
								nodeDescription.setText(mapNodes.get(i).description);
								if (mapNodes.get(i).isTransitionNode) {
									isTransitionCheckbox.setSelected(true);
								} else {
									isTransitionCheckbox.setSelected(false);
								}



								if(mapNodes.get(currentNodeLoc).type != ""){
									System.out.println("In setting of box");
									typeBox.setValue(mapNodes.get(currentNodeLoc).type);
									System.out.println(mapNodes.get(currentNodeLoc).type);

								} else {
									typeBox.setPromptText("Node Type");

								}

								highlightNode(i);
							}
						}
					}
				});

			}
		});



		typeBox.setOnAction(new EventHandler(){

			@Override
			public void handle(Event arg0) {


				mapNodes.get(currentNodeLoc).type = typeBox.getSelectionModel().getSelectedItem();
				System.out.println(mapNodes.get(currentNodeLoc).type);
				String isTrans = mapNodes.get(currentNodeLoc).type;
				switch(isTrans){
				case "Stairs":
					mapNodes.get(currentNodeLoc).isTransitionNode = true;
					break;

				case "Entrance":
					mapNodes.get(currentNodeLoc).isTransitionNode = true;
					break;

				case "Elevator":
					mapNodes.get(currentNodeLoc).isTransitionNode = true;
					break;

				default:
					mapNodes.get(currentNodeLoc).isTransitionNode = false;

				}

				renderEverything();
			}

		});
		loadMap1.setOnAction(new EventHandler() {

			@Override
			public void handle(Event e) {
				resetSaveButton();

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					path = selectedFile.getAbsolutePath();
					path = path + "\\";
					nodeMapName = selectedFile.getName();
					try {
						clearCanvas();

						img = ImageIO.read(new File(Paths.get(path + "map.png").toString()));

						image = SwingFXUtils.toFXImage(img, null);

						mapView.setImage(image);
						mapView.preserveRatioProperty().set(true);

						loadMap1.setText(selectedFile.getName());
						// mapView.setFitHeight(440);
						/// mapView.setFitWidth(1000);
						// mapView.setImage(image);

						// System.out.println(imageWidth);
						// nodeOptions.setLayoutX(1020);
						// edgeOptions.setLayoutX(1020);
						mapNodes.clear();

						genSupermap.setLayoutX(1020);
						genSupermap.setLayoutY(nodeName.getLayoutY() - 21);
						getNodesFromFile1(Paths.get(path + "mapNodes.csv").toString());
						checkForTransNodes(mapNodes, map1TransitionNodes);
						connectEdgesFromFile(mapNodes, Paths.get(path + "mapEdges.csv").toString());
						map1Dropdown.setItems(FXCollections.observableArrayList(map1TransitionNodes));
						imageCanvas.setHeight(img.getHeight());
						imageCanvas.setWidth(img.getWidth());

						stack.getChildren().addAll(mapView, imageCanvas);

						// stack.getChildren().addAll(imageCanvas,mapView);
						scrollImage.getTransforms().add(new Scale(.5, .5));

						scrollImage.setHbarPolicy(ScrollBarPolicy.ALWAYS);
						scrollImage.setVbarPolicy(ScrollBarPolicy.ALWAYS);
						scrollImage.setContent(stack);
						scrollImage.setPrefHeight(900);
						scrollImage.setPrefWidth(1950);

						scrollImage.autosize();

						mapLoaded = true;
						nodeOptions.setLayoutX(scrollImage.getWidth() - 935);
						edgeOptions.setLayoutX(scrollImage.getWidth() - 935);
						edgeOptions.setLayoutY(scrollImage.getHeight() - 650);
						genSupermap.setLayoutX(scrollImage.getWidth() - 935);
						genSupermap.setLayoutY(scrollImage.getHeight() - 370);
						typeBox.setLayoutX(scrollImage.getWidth() - 935);
						typeBox.setLayoutY(scrollImage.getHeight() - 490);

						nodeName.setLayoutY(scrollImage.getHeight() - 335);
						nodeDescription.setLayoutY(scrollImage.getHeight() - 335);
						nodeDescription.setLayoutX(nodeName.getLayoutX() + nodeName.getWidth() + 20);
						name.setLayoutY(nodeName.getLayoutY() - 19);
						//edgeOptions.setLayoutY(280);
						description.setLayoutY(nodeDescription.getLayoutY() - 19);
						description.setLayoutX(nodeDescription.getLayoutX());
						isTransitionCheckbox.setLayoutY(nodeName.getLayoutY() + 4);
						isTransitionCheckbox.setLayoutX(nodeDescription.getLayoutX() + nodeDescription.getWidth() + 5);
						map1Dropdown.setPrefWidth(160);
						loadMap2.setLayoutX(300);
						// map2.setLayoutX(500);
						map2.setLayoutX(loadMap2.getLayoutX());
						map2Dropdown.setLayoutX(loadMap2.getLayoutX() + 7 + loadMap2.getWidth());
						map2Dropdown.setPrefWidth(160);
						makeTransButton.setLayoutX(map2Dropdown.getLayoutX() + map2Dropdown.getWidth() + 150);
						Main.primaryStage.setWidth(1175);
						Main.primaryStage.setHeight(650);
						renderEverything();
						Main.primaryStage.centerOnScreen();
						node2.setLayoutX(map2Dropdown.getLayoutX());
						setTypes();

					} catch (IOException ex) {

						System.out.println("Failed");
						if(img==null){
							System.out.println("Found Campus Map");
							try {
								img = ImageIO.read(new File(Paths.get(path + "campus.png").toString()));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							isCampus = true;
							nodeSizeReg = nodeSizeCampus;
							image = SwingFXUtils.toFXImage(img, null);

							mapView.setImage(image);
							mapView.preserveRatioProperty().set(true);

							loadMap1.setText(selectedFile.getName());
							// mapView.setFitHeight(440);
							/// mapView.setFitWidth(1000);
							// mapView.setImage(image);

							// System.out.println(imageWidth);
							// nodeOptions.setLayoutX(1020);
							// edgeOptions.setLayoutX(1020);
							mapNodes.clear();

							genSupermap.setLayoutX(1020);
							genSupermap.setLayoutY(nodeName.getLayoutY() - 21);
							getNodesFromFile1(Paths.get(path + "mapNodes.csv").toString());
							checkForTransNodes(mapNodes, map1TransitionNodes);
							connectEdgesFromFile(mapNodes, Paths.get(path + "mapEdges.csv").toString());
							map1Dropdown.setItems(FXCollections.observableArrayList(map1TransitionNodes));
							imageCanvas.setHeight(img.getHeight());
							imageCanvas.setWidth(img.getWidth());

							stack.getChildren().addAll(mapView, imageCanvas);

							// stack.getChildren().addAll(imageCanvas,mapView);
							scrollImage.getTransforms().add(new Scale(.5, .5));

							scrollImage.setHbarPolicy(ScrollBarPolicy.ALWAYS);
							scrollImage.setVbarPolicy(ScrollBarPolicy.ALWAYS);
							scrollImage.setContent(stack);
							scrollImage.setPrefHeight(900);
							scrollImage.setPrefWidth(1950);

							scrollImage.autosize();

							mapLoaded = true;
							nodeOptions.setLayoutX(scrollImage.getWidth() - 935);
							edgeOptions.setLayoutX(scrollImage.getWidth() - 935);
							edgeOptions.setLayoutY(scrollImage.getHeight() - 650);
							genSupermap.setLayoutX(scrollImage.getWidth() - 935);
							genSupermap.setLayoutY(scrollImage.getHeight() - 370);
							typeBox.setLayoutX(scrollImage.getWidth() - 935);
							typeBox.setLayoutY(scrollImage.getHeight() - 490);

							nodeName.setLayoutY(scrollImage.getHeight() - 335);
							nodeDescription.setLayoutY(scrollImage.getHeight() - 335);
							nodeDescription.setLayoutX(nodeName.getLayoutX() + nodeName.getWidth() + 20);
							name.setLayoutY(nodeName.getLayoutY() - 19);
							//edgeOptions.setLayoutY(280);
							description.setLayoutY(nodeDescription.getLayoutY() - 19);
							description.setLayoutX(nodeDescription.getLayoutX());
							isTransitionCheckbox.setLayoutY(nodeName.getLayoutY() + 4);
							isTransitionCheckbox.setLayoutX(nodeDescription.getLayoutX() + nodeDescription.getWidth() + 5);
							map1Dropdown.setPrefWidth(160);
							loadMap2.setLayoutX(300);
							// map2.setLayoutX(500);
							map2.setLayoutX(loadMap2.getLayoutX());
							map2Dropdown.setLayoutX(loadMap2.getLayoutX() + 7 + loadMap2.getWidth());
							map2Dropdown.setPrefWidth(160);
							makeTransButton.setLayoutX(map2Dropdown.getLayoutX() + map2Dropdown.getWidth() + 150);
							Main.primaryStage.setWidth(1175);
							Main.primaryStage.setHeight(650);
							renderEverything();
							Main.primaryStage.centerOnScreen();
							node2.setLayoutX(map2Dropdown.getLayoutX());
							setTypes();
							System.out.println(mapNodes);
							System.out.println(map1TransitionNodes.get(0).neighbors);
							System.out.println(map1TransitionNodes.get(1).neighbors);
							snap.setLayoutX(scrollImage.getWidth() - 935);

						}
						ex.printStackTrace();
					}
				}
			}
		});
		
		
		
		snap.setOnAction(new EventHandler(){

			@Override
			public void handle(Event arg0) {
				snapToNodes = !snapToNodes;
			}
			
		});


		zoomProperty.addListener(new InvalidationListener() {


			@Override
			public void invalidated(Observable arg0) {
				// TODO Auto-generated method stub

				mapView.setFitWidth(zoomProperty.get() * 2);
				mapView.setFitHeight(zoomProperty.get() * 3);
				imageCanvas.setScaleX(zoomProperty.get() / 1650);
				imageCanvas.setScaleY(zoomProperty.get() / 1650);


				scrollImage.setContent(stack);

			}
		});



		scrollImage.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() > 0) {
					zoomProperty.set(zoomProperty.get() * 1.2);
				} else if (event.getDeltaY() < 0) {
					zoomProperty.set(zoomProperty.get() / 1.1);
				}
			}
		});


		loadMap2.setOnAction(new EventHandler() {

			@Override
			public void handle(Event e) {

				resetSaveButton();
				String path;
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					path = selectedFile.getAbsolutePath();
					path = path + "\\";
					try {
						img = ImageIO.read(new File(Paths.get(path + "map.png").toString()));
						image = SwingFXUtils.toFXImage(img, null);
						loadMap2.setText(selectedFile.getName());
						getNodesFromFile2(Paths.get(path + "mapNodes.csv").toString());
						checkForTransNodes(mapNodes2, map2TransitionNodes);
						map2Dropdown.setItems(FXCollections.observableArrayList(map2TransitionNodes));

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}

		});

		addEdge.setOnAction(new EventHandler() {

			@Override
			public void handle(Event arg0) {

				resetAllVariables();
				shouldAddEdge = true;
				edgeOptions.setText("Adding Edge");

				imageCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {

						if (!shouldAddEdge) {
							imageCanvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
						}

						// System.out.println("***********");
						// System.out.println(numberClicks);
						// System.out.println(firstNodeLoc);
						// System.out.println(secondNodeLoc);
						// System.out.println("***********");
						// System.out.println("Trying to add edge");

						if (numberClicks == 0) {
							for (int i = 0; i < mapNodes.size(); i++) {
								if (mapNodes.get(i).xPos >= event.getX() - 5 - nodeSizeReg
										&& mapNodes.get(i).xPos <= event.getX() - 5 + nodeSizeReg
										&& mapNodes.get(i).yPos >= event.getY() - 5 - nodeSizeReg
										&& mapNodes.get(i).yPos <= event.getY() - 5 + nodeSizeReg) {
									firstNodeLoc = i;
									numberClicks += 1;

									highlightNode(i);

								}
							}
						} else if (numberClicks != 0) {
							for (int i = 0; i < mapNodes.size(); i++) {
								if (mapNodes.get(i).xPos >= event.getX() - 5 - nodeSizeReg
										&& mapNodes.get(i).xPos <= event.getX() - 5 + nodeSizeReg
										&& mapNodes.get(i).yPos >= event.getY() - 5 - nodeSizeReg
										&& mapNodes.get(i).yPos <= event.getY() - 5 + nodeSizeReg) {
									secondNodeLoc = i;
									numberClicks = 2;
									shouldMakeEdge = true;
									renderEverything();
									highlightNode(i);
								}
							}
						}

						if (shouldMakeEdge && firstNodeLoc != secondNodeLoc && firstNodeLoc != -1 && secondNodeLoc != -1
								&& secondNodeLoc != firstNodeLoc) {
							shouldMakeEdge = false;

							// System.out.println("Making an edge at " +
							// firstNodeLoc + " " + secondNodeLoc);

							if (!mapNodes.get(firstNodeLoc).neighbors.contains(mapNodes.get(secondNodeLoc))) {
								mapNodes.get(firstNodeLoc).neighbors.add(mapNodes.get(secondNodeLoc));
								mapNodes.get(secondNodeLoc).neighbors.add(mapNodes.get(firstNodeLoc));
								edgeNodes.add(mapNodes.get(firstNodeLoc));
								edgeNodes.add(mapNodes.get(secondNodeLoc));
								// System.out.println(mapNodes.get(firstNodeLoc).neighbors);
								renderEverything();
								event.consume();

							}
							firstNodeLoc = -1;
							secondNodeLoc = -1;
							numberClicks = 0;
							shouldMakeEdge = false;
							renderEverything();
							oneSelected = false;
							twoSelected = false;
							// System.out.println("RESTARTING");

						} else {

							if (firstNodeLoc == -1) {
								numberClicks = 0;
								// firstNodeLoc = 0;
								shouldMakeEdge = false;
							} else {
								numberClicks = 1;
								secondNodeLoc = 0;
								// renderEverything();
								shouldMakeEdge = false;
							}

						}

					}
				});

			}

		});

		genSupermap.setOnAction(new EventHandler() {

			@Override
			public void handle(Event event) {

				saveMapNodes(Paths.get(path).toString() + "\\mapNodes.csv");
				saveMapEdges(Paths.get(path).toString() + "\\mapEdges.csv");
				genSupermap.setText("Map Saved!");
				genSupermap.setTextFill(Color.BLUE);

			}

		});

		makeTransButton.setOnAction(new EventHandler() {

			@Override
			public void handle(Event event) {

				resetSaveButton();

				if (map1Dropdown.getSelectionModel().getSelectedItem() != null
						&& map2Dropdown.getSelectionModel().getSelectedItem() != null) {
					map1Dropdown.getSelectionModel().getSelectedItem().neighbors
					.add(map2Dropdown.getSelectionModel().getSelectedItem());
					map2Dropdown.getSelectionModel().getSelectedItem().neighbors
					.add(map1Dropdown.getSelectionModel().getSelectedItem());
					System.out.println(map1Dropdown.getSelectionModel().getSelectedItem().neighbors);
					System.out.println(map2Dropdown.getSelectionModel().getSelectedItem().neighbors);

				}
			}

		});

		deleteEdge.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {

				resetAllVariables();
				shouldDeleteEdge = true;
				edgeOptions.setText("Deleting Edge");

				// Delete edge event handler created.
				imageCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {

						if (!shouldDeleteEdge) {
							imageCanvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
						}

						if (!oneSelected) {

							for (int i = 0; i < mapNodes.size(); i++) {

								if (mapNodes.get(i).xPos >= event.getX() - 5 - nodeSizeReg
										&& mapNodes.get(i).xPos <= event.getX() - 5 + nodeSizeReg
										&& mapNodes.get(i).yPos >= event.getY() - 5 - nodeSizeReg
										&& mapNodes.get(i).yPos <= event.getY() - 5 + nodeSizeReg) {
									oneSelected = true;
									firstNodeLoc = i;
									highlightNode(i);
									break;

								}

							}
						} else if (oneSelected) {

							for (int i = 0; i < mapNodes.size(); i++) {

								if (mapNodes.get(i).xPos >= event.getX() - 5 - nodeSizeReg
										&& mapNodes.get(i).xPos <= event.getX() - 5 + nodeSizeReg
										&& mapNodes.get(i).yPos >= event.getY() - 5 - nodeSizeReg
										&& mapNodes.get(i).yPos <= event.getY() - 5 + nodeSizeReg) {
									twoSelected = true;
									highlightNode(i);
									secondNodeLoc = i;
									break;

								}
							}
						}

						if (oneSelected && twoSelected) {

							oneSelected = false;
							twoSelected = false;

							if (mapNodes.get(firstNodeLoc).neighbors.contains(mapNodes.get(secondNodeLoc))
									&& mapNodes.get(secondNodeLoc).neighbors.contains(mapNodes.get(firstNodeLoc))) {
								mapNodes.get(firstNodeLoc).neighbors.remove(mapNodes.get(secondNodeLoc));
								mapNodes.get(secondNodeLoc).neighbors.remove(mapNodes.get(firstNodeLoc));
								clearCanvas();
								renderEverything();

								event.consume();
							}

							firstNodeLoc = -1;
							secondNodeLoc = -1;
							renderEverything();
						} else {
							twoSelected = false;
						}

					}

				});

			}

		});

	}

	protected void clearCanvas() {
		GraphicsContext gc = imageCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, imageCanvas.getWidth(), imageCanvas.getHeight());
	}

	protected void renderNodes() {
		for (Node n : mapNodes) {
			GraphicsContext gc = imageCanvas.getGraphicsContext2D();
			gc.setFill(Color.RED);
			gc.fillOval(n.xPos, n.yPos, nodeSizeReg, nodeSizeReg);
		}

	}

	protected void fixEdges(Node node) {
		for (Node n : mapNodes) {
			if (n.neighbors.contains(node)) {
				n.neighbors.remove(node);
			}
		}
	}

	protected void showNodeData() {

		System.out.println("Showing node data.");

		for (int i = 0; i < mapNodes.size(); i++) {
			System.out.println(mapNodes.get(i).nodeName);
			System.out.println(mapNodes.get(i).description);
			System.out.println(mapNodes.get(i).isTransitionNode);
		}

	}

	private static void saveMapNodes(String fileName) {

		try {
			FileWriter writer = new FileWriter(fileName);

			for (int i = 0; i < mapNodes.size(); i++) {
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
				writer.append(",");
				writer.append(mapNodes.get(i).type);
				writer.append("\n");

				System.out.println("Print all map nodes.");
				System.out.println(mapNodes.get(i).map);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void renderEdges() {

		GraphicsContext gc = imageCanvas.getGraphicsContext2D();

		for (int i = 0; i < mapNodes.size() - 1; i++) {
			for (int j = 0; j < mapNodes.get(i).neighbors.size(); j++) {

				// Checks if both nodes are on the same map.
				if (mapNodes.get(i).map.equals(mapNodes.get(i).neighbors.get(j).map)){
					int x1 = mapNodes.get(i).xPos + nodeSizeReg/2;
					int y1 = mapNodes.get(i).yPos + nodeSizeReg/2;
					int x2 = mapNodes.get(i).neighbors.get(j).xPos + nodeSizeReg/2;
					int y2 = mapNodes.get(i).neighbors.get(j).yPos + nodeSizeReg/2;
					gc.setLineWidth(4);
					gc.setStroke(Color.BLUE);
					gc.strokeLine(x1, y1, x2, y2);
				} else {
					// Nothing...
				}

			}

		}
	}

	private static void connectEdgesFromFile(List<Node> nodeList, String filePath) {
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";
		int edgeX1Index = 0;
		int edgeY1Index = 1;
		int edgeX2Index = 4;
		int edgeY2Index = 5;
		int edge1map = 3;
		int edge2map =7;
		String map1 ="";
		String map2="";
		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] edgeData = line.split(delimiter);
				int x1 = Integer.parseInt(edgeData[edgeX1Index]);
				int y1 = Integer.parseInt(edgeData[edgeY1Index]);
				int x2 = Integer.parseInt(edgeData[edgeX2Index]);
				int y2 = Integer.parseInt(edgeData[edgeY2Index]);
				map1 = edgeData[edge1map];
				map2 = edgeData[edge2map];
				System.out.println(map1+" "+map2);

				// System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
				Node n1 = findNodeByXY(nodeList, x1, y1);
				Node n2 = findNodeByXY(nodeList, x2, y2);
				// System.out.println("********************");
				// System.out.println(n1.map);
				// System.out.println(n2.map);
				// System.out.println("********************");

				if (n1 != null && n2 != null) {
					if (n1.neighbors == null) {
						n1.neighbors.add(n2);

					} else {
						n1.neighbors.add(n2);
					}

				} else {
					System.out.println("Transition");
					Node node = new Node("",x2,y2,0,map2,"","");
					tempMapTransitionNodes.add(node);
					n1.neighbors.add(node);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Here"+tempMapTransitionNodes);
	}

	public static Node findNodeByXY(List<Node> nodeList, int x, int y)// Want to
	// change
	// this
	// to
	// throwing
	// an
	// exception
	// when
	// the
	// node
	// is
	// not
	// found
	{
		for (Node n : nodeList) {
			if (n.xPos == x && n.yPos == y) {
				return n;
			}
		}
		return null;
	}

	private void getNodesFromFile1(String filePath) {
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
		int nodeTypeIndex = 7;
		String type;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] nodeData = line.split(delimiter);
				String name = nodeData[nodeNameIndex];
				int x = Integer.parseInt(nodeData[nodeXIndex]);
				int y = Integer.parseInt(nodeData[nodeYIndex]);
				int z = Integer.parseInt(nodeData[nodeZIndex]);
				String map = nodeData[nodeMapIndex];
				String description = nodeData[nodeDescIndex];
				if (nodeData[nodeTransIndex] != null) {
					isTrans = Boolean.valueOf(nodeData[nodeTransIndex]);
				}
				if(nodeData[nodeTypeIndex] == null){
					type = "None";
				} else {
					type = nodeData[nodeTypeIndex];
				}


				Node newNode = new Node(name, x, y, z, map, description,type);
				newNode.isTransitionNode = isTrans;
				nodeList.add(newNode);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		mapNodes = nodeList;
	}

	private void getNodesFromFile2(String filePath) {
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
		int nodeTypeIndex = 7;
		String type;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] nodeData = line.split(delimiter);
				String name = nodeData[nodeNameIndex];
				int x = Integer.parseInt(nodeData[nodeXIndex]);
				int y = Integer.parseInt(nodeData[nodeYIndex]);
				int z = Integer.parseInt(nodeData[nodeZIndex]);
				String map = nodeData[nodeMapIndex];
				String description = nodeData[nodeDescIndex];
				if (nodeData[nodeTransIndex] != null) {
					isTrans = Boolean.valueOf(nodeData[nodeTransIndex]);
				}

				if(nodeData[nodeTypeIndex] != null){
					type = nodeData[nodeTypeIndex];
				} else {
					type = "None";
				}

				Node newNode = new Node(name, x, y, z, map, description,type);
				newNode.isTransitionNode = isTrans;
				nodeList.add(newNode);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		mapNodes2 = nodeList;
	}

	private void checkForTransNodes(List<Node> mapNodesNumber, List<Node> transNodeStore) {

		transNodeStore.clear();

		for (Node n : mapNodesNumber) {
			if (n.isTransitionNode == true) {
				transNodeStore.add(n);

			}
		}
	}

	private static void saveMapEdges(String fileName) {

		try {
			FileWriter writer = new FileWriter(fileName);

			for (int i = 0; i < mapNodes.size(); i++) {
				for (int j = 0; j < mapNodes.get(i).neighbors.size(); j++) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void renderTransitionNodes() {
		for (Node n : mapNodes) {
			if (n.isTransitionNode == true) {
				GraphicsContext gc = imageCanvas.getGraphicsContext2D();
				gc.setFill(Color.BLUE);
				gc.fillRect(n.xPos - .75, n.yPos - .75, nodeSizeReg+10, nodeSizeReg+10);
				gc.setFill(Color.WHITE);
				gc.fillOval(n.xPos + 4, n.yPos + 4, nodeSizeReg, nodeSizeReg);
			}
		}
	}

	public void renderEverything() {
		renderEdges();
		renderNodes();
		renderTransitionNodes();
	}

	public void resetSaveButton() {
		genSupermap.setText("Save Map");
		genSupermap.setTextFill(Color.WHITE);
	}

	public void resetAllVariables() {

		// Resets the save map button.
		resetSaveButton();

		// Resets booleans for user's current action.
		shouldAddNode = false;
		shouldDeleteNode = false;
		shouldModifyNode = false;
		shouldAddEdge = false;
		shouldDeleteEdge = false;

		// Resets the name of the Node and Edge menus.
		nodeOptions.setText("Node Options");
		edgeOptions.setText("Edge Options");

		// Selected node booleans.
		oneSelected = false;
		twoSelected = false;

		// Reset the number of clicks.
		numberClicks = 0;

		// Reset node locations.
		firstNodeLoc = -1;
		secondNodeLoc = -1;
	}

	public void setTypes(){
		typeList.add("BathroomW");
		typeList.add("BathroomM");
		typeList.add("Elevator");
		typeList.add("Entrance");
		typeList.add("Room");
		typeList.add("Stairs");
		typeBox.setItems(FXCollections.observableArrayList(typeList));

	}

	public void highlightNode(int iterator){
		GraphicsContext gc = imageCanvas.getGraphicsContext2D();

		if (mapNodes.get(iterator).isTransitionNode) {
			gc.setFill(Color.GOLD);
			gc.fillRect((double) mapNodes.get(iterator).xPos-.75, (double) mapNodes.get(iterator).yPos-.75, nodeSizeReg+10, nodeSizeReg+10);
			gc.setFill(Color.WHITE);
			gc.fillOval((double) mapNodes.get(iterator).xPos + 4, (double) mapNodes.get(iterator).yPos + 4, nodeSizeReg,
					nodeSizeReg);
		} else {
			gc.setFill(Color.GOLD);
			gc.fillOval((double) mapNodes.get(iterator).xPos, (double) mapNodes.get(iterator).yPos, nodeSizeReg, nodeSizeReg);
			gc.setFill(Color.RED);
			gc.fillOval((double) mapNodes.get(iterator).xPos + nodeSizeReg/7.5, (double) mapNodes.get(iterator).yPos + nodeSizeReg/7.5, nodeSizeReg/1.36,
					nodeSizeReg/1.36);
		}
	}


}