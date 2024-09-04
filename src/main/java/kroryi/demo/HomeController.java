package kroryi.demo;

import kroryi.demo.domain.Employee;
import kroryi.demo.domain.User;
import kroryi.demo.service.EmployeeService;
import kroryi.demo.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@Log4j2
public class HomeController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    UserService userService;

    @GetMapping("/userfindall")
    public String userfindall(Model model) {
        List<User> users = userService.findAll();
        log.info(users);
        model.addAttribute("users", users);
        return "home";
    }

    @GetMapping("/findUserName")
    public String findUserName() {
        List<User> users = userService.findByName("이순신");
        log.info(users);
        return "home";
    }

   /* @GetMapping("/home")
    public String home() {

        List<Employee> employeeList = employeeService.findDepartment("회계부");

        System.out.println(employeeList.toString());

        return "home";
    }*/

    @GetMapping("/user")
    public String userUpdate() {
        User user = new User();
        user.setId(2L);
        user.setUsername("강감찬");
        user.setPassword("123456");
        user.setEmail("kroryi@gmail.com");
        user.setPhone("010-1234-5678");
        user.setAddress("대구시 중구");
        User updateUser = userService.updateUser(user);

        log.info(updateUser.toString());
        return "home";
    }

    @GetMapping("/userdelete")
    public String userDelete() {
        userService.deleteUser(2L);
        return "home";
    }

    @GetMapping("/saveDummyUsers")
    public String saveDummyUsers() {
        userService.saveDummyUser();
        return "home";
    }


    @GetMapping("/findNameOrEmail")
    public String findNameOrEmail() {
        List<User> users = userService.findByUsernameOrEmailOrderByIdDesc(
                "사용자1", "user3@yi.or.kr"
        );
        log.info(users);
        return "home";
    }

    @GetMapping("/findByUsernameLikeOrderByIdDesc")
    public String findByUsernameLikeOrderByIdDesc(
            @RequestParam String page,
            @RequestParam String keyword,
            Model model
    ) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("id").descending());

        List<User> userList = userService.findByUsernameLikeOrderByIdDesc("%" + keyword + "%", pageable);
        userList.forEach((list) -> {
            log.info("{}:{}", list.getId(), list.getUsername());
        });

        model.addAttribute("list", userList);

        return "home";
    }

    @GetMapping("/userInsertTr")
    public String userInsertTr() {
        try {
//            userService.createUserWithTransaction();
            userService.createUserWithoutTransaction();
        } catch (RuntimeException e) {
            return "롤백 :" + e.getMessage();
        }
       /* User user1 = new User();
        user1.setUsername("강감찬");
        user1.setPassword("123456");
        user1.setEmail("kroryi@gmail.com");
        user1.setAddress("대구시 중구");

        User user2 = new User();
        user2.setUsername("이순신  ");
        user2.setPassword("12345432");
        user2.setEmail("leesunsin@gmail.com");
        user2.setAddress("조선 한양");

        List<User> users =new ArrayList<>();
        users.add(user1);
        users.add(user2);

        userService.saveUsers(users);*/


        return "home";
    }

}
