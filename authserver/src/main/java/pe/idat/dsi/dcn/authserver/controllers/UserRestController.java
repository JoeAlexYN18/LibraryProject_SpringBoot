package pe.idat.dsi.dcn.authserver.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import pe.idat.dsi.dcn.authserver.models.User;
import pe.idat.dsi.dcn.authserver.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(this.userService.findAll());
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if(result.hasFieldErrors()) {
            return validation(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.userService.save(user));
    }



    private ResponseEntity<?> validation(BindingResult result) {

        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " "+ error.getDefaultMessage());
        });
        
        return ResponseEntity.badRequest().body(result.getFieldErrors());
    }
    
}
