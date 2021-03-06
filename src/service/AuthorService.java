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

  public List<Author> getAll(Integer offset) {
    return authorDao.getAll(offset);
  }

  public List<Author> getByName(String name) {
    return authorDao.getByName(name);
  }

  public Author getById(Long id) {
    return authorDao.getById(id);
  }

  public List<Author> getAllByBookId(Long bookId) {
    return authorDao.getAllByBookId(bookId);
  }

  public void update(Author author) {
    authorDao.update(author);
  }
  
  public void delete(Long id) {
    authorDao.delete(id);
  }
}
