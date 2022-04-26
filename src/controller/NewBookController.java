package controller;

import domain.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import main.Main;
import service.BookService;

public class NewBookController {
		private BookService bookService = new BookService();

  	private Alert infoAlert = new Alert(AlertType.INFORMATION);
  	private Alert errorAlert = new Alert(AlertType.ERROR);

    @FXML
    private TextArea descriptionTitleInput;

    @FXML
    private TextField editionTextInput;

    @FXML
    private Button goBackButton;

    @FXML
    private TextField isbnTextInput;

    @FXML
    private Button newBookButton;

    @FXML
    private TextField titleTextInput;

    @FXML
    public void handleGoBack(ActionEvent event) {
			Main.changeToMainScene();
    }

    @FXML
    public void handleNewBook(ActionEvent event) {
			String bookTitle = titleTextInput.getText();
			String bookDescription = descriptionTitleInput.getText();
			String bookIsbn = isbnTextInput.getText();
			String bookEdition = editionTextInput.getText();

			if (bookTitle == "" || bookDescription == "" || bookIsbn == "" || bookEdition == "") {
				showErrorAlert("Editar Livro", "Erro de Validação", 
							"Preencha todos os campos antes de prosseguir!");
			
				return;
			}

			String onlyNumbersRegex = "[0-9]+";
			Boolean editionHasOnlyNumbers = bookEdition.matches(onlyNumbersRegex);

			if (!editionHasOnlyNumbers) {
				showErrorAlert("Editar Livro", "Erro de Validação", 
							"A edição deve conter apenas números!");
			
				return;
			}

			Boolean editionIsNotNegative = Integer.parseInt(bookEdition) > 0;

			if (!editionIsNotNegative) {
				showErrorAlert("Editar Livro", "Erro de Validação", 
							"A edição não pode ser negativa!");
			
				return;
			}

			Book book = new Book(bookTitle, bookIsbn, Integer.parseInt(bookEdition), bookDescription);
			bookService.insert(book);

			showInfoAlert("Cadastrar Livro", "Sucesso", 
			"Livro cadastrado com sucesso!");

			Main.changeToListBooksScene();
    }

		public void showErrorAlert(String title, String headerText, String message) {
			errorAlert.setTitle(title);
			errorAlert.setHeaderText(headerText);
			errorAlert.setContentText(message);
			errorAlert.showAndWait();
		}
	
		public void showInfoAlert(String title, String headerText, String message) {
			infoAlert.setTitle(title);
			infoAlert.setHeaderText(headerText);
			infoAlert.setContentText(message);
			infoAlert.showAndWait();
		}
}