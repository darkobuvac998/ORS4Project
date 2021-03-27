package application.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerMain extends Application {
	
	@SuppressWarnings("exports")
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("views/ServerView.fxml")); 
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource(
					"/application/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Server");
			primaryStage.setResizable(false);
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(enent ->{
				System.exit(0);
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		
		launch(args);
		
	}

}
