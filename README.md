# photo_album
Single-user photo application that allows storage and management of photos in one or more albums

CS213 Assignment 3
Samantha Ames and Ahmed Elgazzar

Features
Your application must implement the following features:

Date of photo

Since we won't examine the contents of a photo file to get the date the photo was taken, we will instead use the last modification date of the photo file (as provided via the Java API to the filesystem) as a proxy. (The user interface will still refer to this as the date the photo was taken.)

To store and manipulate dates and times, you have two options:

You can use a java.util.Calendar instance.
In which case, when you set a date and time on an instance, also make sure you set milliseconds to zero, as in cal.set(Calendar.MILLISECOND,0), otherwise your equality checks won't work correctly.
Alternatively, you may use the classes in the java.time package.

Tags

Photos can be tagged with pretty much any attribute you think is useful to search on, or group by. Examples are location where photo was taken, and names of people in a photo, so you can search for photos by location and/or names.

From the implementation point of view, it may be useful to think of a tag as a combination of tag name and tag value, e.g. ("location","New Brunswick"), or ("person","susan"). A photo may have multiple tags (name+value pairs), but no two tags for the same photo will have the same name and value combination.

Additional details:

You can set up some tag types beforehand for the user to pick from (e.g. location)
Depending on the tag type, a user can either have a single value for it, or multiple values (e.g. for any photo, location can only have one value, but if there's a person tag, that can have multiple values, one per person that appears in the photo)
A user can define their own tag type and add it to the list (so from that point on, that tag type will show up in the preset list of types the user can choose from)
Location of Photos - Stock photos and User photos

There are two sets of photos, stock photos that come pre-loaded with the application, and user photos that are loaded/imported by a user when they run the application.

Stock photos are photos that you will keep in the application's workspace. You must have no fewer than 5 stock photos, and no more than 10.
Create a special username called "stock" (no password, or password="stock") and store the stock photos under this user, in an album named "stock".

Leave the photos in the application's workspace so the graders can test your application starting with your stock photos, then load other photos from their computer, see "User photos" below.

Try to work with low/medium resolution pictures for the stock photos because they will be on Bitbucket and downloaded by the graders, and you don't want to bloat your project size.

User photos are photos that your application can allow a user to load from their computer, so they can be housed anywhere on the user's machine. The actual photos must NOT be in your application's workspace. Instead, your application should only store the location of the photo on the user's machine. User photo information must NOT be in the released project in Bitbucket since each installation of your application on a machine will have its own set of users.
Login

When the application starts, a user logs in with username. Password implementation is optional. It makes for a "real" scenario, but is irrelevant to the essence of the project. (There is no credit for the password feature, if you choose to implement it.)
Admin Subsystem
There must be a special username admin that will put the application in an administration sub-system. The admin user can then do any of the following:
List users
Create a new user
Delete an existing user
Note: If you elect to implement passwords for users, make "admin" the password for the admin user, so it's easier to grade. Otherwise we will need to ask you, or look in some README file, etc, which just turns out to be a needless hassle.

Non-admin User Subsystem

Once the user logs in successfully, all albums and photo information for this user from a previous session (if any) are loaded from disk.
Initially, all the albums belonging to the user should be displayed. For each album, its name, the number of photos in it, and the range of dates (earliest and latest date) on which photos were taken must be displayed. Use your discretion on how to show this additional information.

The user can then do the following:
Create albums
Delete albums
Rename albums
Open an album. Opening an album displays all photos, with their thumbnail images and captions, inside that album. Once an album is open the user can do the following:
Add a photo
Remove a photo
Caption/recaption a photo
Display a photo in a separate display area. The photo display should also show its caption, its date-time of capture (see Date of photo below), and all its tags (see Tags below).
Add a tag to a photo
Delete a tag from a photo
Copy a photo from one album to another (multiple albums may have copies of the same photo)
Note: If a photo is in multiple albums, it is the same physical photo, just referenced from multiple albums. This means any changes you make to the photo (caption, tags) will be reflected in all the albums in which the photo appears.

Move a photo from one album (source) to another (the photo will be removed from the source album)
Go through photos in an album in sequence forward or backward, one at a time, with user interaction (manual slideshow)
Search for photos (Photos that match the search criteria should be displayed in a similar way to how photos in an album are displayed). Under this, you should provide the following specific features:
Search for photos by a date range.
Search for photos by tag type-value pairs. The following types of tag-based searches should be implemented:
A single tag-value pair, e.g person=sesh
Conjunctive combination of two tag-value pairs, e.g. person=sesh AND location=prague
Disjunctive combination of two tag-value pairs, e.g. person=sesh OR location=prague
For conjunctions and disjunctions, if a tag can have multiple values for a photo, it can appear on both arms of the conjunction/disjunction, e.g. person=andre OR person=maya, person=andre AND person=maya
You are NOT required to do conjunctions/disjunctions on more than two tag-values pairs.
In other words, you are not required to do stuff like t1=v1 and t2=v3 and t3=v3
There should be functionality to create an album containing the search results.
As mentioned earlier (under Copy a photo from one album to another), a photo can be in multiple albums. Creating an album out of search results means copying these photos to a new album, without deleting them from the current album(s) to which they belong.

Note: A single user may not have duplicate album names, but an album name may be (coincidentally) duplicated across users.

Logout

The user (whether admin or non-admin) logs out at the end of the session. All updates made by the user are saved to disk.
After a user logs out, the application is still running, allowing another user to log in.
Quit Application

There should be a way for the user to quit the application safely at any time, bypassing the logout step (e.g. by killing the main window). Safely means that all updates that were made in the application in the user's session are saved on disk.
Unlike logout, the application stops running. The next user that wants to use the application will need to restart it.
Errors

In the application all errors and exceptions should be handled gracefully within the GUI setup. The text console should NOT be used at all: not to report any error, not to read input, not to print output.
