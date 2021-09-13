package pl.czytajto.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.czytajto.books.model.BookComment;

public interface BookCommentRepository extends JpaRepository<BookComment, Long>{

}
