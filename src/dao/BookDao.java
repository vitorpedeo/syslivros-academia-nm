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
import domain.AuthorBook;
import domain.Book;

public class BookDao {
  private Integer booksPerPage = 5;

  public void insert(Book book) {
    String sql = """
                    INSERT INTO livro(titulo, isbn, edicao, descricao) 
                    VALUES(?, ?, ?, ?)""";
    String idSql = """
                      SELECT livros_sequence.currval from dual
                      """;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      PreparedStatement idStatement = connection.prepareStatement(idSql);
    ) {
      connection.setAutoCommit(false);

      prepareParameters(statement, book);

      statement.executeUpdate();

      ResultSet resultSet = idStatement.executeQuery();
      
      if (resultSet.next()) {
        book.setId(resultSet.getLong(1));
      }

      AuthorBookDao authorBookDao = new AuthorBookDao(connection);
      List<AuthorBook> authorBooks = new ArrayList<AuthorBook>();
      AuthorBook authorBook;

      for (Author a : book.getAuthors()) {
        authorBook = new AuthorBook(a.getId(), book.getId());
        authorBooks.add(authorBook);
      }

      authorBookDao.insert(authorBooks);

      connection.commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Book> getAll() {
    String sql = """
      SELECT id, titulo, isbn, edicao, descricao FROM livro FETCH FIRST 5 ROWS ONLY""";
    List<Book> livros = null;

    try(
      Connection connection = ConfigDB.getConnection();
      Statement statement = connection.createStatement();
    ) {
      ResultSet results = statement.executeQuery(sql);
      livros = new ArrayList<Book>();
      Book livro;

      while (results.next()) {
        livro = fillBook(results);
      
        livros.add(livro);        
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return livros;
  }

  public List<Book> getAllPaginated() {
    this.booksPerPage += 5;

    String sql = """
                 SELECT id, titulo, isbn, edicao, descricao FROM livro FETCH FIRST ? ROWS ONLY""";
    List<Book> books = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setInt(1, booksPerPage);

      ResultSet results = statement.executeQuery();
      books = new ArrayList<Book>();
      Book book;

      while (results.next()) {
        book = fillBook(results);
      
        books.add(book);        
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return books;
  }

  public Book getById(Long id) {
    String sql = "SELECT id, titulo, isbn, edicao, descricao FROM livro where id = ?";
    Book livro = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, id);

      ResultSet results = statement.executeQuery();

      if (results.next()) {
        livro = fillBook(results);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return livro;
  }

  public List<Book> getByTitle(String title) {
    String sql = "SELECT id, titulo, isbn, edicao, descricao FROM livro where lower(titulo) like ?";
    List<Book> livros = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      String lowerCasedTitle = title.toLowerCase();
      statement.setString(1, "%" + lowerCasedTitle + "%");

      ResultSet results = statement.executeQuery();
      livros = new ArrayList<Book>();
      Book livro;

      while (results.next()) {
        livro = fillBook(results);
      
        livros.add(livro);        
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return livros;
  }

  public void update(Book updatedBook) {
    String sql = """
                    UPDATE livro SET titulo = ?,
                                     isbn = ?,
                                     edicao = ?,
                                     descricao = ?
                    WHERE id = ? 
                    """;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      prepareParameters(statement, updatedBook);

      Integer lines = statement.executeUpdate();

      System.out.println(lines);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void delete(Long id) {
    String sql = "DELETE FROM livro where id = ?";

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

  private void prepareParameters(PreparedStatement stm, Book livro) throws SQLException {
    stm.setString(1, livro.getTitle());
    stm.setString(2, livro.getIsbn());
    stm.setInt(3, livro.getEdition());
    stm.setString(4, livro.getDescription());

    if (livro.getId() != null) {
      stm.setLong(5, livro.getId());
    }
  }

  private Book fillBook(ResultSet resultSet) throws SQLException {
    Book livro = new Book();
    
    livro.setId(resultSet.getLong("id"));
    livro.setTitle(resultSet.getString("titulo"));
    livro.setIsbn(resultSet.getString("isbn"));
    livro.setEdition(resultSet.getInt("edicao"));
    livro.setDescription(resultSet.getString("descricao"));

    return livro;
  }
}
