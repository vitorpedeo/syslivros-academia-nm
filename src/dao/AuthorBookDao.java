package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import domain.AuthorBook;

public class AuthorBookDao {
  Connection connection;
  
  public AuthorBookDao(Connection connection) {
    this.connection = connection;
  }

  public void insert(List<AuthorBook> authorBooks) throws SQLException {
    String sql = """
        INSERT INTO autor_livro(id_autor, id_livro) VALUES(?, ?)
        """;
    
    try(
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      for (AuthorBook aB : authorBooks) {
        statement.setLong(1, aB.getAuthorId());
        statement.setLong(2, aB.getBookId());
        statement.addBatch();
      }

      statement.executeBatch();
    } catch (Exception e) {
      connection.rollback();

      throw e;
    }
  }
}
