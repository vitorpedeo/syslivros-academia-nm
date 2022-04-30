package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import domain.Author;
import domain.Book;

public class AuthorBookDao {
  Connection connection;
  
  public AuthorBookDao(Connection connection) {
    this.connection = connection;
  }

  public void insert(Book book) throws SQLException {
    String sql = """
        INSERT INTO author_book(author_id, book_id) VALUES(?, ?)
        """;
    
    try(
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      for (Author a : book.getAuthors()) {
        statement.setLong(1, a.getId());
        statement.setLong(2, book.getId());
        statement.addBatch();
      }

      statement.executeBatch();
    } catch (Exception e) {
      connection.rollback();

      throw e;
    }
  }

  public void deleteAllByBookId(Long id) {
    String sql = """
        DELETE FROM author_book WHERE book_id = ?
        """;
    
    try(
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, id);

      statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
