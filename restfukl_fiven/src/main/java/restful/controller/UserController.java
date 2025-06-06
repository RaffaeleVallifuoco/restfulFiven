package restful.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import restful.model.User;
import restful.repository.UserRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepo;

    @GetMapping("/index")
    public ResponseEntity<?> getUser(
            @RequestParam(name = "cf", required = false) String cf,
            @RequestParam(name = "id", required = false) Integer id) {

        List<User> users = userRepo.findAll();
        Optional<User> user;

        if (cf == null && id == null) {
            return ResponseEntity.ok(users);
        } else if (cf == null) {
            user = userRepo.findById(id);
            return ResponseEntity.ok(user);
        } else {
            users = userRepo.findByCfContainingIgnoreCase(cf);
            return ResponseEntity.ok(users);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> store(@ModelAttribute User userForm, BindingResult bindingresult) {
        if (bindingresult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingresult.getAllErrors());
        }

        return ResponseEntity.ok(userRepo.save(userForm));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @ModelAttribute User user) {
        return userRepo.findById(id).map(u -> {
            u.setName(user.getName());
            u.setSurname(user.getSurname());
            u.setCf(user.getCf());
            u.setSalary(user.getSalary());
            u.setBDate(user.getBDate());
            return ResponseEntity.ok(userRepo.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Integer id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
