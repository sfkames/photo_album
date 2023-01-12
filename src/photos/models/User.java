package photos.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User class creates instance of User object with constructor. Has attributes
 * of String Username and an ArrayList of Albums for the corresponding User.
 * Also has getter and setter methods for both attributes.
 * 
 * @author Ahmed Elgazzar, Samantha Ames
 */

public class User implements Serializable {
	private String Username;
	private ArrayList<Album> albums; // change to Album Class later

	/**
	 * User constructor creates an instance of User that represents the User of the
	 * current session and takes in the String username and the ArrayList of Albums
	 * for the corresponding User.
	 * 
	 * @param username username of respective user
	 * @param albums   ArrayList of respective user's Albums
	 */

	public User(String username, ArrayList<Album> albums) {
		Username = username;
		this.albums = albums;
	}

	/**
	 * getUsername() method returns String of respective user's username
	 * 
	 * @return Username username of respective user
	 */

	public String getUsername() {
		return Username;
	}

	/**
	 * setUsername() void method takes in String username and sets it as the
	 * respective User's username
	 * 
	 * @param username username of respective user
	 */

	public void setUsername(String username) {
		Username = username;
	}

	/**
	 * getAlbums() method returns the corresponding ArrayList of Albums for the
	 * respective user
	 * 
	 * @return albums ArrayList of Albums for the user
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}

	/**
	 * setAlbums() method takes in an ArrayList of Albums and sets it for the
	 * respective user
	 * 
	 * @param albums ArrayList of Albums for the user
	 */
	public void setAlbums(ArrayList<Album> albums) {
		this.albums = albums;
	}

}
