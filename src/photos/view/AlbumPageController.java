package photos.view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photos.models.Album;
import photos.models.User;
import photos.models.Users;

/**
 * AlbumPageController class represents the functionality for the Album Page.
 * Its attributes include an int for the index for the listeners, an ArrayList
 * of Users, an ArrayList of Albums for the current, respective user, an Album
 * currAlbum that represents the selected Album, a boolean isFirstLogin that
 * tracks whether or not the Stock User has logged in for the first time so the
 * app can recognize whether to load the original stock photo album or an
 * updated stock photo album, the method initialize(), which populates the Album
 * ArrayList with the current user's respective albums, and the handleChange()
 * method which provides the functionality for the user to create a new album,
 * delete an existing album, rename an existing album, open a selected album,
 * search for tags by date or tag values, logout of the application while saving
 * any changes made by the user, or quit the application while saving any
 * changes made by the user.
 * 
 * @author Ahmed Elgazzar, Samantha Ames
 *
 */
public class AlbumPageController {

	@FXML
	VBox vbox;
	@FXML
	ScrollPane sPane;
	@FXML
	TextField textArea;
	@FXML
	Button createBtn;
	@FXML
	Button deleteBtn;
	@FXML
	Button renameBtn;
	@FXML
	Button openBtn;
	@FXML
	Button searchBtn;
	@FXML
	Button returnAlbumBtn;
	@FXML
	Button logOutBtn;
	@FXML
	Button quitBtn;

	int albumIndex = -1;// Index for Listners

	Users users = new Users(); // instance used for quick access to static User List

	ArrayList<Album> albumList; // List of Albums for a user

	Album currAlbum = null;

	public static String currentUser = null; // Keep track of User

	public static boolean isFirstLogin;

	/**
	 * initialize() void method creates an ArrayList of users and populates it with
	 * pre-existing users. If the username of the current user is recognized in the
	 * ArrayList, and a subsequent ArrayList of Albums is recognized as being
	 * associated with the user, then the Album list is populated and displayed in a
	 * VBox.
	 * 
	 */

	public void initialize() {

		VBox newVbox = new VBox();
		ArrayList<User> users = Users.users;

		User currUser = null;

		for (int i = 0; i < users.size(); i++) {
			currUser = users.get(i);
			if (currUser.getUsername().equals(currentUser)) {
				System.out.println("found");
				break;
			}
		}
		ArrayList<Album> albumList = null;
		if (currUser != null) {
			albumList = currUser.getAlbums();
			if (albumList != null) {
				this.albumList = albumList;
			} else {
				this.albumList = new ArrayList<Album>();
			}

		}

		updateAlbumList(this.albumList);
	}

	/**
	 * handleChange() void method takes an ActionEvent that is triggered by one of
	 * the following buttons being clicked: createBtn creates a new Album and adds
	 * it to an ArrayList of Albums, deleteBtn deletes the selected Album and
	 * removes it from the ArrayList of Albums, renameBtn takes in the input entered
	 * into the text field and renames the selected Album with said input, openBtn
	 * opens the selected Album and redirects to the InsideAlbum interface,
	 * searchBtn opens a window that allows the user to search their albums for any
	 * photos associated with a given date range or tag-value pairs, returnAlbumBtn
	 * allows the user to return from the photo search to their Album page,
	 * logOutBtn logs the user out while saving any changes made during the session,
	 * quitBtn quits the application while saving any changes the user has made
	 * during the sessions.
	 * 
	 * @param e ActionEvent triggered by clicking one of the available buttons
	 * @throws IOException
	 */

	public void handleChange(ActionEvent e) throws IOException {
		Button btnType = (Button) e.getSource();

		if (btnType == createBtn) {
			// create alert with fields

			int NACheck = 0;
			for (int i = 0; i < albumList.size(); i++) {

				if (albumList.get(i).getName().contains("New Album")) {
					if (NACheck < Integer.parseInt(albumList.get(i).getName().substring(10))) {
						NACheck = Integer.parseInt(albumList.get(i).getName().substring(10));
					}

				}

			}
			NACheck++;
			albumList.add(new Album("New Album " + NACheck, null));
//			System
			for (int i = 0; i < Users.users.size(); i++) {
				if (Users.users.get(i).getUsername().equals(currentUser)) {
					Users.users.get(i).setAlbums(albumList);
				}
			}
			updateAlbumList(albumList);

		} else if (btnType == deleteBtn) {
			System.out.println("Deleted");
			System.out.println(currAlbum);
			if (deleteAlbum(currAlbum)) {
				updateAlbumList(albumList);
			} else {
				Alert notFound = new Alert(AlertType.WARNING);
				if (albumList.size() > 0) {
					notFound.setContentText("Select an Album");
				} else {
					notFound.setContentText("Nothing to Delete!");
				}

				notFound.show();

			}
//			System.out.println(albumIndex);

		} else if (btnType == renameBtn) {
			System.out.println(currAlbum);

			if (currAlbum == null) {
				Alert notFound = new Alert(AlertType.WARNING);
				if (albumList.size() == 0) {
					notFound.setContentText("No Albums");
				} else {
					notFound.setContentText("No Album Selected");
				}

				notFound.show();

			} else if (textArea.getText().trim().equals("")) {

				// Add Alert

				Alert notFound = new Alert(AlertType.WARNING);
				notFound.setContentText("Field is Empty");
				notFound.show();
			} else {
				currAlbum.setName(textArea.getText().trim());
				textArea.setText("");
				updateAlbumList(albumList);

			}

		} else if (btnType == openBtn) {
			System.out.println("Open Scene");

			if (currAlbum != null) {
				InsideAlbumController.currentAlbum = currAlbum;
				InsideAlbumController.currentUser = currentUser;
				;
				((Node) e.getSource()).getScene().getWindow().hide();

				Parent root = FXMLLoader.load(getClass().getResource("/photos/view/insidealbum.fxml"));
				Scene scene = new Scene(root);
				Stage stage = new Stage(StageStyle.DECORATED);
				stage.setScene(scene);
				stage.setTitle("Album");
				stage.show();

			} else if (currAlbum == null) {
				Alert notFound = new Alert(AlertType.WARNING);
				if (albumList.size() == 0) {
					notFound.setContentText("No Albums");
				} else {
					notFound.setContentText("No Album Selected");
				}

				notFound.show();
			}

		} else if (btnType == searchBtn) {

			searchController.currentUser = this.currentUser;

			((Node) e.getSource()).getScene().getWindow().hide();
			Parent root = FXMLLoader.load(getClass().getResource("/photos/view/search.fxml"));
			Scene scene = new Scene(root);
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setScene(scene);
			stage.setTitle("Search Photos");
			stage.show();

		} else if (btnType == logOutBtn) {
			User currUser = null;
			for (int i = 0; i < Users.users.size(); i++) {

				currUser = Users.users.get(i);
				if (currUser.getUsername().equals(currentUser)) {
					currUser.setAlbums(albumList);
					break;
				}
			}

			((Node) e.getSource()).getScene().getWindow().hide();
			Parent root = FXMLLoader.load(getClass().getResource("/photos/view/login.fxml"));
			Logout.logout(root);

		} else if (btnType == quitBtn) {
			User currUser = null;
			for (int i = 0; i < Users.users.size(); i++) {

				currUser = Users.users.get(i);
				if (currUser.getUsername().equals(currentUser)) {
					currUser.setAlbums(albumList);
					break;
				}
			}
			Users.save();
			Platform.exit();
		}

	}

	/**
	 * updateAlbumList() void method takes in an Album ArrayList and when called,
	 * updates the main Album ArrayList of the user so that these changes may be
	 * reflected in the user Album Page interface.
	 * 
	 * @param albumList ArrayList of Albums associated with the user
	 */

	public void updateAlbumList(ArrayList<Album> albumList) {
//		System.out.println(albumList.get(0).getPhotos().size());
		vbox.getChildren().setAll();
		currAlbum = null;

		if (albumList.size() == 0) {
			return;
		}

		for (int i = 0; i < albumList.size(); i++) {
			Image photo;
			ImageView image = new ImageView();
			if (albumList.get(i).getPhotos() == null) {
				System.out.println("null check ran");
				photo = new Image("Question_Mark.jpeg");
				image.setImage(photo);

			} else {
				albumList.get(i).updateCoverPath();
				String photoPath = albumList.get(i).getCoverPath();
//				System.out.println(">>>>" + photoPath);
				if (photoPath != null) {
					photo = new Image("file:" + photoPath);
					Image photo2 = photo;
					try {

						photo2 = new Image(photoPath);

					} catch (IllegalArgumentException ex) {
						photo2 = photo;
					}

//					System.out.println(photo2.canRead());
					System.out.println(photo.getWidth());
					System.out.println(photo2.getWidth());
					if (photo2.getWidth() > photo.getWidth()) {
						photo = photo2;
					}
					image.setImage(photo);
				} else {
					photo = new Image("Question_Mark.jpeg");
					image.setImage(photo);
				}

			}

			int index = i;
			// System.out.println(index);
			image.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event e) {

					// System.out.println(photoPath);
					currAlbum = albumList.get(index);
					InnerShadow innerShadow = new InnerShadow();
					innerShadow.setOffsetX(4);
					innerShadow.setOffsetY(4);
					innerShadow.setColor(Color.web("0x3b596d"));
					image.setEffect(innerShadow);
					albumIndex = index * 2;
					// System.out.println(albumIndex);
					for (int j = 0; j < vbox.getChildren().size(); j++) {
						// System.out.println();
						if (j != albumIndex) {
							// System.out.println("ran at: "+ j);
							vbox.getChildren().get(j).setEffect(null);
						}

					}

				}

			});

			image.setFitHeight(200);
			image.setFitWidth(200);
			image.setOpacity(1);

			vbox.getChildren().add(image);

			TextArea text = new TextArea();
			text.setText(albumList.get(i).getName());
			text.setEditable(false);
			text.setMaxHeight(30);
			text.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event e) {
					System.out.println(textArea.getText().trim());

					if (!textArea.getText().trim().equals("")) {
						text.setText(textArea.getText().trim());
						// have to set current stuff in array to correct title...
						textArea.setText("");
					}

				}

			});
			vbox.getChildren().add(text);

		}
		Users.save();

	}

	/**
	 * deleteAlbum() boolean method takes in an Album as input, and when called,
	 * removes the album from the User's Album ArrayList, returning "true" if the
	 * Album was successfully removed from the list and "false" otherwise.
	 * 
	 * @param album Album to be removed
	 * @return "true" if the Album was successfully removed from the list and
	 *         "false" otherwise
	 */

	public boolean deleteAlbum(Album album) {
//		System.out.println(">>>" + album);
		if (albumList != null && album != null) {
			for (int i = 0; i < albumList.size(); i++) {
				if (albumList.get(i).getName().equals(album.getName())) {
//					System.out.println("found at: " + i);
					albumList.remove(i);
					return true;
				}
			}
		}
		return false;
	}

}
