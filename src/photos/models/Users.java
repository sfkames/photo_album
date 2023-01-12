package photos.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import photos.view.AdminController;

/**
 * Users class is comprised of an ArrayList of users that remains static and
 * implements Serializable so that the list of users carries through all
 * sessions. Includes constructor to create an instance of a User ArrayList,
 * methods to get and add Users to the list, methods to save the list throughout
 * sessions, read the users from the list, load the list, and read the user
 * values to Strings.
 * 
 * @author Ahmed Elgazzar, Samantha Ames
 * 
 */

public class Users implements Serializable {
	public static ArrayList<User> users = new ArrayList<User>();

	/**
	 * Users constructor creates an instance of a user ArrayList with "stock" and
	 * "admin" users that carries throughout different sessions. All users that are
	 * added by the admin user will be added to the Users ArrayList.
	 * 
	 * @see AdminController
	 */

	public Users() {

//		ArrayList<Photo> samplePhotos = new ArrayList<Photo>(); // sample photos list
//
//		samplePhotos.add(new Photo("FeatCard.jpg", 1)); // add sample photo
//
//		Album sampleAlbum = new Album("sample", samplePhotos); // sample album
//
//		ArrayList<Album> sampleAlbumList = new ArrayList<Album>(); // list of albums for user
//
//		sampleAlbumList.add(sampleAlbum); // add sample album
//		sampleAlbumList.add(sampleAlbum); // add sample album
//
//		User sampleUser = new User("sampleUser", sampleAlbumList); // sample users
//
//		users.add(sampleUser);

//		try {
//			writeAlbum(users);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			load();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// check if admin and stock users exist
		boolean adminCheck = false;
		boolean stockCheck = false;
		for (int i = 0; i < users.size(); i++) {

			if (users.get(i).getUsername().equals("admin")) {
				adminCheck = true;
			}
			if (users.get(i).getUsername().equals("stock")) {
				stockCheck = true;
			}
			if (adminCheck && stockCheck) {
				break;
			}
		}
		if (adminCheck == false) {

			users.add(new User("admin", null));
			save();
		}
		if (stockCheck == false) {
			users.add(new User("stock", null));
			save();
		}

//		users.add();
		try {
			load();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * getUsers() method returns the ArrayList of users
	 * 
	 * @return users ArrayList of users
	 */

	public ArrayList<User> getUsers() {
		return users;
	}

	/**
	 * writeUsers() void method takes parameter users2 ArrayList of previously added
	 * Users and populates new instance of Users list with these values
	 * 
	 * @param users2 ArrayList of pre-existing users
	 * @throws IOException
	 */

	public static void writeUsers(ArrayList<User> users2) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream("src" + File.separator + "photos/models" + File.separator + "obj.txt"));
		oos.writeObject(users2);
	}

	/**
	 * save() method returns boolean value "true" if Users list is successfully
	 * populated with pre-existing users and "false" otherwise
	 * 
	 * @return "true" if Users list is successfully populated with pre-existing
	 *         users and "false" otherwise
	 */

	public static boolean save() {
		try {
			writeUsers(users);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * readUsers() returns an ObjectInputStream for the pre-existing users so that
	 * they can populate a new instance of Users list
	 * 
	 * @return ois ObjectInputStream of pre-existing users
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public static ObjectInputStream readUsers() throws FileNotFoundException, IOException {
		ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream("src" + File.separator + "photos/models" + File.separator + "obj.txt"));
		return ois;

	}

	/**
	 * load() reads the pre-existing users and loads them as User objects to the
	 * User list; returns "true" if users were successfully loaded and "false"
	 * otherwise
	 * 
	 * @return "true" if users were successfully loaded and "false" otherwise
	 * @throws ClassNotFoundException
	 */

	@SuppressWarnings("unchecked")
	public static boolean load() throws ClassNotFoundException {
		try {
			ObjectInputStream usersOIS = readUsers();
//			System.out.println(">>>>" + usersOIS);
			users = (ArrayList<User>) usersOIS.readObject();
//			System.out.println(users.get(0).getUsername());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * toString() method takes user values of Users list and returns these values as
	 * Strings, each on a new line
	 * 
	 * @return list list of User objects as Strings, each on their own line
	 */
	@Override
	public String toString() {
		String list = "Users: ";
		for (int i = 0; i < users.size(); i++) {
			list = list + "\n " + users.get(i).getUsername();
		}
		return list + "\n";

	}

}
