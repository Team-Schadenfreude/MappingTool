package MappingTool.view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import MappingTool.Node;
import MappingTool.model.NodeList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class InfoboxController {
	@FXML
	private TextField na;
	@FXML
	private TextField desc;
	@FXML
	private RadioButton wheely;
	@FXML
	private TextField xpos;
	@FXML
	private TextField ypos;
	@FXML
	private Label label1;
	NodeList mapNodes = new NodeList();

	@FXML
	public void addDataToNode(MouseEvent e) {
		if(label1.getOpacity()==1.0){
			label1.setOpacity(0.0);
		}
		String name = na.getText();
		String description = desc.getText();
		boolean wheelchair = wheely.isPressed();
		if(xpos.getText().equals("")||ypos.getText().equals("")){
			label1.setOpacity(1.0);
			return;
		}
		int x= Integer.parseInt(xpos.getText());
		int y = Integer.parseInt(ypos.getText());
		
	

		for (String s : mapNodes.getNames()) {

			if (s == name) {
				mapNodes.delete(name);

			}
		}
		
		mapNodes.addNode(name, new Node(name,wheelchair, x, y, description));
		na.clear();
		desc.clear();
		xpos.clear();
		ypos.clear();
		System.out.print(((NodeList) mapNodes).getSize());
		saveMapNodes("src/res/mapNodes.csv");

	}

	private void saveMapNodes(String fileName) {

		try {
			FileWriter writer = new FileWriter(fileName);

			for (String s : mapNodes.getKeys()) {
				writer.append(mapNodes.get(s).nodeName);
				writer.append(',');
				writer.append(Integer.toString(mapNodes.get(s).xPos));
				writer.append(',');
				writer.append(Integer.toString(mapNodes.get(s).yPos));
				writer.append(',');
				writer.append(mapNodes.get(s).description);
				writer.append("\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getNodesFromFile(String filePath) {
		NodeList nodeList = new NodeList();
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";
		int nodeNameIndex = 0;
		int nodeXIndex = 1;
		int nodeYIndex = 2;
		int nodeDescIndex = 3;

		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] nodeData = line.split(delimiter);
				String name = nodeData[nodeNameIndex];
				int x = Integer.parseInt(nodeData[nodeXIndex]);
				int y = Integer.parseInt(nodeData[nodeYIndex]);
				String description = nodeData[nodeDescIndex];
				Node newNode = new Node(name, 0, 0, 0, false, x, y, description);
				nodeList.addNode(newNode);
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

}
