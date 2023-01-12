package photos.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photos.models.Users;

/**
 * Logout class contains logout() method that saves the information of the
 * current user's session and returns the user to the Login scene.
 * 
 * @author Ahmed Elgazzar, Samantha Ames
 */
public class Logout {
	/**
	 * logout() method that saves the information of the current user's session and
	 * returns the user to the Login scene.
	 * 
	 * @param root Parent node of the current scene
	 * @throws IOException
	 */

	@FXML
	public static void logout(Parent root) throws IOException {
		Users.save();

//		loadWindow("/photos/view/albumPage.fxml", "Album-Select Page");
//		System.out.println(root2.getClass().getResource("/photos/view/albumPage.fxml"));
//		Parent root = FXMLLoader.load(root2.getClass().getResource("/photos/view/albumPage.fxml"));
		Scene scene = new Scene(root);
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene(scene);
		stage.setTitle("Photos App Login");
		stage.show();
	}

}
