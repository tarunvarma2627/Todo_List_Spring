
package com.example.todoList_practice;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodoController {

    @Autowired
    private TodoRepository todoRepo;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("todos", todoRepo.findAll());
        return "index";
    }

    @PostMapping("/add")
    public String addTodo(@RequestParam String title, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setCompleted(false);
        todo.setDeadline(deadline);  // 🆕 Set optional deadline
        todoRepo.save(todo);
        return "redirect:/";
    }


    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoRepo.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable Long id) {
        Todo todo = todoRepo.findById(id).get();
        todo.setCompleted(!todo.isCompleted());
        todoRepo.save(todo);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editTodo(@PathVariable Long id, Model model) {
        Todo todo = todoRepo.findById(id).orElse(null);
        model.addAttribute("todo", todo);
        return "edit";
    }

    @PostMapping("/update")
    public String updateTodo(@RequestParam Long id,
                             @RequestParam String title,
                             @RequestParam(required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {

        Todo todo = todoRepo.findById(id).orElse(null);
        if (todo != null) {
            todo.setTitle(title);
            todo.setDeadline(deadline);
            todoRepo.save(todo);
        }

        return "redirect:/";
    }

}
