package mp.zadanie26;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class LoginController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/settings")
    public String settings (Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "settings";
    }

    @GetMapping("/editUser")
    public String edit(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/editUser")
    public String edit(User user) {
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/users")
    public String userList(Model model) {
        List<User> list = userRepository.findAll();
        model.addAttribute("users", list);
        return "users";
    }
}
