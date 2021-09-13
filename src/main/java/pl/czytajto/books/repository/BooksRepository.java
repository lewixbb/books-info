package pl.czytajto.books.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import pl.czytajto.books.model.Book;

@Repository
public interface BooksRepository extends PagingAndSortingRepository<Book, Long> {

	List<Book> findByCategory(String category);
	List<Book> findByAuthor(String author);
	List<Book> findAllByTitle(String title);
	Book findByTitle(String title);
	Page <Book> findAllByCategoryOrAuthorOrTitle (String category, String author, String title, Pageable pageNr);
	
}
	
