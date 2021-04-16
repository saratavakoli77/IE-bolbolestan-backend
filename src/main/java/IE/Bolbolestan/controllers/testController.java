package IE.Bolbolestan.controllers;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class testController {


    @GetMapping("/login")
    public String getLogin(@RequestParam String username) {
        return "hello " + username;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "hello";
    }

}