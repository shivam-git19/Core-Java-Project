package com.thrillio;

import java.util.List;

import com.thrillio.Entities.Bookmark;
import com.thrillio.Entities.User;
import com.thrillio.constant.KidFriendlyStatus;
import com.thrillio.constant.UserType;
import com.thrillio.controllers.BookmarkController;
import com.thrillio.partner.Shareable;

public class View {
	public static void browse(User user, List<List<Bookmark>> bookmarks) {
		System.out.println("\n" + user.getEmail() + " is browsing items...");
		int bookmarkCount = 0;

		for (List<Bookmark> bookmarkList : bookmarks) {
			for (Bookmark bookmark : bookmarkList) {
				// Bookmarking !
				//if (bookmarkCount < DataStore.USER_BOOKMARK_LIMIT) {
					boolean isBookmarked = getBookmarkDecision(bookmark);
					if (isBookmarked) {
						bookmarkCount++;

						BookmarkController.getInstance().saveUserBookmark(user, bookmark);

						System.out.println("New Item Bookmarked -- " + bookmark);					

					}
				//}

				
				if (user.getUserType().equals(UserType.EDITOR) || user.getUserType().equals(UserType.CHIEF_EDITOR)) {
							
					// Mark as Kid-friendly
					if (bookmark.isKidFriendlyEligible()
							&& bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.UNKNOWN)) {
						KidFriendlyStatus KidFriendlyStatus = getKidFriendlyStatusDecision(bookmark);
						if(!KidFriendlyStatus.equals(com.thrillio.constant.KidFriendlyStatus.UNKNOWN)) {
							BookmarkController.getInstance().setKidFriendlyStatus(user, KidFriendlyStatus, bookmark);		
						}
					}
					
					// Sharing !
					if(bookmark.getKidFriendlyStatus().equals(KidFriendlyStatus.APPROVED)
							&& bookmark instanceof Shareable) {
						boolean isShared = getShareDecision();
						if(isShared) {
							BookmarkController.getInstance().share(user, bookmark);
						}
					}
				}
			}
		}
	}

	private static boolean getShareDecision() {
		return Math.random() < 0.5 ? true : false;
	}

	private static KidFriendlyStatus getKidFriendlyStatusDecision(Bookmark bookmark) {
		double randomVal = Math.random();
		return randomVal < 0.4 ? KidFriendlyStatus.APPROVED
				: (randomVal >= 0.4 && randomVal < 0.8) ? KidFriendlyStatus.REJECTED
						: KidFriendlyStatus.UNKNOWN;

	}

	private static boolean getBookmarkDecision(Bookmark bookmaek) {
		return Math.random() < 0.5 ? true : false;
	}
}
