package full.project.OnlineLibrary.repositories;

import full.project.OnlineLibrary.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByNameIgnoreCaseStartingWith(String start);
}
