package com.thrillio.Entities;

import static org.junit.Assert.*;

import org.junit.Test;

import com.thrillio.constant.BookGenre;
import com.thrillio.managers.BookmarkManager;

public class BookTest {

	@Test
	public void testIsKidFriendlyEligible() {
		Book book = BookmarkManager.getInstance().createBook(4000,	"Walden 0",	1854,	"Wilder Publications 0", new String[] {	"Henry", "David", "Thoreau 0"	},BookGenre.PHILOSOPHY,	4.3);
		boolean isKidFriendlyEligible = book.isKidFriendlyEligible();
		assertFalse("For Philosophy Genre", isKidFriendlyEligible);
		
		book = BookmarkManager.getInstance().createBook(4000,	"Walden 0",	1854,	"Wilder Publications 0", new String[] {	"Henry", "David", "Thoreau 0"	},BookGenre.SELF_HELP,	4.3);
		isKidFriendlyEligible = book.isKidFriendlyEligible();
		assertFalse("For self help Genre", isKidFriendlyEligible);
	}

}
