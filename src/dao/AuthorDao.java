package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import config.ConfigDB;
import domain.Author;

public class AuthorDao {
  public void insert(Author author) {
    String sql = """
                    INSERT INTO autor(nome, nacionalidade, ano_nascimento) 
                    VALUES(?, ?, ?)""";
    String idSql = """
                      SELECT autor_sequence.currval from dual
                      """;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
      PreparedStatement idStatement = connection.prepareStatement(idSql);
    ) {
      prepareParameters(statement, author);
      
      statement.executeUpdate();

      ResultSet resultSet = idStatement.executeQuery();
      
      if (resultSet.next()) {
        author.setId(resultSet.getLong(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void prepareParameters(PreparedStatement stm, Author author) {
    try {
      stm.setString(1, author.getName());
      stm.setString(2, author.getNationality());
      stm.setLong(3, author.getBirthYear());

      if (author.getId() != null) {
        stm.setLong(4, author.getId());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
