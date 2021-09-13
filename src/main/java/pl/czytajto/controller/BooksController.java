package pl.czytajto.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.czytajto.books.model.Book;
import pl.czytajto.books.model.BookComment;
import pl.czytajto.books.model.BookCoverImage;
import pl.czytajto.books.repository.BookCommentRepository;
import pl.czytajto.books.repository.BooksRepository;

@RequestMapping("/api/books")
@RestController
public class BooksController{

	private BooksRepository booksRepository;
	private BookCommentRepository commentRepository;

	@Autowired
	public BooksController(BooksRepository booksRepository, BookCommentRepository commentRepository) {
		this.commentRepository = commentRepository;
		this.booksRepository = booksRepository;
	}
		
	@GetMapping ("/page/{nr}")
	public Page <Book> getAllByPage (@PathVariable int nr){

		System.out.println("fawf");
		
		Pageable pageNr = PageRequest.of(nr, 4);
		Page <Book> books =  booksRepository.findAll(pageNr);
		
		return books;
	}
	
	@GetMapping ("/page/{nr}/{value}")
	public Page <Book> getAllBrowser (@PathVariable int nr, @PathVariable String value){
				
		Pageable pageNr = PageRequest.of(nr, 4);
		Page <Book> books;
		
		if(value.contains("all")) {
			books =  booksRepository.findAll(pageNr);
		}else {
			books = booksRepository.findAllByCategoryOrAuthorOrTitle(value, value, value,pageNr);		
		}
		
		return books;
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id){
		Book book = booksRepository.findById(id).get();
				
		if (book != null)
			return ResponseEntity.ok(book);			
		else
			return ResponseEntity.notFound().build();
	}
	
	@GetMapping(path = "/img/{id}")
	public BookCoverImage getImg(@PathVariable Long id){
		
		System.out.println("wczytanie zdjęcia");
		Book book = booksRepository.findById(id).get();
		BookCoverImage img = book.getCover();
					
		return img;
	}
	
	@GetMapping("/category/{category}")
	public ResponseEntity<List<Book>> getByCat(@PathVariable String category){
		List <Book> books = booksRepository.findByCategory(category);
		if (!books.isEmpty())
			return ResponseEntity.ok(books);
		else
			return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/addComment/{id}")
	public void addComment(@PathVariable Long id, @RequestBody BookComment bookComment){
		
		System.out.println("otrzymane id " + id);
		System.out.println(bookComment);
		Book book = booksRepository.findById(id).get();
		List <BookComment> comments = book.getComments();	
		comments.add(bookComment);
		book.setComments(comments);
		
		commentRepository.save(bookComment);
		
		System.out.println(book);
	}
	
	@PostMapping (consumes = {"multipart/form-data","application/json","application/octet-stream"})
	public ResponseEntity<?> addBook (@RequestPart("file") MultipartFile file, @RequestPart("book") String book1) throws IOException{
		
		System.out.print("pierwszy krok");
		System.out.println(book1);
		
		Book book = new ObjectMapper().readValue(book1,Book.class);

		System.out.println(book);
							
		BookCoverImage img = new BookCoverImage(file.getOriginalFilename(), file.getContentType(), 
				file.getBytes());
		
		System.out.println("dodano zdjęcie");
				
	if(book.getId()==null) {
		
		book.setCover(img);
		
		Book addedBook = booksRepository.save(book);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(addedBook.getId())
				.toUri();
		return ResponseEntity.created(location).body(book);
	} else {
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
		
	}
}
