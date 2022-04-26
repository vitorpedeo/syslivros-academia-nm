package domain;

public class Book {
  private Long id;
  private String title;
  private String isbn;
  private Integer edition;
  private String description;

  public Book() {}

  public Book(String title, String isbn, Integer edition, String description) {
    this.title = title;
    this.isbn = isbn;
    this.edition = edition;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Integer getEdition() {
    return edition;
  }

  public void setEdition(Integer edition) {
    this.edition = edition;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Livro [description=" + description + ", edition=" + edition + ", id=" + id + ", isbn=" + isbn
        + ", title=" + title + "]";
  }
}
