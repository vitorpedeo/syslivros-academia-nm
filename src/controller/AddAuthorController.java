package controller;

import java.time.Year;
import java.time.ZoneId;

import dao.AuthorDao;
import domain.Author;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import main.Main;

public class AddAuthorController {
  private static Integer CURRENT_YEAR = Year.now(ZoneId.of("America/Sao_Paulo")).getValue();
  private AuthorDao authorDao = new AuthorDao();

  private Alert infoAlert = new Alert(AlertType.INFORMATION);
  private Alert errorAlert = new Alert(AlertType.ERROR);

  @FXML
  private Button addAuthorButton;

  @FXML
  private TextField birthYearTextInput;

  @FXML
  private Button goBackButton;

  @FXML
  private TextField nameTextInput;

  @FXML
  private TextField nationalityTextInput;

  @FXML
  public void handleGoBack(ActionEvent event) {
    Main.changeToMainScene();
  }

  @FXML
  public void handleAddNewAuthor(ActionEvent event) {
    String authorName = nameTextInput.getText();
    String authorNationality = nationalityTextInput.getText();
    String authorBirthYear = birthYearTextInput.getText();

    if (authorName == "" || authorNationality == "" || authorBirthYear == "") {
      showErrorAlert("Cadastrar Autor", "Erro de Validação", 
            "Preencha todos os campos antes de prosseguir!");
     
      return;
    }

    String onlyNumbersRegex = "[0-9]+";
    Boolean birthYearHasOnlyNumbers = authorBirthYear.matches(onlyNumbersRegex);
    Boolean birthYearHasCorrectLength = authorBirthYear.length() == 4; 

    if (!birthYearHasOnlyNumbers || !birthYearHasCorrectLength) {
      showErrorAlert("Cadastrar Autor", "Erro de Validação", 
            "Informe um ano válido!");
     
      return;
    }

    Boolean birthYearIsInRightInterval = Integer.parseInt(authorBirthYear) < CURRENT_YEAR;

    if (!birthYearIsInRightInterval) {
      showErrorAlert("Cadastrar Autor", "Erro de Validação", 
            "O ano informado deve ser menor que o ano atual!");
     
      return;
    }

    Author author = new Author(authorName, authorNationality, Long.parseLong(authorBirthYear));
    authorDao.insert(author);

    showInfoAlert("Cadastrar Autor", "Sucesso", 
    "Autor cadastrado com sucesso!");

    Main.changeToMainScene();
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
