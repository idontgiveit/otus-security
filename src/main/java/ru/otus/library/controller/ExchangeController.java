package ru.otus.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.library.service.BookService;

@Controller
public class ExchangeController {

    @Autowired
    BookService bookService;

    @RequestMapping(value = "/exchange", method = RequestMethod.GET)
    public String getBooks(Model model) {
        model.addAttribute("books", bookService.getBooks());
        return "exchange";
    }

    @RequestMapping(value = "/exchange/reserve", method = RequestMethod.GET)
    public RedirectView reserveBook(Model model, @RequestParam(value = "id") Long id) {
        bookService.reserveBook(id);
        return new RedirectView("/exchange");
    }

    @RequestMapping(value = "/exchange/return", method = RequestMethod.GET)
    public RedirectView returnBook(Model model, @RequestParam(value = "id") Long id) {
        bookService.returnBook(id);
        return new RedirectView("/exchange");
    }
}
