package main;

import controller.EditBookController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	private static Stage primaryStage;
	
	@Override
	public void start(Stage pStage) throws Exception {
		primaryStage = pStage;
		
		changeToMainScene();
	}

	public static void changeToMainScene() throws Exception {
		Parent root = FXMLLoader.load(Main.class.getResource("../javafx/main.fxml"));
		Scene scene = new Scene(root);

		primaryStage.setTitle("Sistema de Biblioteca");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void changeToNewBookScene() throws Exception {
		Parent root = FXMLLoader.load(Main.class.getResource("../javafx/newbook.fxml"));
		Scene scene = new Scene(root);

		primaryStage.setTitle("Sistema de Biblioteca - Cadastrar Livro");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void changeToEditBookScene(Long bookId) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../javafx/editbook.fxml"));
		Parent root = loader.load();
		
		EditBookController editBookController = loader.getController();
		editBookController.getBookData(bookId);
		
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Sistema de Biblioteca - Editar Livro");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}