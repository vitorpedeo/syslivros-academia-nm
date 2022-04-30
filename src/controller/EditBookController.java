package controller;

import java.util.ArrayList;
import java.util.List;

import domain.Author;
import domain.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import main.Main;
import service.AuthorService;
import service.BookService;

public class EditBookController {
  private AuthorService authorService = new AuthorService();
  private Book editingBook = new Book();

  private BookService bookService = new BookService();

  private Alert infoAlert = new Alert(AlertType.INFORMATION);
  private Alert errorAlert = new Alert(AlertType.ERROR);
  
  private Integer offset = 0;

  @FXML
  private TextArea descriptionTitleInput;

  @FXML
  private Button editBookButton;

  @FXML
  private TextField editionTextInput;

  @FXML
  private Button goBackButton;

  @FXML
  private Button loadMoreAuthorsButton;

  @FXML
  private TextField isbnTextInput;

  @FXML
	private ListView<Author> authorsListView;
  
  @FXML
	private ListView<Author> currentBookAuthorsListView;

  @FXML
  private TextField titleTextInput;

  @FXML
	public void initialize() {
		loadAuthors();

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

		currentBookAuthorsListView.setCellFactory(param -> new ListCell<Author>() {
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

  public void getBookData(Long bookId) {
    editingBook = bookService.getById(bookId);
    editingBook.setAuthors(authorService.getAllByBookId(bookId));

    titleTextInput.setText(editingBook.getTitle());
    descriptionTitleInput.setText(editingBook.getDescription());
    isbnTextInput.setText(editingBook.getIsbn());
    editionTextInput.setText(editingBook.getEdition().toString());

    currentBookAuthorsListView.getItems().addAll(editingBook.getAuthors());
  }

  @FXML
  public void handleAuthorSelect(MouseEvent event) {
    Author selectedAuthor = authorsListView.getSelectionModel().getSelectedItem();
    Boolean isAlreadyAnAuthor = false;

    for (Author author : currentBookAuthorsListView.getItems()) {
      if (selectedAuthor.getId() == author.getId()) {
        isAlreadyAnAuthor = true;
      }
    }

    if (isAlreadyAnAuthor) {
      showErrorAlert("Editar Livro", "Erro de Validação", 
            "O autor já foi selecionado!");
     
      return;
    } else {
      currentBookAuthorsListView.getItems().add(selectedAuthor);
    }
  }

  @FXML
  public void handleCurrentAuthorsSelect(MouseEvent event) {
    int selectedAuthorIndex = currentBookAuthorsListView.getSelectionModel().getSelectedIndex();

    currentBookAuthorsListView.getItems().remove(selectedAuthorIndex);
  }

  @FXML
  public void handleEditBook(ActionEvent event) {
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

    List<Author> selectedAuthors = currentBookAuthorsListView.getItems();
    Boolean isAuthorsEmpty = selectedAuthors.isEmpty();

    if (isAuthorsEmpty) {
      showErrorAlert("Editar Livro", "Erro de Validação", 
			"Escolha ao menos 1 autor!");
			
			return;
    }

    editingBook.setTitle(bookTitle);
    editingBook.setDescription(bookDescription);
    editingBook.setIsbn(bookIsbn);
    editingBook.setEdition(Integer.parseInt(bookEdition));
    editingBook.setAuthors(selectedAuthors);

    bookService.update(editingBook);

    showInfoAlert("Editar Livro", "Sucesso", 
    "Livro editado com sucesso!");

    Main.changeToListBooksScene();
  }
  
  @FXML
  public void handleGoBack(ActionEvent event) {
    Main.changeToListBooksScene();
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

  private void showErrorAlert(String title, String headerText, String message) {
    errorAlert.setTitle(title);
    errorAlert.setHeaderText(headerText);
    errorAlert.setContentText(message);
    errorAlert.showAndWait();
  }

  private void showInfoAlert(String title, String headerText, String message) {
    infoAlert.setTitle(title);
    infoAlert.setHeaderText(headerText);
    infoAlert.setContentText(message);
    infoAlert.showAndWait();
  }
}
