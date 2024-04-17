package full.project.OnlineLibrary.services;

import full.project.OnlineLibrary.models.Book;
import full.project.OnlineLibrary.models.Person;
import full.project.OnlineLibrary.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll(boolean sort){
        if(sort)
            return booksRepository.findAll(Sort.by("year"));
        else
            return booksRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> findAllDividedByPages(Integer page, Integer books_per_page, boolean sort){
        if(sort)
            return booksRepository.findAll(PageRequest.of(page, books_per_page,
                    Sort.by("year"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, books_per_page)).getContent();
    }

    @Transactional(readOnly = true)
    public Book findOne(int id){
        return booksRepository.findById(id).orElse(null);
    }

    public void save(Book book){
        booksRepository.save(book);
    }

    public void update(int id, Book updatedBook){
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    public void delete(int id){
        booksRepository.deleteById(id);
    }

    public Optional<Person> getOwner(int id){
        return booksRepository.findById(id).map(Book::getOwner);
    }

    public void setOwner(int book_id, Person selectedPerson){
        booksRepository.findById(book_id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setOwnedAt(Timestamp.from(Instant.now()));
                    book.setExpired(false);
                }
        );
    }

    public void releaseBook(int id){
        booksRepository.findById(id).ifPresent(
                book ->{
                    book.setOwner(null);
                    book.setExpired(false);
                    book.setOwnedAt(null);
        });
    }

    public List<Book> findByName(String name){
        return booksRepository.findByNameIgnoreCaseStartingWith(name);
    }


}
