package ex.springsecurity_1805.Controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Rest {

    @GetMapping("/up")
    public List<String> info(){
        return List.of("dsfsdfsdf","dfsfgsafasffaf");
    }
}
