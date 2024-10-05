package com.thrillio.dao;

import java.util.List;

import com.thrillio.DataStore;
import com.thrillio.Entities.User;

public class UserDao {
	public List<User> getUsers() {
		return DataStore.getUsers();
	}
}
