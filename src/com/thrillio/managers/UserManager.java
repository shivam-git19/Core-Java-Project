package com.thrillio.managers;

import java.util.List;

import com.thrillio.Entities.User;
import com.thrillio.constant.Gender;
import com.thrillio.constant.UserType;
import com.thrillio.dao.UserDao;

public class UserManager {
	private static UserManager instance = new UserManager();
	private static UserDao  dao = new UserDao();
	
	private UserManager() {
	}

	public static UserManager getInstance() {
		return instance;
	}

	public User createUser(long id, String email, String Password, String firstName, String lastName, Gender gender,
			UserType userType) {
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setPassword(Password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setGender(gender);
		user.setUserType(userType);
		
		return user;
	}
	
	public List<User> getUsers() {   //relaying the call
		return dao.getUsers();
	}
}
