package com.example.CreditRequest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String mostrarEjemplo(Model model) {
        // Puedes definir una variable y establecer su valor en el modelo
        String mensaje = "¡Hola desde el controlador!";
        model.addAttribute("mensaje", mensaje);

        // Retorna el nombre de la plantilla HTML que quieres mostrar
        return "index";
    }
}
