package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.ConfigDB;
import domain.Livro;

public class LivroDao {
  public void insert(Livro livro) throws SQLException {
    String sql = """
                    INSERT INTO livro(titulo, autor, isbn, descricao, edicao) 
                    VALUES(?, ?, ?, ?, ?)""";
    String idSql = """
                      SELECT livros_sequence.currval from dual
                      """;

    try(
      // 1 - Abrir conexão
      Connection connection = ConfigDB.getConnection();
      // 2 - Abrir sessão
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      PreparedStatement idStatement = connection.prepareStatement(idSql);
    ) {
      prepareParameters(statement, livro);
      
      statement.executeUpdate();

      ResultSet resultSet = idStatement.executeQuery();
      
      if (resultSet.next()) {
        livro.setId(resultSet.getLong(1));
      }
      // Desconecta do banco de dados automaticamente
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Livro> getAll() throws SQLException {
    String sql = "SELECT id, titulo, isbn, edicao, autor, descricao FROM livro";
    List<Livro> livros = null;

    try(
      Connection connection = ConfigDB.getConnection();
      Statement statement = connection.createStatement();
    ) {
      ResultSet results = statement.executeQuery(sql);
      livros = new ArrayList<Livro>();
      Livro livro;

      while (results.next()) {
        livro = fillBook(results);
      
        livros.add(livro);        
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return livros;
  }

  public Livro getById(Long id) throws SQLException {
    String sql = "SELECT id, titulo, isbn, edicao, autor, descricao FROM livro where id = ?";
    Livro livro = null;

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

  public List<Livro> getByTitle(String title) throws SQLException {
    String sql = "SELECT id, titulo, isbn, edicao, autor, descricao FROM livro where titulo like ?";
    List<Livro> livros = null;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setString(1, "%" + title + "%");

      ResultSet results = statement.executeQuery();
      livros = new ArrayList<Livro>();
      Livro livro;

      while (results.next()) {
        livro = fillBook(results);
      
        livros.add(livro);        
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return livros;
  }

  public void update(Livro livroAtualizado) throws SQLException {
    String sql = """
                    UPDATE livro SET titulo = ?,
                                     autor = ?,
                                     isbn = ?,
                                     descricao = ?,
                                     edicao = ?
                    WHERE id = ? 
                    """;

    try(
      Connection connection = ConfigDB.getConnection();
      PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      prepareParameters(statement, livroAtualizado);

      Integer numLinhas = statement.executeUpdate();

      System.out.println(numLinhas);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void delete(Long id) throws SQLException {
    String sql = "DELETE FROM livro where id = ?";

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

  private void prepareParameters(PreparedStatement stm, Livro livro) throws SQLException {
    stm.setString(1, livro.getTitulo());
    stm.setString(2, livro.getAutor());
    stm.setString(3, livro.getIsbn());
    stm.setString(4, livro.getDescricao());
    stm.setInt(5, livro.getEdicao());

    if (livro.getId() != null) {
      stm.setLong(6, livro.getId());
    }
  }

  private Livro fillBook(ResultSet resultSet) throws SQLException {
    Livro livro = new Livro();
    
    livro.setId(resultSet.getLong("id"));
    livro.setTitulo(resultSet.getString("titulo"));
    livro.setIsbn(resultSet.getString("isbn"));
    livro.setEdicao(resultSet.getInt("edicao"));
    livro.setAutor(resultSet.getString("autor"));
    livro.setDescricao(resultSet.getString("descricao"));

    return livro;
  }
}
