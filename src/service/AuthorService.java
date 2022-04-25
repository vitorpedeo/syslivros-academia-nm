package service;

import dao.AuthorDao;
import domain.Author;

public class AuthorService {
  AuthorDao authorDao = new AuthorDao();

  public void insert(Author author) {
    authorDao.insert(author);
  }
}
