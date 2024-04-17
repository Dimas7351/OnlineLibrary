package full.project.OnlineLibrary.services;

import full.project.OnlineLibrary.models.Book;
import full.project.OnlineLibrary.models.Person;
import full.project.OnlineLibrary.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Person findOne(int id){
        return peopleRepository.findById(id).orElse(null);
    }

    public void save(Person person){
        peopleRepository.save(person);
    }

    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public List<Book> getCurrentBooks(int id){
        Optional<Person> person = peopleRepository.findById(id);

        if(person.isPresent()) {
//            Hibernate.initialize(person.get().getBooks());
            List<Book> listOfBooks = person.get().getBooks();

            long duration = ((10 * 60 * 60 * 24) * 1000);
            for (Book book : listOfBooks){
                if (book.getOwnedAt()!=null){
                    if ((book.getOwnedAt().getTime() + duration) <= (Timestamp.from(Instant.now()).getTime()))
                        book.setExpired(true);
                }
            }
            return listOfBooks;
        }
        else
            return Collections.emptyList();
    }

    public Optional<Person> findByName(String name){
        return peopleRepository.findByName(name);
    }
}
