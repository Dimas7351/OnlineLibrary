package full.project.OnlineLibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Название книги не может быть пустым")
    @Size(min = 3, message = "Название книги должно быть длиннее 2 символов")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Имя автора не может быть пустым")
    @Size(min = 3, message = "Имя автора должно быть длиннее 2 символов")
    @Column(name = "author")
    private String author;


    @Min(value = 1800)
    private int year;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "owned_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ownedAt;

    @Transient
    private boolean isExpired;

    public Book(){}

    public Book(String name, String author, int year, Person owner) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getOwnedAt() {
        return ownedAt;
    }

    public void setOwnedAt(Date ownedAt) {
        this.ownedAt = ownedAt;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}
