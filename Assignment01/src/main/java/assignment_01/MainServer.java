package assignment_01;


import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;


//@Controller    // This means that this class is a Controller
//@RequestMapping(path="/demo") // This means URL's sta
@RestController
public class MainServer {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @GetMapping("/api/add") // Map ONLY GET Requests
    public @ResponseBody String addNewUser (@RequestParam String name
            , @RequestParam String email) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        User n = new User();
        n.setName(name);
        n.setEmail(email);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping("/api/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @PostMapping("/api/user/register")
    public @ResponseBody String register(@RequestBody User newUser) {
        userRepository.save(newUser);

        /*if (userRepository.findById(Integer.parseInt(newUser.getEmail())).isPresent()) {
         return "This User is already exist";

        }*/


        if(newUser.getName().equals("Fang")) {
            String token = newUser.getEmail()+":"+newUser.getName();
            
            Base64 base64 = new Base64();
            String result = base64.encodeToString(token.getBytes());

            return result+" " + System.currentTimeMillis();
        }else {
            return "{\"email\":\""+newUser.getEmail()+"\", \"name\":\""+newUser.getName()+"\"}";
        }
    }
/*
    @PostMapping("/api/user/register")
    public @ResponseBody String generateToken(@RequestBody User newUser){
        String token = newUser.getEmail()+":"+newUser.getName();
        Base64 base64 = new Base64();
        String result = base64.encodeToString(token.getBytes());


        return result;
    }*/

}