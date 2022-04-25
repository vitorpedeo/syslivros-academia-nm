package controller;

import main.Main;

import java.util.List;
import java.util.ArrayList;

import dao.LivroDao;
import domain.Livro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ListBooksController {

    @FXML
    private ListView<Livro> booksListView;

		@FXML
    private Button deleteBookButton;

    @FXML
    private Button editBookButton;

		@FXML
    private Button newBookButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

		@FXML
		public void initialize() {
			loadBooks();

			booksListView.setCellFactory(param -> new ListCell<Livro>() {
				@Override
				protected void updateItem(Livro item, boolean empty) {
						super.updateItem(item, empty);
		
						if (empty || item == null || item.getTitulo() == null) {
								setText(null);
						} else {
								setText(item.getTitulo());
						}
				}
			});
		}

		private void loadBooks() {
			booksListView.getItems().clear();

			LivroDao livroDao = new LivroDao();

			try {
				List<Livro> livros = livroDao.getAll();
				
				for (Livro l : livros) {
					booksListView.getItems().add(l);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void loadBooks(String filterBy, String searchTerm) {
			booksListView.getItems().clear();

			LivroDao livroDao = new LivroDao();

			try {
				List<Livro> livros = new ArrayList<Livro>();
				
				if (filterBy == "title") {
					livros = livroDao.getByTitle(searchTerm);
				}

				for (Livro l : livros) {
					booksListView.getItems().add(l);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@FXML
    private void handleSearch(ActionEvent event) {
			String searchTerm = searchTextField.getText();

			if (searchTerm == "") {
				loadBooks();
			} else {
				loadBooks("title", searchTerm);
			}
    }

		@FXML
    public void handleBookSelect(MouseEvent event) {
			deleteBookButton.setDisable(false);
			editBookButton.setDisable(false);
    }

		@FXML
    public void handleDeleteBook(ActionEvent event) {
			Livro selectedBook = booksListView.getSelectionModel().getSelectedItem();
			Long bookId = selectedBook.getId();

			LivroDao livroDao = new LivroDao();

			try {
				livroDao.delete(bookId);

				loadBooks();

				deleteBookButton.setDisable(true);
				editBookButton.setDisable(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@FXML
    public void handleNewBook(ActionEvent event) {
			try {
				Main.changeToNewBookScene();
			} catch (Exception e) {
				e.printStackTrace();
			}
    }

    @FXML
    public void handleEditBook(ActionEvent event) {
			Livro selectedBook = booksListView.getSelectionModel().getSelectedItem();
			Long bookId = selectedBook.getId();

			try {
				Main.changeToEditBookScene(bookId);
			} catch (Exception e) {
				e.printStackTrace();
			}
    }
}
