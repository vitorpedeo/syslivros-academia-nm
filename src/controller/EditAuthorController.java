package controller;

import java.time.Year;
import java.time.ZoneId;

import domain.Author;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import main.Main;
import service.AuthorService;

public class EditAuthorController {
  private static Integer CURRENT_YEAR = Year.now(ZoneId.of("America/Sao_Paulo")).getValue();

  private AuthorService authorService = new AuthorService();
  
  private Author editingAuthor = new Author();

  private Alert infoAlert = new Alert(AlertType.INFORMATION);
  private Alert errorAlert = new Alert(AlertType.ERROR);
  
  @FXML
  private TextField birthYearTextInput;
  
  @FXML
  private Button editAuthorButton;
  
  @FXML
  private Button goBackButton;
  
  @FXML
  private TextField nameTextInput;
  
  @FXML
  private TextField nationalityTextInput;
  
  public void getAuthorData(Long authorId) {
    editingAuthor = authorService.getById(authorId);

    nameTextInput.setText(editingAuthor.getName());
    nationalityTextInput.setText(editingAuthor.getNationality());
    birthYearTextInput.setText(editingAuthor.getBirthYear().toString());
  }

  @FXML
  public void handleEditAuthor(ActionEvent event) {
    String authorName = nameTextInput.getText();
    String authorNationality = nationalityTextInput.getText();
    String authorBirthYear = birthYearTextInput.getText();

    if (authorName == "" || authorNationality == "" || authorBirthYear == "") {
      showErrorAlert("Editar Autor", "Erro de Validação", 
            "Preencha todos os campos antes de prosseguir!");
     
      return;
    }

    String onlyNumbersRegex = "[0-9]+";
    Boolean birthYearHasOnlyNumbers = authorBirthYear.matches(onlyNumbersRegex);
    Boolean birthYearHasCorrectLength = authorBirthYear.length() == 4; 

    if (!birthYearHasOnlyNumbers || !birthYearHasCorrectLength) {
      showErrorAlert("Editar Autor", "Erro de Validação", 
            "Informe um ano válido!");
     
      return;
    }

    Boolean birthYearIsInRightInterval = Integer.parseInt(authorBirthYear) < CURRENT_YEAR;

    if (!birthYearIsInRightInterval) {
      showErrorAlert("Editar Autor", "Erro de Validação", 
            "O ano informado deve ser menor que o ano atual!");
     
      return;
    }

    editingAuthor.setName(authorName);
    editingAuthor.setNationality(authorNationality);
    editingAuthor.setBirthYear(Long.parseLong(authorBirthYear));

    authorService.update(editingAuthor);

    showInfoAlert("Editar Autor", "Sucesso", 
    "Autor editado com sucesso!");

    Main.changeToListAuthorsScene();
  }
  
  @FXML
  public void handleGoBack(ActionEvent event) {
    Main.changeToListAuthorsScene();
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
