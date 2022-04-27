package domain;

public class AuthorBook {
  private Long id;
  private Long authorId;
  private Long bookId;

  public AuthorBook() {}

  public AuthorBook(Long authorId, Long bookId) {
    this.authorId = authorId;
    this.bookId = bookId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public Long getBookId() {
    return bookId;
  }

  public void setBookId(Long bookId) {
    this.bookId = bookId;
  }

  @Override
  public String toString() {
    return "AuthorBook [authorId=" + authorId + ", bookId=" + bookId + ", id=" + id + "]";
  }
}
