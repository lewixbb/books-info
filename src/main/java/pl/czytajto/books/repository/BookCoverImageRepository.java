package pl.czytajto.books.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import pl.czytajto.books.model.BookCoverImage;

public interface BookCoverImageRepository extends JpaRepository<BookCoverImage, Long> {

	
}
