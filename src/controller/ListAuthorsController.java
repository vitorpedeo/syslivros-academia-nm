package controller;

import java.util.List;
import java.util.ArrayList;

import domain.Author;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Main;
import service.AuthorService;

public class ListAuthorsController {
    private AuthorService authorService = new AuthorService();

    @FXML
    private ListView<Author> authorsListView;

    @FXML
    private Button deleteAuthorButton;

    @FXML
    private Button editAuthorButton;

    @FXML
    private Button newAuthorButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

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
		}

    private void loadAuthors() {
			authorsListView.getItems().clear();

      List<Author> authors = authorService.getAll();

      for (Author a : authors) {
        authorsListView.getItems().add(a);
      }	
		}

		private void loadAuthors(String filterBy, String searchTerm) {
			authorsListView.getItems().clear();

			List<Author> authors = new ArrayList<Author>();

      for (Author a : authors) {
        authorsListView.getItems().add(a);
      }

      if (filterBy == "name") {
        authors = authorService.getByName(searchTerm);
      }

      for (Author a : authors) {
        authorsListView.getItems().add(a);
      }
		}

    @FXML
    public void handleAuthorSelect(MouseEvent event) {
      deleteAuthorButton.setDisable(false);
			editAuthorButton.setDisable(false);
    }

    @FXML
    public void handleDeleteAuthor(ActionEvent event) {

    }

    @FXML
    public void handleEditAuthor(ActionEvent event) {

    }

    @FXML
    public void handleNewAuthor(ActionEvent event) {
      Main.changeToNewAuthorScene();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
			String searchTerm = searchTextField.getText();

			if (searchTerm == "") {
				loadAuthors();
			} else {
				loadAuthors("name", searchTerm);
			}
    }

}
