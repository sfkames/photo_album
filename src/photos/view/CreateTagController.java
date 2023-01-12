package photos.view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photos.models.CustomTags;
import photos.models.Photo;
import photos.models.Tags;
import photos.models.Users;

/**
 * CreateTagController represents the functionality for the user's Create Tag
 * Page. Its attribute is a Photo object that tracks the photo associated with
 * the tags to be created. Its methods include handleChange(), which is
 * triggered by available buttons and creates one of three tag types with the
 * user's given tag value or returns the user back to the inside album page, or
 * loadWindow() which redirects the user to the destination with which it is
 * called.
 * 
 * @author samanthaames
 *
 */
public class CreateTagController {

	// button
	@FXML
	Button createBtn;
	Button returnBtn;

	// text fields
	@FXML
	TextField locationVal;
	@FXML
	TextField personVal;
	@FXML
	TextField customType;
	@FXML
	TextField customVal;

	public static Photo currentPhoto; // load from InsideAlbumController

//	public void initialize() throws IOException {
//		
//	}
	/**
	 * handleChange() void method takes an ActionEvent that is trigged by either the
	 * createBtn, which creates a new Tag object associated with the current Photo,
	 * and is either a "location" tag, "person" tag, or different custom tag, based
	 * on what the user has input, or the returnBtn, which redirects the user back
	 * to the inside album page. If a duplicate tag-value pair is attempted to be
	 * created, or if the required fields are empty, an alert is generated.
	 * 
	 * 
	 * @param e ActionEvent that is triggered by either the createBtn or returnBtn
	 * @throws IOException
	 */

	public void handleChange(ActionEvent e) throws IOException {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ERROR");
		alert.setHeaderText("Please fix the following: ");
		String content = "";
		ArrayList<Tags> tagList = currentPhoto.getTags();
		ArrayList<String> cusTags = new ArrayList<>();
		Button btnType = (Button) e.getSource();

		if (btnType == createBtn) {

			boolean duplicate = false;

			if ((locationVal.getText().trim().equals("") && personVal.getText().trim().equals("")
					&& customVal.getText().trim().equals(""))
					|| ((!customVal.getText().trim().equals("")) && customType.getText().trim().equals(""))
					|| ((customVal.getText().trim().equals("")) && !customType.getText().trim().equals(""))) {

				content = "Please fill required fields";
				alert.setContentText(content);
				alert.showAndWait();
			} else if (!personVal.getText().trim().isEmpty()
					&& (!customType.getText().trim().isEmpty() && !customVal.getText().trim().isEmpty())) {
				Alert alert2 = new Alert(AlertType.WARNING);
				alert2.setContentText("Can only create one tag at a time");
				alert2.show();
				return;
			} else {
				if (currentPhoto.getTags() == null) {
					tagList = new ArrayList<Tags>();
				}
				if (!locationVal.getText().trim().isEmpty()) {

					for (int i = 0; i < tagList.size(); i++) {
						tagList = currentPhoto.getTags();
						Tags tag = tagList.get(i);
						if (tag.getTagType() == "location") {
							duplicate = true;
							content = "Location Tag Already Exists";
							alert.setContentText(content);
							alert.showAndWait();
						}
					}
					if (!duplicate) {
						Tags newTag = new Tags(locationVal.getText().trim());
						tagList.add(newTag);
						currentPhoto.setTags(tagList);
						locationVal.clear();
					}
				}
				if (!personVal.getText().trim().isEmpty()) {

					ArrayList<String> people = new ArrayList<>();

					for (int i = 0; i < tagList.size(); i++) {
						tagList = currentPhoto.getTags();
						Tags tag = tagList.get(i);
						if (tag instanceof CustomTags) {
							CustomTags custom = (CustomTags) tag;
							if (tag.getTagType().equalsIgnoreCase("person")) {

								if (custom.getCustomTags() == null) {
									people = new ArrayList<String>();
								} else {
									for (int j = 0; j < custom.getCustomTags().size(); j++) {

										people = custom.getCustomTags();

										if (people.get(j).equalsIgnoreCase(personVal.getText().trim())) {
											duplicate = true;
											content = "Duplicate Tag";
											alert.setContentText(content);
											alert.showAndWait();
										}
									}
								}
							}
						}
					}
					if (!duplicate) {
						cusTags.add(personVal.getText().trim());
						CustomTags newTag = new CustomTags(cusTags);
						tagList.add(newTag);
						currentPhoto.setTags(tagList);
						personVal.clear();
					}
				}
			}

			if (!customType.getText().trim().isEmpty() && !customVal.getText().trim().isEmpty()) {

				ArrayList<String> customList = new ArrayList<>();

				for (int i = 0; i < tagList.size(); i++) {
					tagList = currentPhoto.getTags();
					Tags tag = tagList.get(i);
					if (tag instanceof CustomTags) {
						CustomTags custom = (CustomTags) tag;
						if (tag.getTagType().equalsIgnoreCase(customType.getText().trim())) {

							if (custom.getCustomTags() == null) {
								customList = new ArrayList<String>();
							} else {
								for (int j = 0; j < custom.getCustomTags().size(); j++) {

									customList = custom.getCustomTags();

									if (customList.get(j).equalsIgnoreCase(customVal.getText().trim())) {
										duplicate = true;
										content = "Duplicate Tag";
										alert.setContentText(content);
										alert.showAndWait();
										break;
									}
								}
							}
						}
					}
				}
				if (!duplicate) {

					cusTags.add(customVal.getText().trim());
//					ArrayList<String> copy = (ArrayList<String>) cusTags.clone();
//					copy.remove(0);
					CustomTags newTag = new CustomTags(customType.getText().trim(), cusTags.get(0), cusTags);
					tagList.add(newTag);
					currentPhoto.setTags(tagList);
					customType.clear();
					customVal.clear();
				}
			}

		} else {
			Users.save();
			((Node) e.getSource()).getScene().getWindow().hide();
			loadWindow("/photos/view/insidealbum.fxml", "Album");
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