package ru.otus.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.library.service.BookService;

import java.net.http.HttpRequest;

@Controller
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public String getBooks(Model model) {
        model.addAttribute("books", bookService.getBooks());
        return "books";
    }

    @RequestMapping(value = "/books/delete", method = RequestMethod.GET)
    public RedirectView delete(Model model, @RequestParam(value = "id") Long id) {
        bookService.removeBook(id);
        return new RedirectView("/books");
    }

    @RequestMapping(value = "/books", method = RequestMethod.POST)
    public RedirectView addBook(@RequestParam(value = "book") String name) {
        bookService.addBook(name);
        return new RedirectView("/books");
    }
}
