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
            UserDataDto userDataDto = new UserDataDto();
            userDataDto.setUserId(user.getId());
            userDataDto.setUsername(user.getUsername());
            userDataDto.setFirstName(user.getFirstName());
            userDataDto.setLastName(user.getLastName());
            model.addAttribute("userData", userDataDto);
            return "editUser";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/editUser")
    public String edit(UserDataDto userDataDto) {

        Long userId = userDataDto.getUserId();
        Optional<User> optional = userRepository.findById(userId);
        User user = optional.orElseThrow(() -> new RuntimeException("User nie znaleziony"));
        user.setFirstName(userDataDto.getFirstName());
        user.setLastName(userDataDto.getLastName());

        if(userDataDto.getPassword() != null && !userDataDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDataDto.getPassword()));
        }

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
        if (optional.isPresent()) {
            User user = optional.get();
            model.addAttribute("user", user);
            UserRole userRole = userRoleRepository.findUserRoleByUsername(user.getUsername());
            model.addAttribute("userRole", userRole);
            return "userRole";
        } else return "redirect:/";
    }

    @GetMapping("/editRole")
    public String editRole(@RequestParam Long id, Model model) {
        Optional<UserRole> optional = userRoleRepository.findById(id);
        if(optional.isPresent()) {
            UserRole userRole = optional.get();
            model.addAttribute("userRole", userRole);
            String role = userRole.getRole();
            if(role.equals("ROLE_ADMIN")){
                userRole.setRole("ROLE_USER");
            } else userRole.setRole("ROLE_ADMIN");
            userRoleRepository.save(userRole);
            return "newRole";
        }
        else return "redirect:/";
    }
}
