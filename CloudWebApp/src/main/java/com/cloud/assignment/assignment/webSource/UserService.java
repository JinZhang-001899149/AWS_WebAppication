package com.cloud.assignment.assignment.webSource;

import com.cloud.assignment.assignment.AmazonS3_dev.AmazonS3Client;
import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minidev.json.JSONArray;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


//@Controller    // This means that this class is a Controller
//@RequestMapping(path="/demo") // This means URL's sta
@RestController
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @Autowired
    private AmazonS3Client amazonClient;

    @Autowired(required=false)
    private StatsDClient statsDClient;


    @GetMapping("/api/add/") // Map ONLY GET Requests
    public @ResponseBody
    String addNewUser(@RequestParam String password
            , @RequestParam String email, HttpServletResponse response) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        statsDClient.incrementCounter("endpoint.api/register.http.get");
        User n = new User();
        n.setPassword(password);
        n.setEmail(email);
        userRepository.save(n);
        response.setStatus(201);
        return "{ \n  \"code\":\"201 Created.\",\n  \"reason\":\"Saved.\"\n}";
    }

    @GetMapping("/api/all/")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        statsDClient.incrementCounter("endpoint.api/all.http.get");
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }


    //get for assignment
    @GetMapping("/") // Map ONLY GET Requests
    public @ResponseBody

    String authentiction(@RequestHeader String Authorization, HttpServletResponse response) {

    //String authentiction(@RequestParam String auth,Headers Authentication)
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request


        statsDClient.incrementCounter("endpoint.http.get");
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




        int index3 = Authorization.indexOf(" ");
        String code = Authorization.substring(index3+1);
        Base64 base64 = new Base64();
        BASE64Decoder decoder = new BASE64Decoder();

        String decode = null;
        try {
            decode = new String(decoder.decodeBuffer(code),"UTF-8");
        } catch (IOException e) {
            return "Decode fail";
        }
        int index = decode.indexOf(":");

        String password = decode.substring(index+1);
        String email = decode.substring(0,index);


        //String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        //String userAuth = email+":"+password;
        //String baseAuth = base64.encodeToString(userAuth.getBytes());

        for (User user : list) {


           /* if(user.getEmail().equals(email)){
                return user.getToken()+"/"+baseAuth;
            }*/

           if (user.getEmail().equals(email)&&BCrypt.checkpw(password,user.getPassword())) {

                return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

//
            }


        }


        response.setStatus(404);
        return "{ \n  \"code\":\"404 Not Found.\",\n  \"reason\":\"You are not logged in.\"\n}";

    }


    //post for assignment
    @PostMapping("/user/register")
    public @ResponseBody
    String

    register(@RequestBody User newUser, HttpServletResponse response) {

        statsDClient.incrementCounter("endpoint.user/register.http.post");
        if (
                newUser.getEmail().matches("[\\w\\-\\.]+@[a-zA-Z0-9]+(\\.[A-Za-z]{2,3}){1,2}")
        ) {
            //get users from database
            ArrayList<User> list = (ArrayList<User>) getAllUsers();

            logger.info("{}",newUser.getEmail());
            if (list.size() == 0) {
                logger.info("{}",newUser.getPassword());
                if (
                                newUser.getPassword().matches(".*[a-zA-Z].*") &&
                                newUser.getPassword().matches(".*[0-9].*") &&
                                newUser.getPassword().length() >= 8 &&
                                newUser.getPassword().length() <= 20) {


                    // BCrypt
                    String password = newUser.getPassword();
                    String hashed = BCrypt.hashpw(password, BCrypt.gensalt());


                    //create token
                    String token = newUser.getEmail() + ":" + newUser.getPassword();


                    newUser.setPassword(hashed);

                    //create token
                    Base64 base64 = new Base64();
                    String result = base64.encodeToString(token.getBytes());


                    String token2 = newUser.getEmail()+ ":" + hashed;


                    Base64 base642 = new Base64();
                    String result2 = base642.encodeToString(token2.getBytes());



                    ArrayList<String> listtoken = new ArrayList<String>();
                    listtoken.add(result2);
                    JSONArray jsarray = new JSONArray();

                    jsarray.add(listtoken);


                    response.setHeader("Token",result2);



                    logger.info("{}",newUser);
                    //newUser.setToken(result);

                    //newUser.setPassword(hashed);

                    // the format of the password is correct and make it into Bcrypt token then save the user
                    userRepository.save(newUser);




                    response.setStatus(201);

                    return "{ \n  \"code\":\"201 Created.\",\n  \"reason\":\"Successfully Registered.\"\n}";


                } else {

                    response.setStatus(406);
                   // return "{\"password invalid, The password must containing letters and numbers\"}";
                    return "{ \n  \"code\":\"406 Not Acceptable.\",\n  \"reason\":\"Invalid Password. The password must containing letters and numbers.\"\n}";

                }
            } else {


                //Bug here! 

                for (int i = 0; i < list.size(); i++) {
                    User user = list.get(i);
                    if (user.getEmail().equalsIgnoreCase(newUser.getEmail())) {
                        //return "{\"result\":\"exist\"}";
                        response.setStatus(403);
                          return "{ \n  \"code\":\"403 Forbidden.\",\n  \"reason\":\"The account already exists.\"\n}";

                    } else {
                        if(i == list.size() - 1) {
                            if (
                                    newUser.getPassword().matches(".*[a-zA-Z].*") &&
                                            newUser.getPassword().matches(".*[0-9].*") &&
                                            newUser.getPassword().length() >= 8 &&
                                            newUser.getPassword().length() <= 20) {


                                // BCrypts

                                String password = newUser.getPassword();
                                String hashed = BCrypt.hashpw(password, BCrypt.gensalt(10));

                                //create token
                                String token = newUser.getEmail() + ":" + newUser.getPassword();
                                Base64 base64 = new Base64();
                                String result = base64.encodeToString(token.getBytes());


                                String token2 = newUser.getEmail()+ ":"+ hashed;
                                Base64 base642 = new Base64();
                                String result2 = base642.encodeToString(token2.getBytes());


                                ArrayList<String> listtoken = new ArrayList<String>();
                                listtoken.add(result2);
                                JSONArray jsarray = new JSONArray();

                                response.setHeader("Token",result2);

                                jsarray.add(listtoken);


                                //newUser.setToken(result);
                                newUser.setPassword(hashed);

                                // the format of the password is correct and make it into Bcrypt token then save the user
                                userRepository.save(newUser);


                                // return the token and tell user successfully registered

                                response.setStatus(201);
 

                                return "{ \n  \"code\":\"201 Created.\",\n  \"reason\":\"Successfully Registered.\"\n}";


                                  
                                 

                            }


                            else {
                                response.setStatus(406);

                               return "{ \n  \"code\":\"406 Not Acceptable.\",\n  \"reason\":\"Invalid Password. The password must containing letters and numbers.\"\n}";


                            }
                        } else {
                            continue;
                        }
                }
                }
            }

        } else {

            response.setStatus(406);
          return "{ \n  \"code\":\"406 Not Acceptable.\",\n  \"reason\":\"Invalid Email. Please input the right format of email to create an account.\"\n}";

        }

        return null;
    }
}
