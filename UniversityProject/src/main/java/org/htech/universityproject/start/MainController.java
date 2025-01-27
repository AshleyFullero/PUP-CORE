package org.htech.universityproject.start;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import org.htech.universityproject.utilities.UtilityMethods;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private ImageView imageView;

    private final List<Image> images = new ArrayList<>();
    private final List<String> animationTypes = new ArrayList<>();
    private final List<Double> animationDurations = new ArrayList<>();

    @FXML
    public void initialize() {
        loadImages();
        configureAnimationTypes();
        configureAnimationDurations();
        startImageTransitions();
    }

    private void loadImages() {
        for (int i = 1; i < 9; i++) {
            String imagePath = getClass().getResource("/introImages/" + i + ".png").toExternalForm();
            images.add(new Image(imagePath));
        }
    }

    private void configureAnimationTypes() {
        animationTypes.add("ease-in");
        animationTypes.add("ease-out");
        animationTypes.add("ease-out");
        animationTypes.add("ease-out");
        animationTypes.add("ease-out");
        animationTypes.add("ease-in");
        animationTypes.add("ease-in");
        animationTypes.add("ease-in");
    }

    private void configureAnimationDurations() {
        animationDurations.add(0.3);
        animationDurations.add(0.3);
        animationDurations.add(0.3);
        animationDurations.add(0.3);
        animationDurations.add(0.3);
        animationDurations.add(0.6);
        animationDurations.add(0.3);
        animationDurations.add(0.3);
    }

    private void startImageTransitions() {
        List<Transition> transitions = new ArrayList<>();

        int index =0;
        for (Image image : images) {
            String animation = animationTypes.get(index);
            TranslateTransition translate = new TranslateTransition(Duration.seconds(animationDurations.get(index)), imageView);
            translate.setInterpolator(animation.equals("ease-in") ? Interpolator.EASE_IN : Interpolator.EASE_OUT);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(animationDurations.get(index)), imageView);
            fadeIn.setOnFinished(event -> imageView.setImage(image));
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(animationDurations.get(index)), imageView);
            SequentialTransition transition = new SequentialTransition(fadeIn, fadeOut);
            transitions.add(transition);
            index++;
        }

        SequentialTransition allTransitions = new SequentialTransition();
        allTransitions.getChildren().addAll(transitions);
        allTransitions.setOnFinished(event -> redirectToLogin());
        allTransitions.play();
    }

    private void redirectToLogin() {
        UtilityMethods.switchToScene(imageView, "login");
    }
}
