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
                    INSERT INTO author(name, nationality, birth_year) 
                    VALUES(?, ?, ?)""";

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    ) {
      prepareParameters(statement, author);
      
      statement.executeUpdate();

      ResultSet resultSet = statement.getGeneratedKeys();
      
      if (resultSet.next()) {
        author.setId(resultSet.getLong(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Author> getAll() {
    String sql = """
                SELECT id, name, nationality, birth_year FROM author 
                FETCH FIRST 5 ROWS ONLY""";
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

  public List<Author> getAll(Integer offset) {
    String sql = """
                SELECT id, name, nationality, birth_year FROM author 
                OFFSET ? ROWS FETCH FIRST 5 ROWS ONLY""";
    List<Author> authors = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setInt(1, offset);

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
    String sql = "SELECT id, name, nationality, birth_year FROM author where id = ?";
    Author author = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, id);

      ResultSet results = statement.executeQuery();

      if (results.next()) {
        author = fillAuthor(results);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return author;
  }

  public List<Author> getAllByBookId(Long bookId) {
    String sql = """
                    SELECT a.* FROM author a
                    INNER JOIN author_book al
                    ON a.id = al.author_id
                    AND al.book_id = ?""";
    List<Author> authors = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, bookId);

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

  public List<Author> getByName(String name) {
    String sql = """
      SELECT id, name, nationality, birth_year FROM author where lower(name) like ?
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
                    UPDATE author SET name = ?,
                                      nationality = ?,
                                      birth_year = ?
                    WHERE id = ? 
                    """;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      prepareParameters(statement, updatedAuthor);

      Integer lines = statement.executeUpdate();

      System.out.println(lines);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void delete(Long id) {
    String sql = "DELETE FROM author where id = ?";

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
    author.setName(resultSet.getString("name"));
    author.setNationality(resultSet.getString("nationality"));
    author.setBirthYear(resultSet.getLong("birth_year"));

    return author;
  }
}
