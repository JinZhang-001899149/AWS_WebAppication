package webSource;

import com.sun.net.httpserver.Headers;
import net.minidev.json.JSONArray;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.jvnet.mimepull.Header;
import org.omg.CORBA.portable.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Basic;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


//@Controller    // This means that this class is a Controller
//@RequestMapping(path="/demo") // This means URL's sta
@RestController
public class UserService {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @GetMapping("/api/add/") // Map ONLY GET Requests
    public @ResponseBody
    String addNewUser(@RequestParam String password
            , @RequestParam String email) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        User n = new User();
        n.setPassword(password);
        n.setEmail(email);
        userRepository.save(n);
        return "{ \n  \"code\":\"201 Created.\",\n  \"reason\":\"Saved.\"\n}";
    }

    @GetMapping("/api/all/")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }


    //get for assignment
    @GetMapping("/") // Map ONLY GET Requests
    public @ResponseBody
    //String authentiction(@RequestParam String auth,Headers Authentication) {
    String authentiction(@RequestParam String auth) {

        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request


       // auth = @RequestHeader Authorization;
        ArrayList<User> list = (ArrayList<User>) getAllUsers();
       // System.out.println(Authorization);

//        User newUser = new User();
//        String token3 = newUser.getEmail()+":"+ newUser.getPassword();
//        Base64 base642 = new Base64();
//        String result3 = base642.encodeToString(token3.getBytes());
//
//
//            if(result3.equals(auth)){
//
//
//            }




        for (User user : list) {
            if (user.getToken().equals(auth)) {


                return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

//
            }


        }

        return "{ \n  \"code\":\"404 Not Found.\",\n  \"reason\":\"You are not logged in.\"\n}";

    }


    //post for assignment
    @PostMapping("/api/user/register")
    public @ResponseBody
    String

    register(@RequestBody User newUser, HttpServletResponse response) {

        if (
                newUser.getEmail().matches("[\\w\\-]+@[a-zA-Z0-9]+(\\.[A-Za-z]{2,3}){1,2}")
        ) {
            //get users from database
            ArrayList<User> list = (ArrayList<User>) getAllUsers();

            if (list.size() == 0) {
                if (
                        newUser.getPassword().matches(".*[a-zA-Z].*") &&
                                newUser.getPassword().matches(".*[0-9].*") &&
                                newUser.getPassword().length() >= 8 &&
                                newUser.getPassword().length() <= 20) {


                    // BCrypt
                    String password = newUser.getPassword();
                    String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
                    newUser.setPassword(hashed);

                    //create token
                    String token = newUser.getEmail() + ":" + hashed;
                    Base64 base64 = new Base64();
                    String result = base64.encodeToString(token.getBytes());



                    String token2 = newUser.getEmail()+":"+ password;
                    Base64 base642 = new Base64();
                    String result2 = base642.encodeToString(token2.getBytes());



                    ArrayList<String> listtoken = new ArrayList<String>();
                    listtoken.add(result2);
                    JSONArray jsarray = new JSONArray();

                    jsarray.add(listtoken);


                    response.setHeader("Token",result2);



                    newUser.setToken(result);

                    // the format of the password is correct and make it into Bcrypt token then save the user
                    userRepository.save(newUser);



                    return "{ \n  \"code\":\"201 Created.\",\n  \"reason\":\"Successfully Registered.\"\n}";


                } else {

                   // return "{\"password invalid, The password must containing letters and numbers\"}";
                    return "{ \n  \"code\":\"406 Not Acceptable.\",\n  \"reason\":\"Invalid Password. The password must containing letters and numbers.\"\n}";

                }
            } else {


                //Bug here! 

                for (int i = 0; i < list.size(); i++) {
                    User user = list.get(i);
                    if (user.getEmail().equalsIgnoreCase(newUser.getEmail())) {
                        //return "{\"result\":\"exist\"}";
                          return "{ \n  \"code\":\"403 Not Forbidden.\",\n  \"reason\":\"The account already exists.\"\n}";

                    } else {
                        if(i == list.size() - 1) {
                            if (
                                    newUser.getPassword().matches(".*[a-zA-Z].*") &&
                                            newUser.getPassword().matches(".*[0-9].*") &&
                                            newUser.getPassword().length() >= 8 &&
                                            newUser.getPassword().length() <= 20) {


                                // BCrypt
                                String password = newUser.getPassword();
                                String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
                                newUser.setPassword(hashed);
                                //create token
                                String token = newUser.getEmail() + ":" + hashed;
                                Base64 base64 = new Base64();
                                String result = base64.encodeToString(token.getBytes());


                                String token2 = newUser.getEmail()+ ":"+ password;
                                Base64 base642 = new Base64();
                                String result2 = base642.encodeToString(token2.getBytes());


                                ArrayList<String> listtoken = new ArrayList<String>();
                                listtoken.add(result2);
                                JSONArray jsarray = new JSONArray();

                                response.setHeader("Token",result2);

                                jsarray.add(listtoken);

                                newUser.setToken(result);

                                // the format of the password is correct and make it into Bcrypt token then save the user
                                userRepository.save(newUser);


                                // return the token and tell user successfully registered

 
                                  return "{ \n  \"code\":\"201 Created.\",\n  \"reason\":\"Successfully Registered.\"\n}";

                                  
                                 

                            }


                            else {

                               return "{ \n  \"code\":\"406 Not Acceptable.\",\n  \"reason\":\"Invalid Password. The password must containing letters and numbers.\"\n}";


                            }
                        } else {
                            continue;
                        }
                }
                }
            }

        } else {

          return "{ \n  \"code\":\"406 Not Acceptable.\",\n  \"reason\":\"Invalid Email. Please input the right format of email to create an account.\"\n}";

        }

        return null;
    }
}
