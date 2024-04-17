package full.project.OnlineLibrary.controllers;

import full.project.OnlineLibrary.models.Person;
import full.project.OnlineLibrary.services.AdminService;
import full.project.OnlineLibrary.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PeopleService peopleService;
    private final AdminService adminService;

    @Autowired
    public AdminController(PeopleService peopleService, AdminService adminService) {
        this.peopleService = peopleService;
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public String show(Model model, @ModelAttribute("person") Person person){
            model.addAttribute("people", peopleService.findAll());
        return "/admin/manage";
    }

}
