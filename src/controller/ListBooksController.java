package controller;

import java.util.ArrayList;
import java.util.List;

import domain.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import main.Main;
import service.BookService;

public class ListBooksController {
		private BookService bookService = new BookService();

    @FXML
    private ListView<Book> booksListView;

    @FXML
    private Button deleteBookButton;

    @FXML
    private Button editBookButton;

    @FXML
    private Button goBackButton;

    @FXML
    private Button loadMoreButton;

    @FXML
    private Button newBookButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

		@FXML
		public void initialize() {
			loadBooks("initial");

			booksListView.setCellFactory(param -> new ListCell<Book>() {
				@Override
				protected void updateItem(Book item, boolean empty) {
						super.updateItem(item, empty);
		
						if (empty || item == null || item.getTitle() == null) {
								setText(null);
						} else {
								setText(item.getTitle());
						}
				}
			});
		}

		private void loadBooks(String type) {
			booksListView.getItems().clear();

      List<Book> books = new ArrayList<Book>();

      if (type == "initial") {
        books = bookService.getAll();
      } else if (type == "more") {
        books = bookService.getAllPaginated();
      }

      for (Book b : books) {
        booksListView.getItems().add(b);
      }	
		}

		private void loadBooks(String filterBy, String searchTerm) {
			booksListView.getItems().clear();

			List<Book> books = new ArrayList<Book>();

      if (filterBy == "title") {
        books = bookService.getByTitle(searchTerm);
      }

      for (Book b : books) {
        booksListView.getItems().add(b);
      }
		}

    @FXML
    public void handleBookSelect(MouseEvent event) {
			deleteBookButton.setDisable(false);
			editBookButton.setDisable(false);
    }

    @FXML
    public void handleDeleteBook(ActionEvent event) {
			Book selectedBook = booksListView.getSelectionModel().getSelectedItem();
			Long bookId = selectedBook.getId();

      Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, 
                                "Tem certeza que deseja deletar o livro " + selectedBook.getTitle() + " ?",
                                ButtonType.YES,
                                ButtonType.CANCEL
                                );
      confirmationAlert.showAndWait();
      
      if (confirmationAlert.getResult() == ButtonType.YES) {
        bookService.delete(bookId);

        booksListView.getItems().remove(selectedBook);

        editBookButton.setDisable(true);
        deleteBookButton.setDisable(true);
      }
    }

    @FXML
    public void handleEditBook(ActionEvent event) {
			Book selectedBook = booksListView.getSelectionModel().getSelectedItem();
			Long bookId = selectedBook.getId();

      Main.changeToEditBookScene(bookId);
    }

    @FXML
    public void handleGoBack(ActionEvent event) {
			Main.changeToMainScene();
    }

    @FXML
    public void handleLoadMore(ActionEvent event) {
			loadBooks("more");
    }	

    @FXML
    public void handleNewBook(ActionEvent event) {
			Main.changeToNewBookScene();
    }

    @FXML
    public void handleSearch(ActionEvent event) {
			String searchTerm = searchTextField.getText();

			if (searchTerm == "") {
				loadBooks("initial");
			} else {
				loadBooks("title", searchTerm);
			}
    }
}