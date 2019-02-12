package Note;
//

import com.sun.net.httpserver.Headers;
import net.minidev.json.JSONArray;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.jvnet.mimepull.Header;
import org.omg.CORBA.portable.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
public class NoteController {

    @Autowired // This means to get the bean called noteRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private NoteRepository noteRepository;

//    @GetMapping(path="/add") // Map ONLY GET Requests
//    public @ResponseBody String addNewNote (@RequestParam String name, @RequestParam String email) {
//        // @ResponseBody means the returned String is the response, not a view name
//        // @RequestParam means it is a parameter from the GET or POST request
//
//        User n = new User();
//        n.setName(name);
//        n.setEmail(email);
//        userRepository.save(n);
//        return "Saved";
//    }

    @GetMapping(path="/all/note")
    public @ResponseBody Iterable<Note> getAllNote() {
        // This returns a JSON or XML with the users
        return noteRepository.findAll();
    }



    @PostMapping("/note/create")
    public @ResponseBody
    String register(@RequestBody Note newNote, HttpServletResponse response) {



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

        newNote.setCreated_on(sdf.format(new Date()));

        // get the create date for later use
        String createdate = newNote.getCreated_on();

        newNote.setLast_updated_on(sdf.format(new Date()));


        noteRepository.save(newNote);

        response.setStatus(200);

        return("saved");


    }


}
