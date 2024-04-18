package full.project.OnlineLibrary.controllers;


import full.project.OnlineLibrary.models.Book;
import full.project.OnlineLibrary.models.Person;
import full.project.OnlineLibrary.services.BooksService;
import full.project.OnlineLibrary.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
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

        return "user/books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id,
                       @ModelAttribute("person") Person person,
                       Principal principal) {
        String username = principal.getName();
        String role = peopleService.getUserRole(username);

        model.addAttribute("principal", username);
        model.addAttribute("book", booksService.findOne(id));

        Optional<Person> bookOwner = booksService.getOwner(id);

        System.out.println(principal.getName());

        bookOwner.ifPresent(value -> model.addAttribute("owner", value));

        System.out.println("SDDDDDDDDDDDDDDDDDD" + role);

        if ("ROLE_ADMIN".equals(role)) {
            model.addAttribute("people", peopleService.findAll());
            return "admin/books/show";
        }

        return "user/books/show";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());

        return "user/books/new";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "redirect:/new";

        booksService.save(book);
        return "redirect:/user/books";

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", booksService.findOne(id));

        return "user/books/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id){
        if (bindingResult.hasErrors())
            return "user/books/edit";

        booksService.update(id, book);
        return "redirect:/user/books";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/user/books";
    }

    @PatchMapping("/{id}/set_owner")
    public String setUser(@ModelAttribute("person") Person person,
                          @PathVariable("id") int id){
        booksService.setOwner(id, person);
        return "redirect:/user/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id){
        booksService.releaseBook(id);
        return "redirect:/user/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage(){
        return "user/books/search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("starting") String start){
        model.addAttribute("books", booksService.findByName(start));
        return "user/books/search";
    }
}
