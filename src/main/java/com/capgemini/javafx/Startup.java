package com.capgemini.javafx;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Startup extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		String langCode = getParameters().getNamed().get("lang");
		if (langCode != null && !langCode.isEmpty()) {
			Locale.setDefault(Locale.forLanguageTag(langCode));
		}

		primaryStage.setTitle("StarterKit-JavaFX");

		Parent root = FXMLLoader.load(getClass().getResource("/com/capgemini/javafx/view/library-manager.fxml"), //
				ResourceBundle.getBundle("com/capgemini/javafx/bundle/base"));

		Scene scene = new Scene(root);

		scene.getStylesheets().add(getClass().getResource("/com/capgemini/javafx/css/standard.css").toExternalForm());
		// scene.getStylesheets().add(getClass().getResource("/com/capgemini/javafx/css/alternative.css").toExternalForm());

		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
}
