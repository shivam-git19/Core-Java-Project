package com.thrillio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.thrillio.Entities.Bookmark;
import com.thrillio.Entities.User;
import com.thrillio.Entities.UserBookmark;
import com.thrillio.constant.BookGenre;
import com.thrillio.constant.Gender;
import com.thrillio.constant.MovieGenre;
import com.thrillio.constant.UserType;
import com.thrillio.managers.BookmarkManager;
import com.thrillio.managers.UserManager;
import com.thrillio.util.IOUtil;

public class DataStore {
	
	private static List<User> users = new ArrayList<>();
	public static List<User> getUsers() {
		return users;
	}


	private static List<List<Bookmark>> bookmarks = new ArrayList<>();
	public static List<List<Bookmark>> getBookmarks() {
		return bookmarks;
	}


	private static List<UserBookmark> userBookmarks = new ArrayList<>();

	
	public static void loadData() {
		/*
		 * loadUsers(); loadWebLinks(); loadMovies(); loadBooks();
		 */
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
		//try-with-resources ==> conn & stmt will be closed
		//Connection string: <protocol>:<sub-protocol>:<data-source details>
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jid_thrillio?useSSL=false", "root","root");
			Statement stmt = conn.createStatement();){
			loadUsers(stmt);
			loadWebLinks(stmt);
			loadMovies(stmt);
			loadBooks(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	


	private static void loadUsers(Statement stmt)throws SQLException {
		String query = "Select*from User";
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()) {
			long id = rs.getLong("id");
			String email = rs.getString("email");
			String password = rs.getString("password");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			int gender_id = rs.getInt("gender_id");
			Gender gender = Gender.values()[gender_id];
			int user_type_id = rs.getInt("user_type_id");
			UserType userType = UserType.values()[user_type_id];
			
			User user = UserManager.getInstance().createUser(id, email, password, firstName, lastName, gender, userType);
			users.add(user);
			
		}
		}
	
	
	private static void loadWebLinks(Statement stmt) throws SQLException {
		/*bookmarks[0][0] = BookmarkManager.getInstance().createWebLink(2000,	"Taming Tiger Part 2",	"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",	"http://www.javaworld.com");
		bookmarks[0][1] = BookmarkManager.getInstance().createWebLink(2001,	"How do I import a pre-existing Java project into Eclipse and get up and running?",	"http://stackoverflow.com/questions/142863/how-do-i-import-a-pre-existing-java-project-into-eclipse-and-get-up-and-running",	"http://www.javaworld.com");
		bookmarks[0][2] = BookmarkManager.getInstance().createWebLink(2002,	"Taming Tiger Part 3",	"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",	"http://www.javaworld.com");
		bookmarks[0][3] = BookmarkManager.getInstance().createWebLink(2003,	"Taming Tiger Part 4",	"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",	"http://www.javaworld.com");
		bookmarks[0][4] = BookmarkManager.getInstance().createWebLink(2004,	"Taming Tiger Part 5",	"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",	"http://www.javaworld.com");
	*/
		String query = "Select*from WebLink";
		ResultSet rs = stmt.executeQuery(query);
		
		List<Bookmark> bookmarkList = new ArrayList<>();
		while (rs.next()) {
			long id = rs.getLong("id");
			String title = rs.getString("title");
			String url = rs.getString("url");
			String host = rs.getString("host");
			
			Bookmark bookmark = BookmarkManager.getInstance().createWebLink(id, title, url,host);
			bookmarkList.add(bookmark);
		}
		bookmarks.add(bookmarkList);
	}
	
	private static void loadMovies(Statement stmt) throws SQLException {
		String query = "Select m.id, title, release_year, GROUP_CONCAT(DISTINCT a.name SEPARATOR ',') AS cast, GROUP_CONCAT(DISTINCT d.name SEPARATOR ',') AS directors, movie_genre_id, imdb_rating"
				+ " from Movie m, Actor a, Movie_Actor ma, Director d, Movie_Director md "
				+ "where m.id = ma.movie_id and ma.actor_id = a.id and "
				      + "m.id = md.movie_id and md.director_id = d.id group by m.id";
		ResultSet rs = stmt.executeQuery(query);
		
		List<Bookmark> bookmarkList = new ArrayList<>();
		while (rs.next()) {
			long id = rs.getLong("id");
			String title = rs.getString("title");
			int releaseYear = rs.getInt("release_year");
			String[] cast = rs.getString("cast").split(",");
			String[] directors = rs.getString("directors").split(",");			
			int genre_id = rs.getInt("movie_genre_id");
			MovieGenre genre = MovieGenre.values()[genre_id];
			double imdbRating = rs.getDouble("imdb_rating");
			
			Bookmark bookmark = BookmarkManager.getInstance().createMovie(id, title,"", releaseYear, cast, directors, genre, imdbRating);
    		bookmarkList.add(bookmark); 
		}
		bookmarks.add(bookmarkList);
	}

	

	private static void loadBooks(Statement stmt) throws SQLException {
		/*bookmarks[2][0] =  BookmarkManager.getInstance().createBook(4000,	"Walden 0",	1854,	"Wilder Publications 0", new String[] {	"Henry", "David", "Thoreau 0"	},BookGenre.PHILOSOPHY,	4.3);
		bookmarks[2][1] =  BookmarkManager.getInstance().createBook(4001,	"Walden 1",	1855,	"Wilder Publications 1", new String[] {	"Henry", "David", "Thoreau 1"	},BookGenre.PHILOSOPHY,	4.2);
		bookmarks[2][2] =  BookmarkManager.getInstance().createBook(4002,	"Walden 2",	1856,	"Wilder Publications 2", new String[] {	"Henry", "David", "Thoreau 2"	},BookGenre.PHILOSOPHY,	4.1);
		bookmarks[2][3] =  BookmarkManager.getInstance().createBook(4003,	"Walden 3",	1857,	"Wilder Publications 3", new String[] {	"Henry", "David", "Thoreau 3"	},BookGenre.TECHNICAL,	4.0);
		bookmarks[2][4] =  BookmarkManager.getInstance().createBook(4004,	"Walden 4",	1858,	"Wilder Publications 4", new String[] {	"Henry", "David", "Thoreau 4"	},BookGenre.TECHNICAL,	4.4);
		*/
		
//		List<String> data = new ArrayList<>();
//    	IOUtil.read(data, "Book_");
		
		String query = "Select b.id, title, publication_year, p.name, GROUP_CONCAT(a.name SEPARATOR ',') AS authors, book_genre_id, amazon_rating, created_date"
				+ " from Book b, Publisher p, Author a, Book_Author ba "
				+ "where b.publisher_id = p.id and b.id = ba.book_id and ba.author_id = a.id group by b.id";

		
		ResultSet rs = stmt.executeQuery(query);
		
    	List<Bookmark> bookmarkList = new ArrayList<>();
    	while (rs.next()) {
    		long id = rs.getLong("id");
			String title = rs.getString("title");
			int publicationYear = rs.getInt("publication_year");
			String publisher = rs.getString("name");		
			String[] authors = rs.getString("authors").split(",");			
			int genre_id = rs.getInt("book_genre_id");
			BookGenre genre = BookGenre.values()[genre_id];
			double amazonRating = rs.getDouble("amazon_rating");
			
			Date createdDate = rs.getDate("created_date");
			System.out.println("createdDate: " + createdDate);
			Timestamp timeStamp = rs.getTimestamp(8);
			System.out.println("timeStamp: " + timeStamp);
			System.out.println("localDateTime: " + timeStamp.toLocalDateTime());
			
			System.out.println("id: " + id + ", title: " + title + ", publication year: " + publicationYear + ", publisher: " + publisher + ", authors: " + String.join(", ", authors) + ", genre: " + genre + ", amazonRating: " + amazonRating);
    		
    		Bookmark bookmark = BookmarkManager.getInstance().createBook(id, title, publicationYear, publisher, authors, genre, amazonRating);
    		bookmarkList.add(bookmark); 
    	}
    	bookmarks.add(bookmarkList);
    }


	public static void add(UserBookmark userBookmark) {
		userBookmarks.add(userBookmark);
	}
}
