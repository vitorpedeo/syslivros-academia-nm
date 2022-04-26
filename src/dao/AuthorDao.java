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
  private Integer authorsPerPage = 5;

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
    String sql = """
                SELECT id, nome, nacionalidade, ano_nascimento FROM autor FETCH FIRST 5 ROWS ONLY""";
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

  public List<Author> getAllPaginated() {
    this.authorsPerPage += 5;

    String sql = """
                 SELECT id, nome, nacionalidade, ano_nascimento FROM autor FETCH FIRST ? ROWS ONLY""";
    List<Author> authors = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setInt(1, authorsPerPage);

      ResultSet results = statement.executeQuery();
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

  public Author getById(Long id) {
    String sql = "SELECT id, nome, nacionalidade, ano_nascimento FROM autor where id = ?";
    Author livro = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, id);

      ResultSet results = statement.executeQuery();

      if (results.next()) {
        livro = fillAuthor(results);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return livro;
  }

  public List<Author> getByName(String name) {
    String sql = """
      SELECT id, nome, nacionalidade, ano_nascimento FROM autor where lower(nome) like ?
      """;
    List<Author> authors = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      String lowerCasedName = name.toLowerCase();
      statement.setString(1, "%" + lowerCasedName + "%");

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

  public void update(Author updatedAuthor) {
    String sql = """
                    UPDATE autor SET nome = ?,
                                      nacionalidade = ?,
                                      ano_nascimento = ?
                    WHERE id = ? 
                    """;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      prepareParameters(statement, updatedAuthor);

      Integer numLinhas = statement.executeUpdate();

      System.out.println(numLinhas);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void delete(Long id) {
    String sql = "DELETE FROM autor where id = ?";

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, id);

      Integer numLinhas = statement.executeUpdate();

      System.out.println(numLinhas);
    } catch (SQLException e) {
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

  private Author fillAuthor(ResultSet resultSet) throws SQLException {
    Author author = new Author();
    
    author.setId(resultSet.getLong("id"));
    author.setName(resultSet.getString("nome"));
    author.setNationality(resultSet.getString("nacionalidade"));
    author.setBirthYear(resultSet.getLong("ano_nascimento"));

    return author;
  }
}
