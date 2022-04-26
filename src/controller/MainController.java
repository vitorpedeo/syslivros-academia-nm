package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.Main;

public class MainController {
	
	@FXML
	private Button addAuthorButton;
	
	@FXML
	private Button addBookButton;
	
	@FXML
	private Button listAuthorsButton;
	
	@FXML
	private Button listBooksButton;
	
	@FXML
	void handleAddAuthor(ActionEvent event) {
		Main.changeToNewAuthorScene();	
	}
	
	@FXML
	void handleAddBook(ActionEvent event) {
		Main.changeToNewBookScene();
	}
	
	@FXML
	void handleListAuthors(ActionEvent event) {
		Main.changeToListAuthorsScene();	
	}
	
	@FXML
	void handleListBooks(ActionEvent event) {
		Main.changeToListBooksScene();
	}
	
}
