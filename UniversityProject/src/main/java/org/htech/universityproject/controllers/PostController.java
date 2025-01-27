package org.htech.universityproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.htech.universityproject.database.DBConnection;
import org.htech.universityproject.modal.Post;
import org.htech.universityproject.start.Navigation;
import org.htech.universityproject.utilities.SessionManager;
import org.htech.universityproject.utilities.UtilityMethods;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class PostController extends Navigation {

    @FXML
    private ListView<HBox> postListView;

    @FXML
    private ImageView homeBtn;

    @FXML
    private TextField postTextField;

    @FXML
    private ImageView profileBtn;

    private Queue<Post> postQueue = new LinkedList<>();

    public void initialize() {
        loadPosts();
        homeBtn.setOnMouseClicked(event -> UtilityMethods.switchToScene(postListView, "student_dashboard"));
        profileBtn.setOnMouseClicked(event -> UtilityMethods.switchToScene(postListView, "profile"));
        postTextField.setOnMouseClicked(event -> UtilityMethods.switchToScene(postListView, "create_post"));
    }

    private Queue<Post> loadPostsFromDB() {
        Queue<Post> posts = new LinkedList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = """
            SELECT p.post_id, p.user_id, p.content, p.media, p.created_at, 
                   u.username, u.profile_picture, u.role,
                   c.course_name, s.active_subjects,
                   pr.subjects_taught
            FROM posts p 
            JOIN users u ON p.user_id = u.user_id
            LEFT JOIN students s ON u.user_id = s.user_id
            LEFT JOIN courses c ON s.course_id = c.course_id
            LEFT JOIN professors pr ON u.user_id = pr.user_id
            ORDER BY p.created_at DESC
        """;
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int postId = rs.getInt("post_id");
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String content = rs.getString("content");
                byte[] media = rs.getBytes("media");
                Timestamp createdAt = rs.getTimestamp("created_at");
                byte[] profilePicture = rs.getBytes("profile_picture");
                String role = rs.getString("role");
                String course = rs.getString("course_name");
                String activeSubjects = rs.getString("active_subjects");
                String subjectsTaught = rs.getString("subjects_taught");

                Post post = new Post(userId, postId, username, profilePicture, content, createdAt, media);
                post.setRole(role);
                post.setCourse(course);
                post.setActiveSubjects(activeSubjects);
                post.setSubjectsTaught(subjectsTaught);
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }



    private void loadPosts() {
        postQueue = loadPostsFromDB();
        HBox postContainer;
        for (Post post : postQueue) {
            postContainer = createPostView(post);
            postContainer.setMaxWidth(440);
            postContainer.setUserData(post);
            postListView.getItems().add(postContainer);
        }

        postListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                HBox box = postListView.getSelectionModel().getSelectedItem();
                if (box != null) {
                    Post post = (Post) box.getUserData();
                    openUserProfile(post.getUserId());
                }
            }
        });
    }

    private HBox createPostView(Post post) {
        String formattedDate = new SimpleDateFormat("MMMM dd, yyyy").format(new Date(post.getCreatedAt().getTime()));

        HBox postItem = new HBox(15);
        postItem.setMaxWidth(430);
        postItem.setStyle("-fx-padding: 10; -fx-background-color: #F1ECE6; -fx-border-color: #ddd; -fx-border-radius: 5;");
        postItem.setAlignment(Pos.TOP_LEFT);

        ImageView userIcon = new ImageView();
        userIcon.setFitHeight(50);
        userIcon.setFitWidth(50);
        userIcon.setSmooth(true);
        Circle clip = new Circle(25);
        clip.setCenterX(25);
        clip.setCenterY(25);
        userIcon.setClip(clip);
        if (post.getProfilePicture() != null) {
            userIcon.setImage(new Image(new ByteArrayInputStream(post.getProfilePicture())));
        } else {
            userIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/default-profile.png"))));
        }

        userIcon.setOnMouseClicked(event -> openUserProfile(post.getUserId()));

        VBox postDetails = new VBox(5);

        HBox userInfo = new HBox(10);
        userInfo.setAlignment(Pos.CENTER_LEFT);
        Text usernameText = new Text(post.getUsername());
        usernameText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        usernameText.setStyle("-fx-fill: #800100;");

        Text dateText = new Text(formattedDate);
        dateText.setFont(Font.font("Arial", 12));
        dateText.setStyle("-fx-fill: #800100;");

        userInfo.getChildren().addAll(usernameText, dateText);

        HBox userDetails = new HBox();
        userDetails.setAlignment(Pos.CENTER_LEFT);

        Text roleInfo = new Text();
        if ("student".equals(post.getRole()) && post.getCourse() != null) {
            roleInfo.setText("Student: " + post.getCourse());
        } else if ("professor".equals(post.getRole()) && post.getSubjectsTaught() != null) {
            roleInfo.setText("Professor: " + post.getSubjectsTaught());
        } else if ("student".equalsIgnoreCase(post.getRole())) {
            roleInfo.setText("Student"+"--------");
        }
        else{
            roleInfo.setText("Professor"+"-------");
        }
        roleInfo.setStyle("-fx-fill: #800100; -fx-font-size: 11px");
        userDetails.getChildren().add(roleInfo);

        TextArea descriptionArea = new TextArea(post.getContent());
        descriptionArea.setWrapText(true);
        descriptionArea.setEditable(false);
        descriptionArea.setStyle("-fx-control-inner-background: #F6F5F3; -fx-border-color: transparent;");
        descriptionArea.setFont(Font.font("Arial", 12));
        descriptionArea.setPrefRowCount(3);

        postDetails.getChildren().addAll(userInfo,userDetails, descriptionArea);

        if (post.getMedia() != null) {
            ImageView mediaView = new ImageView(new Image(new ByteArrayInputStream(post.getMedia())));
            mediaView.setFitHeight(100);
            mediaView.setPreserveRatio(true);
            postDetails.getChildren().add(mediaView);
        }

        postItem.getChildren().addAll(userIcon, postDetails);
        postItem.setMaxWidth(430);

        ContextMenu contextMenu = new ContextMenu();
        if(post.getUserId() == SessionManager.getCurrentUserId()){
            MenuItem deletePostItem = new MenuItem("Delete Post");
            deletePostItem.setOnAction(event -> deletePost(post.getPostId()));
            contextMenu.getItems().add(deletePostItem);
        }
        else if (post.getUserId() != SessionManager.getCurrentUserId()) {
            MenuItem openProfileItem = new MenuItem("Open Profile");
            openProfileItem.setOnAction(event -> openUserProfile(post.getUserId()));
            contextMenu.getItems().add(openProfileItem);
        }

        postItem.setOnContextMenuRequested(event -> contextMenu.show(postItem, event.getScreenX(), event.getScreenY()));


        return postItem;
    }

    private void openUserProfile(int userId) {
      UtilityMethods.openUserProfile(postListView, userId);
    }

    private void deletePost(int post_id){
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM posts WHERE post_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, post_id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                UtilityMethods.showPopup("Post deleted successfully!");
            }
            postListView.getItems().clear();
            loadPosts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
