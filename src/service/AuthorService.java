package service;

import java.util.List;

import dao.AuthorDao;
import domain.Author;

public class AuthorService {
  AuthorDao authorDao = new AuthorDao();

  public void insert(Author author) {
    authorDao.insert(author);
  }

  public List<Author> getAll() {
    return authorDao.getAll();
  }

  public List<Author> getByName(String name) {
    return authorDao.getByName(name);
  }
}
