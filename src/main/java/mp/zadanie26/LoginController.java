package mp.zadanie26;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserRoleRepository userRoleRepository;

    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
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
    public String edit(@RequestParam Long id, Model model) {
        Optional<User> optional = userRepository.findById(id);
        if(optional.isPresent()) {
            User user = optional.get();
            model.addAttribute("user", user);
            return "editUser";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/editUser")
    public String edit(User user) {
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String userList(Model model) {
        List<User> list = userRepository.findAll();
        model.addAttribute("users", list);
        return "users";
    }

    @GetMapping("/user")
    public String info(@RequestParam Long id, Model model) {
        Optional<User> optional = userRepository.findById(id);
        if(optional.isPresent()) {
            User user = optional.get();
            model.addAttribute("user", user);
            List<UserRole> list = userRoleRepository.findAll();
            for (int i = 0; i < list.size(); i++) {
                UserRole userRole = list.get(i);
                if(user.getUsername().equals(userRole.getUsername())){
                    model.addAttribute("userRole", userRole);
                }
            }
            return "userRole";
        }
        else return "redirect:/";
    }

}
