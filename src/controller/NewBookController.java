package controller;

import java.util.List;
import java.util.ArrayList;

import domain.Author;
import domain.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import main.Main;
import service.AuthorService;
import service.BookService;

public class NewBookController {
	private AuthorService authorService = new AuthorService();
	private BookService bookService = new BookService();
	
	private Alert infoAlert = new Alert(AlertType.INFORMATION);
	private Alert errorAlert = new Alert(AlertType.ERROR);
	
	private Integer offset = 0;

	@FXML
	private TextArea descriptionTitleInput;
	
	@FXML
	private TextField editionTextInput;
	
	@FXML
	private Button goBackButton;
	
	@FXML
	private TextField isbnTextInput;
	
	@FXML
  private Button loadMoreAuthorsButton;

	@FXML
	private Button newBookButton;
	
	@FXML
	private ListView<Author> authorsListView;
	
	@FXML
	private TextField titleTextInput;

	private List<Author> selectedAuthors = new ArrayList<Author>();
	
	@FXML
	public void initialize() {
		loadAuthors();

		authorsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		authorsListView.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
			selectedAuthors.clear();
			selectedAuthors.addAll(authorsListView.getSelectionModel().getSelectedItems());
		});

		authorsListView.setCellFactory(param -> new ListCell<Author>() {
			@Override
			protected void updateItem(Author item, boolean empty) {
					super.updateItem(item, empty);
	
					if (empty || item == null || item.getName() == null) {
							setText(null);
					} else {
							setText(item.getName());
					}
			}
		});
	}

	private void loadAuthors() {
		List<Author> authors = new ArrayList<Author>();

		authors = authorService.getAll();

		for (Author a : authors) {
			authorsListView.getItems().add(a);
		}	
	}

	@FXML
	public void handleGoBack(ActionEvent event) {
		Main.changeToMainScene();
	}

	@FXML
  public void handleLoadMoreAuthors(ActionEvent event) {
		List<Author> authors = new ArrayList<Author>();

		offset += 5;
		authors = authorService.getAll(offset);

		for (Author a : authors) {
			authorsListView.getItems().add(a);
		}	
  }
	
	@FXML
	public void handleNewBook(ActionEvent event) {
		String bookTitle = titleTextInput.getText();
		String bookDescription = descriptionTitleInput.getText();
		String bookIsbn = isbnTextInput.getText();
		String bookEdition = editionTextInput.getText();
		
		if (bookTitle == "" || bookDescription == "" || bookIsbn == "" || bookEdition == "") {
			showErrorAlert("Cadastrar Livro", "Erro de Valida????o", 
			"Preencha todos os campos antes de prosseguir!");
			
			return;
		}
		
		String onlyNumbersRegex = "[0-9]+";
		Boolean editionHasOnlyNumbers = bookEdition.matches(onlyNumbersRegex);
		
		if (!editionHasOnlyNumbers) {
			showErrorAlert("Cadastrar Livro", "Erro de Valida????o", 
			"A edi????o deve conter apenas n??meros!");
			
			return;
		}
		
		Boolean editionIsNotNegative = Integer.parseInt(bookEdition) > 0;
		
		if (!editionIsNotNegative) {
			showErrorAlert("Cadastrar Livro", "Erro de Valida????o", 
			"A edi????o n??o pode ser negativa!");
			
			return;
		}

		Boolean isAuthorsEmpty = selectedAuthors.size() == 0;
		
		if (isAuthorsEmpty) {
			showErrorAlert("Cadastrar Livro", "Erro de Valida????o", 
			"Escolha ao menos 1 autor!");
			
			return;
		}

		Book book = new Book(bookTitle, bookIsbn, Integer.parseInt(bookEdition), bookDescription);
		book.setAuthors(selectedAuthors);
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