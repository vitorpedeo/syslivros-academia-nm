package service;

import java.util.List;

import dao.BookDao;
import domain.Book;

public class BookService {
  BookDao bookDao = new BookDao();

  public void insert(Book book) {
    bookDao.insert(book);
  }

  public List<Book> getAll() {
    return bookDao.getAll();
  }

  public List<Book> getAllPaginated() {
    return bookDao.getAllPaginated();
  }

  public List<Book> getByTitle(String title) {
    return bookDao.getByTitle(title);
  }

  public Book getById(Long id) {
    return bookDao.getById(id);
  }

  public void update(Book Book) {
    bookDao.update(Book);
  }
  
  public void delete(Long id) {
    bookDao.delete(id);
  }
}
