package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class ClientMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("views/LogInView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("LogIn - EuroBank");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(event ->{
				System.exit(0);
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		//komentar za git
	}
	public static void main(String[] args) {
		launch(args);
	}
}
