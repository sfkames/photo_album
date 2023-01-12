package photos.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import photos.models.Album;
import photos.models.Photo;
import photos.models.Tags;
import photos.models.User;
import photos.models.Users;

/**
 * searchController class provides all of the functionality for users to search
 * through their Albums for any photos with a user-input date range or set of
 * tag-value pairs. Attributes include a String currentUser, which tracks the
 * current user, and an ArrayList of Photos that stores any of the photos that
 * result from the search. Methods include initialize(), which initializes the
 * search photo results, handleChange(), which allows for the input of the
 * user's chosen search values and allows for the user to launch the search for
 * the results to generate, searchByTags(), which performs the search if the
 * user chooses to search by tag-value pairs, searchByDates(), which performs
 * the search if the users chooses to search by date, and loadWindow(), which
 * redirects the user to location with which it is called.
 * 
 * 
 * @author Ahmed Elgazzar, Samantha Ames
 *
 */
public class searchController {

	// button
	@FXML
	Button searchBtn;
	@FXML
	Button returnBtn;
	@FXML
	Button resultsBtn;
	@FXML
	Button logOutBtn;
	@FXML
	Button quitBtn;

	// text fields
	@FXML
	TextField firstDate;
	@FXML
	TextField secondDate;
	@FXML
	TextField t1Type;
	@FXML
	TextField t1Value;
	@FXML
	TextField t2Type;
	@FXML
	TextField t2Value;

	// Check Boxes
	@FXML
	CheckBox and;
	@FXML
	CheckBox or;

	public static String currentUser;
	public static ArrayList<Photo> SR;

	/**
	 * initialize() void method initializes the search photo results
	 * 
	 * @throws IOException
	 */

	public void initialize() throws IOException {
		searchController.SR = null;

	}

	/**
	 * handleChange() void method takes an ActionEvent based on the clicking of the
	 * following buttons: searchBtn initiates the search based on the date inputs or
	 * the tag-value pair inputs; if the user attempts to search by both methods, an
	 * alert is generated, returnBtn redirects the user to their respective Album
	 * Page, logOutBtn saves any changes made by the user and redirects to the Login
	 * page, and quitBtn saves any changes made by the user and closes the
	 * application.
	 * 
	 * @param e ActionEvent based on the clicking of the available buttons
	 * @throws IOException
	 */

	@SuppressWarnings("unused")
	public void handleChange(ActionEvent e) throws IOException {
		User user = null;
		ArrayList<Photo> searchPhotos = new ArrayList<Photo>();
		// find user
		for (int i = 0; i < Users.users.size(); i++) {
			if (Users.users.get(i).getUsername().equals(currentUser)) {
				user = Users.users.get(i);
				break;
			}
		}
		int searchResults = 0;

		Button btnType = (Button) e.getSource();

		if (btnType == searchBtn) {
			String Day1 = firstDate.getText();
			String Day2 = secondDate.getText();
			System.out.println("D1: " + Day1);
			System.out.println("D2: " + Day2);
			if ((!Day1.trim().equals("") || !Day2.trim().equals(""))
					&& (!t1Type.getText().trim().equals("") || !t1Value.getText().trim().equals("")
							|| !t2Type.getText().trim().equals("") || !t2Value.getText().trim().equals(""))) {

				Alert results = new Alert(AlertType.ERROR);

				results.setContentText("Can only search by Date or Tags - NOT BOTH!!");

				results.show();
				return;

			} else if ((Day1.trim().equals("") && Day2.trim().equals(""))
					&& (t1Type.getText().trim().equals("") && t1Value.getText().trim().equals("")
							&& t2Type.getText().trim().equals("") && t2Value.getText().trim().equals(""))) {
				Alert results = new Alert(AlertType.ERROR);

				results.setContentText("Search by Date or Tags!");

				results.show();
				return;
			}

			// Date Range
			if (!Day1.trim().equals("") || !Day2.trim().equals("")) {
				System.out.println("User is" + user.getUsername());
				System.out.println("User is" + user.getAlbums().size());
				if (!Day1.trim().equals("") && !Day2.trim().equals("")) {

				} else {
					Alert results = new Alert(AlertType.ERROR);

					results.setContentText("Fill out date range!");

					results.show();
					return;
				}

				searchPhotos = searchByDates(user, Day1, Day2);

//				System.out.println("dates found result: " + searchPhotos.size());
				if (searchPhotos != null) {
					firstDate.clear();
					secondDate.clear();
				}

			} else {

				// Tags
				if (and.isSelected() && or.isSelected()) {
					Alert both = new Alert(AlertType.WARNING);
					both.setContentText("Cannot selected both 'And' & 'or' combinations!");
					both.show();
				} else if (and.isSelected()) {
					searchPhotos = searchByTags(user, 1);

				} else if (or.isSelected()) {
					searchPhotos = searchByTags(user, 2);

				} else {

					searchPhotos = searchByTags(user, 3);

				}
				if (searchPhotos != null) {
					t1Type.clear();
					t1Value.clear();
					t2Type.clear();
					t2Value.clear();

					if (searchPhotos.size() > 0) {
						// ArrayList<Photo> searchPhotos
					}
				}
			}
			System.out.println("reached");

			if (searchPhotos != null) {
				Alert results = new Alert(AlertType.INFORMATION);
				if (searchPhotos.size() == 0) {
					results.setContentText("0 pictures found");
				} else {
					results.setContentText(searchPhotos.size() + " pictures found");
				}
				results.show();

			}
			searchController.SR = searchPhotos;
//			else {
//				Alert results = new Alert(AlertType.ERROR);
//
//				results.setContentText("Error occured- check input");
//
//				results.show();
//
//			}

		} else if (btnType == resultsBtn) {
			Alert results = new Alert(AlertType.ERROR);
			results.setContentText("Nothing found");
			ArrayList<Photo> newSR = searchController.SR;

			if (newSR != null) {
				System.out.println(newSR.size());
				if (newSR.size() == 0) {
					System.out.println("reached");
					results.show();

				}
				if (newSR.size() > 0) {
					System.out.println("change to inside Album2");
					((Node) e.getSource()).getScene().getWindow().hide();
					insideSRController.currentAlbum = new Album("SR", newSR);
					insideSRController.currentUser = searchController.currentUser;
					insideSRController.currUser = user;
					loadWindow("/photos/view/insidealbum2.fxml", "Album-Select Page");
				}
			} else {
				System.out.println("reached");
				results.show();
			}

		} else if (btnType == returnBtn) {
			((Node) e.getSource()).getScene().getWindow().hide();
			loadWindow("/photos/view/albumPage.fxml", "Album-Select Page");

		} else if (btnType == logOutBtn) {
			((Node) e.getSource()).getScene().getWindow().hide();
			Parent root = FXMLLoader.load(getClass().getResource("/photos/view/login.fxml"));
			Logout.logout(root);

		} else if (btnType == quitBtn) {
			Users.save();
			Platform.exit();

		}
	}

	/**
	 * searchByTags() method takes in User, which is the respective user, and int
	 * that corresponds with Tag 1 or Tag 2, depending on whether the user chooses
	 * to search disjunctively or conjunctively, or singly. It returns an ArrayList
	 * of Photos that correspond to the respective tag-value pairs.
	 * 
	 * @param user current user of the session
	 * @param type tag 1, tag 2, or both
	 * @return
	 */

	public ArrayList<Photo> searchByTags(User user, int type) {
		String type1 = t1Type.getText();
		String val1 = t1Value.getText();
		String type2 = t2Type.getText();
		String val2 = t2Value.getText();
		System.out.println(type1 + " " + val1);
		System.out.println(type2 + " " + val2);
		ArrayList<Photo> searchPhotos = new ArrayList<Photo>();

		if (type == 3) {
			String tag = "";
			String val = "";
			if (((!type1.trim().equals("") && !val1.trim().equals(""))
					&& (type2.trim().equals("") && val2.trim().equals("")))) {
				tag = type1.trim();
				val = val1.trim();

			} else if (((type1.trim().equals("") && val1.trim().equals(""))
					&& (!type2.trim().equals("") && !val2.trim().equals("")))) {
				tag = type2.trim();
				val = val2.trim();
			} else {

				Alert both = new Alert(AlertType.WARNING);
				both.setContentText("Select a composition / Only fill tag and vlaue");
				both.show();
				return null;

			}
			ArrayList<Album> userAlbums = user.getAlbums();

			for (int i = 0; i < userAlbums.size(); i++) {
				ArrayList<Photo> currAlbum = userAlbums.get(i).getPhotos();
				if (currAlbum == null) {
					return null;
				}
				for (int j = 0; j < currAlbum.size(); j++) {
					ArrayList<Tags> tags = currAlbum.get(j).getTags();
					if (tags == null) {
						return null;
					}
					for (int k = 0; k < tags.size(); k++) {
						if (tags.get(k).getTagType().equalsIgnoreCase(tag)
								&& tags.get(k).getTagName().equalsIgnoreCase(val)) {
							if (searchPhotos.indexOf(currAlbum.get(j)) == -1) {
								searchPhotos.add(currAlbum.get(j));
							}

							System.out.println("found");
							break;
						}
					}

				}
			}

		} else if (type == 2) {
			String tag = "";
			String val = "";
			String tag_2 = "";
			String val_2 = "";
			if (((!type1.trim().equals("") && !val1.trim().equals(""))
					&& (!type2.trim().equals("") && !val2.trim().equals("")))) {
				tag = type1.trim();
				val = val1.trim();
				tag_2 = type2.trim();
				val_2 = val2.trim();

			} else {

				Alert both = new Alert(AlertType.WARNING);
				both.setContentText("Fill out both tag inputs");
				both.show();
				return null;

			}
			ArrayList<Album> userAlbums = user.getAlbums();

			for (int i = 0; i < userAlbums.size(); i++) {
				ArrayList<Photo> currAlbum = userAlbums.get(i).getPhotos();
				if (currAlbum == null) {
					return null;
				}
				for (int j = 0; j < currAlbum.size(); j++) {
					ArrayList<Tags> tags = currAlbum.get(j).getTags();
					if (tags == null) {
						continue;
					}
					for (int k = 0; k < tags.size(); k++) {
						if ((tags.get(k).getTagType().equalsIgnoreCase(tag)
								&& tags.get(k).getTagName().equalsIgnoreCase(val))
								|| (tags.get(k).getTagType().equalsIgnoreCase(tag_2)
										&& tags.get(k).getTagName().equalsIgnoreCase(val_2))) {
							if (searchPhotos.indexOf(currAlbum.get(j)) == -1) {
								searchPhotos.add(currAlbum.get(j));
								System.out.println("found: " + searchPhotos.size());

							}

							break;
						}
					}

				}
			}

		} else if (type == 1) {
			String tag = "";
			String val = "";
			String tag_2 = "";
			String val_2 = "";
			if (((!type1.trim().equals("") && !val1.trim().equals(""))
					&& (!type2.trim().equals("") && !val2.trim().equals("")))) {
				tag = type1.trim();
				val = val1.trim();
				tag_2 = type2.trim();
				val_2 = val2.trim();

			} else {

				Alert both = new Alert(AlertType.WARNING);
				both.setContentText("Fill out both tag inputs");
				both.show();
				return null;

			}
			ArrayList<Album> userAlbums = user.getAlbums();

			for (int i = 0; i < userAlbums.size(); i++) {
				ArrayList<Photo> currAlbum = userAlbums.get(i).getPhotos();
				if (currAlbum == null) {
					return null;
				}
				for (int j = 0; j < currAlbum.size(); j++) {
					ArrayList<Tags> tags = currAlbum.get(j).getTags();
					if (tags == null) {
						continue;
					}
					int found1 = -1;
					int found2 = -1;
					for (int k = 0; k < tags.size(); k++) {
						if ((tags.get(k).getTagType().equalsIgnoreCase(tag)
								&& tags.get(k).getTagName().equalsIgnoreCase(val))) {
							found1 = k;
							System.out.println("found1");

						}
					}
					if (found1 != -1) {
						for (int k = 0; k < tags.size(); k++) {
							if ((tags.get(k).getTagType().equalsIgnoreCase(tag_2)
									&& tags.get(k).getTagName().equalsIgnoreCase(val_2))) {
								found2 = k;
								System.out.println("found2");

							}
						}

					}

					if (found1 != -1 && found2 != -1) {
						searchPhotos.add(currAlbum.get(j));
						System.out.println("added: " + searchPhotos.size());
					}

				}
			}

		}
		return searchPhotos;

	}

	/**
	 * dateCheck() method takes in a String as the date of the user's input for
	 * their search and returns a boolean, "true" if the given input is in correct
	 * format and "false" otherwise
	 * 
	 * @param Date String of user input for search
	 * @return "true" if the given input is in correct format and "false" otherwise
	 */

	public boolean dateCheck(String Date) {
		// xx-xx-xxxx
		int firstDash = Date.indexOf('-');
		int lastDash = Date.lastIndexOf('-');

		if (firstDash == lastDash) {
			return false;
		}

		String monthS = Date.substring(0, firstDash);
		String dayS = Date.substring(firstDash + 1, lastDash);
		String yearS = Date.substring(lastDash + 1);

		if (monthS.length() == 0 || monthS.length() > 2) {
			return false;
		}
		if (dayS.length() == 0 || dayS.length() > 2) {
			return false;
		}
		if (yearS.length() == 0 || yearS.length() > 4) {
			return false;
		}

		for (int i = 0; i < 3; i++) {
			String num = "";
			if (i == 0) {
				num = monthS;
			} else if (i == 1) {
				num = dayS;
			} else {
				num = yearS;
			}
			char nm = '-';
			for (int j = 0; j < num.length(); j++) {
				nm = num.charAt(j);
				if (nm <= 47 || nm >= 58) {
					return false;
				}
			}
		}

		int month = Integer.valueOf(monthS);
		int day = Integer.valueOf(dayS);
		int year = Integer.valueOf(yearS);

		return true;

	}

	/**
	 * searchByDates() method takes in User as current user, and Strings, Day1 and
	 * Day2, which are user inputs that correspond to the starting and ending date
	 * range, respectfully. It returns an ArrayList of Photos that correspond to the
	 * respective date range.
	 * 
	 * @param user the current user
	 * @param Day1 the starting date for the respective search
	 * @param Day2 the ending date for the respective search
	 * @return ArrayList of Photos that correspond to the respective search
	 */

	public ArrayList<Photo> searchByDates(User user, String Day1, String Day2) {

		if (!dateCheck(Day1) || !dateCheck(Day2)) {
			// Make Alert

			return null;
		}

		// first date
		int firstDash = Day1.indexOf('-');
		int lastDash = Day1.lastIndexOf('-');

		String monthS = Day1.substring(0, firstDash);
		String dayS = Day1.substring(firstDash + 1, lastDash);
		String yearS = Day1.substring(lastDash + 1);
		int month = Integer.valueOf(monthS);
		int day = Integer.valueOf(dayS);
		int year = Integer.valueOf(yearS);
		System.out.println("M: " + month);
		System.out.println("D: " + day);
		System.out.println("Y: " + year);

		Calendar dOne = Calendar.getInstance();
		dOne.set(year, month, day);
		dOne.set(Calendar.MILLISECOND, 0);
		dOne.set(Calendar.SECOND, 0);
		dOne.set(Calendar.MINUTE, 0);
		dOne.set(Calendar.HOUR, 0);

		// secondDate
		int firstDash2 = Day2.indexOf('-');
		int lastDash2 = Day2.lastIndexOf('-');

		String monthS2 = Day2.substring(0, firstDash2);
		String dayS2 = Day2.substring(firstDash2 + 1, lastDash2);
		String yearS2 = Day2.substring(lastDash2 + 1);
		int month2 = Integer.valueOf(monthS2);
		int day2 = Integer.valueOf(dayS2);
		int year2 = Integer.valueOf(yearS2);

		Calendar dTwo = Calendar.getInstance();
		dTwo.set(year2, month2, day2);
		dTwo.set(Calendar.MILLISECOND, 0);
		dTwo.set(Calendar.SECOND, 0);
		dTwo.set(Calendar.MINUTE, 0);
		dTwo.set(Calendar.HOUR, 0);
		if (!dOne.before(dTwo)) {
			if (dOne.after(dTwo)) {
				return null;
			}

		}

		// search User's album's for photos

		if (user == null) {
			System.out.println("User is Null");
			return null;
		}

		if (user.getAlbums() == null) {
			System.out.println("User's albums is Null");
			return null;
		}

		ArrayList<Album> userAlbums = user.getAlbums();
		System.out.println(userAlbums.size());

		ArrayList<Photo> searchPhotos = new ArrayList<Photo>();

		for (int i = 0; i < userAlbums.size(); i++) {
			ArrayList<Photo> currAlbum = userAlbums.get(i).getPhotos();
			if (currAlbum == null) {
				continue;
			}
			for (int j = 0; j < currAlbum.size(); j++) {
				Calendar c1 = currAlbum.get(j).getCalendar();

				System.out.println("The Date: " + currAlbum.get(0).getCalendar().getTime());
				System.out.println("ran " + c1.after(dOne));
				System.out.println("ran " + c1.before(dOne));
				System.out.println("ran " + c1.after(dTwo));
				System.out.println("ran " + c1.before(dTwo));

				if (c1.after(dOne) && c1.before(dTwo)) {
					System.out.println("Found 1");
					if (searchPhotos.indexOf(currAlbum.get(j)) == -1) {
						searchPhotos.add(currAlbum.get(j));
					}

				} else if ((!c1.after(dOne) && !c1.before(dOne)) || (!c1.after(dTwo) && !c1.before(dTwo))) {
					if (searchPhotos.indexOf(currAlbum.get(j)) == -1) {
						searchPhotos.add(currAlbum.get(j));
					}
					System.out.println("Found 2: " + searchPhotos.indexOf(currAlbum.get(0)));
				}

			}
		}
		return searchPhotos;

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
