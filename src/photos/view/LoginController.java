package photos.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photos.models.Album;
import photos.models.Photo;
import photos.models.User;
import photos.models.Users;

/**
 * LoginController represents the functionality for the Login Page. Attributes
 * include an ObservableList of Strings, an ArrayList of Strings, userList, and
 * a static String, uname. Methods include loginUser(), which populates an
 * ArrayList of Strings, userList, with pre-existing users. If the username is
 * recognized in the userList, the user will be redirected accordingly. If not,
 * an alert will be generated to alert the user that such a username is not in
 * the system. The loadWindow() method redirects the user to their respective
 * scene, when called.
 * 
 * @author Ahmed Elgazzar, Samantha Ames
 */
public class LoginController {

	// button
	@FXML
	Button loginBtn;

	// text field - user name
	@FXML
	TextField username;

	// label - user name
	@FXML
	Label user;

	private ObservableList<String> obsList;
	public ArrayList<String> userList;

	static String uname = "";

	/**
	 * loginUser() method takes ActionEvent as parameter, which is activated by the
	 * user clicking on the loginBtn. An ArrayList of Strings, userList, is
	 * populated by pre-existing list of users. If the text field for the username
	 * is empty, an alert is generated to notify the user. If a username that is not
	 * present in userList is entered, an alert is generated to notify the user.
	 * Otherwise, if the name entered is "admin," loadWindow() is called and
	 * redirects the user to the Admin Subsystem. If the name entered is "stock,"
	 * then the user is redirected to an album page containing the stock album.
	 * Otherwise, if the username is recognized in the userList, loadWindow() is
	 * called and the user is redirected to the non-admin user subsystem.
	 * 
	 * @param event
	 * @throws IOException
	 */

	@FXML
	private void loginUser(ActionEvent event) throws IOException {

		ArrayList<String> userList = new ArrayList<String>();
//		userList.add("admin");
//		userList.add("stock");
//		userList.add("user1");
//		userList.add("user2");
		for (int i = 0; i < Users.users.size(); i++) {
			System.out.println(Users.users.get(i).getUsername());
			userList.add(Users.users.get(i).getUsername());
		}
		String name = username.getText();
		obsList = FXCollections.observableArrayList(userList);
		this.userList = userList;

		// if text field is empty, alert
		if (name.isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Fill required field");
			alert.showAndWait();
		} else {

			if (name.contentEquals("admin")) {

				// if admin, redirect to admin ui
				LoginController.uname = name;
				((Node) event.getSource()).getScene().getWindow().hide();
				loadWindow("/photos/view/admin.fxml", "Administrative Page");

			} else if (name.contentEquals("stock")) {
				if (AlbumPageController.isFirstLogin != true) {

					AlbumPageController.isFirstLogin = true;

					ArrayList<Album> stockAlbums = new ArrayList<>();
					int index = -1;
					for (int i = 0; i < Users.users.size(); i++) {
						if (Users.users.get(i).getUsername().equals("stock")) {
							index = i;
							break;
						}
					}

					User stockUser = null;
					if (index != -1) {
						System.out.println("stocker");
						ArrayList<Photo> stockPhotos = new ArrayList<>();
						Photo stockPhoto1 = new Photo("StockPhoto1.PNG");
						Photo stockPhoto2 = new Photo("StockPhoto2.PNG");
						Photo stockPhoto3 = new Photo("StockPhoto3.PNG");
						Photo stockPhoto4 = new Photo("StockPhoto4.gif");
						Photo stockPhoto5 = new Photo("StockPhoto5.jpeg");
						stockPhoto1.setDateTaken(Calendar.getInstance());
						stockPhoto2.setDateTaken(Calendar.getInstance());
						stockPhoto3.setDateTaken(Calendar.getInstance());
						stockPhoto4.setDateTaken(Calendar.getInstance());
						stockPhoto5.setDateTaken(Calendar.getInstance());
						stockPhotos.add(stockPhoto1);
						stockPhotos.add(stockPhoto2);
						stockPhotos.add(stockPhoto3);
						stockPhotos.add(stockPhoto4);
						stockPhotos.add(stockPhoto5);
						for (int i = 0; i < 5; i++) {
							System.out.println(stockPhotos.get(i).getPath());
						}

						stockAlbums.add(new Album("Stock Album", stockPhotos));
						stockUser = new User("stock", stockAlbums);
					} else {
						stockUser = Users.users.get(index);
					}
//					if (stockUser.getAlbums() == null) {
//						ArrayList<Photo> stockPhotos = new ArrayList<>();
//						Photo stockPhoto1 = new Photo("feat.PNG");
//						Photo stockPhoto2 = new Photo("StockPhoto2.PNG");
//						Photo stockPhoto3 = new Photo("StockPhoto3.PNG");
//						Photo stockPhoto4 = new Photo("StockPhoto4.gif");
//						Photo stockPhoto5 = new Photo("StockPhoto5.jpeg");
//						stockPhoto1.setDateTaken(Calendar.getInstance());
//						stockPhoto2.setDateTaken(Calendar.getInstance());
//						stockPhoto3.setDateTaken(Calendar.getInstance());
//						stockPhoto4.setDateTaken(Calendar.getInstance());
//						stockPhoto5.setDateTaken(Calendar.getInstance());
//						stockPhotos.add(stockPhoto1);
//						stockPhotos.add(stockPhoto2);
//						stockPhotos.add(stockPhoto3);
//						stockPhotos.add(stockPhoto4);
//						stockPhotos.add(stockPhoto5);
//
//						stockAlbums.add(new Album("Stock Album", stockPhotos));
//					}
					for (int i = 0; i < Users.users.size(); i++) {

						if (Users.users.get(i).getUsername().equals("stock")) {

							stockAlbums.get(0).updateCoverPath();
							System.out.println(stockAlbums.get(0).getCoverPath());
							Users.users.get(i).setAlbums(stockAlbums);

							Users.save();
							break;

						}
					}
				}

				System.out.println("stock redirect");
				LoginController.uname = name;
				((Node) event.getSource()).getScene().getWindow().hide();
				AlbumPageController.currentUser = name;

				loadWindow("/photos/view/albumPage.fxml", "Stock Album-Select Page");
			} else if (name.contentEquals("album")) {
				AlbumPageController.currentUser = "sampleUser";
				LoginController.uname = name;
				((Node) event.getSource()).getScene().getWindow().hide();
				loadWindow("/photos/view/albumPage.fxml", "Album-Select Page");
			} else {
				boolean userExists = false;
				System.out.println("");
				for (int i = 0; i < userList.size(); i++) {

					// if user name is in system, redirect to user ui
					if (name.contentEquals(userList.get(i))) {
						System.out.println("user redirect");
						LoginController.uname = name;
						((Node) event.getSource()).getScene().getWindow().hide();
						AlbumPageController.currentUser = name;

						loadWindow("/photos/view/albumPage.fxml", "Album-Select Page");
						userExists = true;
					}
				}
				// if user name not found, alert
				if (!userExists) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setContentText("Please enter correct credentials");
					alert.showAndWait();
				}

			}
		}

	}

	/**
	 * loadWindow() void method takes String parameters for the location of
	 * re-direction and the title of the page that will be loaded.
	 * 
	 * @param location path of FXML file for re-direction destination
	 * @param title    title for page of re-direction
	 * @throws IOException
	 */

	private void loadWindow(String location, String title) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource(location));
		Scene scene = new Scene(root);
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene(scene);
		stage.setTitle(title);
		stage.show();
	}
}