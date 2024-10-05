package com.thrillio.Entities;

import static org.junit.Assert.*;

import org.junit.Test;

import com.thrillio.constant.MovieGenre;
import com.thrillio.managers.BookmarkManager;

public class MovieTest {

	@Test
	public void test() {
		Movie movie = BookmarkManager.getInstance().createMovie(3000,	"Citizen Kane 0","",1941,new String[] {	"Orson Welles","Joseph Cotten 0"},new String[]	{"Orson Welles 0"},	MovieGenre.HORROR,	8.5);
		boolean isKidFriendlyEligible = movie.isKidFriendlyEligible();
		assertFalse("If Genre is horror", isKidFriendlyEligible);
		
		movie = BookmarkManager.getInstance().createMovie(3000,	"Citizen Kane 0","",1941,new String[] {	"Orson Welles","Joseph Cotten 0"},new String[]	{"Orson Welles 0"},	MovieGenre.THRILLERS,	8.5);
		isKidFriendlyEligible = movie.isKidFriendlyEligible();
		assertFalse("If Genre is thriller", isKidFriendlyEligible);
	}

}
