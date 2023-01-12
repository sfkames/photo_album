package photos.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import photos.models.Users;

/**
 * Photos class extends application with start method and main method to start
 * the application.
 * 
 * @author Ahmed Elgazzar, Samantha Ames
 */

public class Photos extends Application {

	/**
	 * start() void method takes Stage object, primaryStage, and uses FXMLLoader,
	 * loader, to load login.fxml, whose controller is LoginController.
	 * 
	 * @param primaryStage the primary stage of the application for user login
	 */

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
//		Users.load();
		System.out.println(new Users().toString());
//		Users.users.add(new User("new User", new ArrayList<Album>()));
//		Users.save();

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/photos/view/login.fxml"));
		GridPane root = (GridPane) loader.load();
		Scene scene = new Scene(root, 400, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photos App Login");
		primaryStage.setResizable(false);
		primaryStage.show();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				Users.save(); // should I do this -- saves before user closes using x button
				Platform.exit();
				System.exit(0);
			}
		});
	}

	/**
	 * main() void method launches application
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
