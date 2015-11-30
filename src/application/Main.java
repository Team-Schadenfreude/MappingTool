package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class Main extends Application {
	
    private Pane rootLayout;
    public static Stage primaryStage;

    @Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			
			initRootLayout();
			 if(MainController.mapLoaded){
				 System.out.println("Resizing");
				 primaryStage.setResizable(true);
				 primaryStage.setWidth(1200);
				 primaryStage.setHeight(800);
				 primaryStage.setMinWidth(1200);
				 primaryStage.setMinHeight(800);

				// primaryStage.set
			 }
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("RootLayout.fxml"));
            rootLayout = (Pane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Randy Advanced Mapping Peripheral (R.A.M.P");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
