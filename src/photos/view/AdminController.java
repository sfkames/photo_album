package photos.view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import photos.models.User;
import photos.models.Users;

/**
 * AdminController class represents all functionality for the Admin Subsystem.
 * Its attributes include an ObservableList of String values for the users and
 * an ArrayList of Strings for the Users that populates the ObservableList. The
 * initialize() method populates a ListView with an ArrayList of users
 * pre-existing users. The handleChange() mmethod allows the admin user to add
 * or delete users, and to logout or quit the application.
 * 
 * @author Ahmed Elgazzar, Samantha Ames
 */
public class AdminController {

	// buttons
	@FXML
	Button addBtn;
	@FXML
	Button deleteBtn;
	@FXML
	Button logoutBtn;
	@FXML
	Button quitBtn;

	// add user name text field
	@FXML
	TextField addUser;

	// list of users
	@FXML
	ListView<String> listView;

	private ObservableList<String> obsList;
	public ArrayList<String> userList;

	/**
	 * 
	 * initialize() void method takes an ArrayList of Strings and populates it with
	 * the usernames of the application's pre-existing Users. This ArrayList of
	 * Strings, userList, then populates the ObservableList that populates the
	 * ListView.
	 * 
	 * @throws IOException
	 */

	public void initialize() throws IOException {

		ArrayList<String> userList = new ArrayList<String>();
		for (int i = 0; i < Users.users.size(); i++) {
			userList.add(Users.users.get(i).getUsername());
		}

//		userList.add("stock");

		this.userList = userList;

		// System.out.println(userList.get(0));
		// System.out.println(userList.get(1));

		obsList = FXCollections.observableArrayList(userList);

		listView.setItems(obsList);
	}

	/**
	 * handleChange() method takes in an ActionEvent that is initialized via the
	 * clicking of buttons. If the addBtn button is clicked and the addUser
	 * TextField contains input, the input will be taken as a String and if the
	 * String is unique to the UserList, it will then be added to both the UserList
	 * and ObservableList. Otherwise, alerts will populate if the text field is
	 * empty, or if the user already exists. If the deleteBtn is clicked and a user
	 * is selected from the ListView, unless the selected user is "admin" or
	 * "stock," that user will be deleted from both the UserList and the
	 * ObservableList. Otherwise, an alert will be generated for an invalid
	 * selection. If the logoutBtn is clicked, any changes made by the admin will
	 * save and the scene will be redirected to the login page. If the quitBtn is
	 * clicked, the application will close entirely will saving any changes made by
	 * the admin user.
	 * 
	 * @param e
	 * @throws IOException
	 */
	public void handleChange(ActionEvent e) throws IOException {

		// ArrayList<String> userList = new ArrayList<String>();

		Button btnType = (Button) e.getSource();

		if (btnType == addBtn) {

			String name = addUser.getText();

			// if text field is empty, alert
			if (name.isEmpty()) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Fill required field");
				alert.showAndWait();
			} else {
				boolean exists = false;

				// if user already exists, alert
				for (int i = 0; i < userList.size(); i++) {
					if (userList.get(i).equalsIgnoreCase(name)) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setHeaderText(null);
						alert.setContentText("User Already Exists");
						alert.showAndWait();
						exists = true;
						break;
					}
				}
				if (!exists) {
					userList.add(name);
					obsList.add(name);
					addUser.setText("");
					Users.users.add(new User(name, null));

				}

			}
		}

		// delete button
		else if (btnType == deleteBtn) {

			int index = -1;
			String name = obsList.get(listView.getSelectionModel().getSelectedIndex());

			// if list is empty, alert
			if (name.contentEquals("admin") || name.contentEquals("stock")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Invalid Selection");
				alert.showAndWait();
			} else {
				System.out.println("ran");

				for (int i = 0; i < userList.size(); i++) {

					if (userList.get(i).equals(name)) {
						System.out.println("found");
						index = userList.indexOf(i);
						userList.remove(i);

						int ListIndex = listView.getSelectionModel().getSelectedIndex();

						obsList.remove(ListIndex);

						if (ListIndex == obsList.size()) {
							listView.getSelectionModel().select(ListIndex - 1);
						} else {
							listView.getSelectionModel().select(ListIndex);
						}
						for (int j = 0; j < Users.users.size(); j++) {
							if (Users.users.get(j).getUsername().equals(name)) {
								Users.users.remove(j);
							}
						}
					}

				}
			}
		} else if (btnType == logoutBtn) {
			((Node) e.getSource()).getScene().getWindow().hide();
//			loadWindow("/photos/view/login.fxml", "Login Page");
			Parent root = FXMLLoader.load(getClass().getResource("/photos/view/login.fxml"));
			Logout.logout(root);
		} else { // quit
			Users.save();
			Platform.exit();

		}
		Users.save();
		try {
			Users.load();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}