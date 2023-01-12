package photos.view;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.application.Platform;
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
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photos.models.Album;
import photos.models.Photo;
import photos.models.Tags;
import photos.models.User;
import photos.models.Users;

/**
 * InsideAlbumController class provides all of the functionality of the page
 * that a user enters once they have chosen an Album to open, Inside Album Page.
 * Attributes include an ObservableList of images, an ArrayList of Strings that
 * are comprised of the associated attributes of a Photo object, a String,
 * currentUser which tracks the current user of that session, an ArrayList of
 * images, an ObservableList of images, an Album, currentAlbum which tracks the
 * album that has been opened, String currentTag which represents the selected
 * tag to be deleted, and Photo object currPhoto, which represents which photo
 * has been selected to be modified by the user. Methods include initialize(),
 * which populates an ArrayList of photos with the photos that belong to the
 * respective album, sets the first photo in the album or a different photo that
 * the user has selected to populate an ImageView in the center of the scene
 * with its corresponding attributes populating a ListView, and populates an
 * hbox at the bottom of the scene with all of the photos in the album with
 * their corresponding captions, to be scrolled through, handleChange() which is
 * triggered by the clicking of the available buttons that enable the user to
 * modify the photo's caption, tags, or location, loadWindow() which redirects
 * the user to the location with which it is called, updatePhotos() which
 * updates the Photos List and ObservableLists so that scene is always updated
 * with the most relevant information provided by the user, and formatUploads()
 * which modifies the String path associated with an uploaded photo so that it
 * may be correctly read for further use in the application.
 * 
 * 
 * @author Ahmed Elgazzar, Samantha Ames
 *
 */
public class InsideAlbumController {

	// buttons
	@FXML
	Button addPhotoBtn;
	@FXML
	Button removePhotoBtn;
	@FXML
	Button addEditCapBtn;
	@FXML
	TextField captionTF;
	@FXML
	Button addTagBtn;
	@FXML
	Button deleteTagBtn;
	@FXML
	Button copyPhotoBtn;
	@FXML
	Button movePhotoBtn;
	@FXML
	Button logoutBtn;
	@FXML
	Button quitBtn;
	@FXML
	Button returnAlbumBtn;

	// text field
	@FXML
	TextField albumName;

	// list view
	@FXML
	ListView<String> photoDetail;

	// main image view
	@FXML
	ImageView mainImage;

	// hbox
	@FXML
	HBox hboxImage;

	// scrollpane
	@FXML
	ScrollPane scroll;

	private List<Image> images = new ArrayList<>();
//	private Album sampleAlbum = new Album("Sample Album", null);
	private ObservableList<Image> obsImage;
	private ObservableList<String> obsInfo;

	public ArrayList<Photo> photos = new ArrayList<Photo>();
	public ArrayList<String> photoInfo;

	public static String currentUser;
	public static Album currentAlbum; // load from AlbumPageController
	public String currentTag = "";
	Photo currPhoto = null;

	/**
	 * initialize() void method populates the following components of the scene: an
	 * hbox displays all of the corresponding photos in a ScrollView with their
	 * respective captions, the center of the scene displays an ImageView of the
	 * first photo in the Photos ArrayList or whichever photo the user has selected,
	 * a ListView in the left pane displays the corresponding photo details of photo
	 * attributes of whichever photo is in the ImageView, and the right pane
	 * contains all of the buttons available to the user that trigger the
	 * handleChange() method.
	 * 
	 * @throws IOException
	 */

	public void initialize() throws IOException {
		photoInfo = new ArrayList<String>();
		System.out.println("current Album: " + currentAlbum.getPhotos());

		if (currentAlbum.getPhotos() == null) {
			currentAlbum.setPhotos(new ArrayList<Photo>());

		}
		photos = currentAlbum.getPhotos();
//		if(photos == null) {
//			currentAlbum.setPhotos(new ArrayList<Photo>());
//			p
//		}

		if (photos != null) {
			if (photos.size() > 0) {
				String caption = (photos.get(0).getCaption() != null) ? (photos.get(0).getCaption()) : ("[No Caption]");
				photoInfo.add(caption);
				String dateTaken = photos.get(0).getDateTaken();
				photoInfo.add(dateTaken);
				if (photos.get(0).getTags() != null) {
					for (int j = 0; j < photos.get(0).getTags().size(); j++) {
						String photoTag = photos.get(0).getTags().get(j).toString();
						photoInfo.add(photoTag);
					}
				}

			}
		}

		obsInfo = FXCollections.observableArrayList(photoInfo);
		if (photos != null) {
			for (int i = 0; i < photos.size(); i++) {
				if (photos.get(i).getPath().toString() != null) {
					System.out.println(photos.get(i).getPath().toString());
					Image image = new Image("file:" + photos.get(i).getPath().toString());
					Image image2 = null;

					try {
						image2 = new Image(photos.get(i).getPath().toString());
					} catch (IllegalArgumentException ex) {
						image2 = image;
					}

					if (image2.getWidth() > image.getWidth()) {
						image = image2;
					}
					images.add(image);

				}

			}

		}

		obsImage = FXCollections.observableArrayList(images);

		// display first image in album
		if (obsImage.size() > 0) {
			mainImage.setImage(obsImage.get(0));
			mainImage.setPreserveRatio(true);
			currPhoto = photos.get(0);

		}

		hboxImage.setSpacing(10);

		photoDetail.setItems(obsInfo);

		photoDetail.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			currentTag = newVal;
			System.out.println(newVal);
		});

		for (int i = 0; i < obsImage.size(); i++) {
			ImageView imageView = new ImageView();
			TextArea textArea = new TextArea();
			textArea.setMaxSize(150, 30);
			textArea.setMinSize(150, 30);
			textArea.setEditable(false);

			imageView.setOnMouseClicked(event -> {
				mainImage.setImage(imageView.getImage());
				System.out.println(imageView.getImage().getUrl());
				ArrayList<Photo> photos1 = currentAlbum.getPhotos();
				for (int j = 0; j < photos1.size(); j++) {

					if (imageView.getImage().getUrl().contains(photos1.get(j).getPath())) {
						System.out.println("print");
						currPhoto = photos.get(j);
						obsInfo.clear();
						photoDetail.getItems().clear();
						photoInfo.clear();
						String caption1 = (photos.get(j).getCaption() != null) ? (photos.get(j).getCaption())
								: ("[No Caption]");
						photoInfo.add(caption1);
						String dateTaken1 = photos1.get(j).getDateTaken();
						photoInfo.add(dateTaken1);
						if (photos1.get(j).getTags() != null) {
							for (int k = 0; k < photos1.get(j).getTags().size(); k++) {
								String photoTag = photos1.get(j).getTags().get(k).toString();
								photoInfo.add(photoTag);

							}

						}

						obsInfo = FXCollections.observableArrayList(photoInfo);

						photoDetail.setItems(obsInfo);
					}
				}
			});

			imageView.setImage(obsImage.get(i));
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(200);
			imageView.setFitWidth(300);
			String caption = (photos.get(i).getCaption() != null) ? (photos.get(i).getCaption()) : ("[No Caption]");
			textArea.setText(caption);

			hboxImage.getChildren().add(imageView);
			hboxImage.getChildren().add(textArea);

//			System.out.println(obsImage.get(i));
			System.out.println("size: " + photos.size());
		}

	}

	/**
	 * handleChange() void method takes an ActionEvent that is triggered by the
	 * following buttons: addPhotoBtn allows the user to upload photo files with the
	 * given extensions, removePhotoBtn removes the selected photo from the Photo
	 * ArrayList and ObservableList, addEditCapBtn allows the user to replace the
	 * current, selected photo's caption with their input from the corresponding
	 * text field, addTagBtn redirects the user to the Create Tag Page, deleteTagBtn
	 * removes the selected tag from the ListView and from the associated Photo's
	 * Tags List, copyPhotoBtn and movePhotoBtn take the input from the
	 * corresponding text field and either copy the photo to the specified album or
	 * move the photo to the specified album, respectively, the logoutBtn saves any
	 * changes made by the user and logs the user out of the session while
	 * redirecting to the Login Page, and quitBtn saves any changes made by the user
	 * while closing the application.
	 * 
	 * @param e ActionEvent that is triggered by the available buttons
	 * @throws IOException
	 */

	public void handleChange(ActionEvent e) throws IOException {

		ArrayList<Photo> photos = currentAlbum.getPhotos();
//		System.out.println("photos: " + photos);
		Button btnType = (Button) e.getSource();

		// add photo to album
		if (btnType == addPhotoBtn) {

			FileChooser fileChooser = new FileChooser();

			// set extension filter
			FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
			FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
			FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter("JPEG files (*.JPEG)",
					"*.JPEG");
			FileChooser.ExtensionFilter extFilterjpeg = new FileChooser.ExtensionFilter("jpeg files (*.jpeg)",
					"*.jpeg");
			FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
			FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
			FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("BMP files (*.BMP)", "*.BMP");
			FileChooser.ExtensionFilter extFilterbmp = new FileChooser.ExtensionFilter("bmp files (*.bmp)", "*.bmp");
			FileChooser.ExtensionFilter extFilterGIF = new FileChooser.ExtensionFilter("GIF files (*.GIF)", "*.GIF");
			FileChooser.ExtensionFilter extFiltergif = new FileChooser.ExtensionFilter("gif files (*.gif)", "*.gif");

			// show open file dialog
			File file = fileChooser.showOpenDialog(null);
			if (file == null) {
				return; // stop method if adding is aborted
			} else {
//				System.out.println("file PAth: >>>>>" + file.toURI().toString());
//				System.out.println("file PAth: >>>>>" + photos.get(0).getPath());

				for (int i = 0; i < photos.size(); i++) {
					if (file.toURI().toString().contains(photos.get(i).getPath())) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setContentText("Photo alreadry in Album");
						alert.show();
					}
				}

			}
			Image image = new Image(file.toURI().toString());

			System.out.println(file.toURI().toString() + "in handle change \n");
			if (photos != null) {
				for (int i = 0; i < photos.size(); i++) {
					System.out.println(">>>>" + photos.get(i).getPath().toString());
					if ((file.toURI().toString().contains(photos.get(i).getPath().toString()))) {
						// Add Alert Here
						System.out.println("found Imagges HERE");
						return;
					}
				}

			}

			obsImage.add(image);

			Photo p = new Photo(file.toString());

			// encoded file replaces " " with "%20", must replace for search to work
			String formattedFile = formatUploads(file.toString());
			p.setPath(formattedFile);
			p.setCaption("[no caption]");

//			System.out.println(">>>>" + file.lastModified());

			Long LM = file.lastModified();
			DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
			String Date = formatter.format(Instant.ofEpochMilli(LM).atZone(ZoneId.systemDefault()));
			System.out.println("Date: " + Date);
			Calendar LMCal = Calendar.getInstance();
			LMCal.set(Integer.parseInt(Date.substring(0, 4)), Integer.parseInt(Date.substring(5, 7)),
					Integer.parseInt(Date.substring(8, 10)));
			p.setDateTaken(LMCal);
//			Date date = new Date(lastModified);
//			p.setDateTaken(date);
			photos.add(p);
			updatePhotos();

//			System.out.println(date1);

//			System.out.println(p.getPath());
		}

		// remove selected photo from album
		else if (btnType == removePhotoBtn) {
			System.out.println();
			if (mainImage.getImage() == null) { // if no Main Image stop trying to remove
				System.out.println("remove Stopped");
				return;
			}

			for (int i = 0; i < photos.size(); i++) {
				if (mainImage.getImage().getUrl().contains(photos.get(i).getPath())) {
					System.out.println("found;");
					photos.remove(i);
					obsImage.remove(i);

				}
			}

			if (photos.size() == 0) {
				mainImage.setImage(null);
				obsInfo.clear();
			}
			updatePhotos();

		}

		// add or edit selected photo caption
		else if (btnType == addEditCapBtn) {
			System.out.println("cap clicked");
			String newCaption = captionTF.getText().trim();
			String photoPath = mainImage.getImage().getUrl();
			System.out.println(photoPath);
			Photo photo = null;
			if (!newCaption.equals("")) {
				if (photoPath != null) {

					for (int i = 0; i < photos.size(); i++) {
//						System.out.println(photos.get(i).getPath());
						if (photoPath.contains(photos.get(i).getPath())) {

							photo = photos.get(i);
							break;
						}
					}
				}
			}
			if (photo != null) {
				photo.setCaption(newCaption);
				captionTF.clear();

			}
			updatePhotos();
			Users.save();

		}

		// add tag to selected photo
		else if (btnType == addTagBtn) {
			System.out.println(this.currPhoto);
			if (currPhoto == null) {
				Alert notFound = new Alert(AlertType.WARNING);
				notFound.setHeaderText("Please fix the following: ");
				if (photos.size() == 0) {
					notFound.setContentText("No Photos");
				}
				notFound.setContentText("Please select photo");
				notFound.showAndWait();
			} else {
				CreateTagController.currentPhoto = currPhoto;
				((Node) e.getSource()).getScene().getWindow().hide();
				loadWindow("/photos/view/createTag.fxml", "Create New Tag");
			}

		}

		// delete tag from selected photo
		else if (btnType == deleteTagBtn) {
			System.out.println("----" + currPhoto.getTags());

			ArrayList<Tags> currTags = currPhoto.getTags();

			if (currTags != null) {
				if (currTags.size() == 0) {
					Alert empty = new Alert(AlertType.WARNING);
					empty.setContentText("Tag Not Found");
					empty.showAndWait();
					return;
				}

			}

			if (currentTag.equals("")) {
				Alert empty = new Alert(AlertType.WARNING);
				empty.setContentText("No Tag Selected");
				empty.showAndWait();
				return;
			}
			String currentTag2 = currentTag.trim();
			int index = currentTag2.indexOf(":");
			if (index != -1) {
				if (currPhoto != null) {
					int found = -1;
					for (int i = 0; i < currTags.size(); i++) {
						if (currTags.get(i).toString().equals(currentTag2)) {
							currTags.remove(i);
							Photo photos1 = currPhoto;
							obsInfo.clear();
							photoDetail.getItems().clear();
							photoInfo.clear();
							String caption1 = (photos1.getCaption() != null) ? (photos1.getCaption()) : "[No Caption]";
							photoInfo.add(caption1);
							String dateTaken1 = photos1.getDateTaken();
							photoInfo.add(dateTaken1);
							if (photos1.getTags() != null) {
								for (int k = 0; k < photos1.getTags().size(); k++) {
									String photoTag = photos1.getTags().get(k).toString();
									photoInfo.add(photoTag);

								}

							}

							obsInfo = FXCollections.observableArrayList(photoInfo);

							photoDetail.setItems(obsInfo);
//							updatePhotos();
							found = i;
						}
					}

				}
			}

		}

		// copy selected photo to specified album
		else if (btnType == copyPhotoBtn) {
			if (albumName.getText().equals(currentAlbum.getName())) {
				System.out.println("Stopped");
				return;
			}
			System.out.println(currentUser);
			User user = null;
			for (int i = 0; i < Users.users.size(); i++) {
				if (Users.users.get(i).getUsername().equals(currentUser)) {
					user = Users.users.get(i);
				}
			}
			if (user != null) {
				ArrayList<Album> albums = user.getAlbums();
				for (int i = 0; i < albums.size(); i++) {
					if (albums.get(i).getName().equals(albumName.getText().trim())) {
						int index = -1;
						for (int j = 0; j < photos.size(); j++) {
							if (mainImage.getImage().getUrl().contains(photos.get(j).getPath())) {
								index = j;
								break;
							}
						}
						for (int z = 0; z < albums.get(i).getPhotos().size(); z++) {
							if (albums.get(i).getPhotos().get(z).getPath().equals(photos.get(index).getPath())) {

								System.out.println("Photo already in Album");
								Alert alreadyExists = new Alert(AlertType.WARNING);
								alreadyExists.setContentText("Photo already in Album");
								alreadyExists.show();
								return;
							}
						}
						if (index >= 0) {
							System.out.println("found and copied");
							albums.get(i).getPhotos().add(photos.get(index));
							break;
						}

					}
				}
			}
			albumName.clear();
			Users.save();

		}

		// move selected photo to specified album
		else if (btnType == movePhotoBtn) {
			System.out.println(currentAlbum.getName());
			if (albumName.getText().equals(currentAlbum.getName())) {
				System.out.println("Stopped");
				return;
			}
			User user = null;
			for (int i = 0; i < Users.users.size(); i++) {
				if (Users.users.get(i).getUsername().equals(currentUser)) {
					user = Users.users.get(i);
				}
			}
			if (user != null) {
				ArrayList<Album> albums = user.getAlbums();
				for (int i = 0; i < albums.size(); i++) {
					if (albums.get(i).getName().equals(albumName.getText().trim())) {
						int index = -1;
						for (int j = 0; j < photos.size(); j++) {
							if (mainImage.getImage().getUrl().contains(photos.get(j).getPath())) {
								index = j;
								break;
							}
						}

						for (int z = 0; z < albums.get(i).getPhotos().size(); z++) {
							if (albums.get(i).getPhotos().get(z).getPath().equals(photos.get(index).getPath())) {
								System.out.println("Photo already in Album");
								Alert alreadyExists = new Alert(AlertType.WARNING);
								alreadyExists.setContentText("Photo already in Album");
								alreadyExists.show();
								return;
							}
						}

						if (index >= 0) {
							System.out.println("found and copied");
							albums.get(i).getPhotos().add(photos.get(index));
							photos.remove(index);
							obsImage.remove(index);
							break;
						}

					}
				}
			}
			if (photos.size() == 0) {
				mainImage.setImage(null);

			}

			albumName.clear();
			System.out.println("reached bottom");
			updatePhotos();
			Users.save();

		}

		// return to all albums
		else if (btnType == returnAlbumBtn) {
			Users.save();
			((Node) e.getSource()).getScene().getWindow().hide();
			loadWindow("/photos/view/albumPage.fxml", "Album-Select Page");

		}

		// logout from application, return to login
		else if (btnType == logoutBtn) {
			((Node) e.getSource()).getScene().getWindow().hide();
			Parent root = FXMLLoader.load(getClass().getResource("/photos/view/login.fxml"));
			Logout.logout(root);

		}

		// quit application
		else if (btnType == quitBtn) {
			updatePhotos();
			Users.save();
			Platform.exit();

		}
	}

	private void loadWindow(String location, String title) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource(location));
		Scene scene = new Scene(root);
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setScene(scene);
		stage.setTitle(title);
		stage.show();
	}

	public void updatePhotos() {

		hboxImage.getChildren().setAll();
		if (photos.size() == 0) {

			obsInfo.clear();
			photoDetail.getItems().clear();
			photoInfo.clear();

		}

		// populate arraylist of images to hbox
		for (int i = 0; i < obsImage.size(); i++) {
			ImageView imageView = new ImageView();
			TextArea textArea = new TextArea();
			textArea.setMaxSize(150, 30);
			textArea.setMinSize(150, 30);
			textArea.setEditable(false);

			imageView.setImage(obsImage.get(i));
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(200);
			imageView.setFitWidth(300);

//			System.out.println(photos.size());
//			System.out.println(obsImage.size());

			if (i == 0 && photos.size() > 0) {

				mainImage.setImage(imageView.getImage());
				obsInfo.clear();
				photoDetail.getItems().clear();
				photoInfo.clear();
//				System.out.println("obs info" + obsInfo.size());
				String caption = (photos.get(0).getCaption() != null) ? photos.get(0).getCaption() : "[No Caption]";
				photoInfo.add(caption);
				String dateTaken = photos.get(0).getDateTaken().toString();
				System.out.println("DT" + dateTaken);

				photoInfo.add(dateTaken);
				if (photos.get(0).getTags() != null) {
					for (int k = 0; k < photos.get(0).getTags().size(); k++) {
						String photoTag = photos.get(0).getTags().get(k).toString();
						photoInfo.add(photoTag);
					}
				}
				obsInfo = FXCollections.observableArrayList(photoInfo);

				photoDetail.setItems(obsInfo);

				if (photos.get(i).getCaption() != null) {

					textArea.setText(photos.get(i).getCaption());
				} else {
					textArea.setText("[No Caption]");
				}

			}
			System.out.println("Photos Size: " + photos.size());
			if (photos.get(i).getCaption() != null) {

				textArea.setText(photos.get(i).getCaption());
			} else {
				textArea.setText("[No Caption]");
			}
			hboxImage.getChildren().add(imageView);
			hboxImage.getChildren().add(textArea);

//			System.out.println(obsImage.get(i));

			imageView.setOnMouseClicked(event -> {
				mainImage.setImage(imageView.getImage());
//				System.out.println(imageView.getImage().getUrl());
				boolean found = false;
				for (int j = 0; j < photos.size(); j++) {
					if (imageView.getImage().getUrl().contains(photos.get(j).getPath())) {
						found = true;
						currPhoto = photos.get(j);
//						System.out.println("print");
						obsInfo.clear();
						photoDetail.getItems().clear();
						photoInfo.clear();
//						System.out.println("obs info" + obsInfo.size());
						String caption = (photos.get(j).getCaption() != null) ? photos.get(j).getCaption()
								: "[No Caption]";
						photoInfo.add(caption);
						String dateTaken = photos.get(j).getDateTaken().toString();
//						System.out.println("DT" + dateTaken);

						photoInfo.add(dateTaken);
						if (photos.get(j).getTags() != null) {
							for (int k = 0; k < photos.get(j).getTags().size(); k++) {
								String photoTag = photos.get(j).getTags().get(k).toString();
								photoInfo.add(photoTag);
							}
						}
						obsInfo = FXCollections.observableArrayList(photoInfo);

						photoDetail.setItems(obsInfo);
					}
				}
				if (!found) {
					obsInfo.clear();
					photoDetail.getItems().clear();
					photoInfo.clear();
					System.out.println("obs info" + obsInfo.size());

					obsInfo = FXCollections.observableArrayList(photoInfo);

					photoDetail.setItems(obsInfo);
				}
			});
		}
		currentAlbum.setPhotos(photos);
		for (int i = 0; i < Users.users.size(); i++) {
			if (Users.users.get(i).getUsername().equals(currentUser)) {
				ArrayList<Album> userAlbums = Users.users.get(i).getAlbums();
				for (int j = 0; j < userAlbums.size(); j++) {
					if (userAlbums.get(j).equals(currentAlbum)) {
//						System.out.println("found Album: " + userAlbums.get(j).getNumOfPhotos());
					}

				}
			}
		}
	}

	/**
	 * formatUploads() method takes a String and identifies any characters that may
	 * be encoded differently when uploaded from files to be used as paths in the
	 * application and modifies the String path associated with an uploaded photo so
	 * that it may be correctly read for further use in the application.
	 * 
	 * @param upload String from uploaded photo
	 * @return String path to be correctly read for further use in Photos objects
	 */

	public String formatUploads(String upload) {

		String formattedFile = upload;

		if (upload.indexOf('?') != -1) {

			formattedFile = upload.toString().replace("?", "%3F");

		} else if (upload.indexOf(' ') != -1) {

			formattedFile = upload.toString().replace(" ", "%20");
		} else if (upload.indexOf('"') != -1) {

			formattedFile = upload.toString().replace("\"", "%22");
		} else if (upload.indexOf('<') != -1) {

			formattedFile = upload.toString().replace("<", "%3C");
		} else if (upload.indexOf('>') != -1) {

			formattedFile = upload.toString().replace(">", "%3E");
		} else if (upload.indexOf('#') != -1) {

			formattedFile = upload.toString().replace("#", "%23");
		} else if (upload.indexOf('%') != -1) {

			formattedFile = upload.toString().replace("%", "%25");
		} else if (upload.indexOf('{') != -1) {

			formattedFile = upload.toString().replace("{", "%7B");
		} else if (upload.indexOf('}') != -1) {

			formattedFile = upload.toString().replace("}", "%7D");
		} else if (upload.indexOf('|') != -1) {

			formattedFile = upload.toString().replace("|", "%7C");
		} else if (upload.indexOf('\\') != -1) {

			formattedFile = upload.toString().replace("\\", "%5C");
		} else if (upload.indexOf('^') != -1) {

			formattedFile = upload.toString().replace("^", "%5E");
		} else if (upload.indexOf('[') != -1) {

			formattedFile = upload.toString().replace("[", "%5B");
		} else if (upload.indexOf(']') != -1) {

			formattedFile = upload.toString().replace("]", "%5D");
		} else if (upload.indexOf('`') != -1) {

			formattedFile = upload.toString().replace("`", "%60");
		} else
			return formattedFile;

		return formattedFile;

	}
}