package controller;

import dao.LivroDao;
import domain.Livro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.Main;

public class NewBookController {
	
	@FXML
	private TextField authorTextField;
	
	@FXML
	private TextArea descriptionTextField;
	
	@FXML
	private TextField editionTextField;
	
	@FXML
	private Button goBackButton;
	
	@FXML
	private TextField isbnTextField;
	
	@FXML
	private Button registerBookButton;
	
	@FXML
	private TextField titleTextField;
	
	@FXML
	public void handleGoBack(ActionEvent event) {
		try {
			Main.changeToMainScene();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void handleNewBook(ActionEvent event) {
		Livro livro = new Livro();
		
		livro.setTitulo(titleTextField.getText());
		livro.setDescricao(descriptionTextField.getText());
		livro.setIsbn(isbnTextField.getText());
		livro.setEdicao(Integer.parseInt(editionTextField.getText()));
		livro.setAutor(authorTextField.getText());
		
		LivroDao livroDao = new LivroDao();
		
		try {
			livroDao.insert(livro);
			
			titleTextField.clear();
			descriptionTextField.clear();
			isbnTextField.clear();
			editionTextField.clear();
			authorTextField.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}