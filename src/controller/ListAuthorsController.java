package controller;

import java.util.List;
import java.util.ArrayList;

import domain.Author;
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
    private Button goBackButton;

    @FXML
    private Button loadMoreButton;

    @FXML
    private Button newAuthorButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
		public void initialize() {
			loadAuthors("initial");

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

    private void loadAuthors(String type) {
			authorsListView.getItems().clear();

      List<Author> authors = new ArrayList<Author>();

      if (type == "initial") {
        authors = authorService.getAll();
      } else if (type == "more") {
        authors = authorService.getAllPaginated();
      }

      for (Author a : authors) {
        authorsListView.getItems().add(a);
      }	
		}

		private void loadAuthors(String filterBy, String searchTerm) {
			authorsListView.getItems().clear();

			List<Author> authors = new ArrayList<Author>();

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
      Author selectedAuthor = authorsListView.getSelectionModel().getSelectedItem();
			Long authorId = selectedAuthor.getId();

      Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, 
                                "Tem certeza que deseja deletar o autor " + selectedAuthor.getName() + " ?",
                                ButtonType.YES,
                                ButtonType.CANCEL
                                );
      confirmationAlert.showAndWait();
      
      if (confirmationAlert.getResult() == ButtonType.YES) {
        authorService.delete(authorId);

        authorsListView.getItems().remove(selectedAuthor);

        editAuthorButton.setDisable(true);
        deleteAuthorButton.setDisable(true);
      }
    }

    @FXML
    public void handleEditAuthor(ActionEvent event) {
      Author selectedAuthor = authorsListView.getSelectionModel().getSelectedItem();
			Long authorId = selectedAuthor.getId();

      Main.changeToEditAuthorScene(authorId);
    }

    @FXML
    public void handleGoBack(ActionEvent event) {
      Main.changeToMainScene();
    }

    @FXML
    void handleLoadMore(ActionEvent event) {
      loadAuthors("more");
    }

    @FXML
    public void handleNewAuthor(ActionEvent event) {
      Main.changeToNewAuthorScene();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
			String searchTerm = searchTextField.getText();

			if (searchTerm == "") {
				loadAuthors("initial");
			} else {
				loadAuthors("name", searchTerm);
			}
    }
}
