package org.htech.universityproject.utilities;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.Notifications;
import org.htech.universityproject.controllers.CommunicationController;
import org.htech.universityproject.controllers.ViewProfileController;

import java.io.File;
import java.util.Objects;
import java.util.regex.Pattern;

public class UtilityMethods {


    public static void switchToScene(Node node, String fxmlFile) {
        try {
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(UtilityMethods.class.getResource("/views/"+fxmlFile+".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("My Expense Tracker");
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            setupMovement(stage,root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchToScene(ActionEvent event, String fxmlFile) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(UtilityMethods.class.getResource("/views/"+fxmlFile+".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("My Expense Tracker");
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(e -> {
                System.exit(0);
            });

            setupMovement(stage, root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchToScene(String fxmlFile) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(UtilityMethods.class.getResource("/views/"+fxmlFile+".fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.centerOnScreen();
            stage.setTitle("My Expense Tracker");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void switchToScene(String fxmlFile, Runnable onSuccessCallback) {
        try {
            FXMLLoader loader = new FXMLLoader(UtilityMethods.class.getResource("/views/" + fxmlFile + ".fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Friend Connections Graph");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            Object controller = loader.getController();
            if (controller instanceof CallbackHandler) {
                ((CallbackHandler) controller).setOnSuccessCallback(onSuccessCallback);
            }

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openUserChat(Node node, int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(UtilityMethods.class.getResource("/views/chat_message.fxml"));
            setupStage(node, userId, loader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setupStage(Node node, int userId, FXMLLoader loader) throws java.io.IOException {
        Parent root = loader.load();

        CommunicationController controller = loader.getController();
        controller.loadMessages(userId);
        Stage oldStage = (Stage) node.getScene().getWindow();
        oldStage.close();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();

        UtilityMethods.setupMovement(stage,root);
    }


    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static void showPopupCenter(String message) {
        Notifications.create()
                .title("Notification")
                .text(message)
                .position(Pos.CENTER)
                .hideAfter(javafx.util.Duration.seconds(1.5))
                .darkStyle()
                .hideCloseButton()
                .showInformation();
    }

    public static void showPopup(String message) {
        Notifications.create()
                .title("Notification")
                .text(message)
                .position(Pos.BOTTOM_RIGHT)
                .hideAfter(javafx.util.Duration.seconds(1.5))
                .darkStyle()
                .hideCloseButton()
                .showInformation();
    }

    public static void showPopupWarning(String message) {
        Notifications.create()
                .title("Warning")
                .text(message)
                .position(Pos.BOTTOM_RIGHT)
                .hideAfter(javafx.util.Duration.seconds(1.5))
                .darkStyle()
                .hideCloseButton()
                .showError();
    }


    public static void openUserProfile(Node node, int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(UtilityMethods.class.getResource("/views/view_profile.fxml"));
            Parent root = loader.load();

            ViewProfileController controller = loader.getController();
            controller.loadProfileDetails(userId);
            Stage oldStage = (Stage) node.getScene().getWindow();
            oldStage.close();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static File handleChooseImage(Node node) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg")
        );
        Stage stage = (Stage) node.getScene().getWindow();
        File selectedImage = fileChooser.showOpenDialog(stage);

        if (selectedImage != null && selectedImage.length() <= 2 * 1024 * 1024) {
            UtilityMethods.showPopup("Image selected successfully!");
        } else {
            selectedImage = null;
            UtilityMethods.showPopupWarning("Invalid image!");
        }
        return selectedImage;
    }

    public static void setupMovement(Stage stage, Parent root) {
        final double[] offsetX = {0};
        final double[] offsetY = {0};

        root.setOnMousePressed(event -> {
            offsetX[0] = event.getSceneX();
            offsetY[0] = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - offsetX[0]);
            stage.setY(event.getScreenY() - offsetY[0]);
        });

        root.setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ESCAPE) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit Confirmation");
                alert.setHeaderText("Are you sure you want to exit?");
                alert.setContentText("Press OK to exit or Cancel to stay.");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.exit(0);
                    }
                });
            }
        });

    }

    public static void switchToScene(MouseEvent event, String fxmlFile) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(UtilityMethods.class.getResource("/views/"+fxmlFile+".fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("My Expense Tracker");
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(e -> {
                System.exit(0);
            });

            setupMovement(stage, root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
