package controller;

import dao.LivroDao;
import domain.Livro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.Main;

public class EditBookController {

    @FXML
    private TextField authorTextField;

    @FXML
    private TextArea descriptionTextField;

    @FXML
    private Button editBookButton;

    @FXML
    private TextField editionTextField;

    @FXML
    private Button goBackButton;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField isbnTextField;

    @FXML
    private TextField titleTextField;

    @FXML
    void handleEditBook(ActionEvent event) {
      Livro livro = new Livro();

      livro.setId(Long.parseLong(idTextField.getText()));
      livro.setTitulo(titleTextField.getText());
      livro.setDescricao(descriptionTextField.getText());
      livro.setIsbn(isbnTextField.getText());
      livro.setEdicao(Integer.parseInt(editionTextField.getText()));
      livro.setAutor(authorTextField.getText());

      LivroDao livroDao = new LivroDao();

      try {
          livroDao.update(livro);
      } catch (Exception e) {
          e.printStackTrace();
      } 
    }

    public void getBookData(Long bookId) {
      LivroDao livroDao = new LivroDao();

      try {
        Livro livro = livroDao.getById(bookId);

        idTextField.setText(livro.getId().toString());
        titleTextField.setText(livro.getTitulo());
        descriptionTextField.setText(livro.getDescricao());
        isbnTextField.setText(livro.getIsbn());
        editionTextField.setText(livro.getEdicao().toString());
        authorTextField.setText(livro.getAutor());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    @FXML
    void handleGoBack(ActionEvent event) {
      try {
        Main.changeToMainScene();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}
