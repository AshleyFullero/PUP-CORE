package org.htech.universityproject.utilities;

import javafx.scene.image.Image;
import org.htech.universityproject.modal.User;

public class SessionManager {

    private static int CurrentUserId;
    private static String CurrentUserEmail;
    private static String CurrentUserName;
    private static int CurrentGroupId;
    private static String CurrentGroupName;
    private static boolean CurrentUserStudent;
    private static String CurrentUserAboutMe;
    private static Image CurrentUserImage;
    private static User CurrentUser;


    public static int getCurrentUserId() {
        return CurrentUserId;
    }

    public static void setCurrentUserId(int currentUserId) {
        CurrentUserId = currentUserId;
    }

    public static String getCurrentUserEmail() {
        return CurrentUserEmail;
    }

    public static void setCurrentUserEmail(String currentUserEmail) {
        CurrentUserEmail = currentUserEmail;
    }

    public static String getCurrentUserName() {
        return CurrentUserName;
    }

    public static void setCurrentUserName(String currentUserName) {
        CurrentUserName = currentUserName;
    }

    public static int getCurrentGroupId() {
        return CurrentGroupId;
    }

    public static void setCurrentGroupId(int currentGroupId) {
        CurrentGroupId = currentGroupId;
    }

    public static String getCurrentGroupName() {
        return CurrentGroupName;
    }

    public static void setCurrentGroupName(String currentGroupName) {
        CurrentGroupName = currentGroupName;
    }

    public static String getCurrentUserAboutMe() {
        return CurrentUserAboutMe;
    }

    public static void setCurrentUserAboutMe(String currentUserAboutMe) {
        CurrentUserAboutMe = currentUserAboutMe;
    }

    public static Image getCurrentUserImage() {
        return CurrentUserImage;
    }

    public static void setCurrentUserImage(Image currentUserImage) {
        CurrentUserImage = currentUserImage;
    }

    public static boolean isCurrentUserStudent() {
        return CurrentUserStudent;
    }

    public static void setCurrentUserStudent(boolean currentUserStudent) {
        CurrentUserStudent = currentUserStudent;
    }

    public static User getCurrentUser() {
        return CurrentUser;
    }

    public static void setCurrentUser(User currentUser) {
        CurrentUser = currentUser;
    }
}
