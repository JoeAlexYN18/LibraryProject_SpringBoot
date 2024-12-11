package pe.idat.dsi.dcn.authserver.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.idat.dsi.dcn.authserver.models.Role;
import pe.idat.dsi.dcn.authserver.services.RoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/roles")
public class RoleRestControlller {

    private RoleService roleService;

    public RoleRestControlller(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity<List<Role>> getAll(@RequestParam String param) {
        return ResponseEntity.ok(this.roleService.findAll());
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody Role role, BindingResult result) {
        if(result.hasFieldErrors()) {
            return validation(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.roleService.save(role));
    }

    private ResponseEntity<?> validation(BindingResult result) {

        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " "+ error.getDefaultMessage());
        });
        
        return ResponseEntity.badRequest().body(result.getFieldErrors());
    }
    
    
    
}
