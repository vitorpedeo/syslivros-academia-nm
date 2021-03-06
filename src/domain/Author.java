package domain;

public class Author {
  private Long id;
  private String name;
  private String nationality;
  private Long birthYear;

  public Author() {}

  public Author(String name, String nationality, Long birthYear) {
    this.name = name;
    this.nationality = nationality;
    this.birthYear = birthYear;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public Long getBirthYear() {
    return birthYear;
  }

  public void setBirthYear(Long birthYear) {
    this.birthYear = birthYear;
  }

  @Override
  public String toString() {
    return "Author [birthYear=" + birthYear + ", id=" + id + ", name=" + name + ", nationality=" + nationality + "]";
  }
}
