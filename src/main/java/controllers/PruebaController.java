package controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prueba")
public class PruebaController {
    @GetMapping("/todos")
    public String paraTodos() {
        return "Accesible a todos";
    }

    @GetMapping("/usuario")
    @PreAuthorize("hasRole('ROLE_USUARIO') or hasRole('ROLE_GERENTE') or hasRole('ROLE_ADMINISTRADOR')")
    public String paraUsuario() {
        return "Accesible a todos los usuarios";
    }

    @GetMapping("/gerente")
    @PreAuthorize("hasRole('ROLE_GERENTE')")
    public String paraGestor() {
        return "Accesible solo a gerentes";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public String paraAdmin() {
        return "Accesible solo a administradores";
    }

}