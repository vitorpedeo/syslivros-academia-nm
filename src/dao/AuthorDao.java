package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

  public List<Author> getAll() {
    String sql = "SELECT id, nome, nacionalidade, ano_nascimento FROM autor";
    List<Author> authors = null;

    try(
      Connection connection = ConfigDB.getConnection();
      Statement statement = connection.createStatement();
    ) {
      ResultSet results = statement.executeQuery(sql);
      authors = new ArrayList<Author>();
      Author author;

      while (results.next()) {
        author = fillAuthor(results);
      
        authors.add(author);        
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return authors;
  }

  public List<Author> getByName(String name) {
    String sql = "SELECT id, nome, nacionalidade, ano_nascimento FROM autor where nome like ?";
    List<Author> authors = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setString(1, "%" + name + "%");

      ResultSet results = statement.executeQuery();
      authors = new ArrayList<Author>();
      Author author;

      while (results.next()) {
        author = fillAuthor(results);
      
        authors.add(author);        
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return authors;
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

  private Author fillAuthor(ResultSet resultSet) throws SQLException {
    Author author = new Author();
    
    author.setId(resultSet.getLong("id"));
    author.setName(resultSet.getString("nome"));
    author.setNationality(resultSet.getString("nacionalidade"));
    author.setBirthYear(resultSet.getLong("ano_nascimento"));

    return author;
  }
}
