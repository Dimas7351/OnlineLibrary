package full.project.OnlineLibrary.controllers;


import full.project.OnlineLibrary.models.Book;
import full.project.OnlineLibrary.models.Person;
import full.project.OnlineLibrary.services.BooksService;
import full.project.OnlineLibrary.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(@RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "books_per_page", required = false) Integer books_per_page,
            @RequestParam(value = "sort_by_year",required = false) boolean sort,
            Model model){
        if(page == null || books_per_page == null)
            model.addAttribute("books", booksService.findAll(sort));
        else
            model.addAttribute("books", booksService.findAllDividedByPages(page, books_per_page, sort));

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id,
                       @ModelAttribute("person") Person person){
        model.addAttribute("book",booksService.findOne(id));

        Optional<Person> bookOwner = booksService.getOwner(id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", peopleService.findAll());
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());

        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "redirect:/new";

        booksService.save(book);
        return "redirect:/books";

    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", booksService.findOne(id));

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id){
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/set_owner")
    public String setUser(@ModelAttribute("person") Person person,
                          @PathVariable("id") int id){
        booksService.setOwner(id, person);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id){
        booksService.releaseBook(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage(){
        return "books/search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("starting") String start){
        model.addAttribute("books", booksService.findByName(start));
        return "books/search";
    }
}
