package dev.kel3pbo.todolist.Controller;

import dev.kel3pbo.todolist.Model.Task;
import dev.kel3pbo.todolist.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Menampilkan form untuk membuat task
    @GetMapping("/createtask")
    public String showCreateTaskForm(Model model) {
        model.addAttribute("task", new Task()); // Mengirimkan objek kosong untuk diisi di form
        return "createtask"; // Nama template untuk form HTML
    }

    // Menangani pengiriman form untuk membuat task
    @PostMapping
    public String createTask(@ModelAttribute("task") Task task) {
        taskService.addTask(task); // Simpan task ke database
        return "redirect:/"; // Redirect ke halaman daftar task
    }

    // Menampilkan daftar semua task
    @GetMapping
    public String listAllTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "dashboard"; // Template untuk menampilkan semua task
    }

    @GetMapping("/statistic")
    public String showStatistic() {
        return "statistic"; // Mengarahkan ke template statistic.html
    }

    @GetMapping("/notification")
    public String showNotification() {
        return "notification"; // Mengarahkan ke template notification.html
    }
    
    // Menghapus task berdasarkan ID
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return "redirect:/"; // Redirect ke dashboard setelah delete
    }

    // Menampilkan form edit task berdasarkan ID
    @GetMapping("/edittask/{id}")
    public String showEditTaskForm(@PathVariable("id") Long id, Model model) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            model.addAttribute("task", task.get()); // Kirim data task ke template
            return "edittask"; // Template form edit task
        } else {
            return "redirect:/"; // Redirect jika task tidak ditemukan
        }
    }

    // Menangani pengiriman form untuk update task
    @PostMapping("/update-task/{id}")
    public String updateTask(@PathVariable("id") Long id, @ModelAttribute("task") Task updatedTask) {
        taskService.updateTask(id, updatedTask); // Update data task
        return "redirect:/"; // Redirect ke halaman daftar task
    }
    // Mengupdate status task
    @PostMapping("/update-status/{id}")
    public String updateTaskStatus(@PathVariable("id") Long id, @RequestParam("status") String statusName) {
        taskService.updateStatus(id, statusName);
        return "redirect:/"; // Redirect ke dashboard setelah status diupdate
    }

    @GetMapping("/remove/{id}")
    public String removeTask(@PathVariable("id") Long id) {
        taskService.removeTask(id);
        return "redirect:/category"; // Redirect ke dashboard setelah delete
    }

}
