package com.decagon.project8.controller;

import com.decagon.project8.dto.TaskDTO;
import com.decagon.project8.dto.UserDTO;
import com.decagon.project8.model.Task;
import com.decagon.project8.model.User;
import com.decagon.project8.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public String index(Model model, HttpSession session){
        if(session.getAttribute("id")==null){
            return "redirect:/login";
        }

        else {
            List<Task> allTasks = userService.showTaskByUser((Integer) session.getAttribute("id"));
            model.addAttribute("tasks", allTasks);
            session.setAttribute("tasks", allTasks);
            return "dashboard";
        }
    }


    @GetMapping(value = "/login")
    public String displayLoginPage(Model model){
        model.addAttribute("userDetails", new UserDTO());
        return "login";
    }

    @PostMapping(value = "/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session, Model model){
        String message = userService.loginUser(email, password);
        if(message.equals("success")){
            User user = userService.getUserByEmail(email);
            session.setAttribute("email,", user.getEmail());
            session.setAttribute("id", user.getId());
            session.setAttribute("fullName", user.getFullName());
            return "redirect:/dashboard";
        }
        else{
            model.addAttribute("errorMessage", message);
            return "redirect:/login";
        }

    }
    @GetMapping(value = "/register")

    public String showRegistrationForm(Model model){
        model.addAttribute("user_Details", new UserDTO());
        return "register";
    }

    @PostMapping(value = "/register")
    public String registration(@ModelAttribute UserDTO userDTO){
        User registeredUser = userService.registerUser(userDTO);
        if(registeredUser != null){
            return "redirect:/login";
        }
        else{
            return "redirect:/register";
        }
    }

    @GetMapping(value = "/task/{status}")
    public String taskByStatus(@PathVariable(name = "status") String status, Model model, HttpSession session){
        List<Task> viewAllTask = userService.viewAllTaskByStatus(status, (Integer) session.getAttribute("id"));
        model.addAttribute("viewAllTask", viewAllTask);
        return "viewAllByStatus";
    }


    @GetMapping(value = "/addTask")
    public String addTask(Model model){
        model.addAttribute("newTask", new TaskDTO());
        return "addTask";
    }

        @PostMapping(value="/addTask")
                public String createTask(@ModelAttribute TaskDTO dto){
            userService.createdTask(dto);
            return "redirect:/dashboard";

        }

        @GetMapping(value="/editTask/{id}")
        public String showEditPage(@PathVariable(name ="id") Integer id, Model model){
        Task task = userService.getTaskById(id);
        model.addAttribute("singleTask", task);
        model.addAttribute("taskBody", new TaskDTO());
        return "editTask";
    }

    @PostMapping(value="/editTask")
    public String editTask(@RequestParam("hide") String id, @ModelAttribute("singleTask") TaskDTO dto){
        userService.updateTitleAndDescription(dto, Integer.parseInt(id));
        return "redirect:/dashboard";
    }


    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable(name = "id") Integer id){
        userService.deletedById(id);
        return "redirect:/dashboard";
    }

    @GetMapping(value="/logout")
    public String loggingOut(HttpSession session){
        session.invalidate();
        return "redirect:/login";
            }

    @GetMapping("/viewTask/{id}")
    public String viewSingleTask(@PathVariable(name = "id") int id, Model model){
        Task task = userService.getTaskById(id);
        model.addAttribute("singleTask", task);
        return "viewTask";
    }

    @GetMapping(value = "/forward/{id}")
    public String moveForward(@PathVariable(name = "id") Integer id){
    userService.moveStatusForward(id);
    return "redirect:/dashboard";
            }

    @GetMapping(value= "/backward/{id}")
    public String moveBackward(@PathVariable(name = "id") Integer id){
        userService.moveStatusBackwards(id);
        return "redirect:/dashboard";
    }
}
