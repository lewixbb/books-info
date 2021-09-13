package pl.czytajto.books.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javassist.SerialVersionUID;

@Entity
@Component
@Table(name="books")
public class Book implements Serializable{
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="book_id")
	private Long id;
	private String title;
	private String author;
	private String category;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private BookCoverImage cover;
	@OneToMany
	private List <BookComment> comments;
	
	Book(){}

	public Book(String title, String author, String category, BookCoverImage cover, List<BookComment> comments) {
		super();
		this.title = title;
		this.author = author;
		this.category = category;
		this.cover = cover;
		this.comments = comments;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public List<BookComment> getComments() {
		return comments;
	}

	public void setComments(List<BookComment> comments) {
		this.comments = comments;
	}

	public BookCoverImage getCover() {
		return cover;
	}

	public void setCover(BookCoverImage cover) {
		
		System.out.println("próba dodania zdjecia");
		this.cover = cover;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", category=" + category + ", comments="
				+ comments + "]";
	}


}
