package com.softserve.edu.controller;

        import com.softserve.edu.model.Marathon;
        import com.softserve.edu.model.User;
        import com.softserve.edu.service.MarathonService;
        import com.softserve.edu.service.UserService;
        import lombok.Data;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.validation.BindingResult;
        import org.springframework.validation.annotation.Validated;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;

@Controller
@Data
public class StudentController {
    Logger logger = LoggerFactory.getLogger(StudentController.class);
    private UserService studentService;
    private MarathonService marathonService;

    public StudentController(UserService studentService, MarathonService marathonService) {
        this.studentService = studentService;
        this.marathonService = marathonService;
    }

    @GetMapping("/create-student")
    public String createStudent(Model model) {
        logger.info("Get student for creating");
        model.addAttribute("user", new User());
        return "create-student";
    }

    @PostMapping("students/{marathon_id}/add")
    public String createStudent(@PathVariable("marathon_id") long marathonId, @Validated @ModelAttribute User user, BindingResult result) {
        logger.info("Add student by id in marathon");
        if (result.hasErrors()) {
            return "create-student";
        }
        studentService.addUserToMarathon(
                studentService.createOrUpdateUser(user),
                marathonService.getMarathonById(marathonId));
        return "redirect:/students/" + marathonId;
    }

    @GetMapping("students/{marathon_id}/add")
    public String createStudent(@RequestParam("user_id") long userId, @PathVariable("marathon_id") long marathonId) {
        logger.info("Get student by id for adding in marathon");
        studentService.addUserToMarathon(
                studentService.getUserById(userId),
                marathonService.getMarathonById(marathonId));
        return "redirect:/students/" + marathonId;
    }

    @GetMapping("/students/{marathon_id}/edit/{student_id}")
    public String updateStudent(@PathVariable("marathon_id") long marathonId, @PathVariable("student_id") long studentId, Model model) {
        logger.info("Get student by id for editing from marathon");
        User user = studentService.getUserById(studentId);
        model.addAttribute("user", user);
        return "update-student";
    }

    @PostMapping("/students/{marathon_id}/edit/{student_id}")
    public String updateStudent(@PathVariable("marathon_id") long marathonId, @PathVariable("student_id") long studentId, @Validated @ModelAttribute User user, BindingResult result) {
        logger.info("Edit student by id from marathon");
        if (result.hasErrors()) {
            return "update-marathon";
        }
        studentService.createOrUpdateUser(user);
        return "redirect:/students/" + marathonId;
    }

    @GetMapping("/students/{marathon_id}/delete/{student_id}")
    public String deleteStudent(@PathVariable("marathon_id") long marathonId, @PathVariable("student_id") long studentId) {
        logger.info("Get student by id for deleting from marathon");
        studentService.deleteUserFromMarathon(
                studentService.getUserById(studentId),
                marathonService.getMarathonById(marathonId));
        return "redirect:/students/" + marathonId;
    }

    @GetMapping("/students")
    public String getAllStudents(Model model) {
        logger.info("Get all students");
        List<User> students = studentService.getAll();
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/students/edit/{id}")
    public String updateStudent(@PathVariable long id, Model model) {
        logger.info("Get student by id for editing");
        User user = studentService.getUserById(id);
        model.addAttribute("user", user);
        return "update-student";
    }

    @PostMapping("/students/edit/{id}")
    public String updateStudent(@PathVariable long id, @Validated @ModelAttribute User user, BindingResult result) {
        logger.info("Edit student by id");
        if (result.hasErrors()) {
            return "update-marathon";
        }
        studentService.createOrUpdateUser(user);
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable long id) {
        logger.info("Get student by id for deleting");
        User student = studentService.getUserById(id);
        for (Marathon marathon : student.getMarathons()) {
            studentService.deleteUserFromMarathon(student, marathon);
        }
        studentService.deleteUserById(id);
        return "redirect:/students";
    }
}
