package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.ConfigDB;
import domain.Book;

public class BookDao {
  private Integer booksPerPage = 5;

  public void insert(Book book) {
    String sql = """
                    INSERT INTO book(title, isbn, edition, description) 
                    VALUES(?, ?, ?, ?)""";

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    ) {
      connection.setAutoCommit(false);

      prepareParameters(statement, book);

      statement.executeUpdate();

      ResultSet resultSet = statement.getGeneratedKeys();
      
      if (resultSet.next()) {
        book.setId(resultSet.getLong(1));
      }

      AuthorBookDao authorBookDao = new AuthorBookDao(connection);

      authorBookDao.insert(book);

      connection.commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Book> getAll() {
    String sql = """
      SELECT id, title, isbn, edition, description FROM book FETCH FIRST 5 ROWS ONLY""";
    List<Book> books = null;

    try(
      Connection connection = ConfigDB.getConnection();
      Statement statement = connection.createStatement();
    ) {
      ResultSet results = statement.executeQuery(sql);
      books = new ArrayList<Book>();
      Book book;

      while (results.next()) {
        book = fillBook(results);
      
        books.add(book);        
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return books;
  }

  public List<Book> getAll(Integer offset) {
    this.booksPerPage += 5;

    String sql = """
                SELECT id, title, isbn, edition, description FROM book 
                OFFSET ? ROWS FETCH FIRST 5 ROWS ONLY""";
    List<Book> books = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setInt(1, offset);

      ResultSet results = statement.executeQuery();
      books = new ArrayList<Book>();
      Book author;

      while (results.next()) {
        author = fillBook(results);
      
        books.add(author);        
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return books;
  }

  public Book getById(Long id) {
    String sql = "SELECT id, title, isbn, edition, description FROM book where id = ?";
    Book book = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, id);

      ResultSet results = statement.executeQuery();

      if (results.next()) {
        book = fillBook(results);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return book;
  }

  public List<Book> getByTitle(String title) {
    String sql = "SELECT id, title, isbn, edition, description FROM book where lower(title) like ?";
    List<Book> books = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      String lowerCasedTitle = title.toLowerCase();
      statement.setString(1, "%" + lowerCasedTitle + "%");

      ResultSet results = statement.executeQuery();
      books = new ArrayList<Book>();
      Book book;

      while (results.next()) {
        book = fillBook(results);
      
        books.add(book);        
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return books;
  }

  public void update(Book updatedBook) {
    String sql = """
                    UPDATE book SET title = ?,
                                     isbn = ?,
                                     edition = ?,
                                     description = ?
                    WHERE id = ? 
                    """;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      prepareParameters(statement, updatedBook);

      statement.executeUpdate();

      AuthorBookDao authorBookDao = new AuthorBookDao(connection);

      authorBookDao.deleteAllByBookId(updatedBook.getId());

      authorBookDao.insert(updatedBook);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void delete(Long id) {
    String sql = "DELETE FROM book where id = ?";

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, id);

      Integer lines = statement.executeUpdate();

      System.out.println(lines);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void prepareParameters(PreparedStatement stm, Book book) throws SQLException {
    stm.setString(1, book.getTitle());
    stm.setString(2, book.getIsbn());
    stm.setInt(3, book.getEdition());
    stm.setString(4, book.getDescription());

    if (book.getId() != null) {
      stm.setLong(5, book.getId());
    }
  }

  private Book fillBook(ResultSet resultSet) throws SQLException {
    Book book = new Book();
    
    book.setId(resultSet.getLong("id"));
    book.setTitle(resultSet.getString("title"));
    book.setIsbn(resultSet.getString("isbn"));
    book.setEdition(resultSet.getInt("edition"));
    book.setDescription(resultSet.getString("description"));

    return book;
  }
}
