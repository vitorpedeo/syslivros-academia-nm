package main;

import controller.EditAuthorController;
import controller.EditBookController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	private static Stage primaryStage;
	
	@Override
	public void start(Stage pStage) {
		primaryStage = pStage;
		
		changeToMainScene();
	}

	public static void changeToMainScene() {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("../javafx/main.fxml"));
			Scene scene = new Scene(root);

			primaryStage.setTitle("Sistema de Biblioteca");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void changeToNewAuthorScene() {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("../javafx/newauthor.fxml"));
			Scene scene = new Scene(root);

			primaryStage.setTitle("Sistema de Biblioteca - Cadastrar Autor");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void changeToListAuthorsScene() {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("../javafx/listauthors.fxml"));
			Scene scene = new Scene(root);

			scene.getStylesheets().add("/resources/list-authors.css");

			primaryStage.setTitle("Sistema de Biblioteca - Listagem dos Autores");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void changeToEditAuthorScene(Long authorId) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../javafx/editauthor.fxml"));
			Parent root = loader.load();
			
			EditAuthorController editAuthorController = loader.getController();
			editAuthorController.getAuthorData(authorId);
			
			Scene scene = new Scene(root);
			
			primaryStage.setTitle("Sistema de Biblioteca - Editar Autor");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void changeToNewBookScene() {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("../javafx/newbook.fxml"));
			Scene scene = new Scene(root);

			scene.getStylesheets().add("/resources/new-book.css");

			primaryStage.setTitle("Sistema de Biblioteca - Cadastrar Livro");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void changeToListBooksScene() {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("../javafx/listbooks.fxml"));
			Scene scene = new Scene(root);

			scene.getStylesheets().add("/resources/list-books.css");

			primaryStage.setTitle("Sistema de Biblioteca - Listagem de Livros");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void changeToEditBookScene(Long bookId) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../javafx/editbook.fxml"));
			Parent root = loader.load();
			
			EditBookController editBookController = loader.getController();
			editBookController.getBookData(bookId);
			
			Scene scene = new Scene(root);

			scene.getStylesheets().add("/resources/edit-book.css");
			
			primaryStage.setTitle("Sistema de Biblioteca - Editar Livro");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}