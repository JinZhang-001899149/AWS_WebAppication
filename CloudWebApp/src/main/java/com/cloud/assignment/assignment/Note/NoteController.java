package com.cloud.assignment.assignment.Note;
//

import com.cloud.assignment.assignment.webSource.BCrypt;
import com.cloud.assignment.assignment.webSource.User;
import com.cloud.assignment.assignment.webSource.UserRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class NoteController {
    @Autowired // This means to get the bean called noteRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;


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

    @GetMapping(path = "/note")
    public Object getAllNote(@RequestHeader String Authorization, Note newNote, HttpServletResponse response) {

        // This returns a JSON or XML with the users
        //if(!newNote.getEmail().equals())
        int index3 = Authorization.indexOf(" ");
        String code = Authorization.substring(index3+1);
        Base64 base64 = new Base64();
        BASE64Decoder decoder = new BASE64Decoder();

        String decode = null;

        try {
            decode = new String(decoder.decodeBuffer(code),"UTF-8");
        } catch (IOException e) {
            return null;
        }
        int index = decode.indexOf(":");

        String password = decode.substring(index+1);
        String email = decode.substring(0,index);


       // ArrayList<User> list = (ArrayList<User>) userRepository.findAll();

        //for (int i = 0; i < list.size(); i++) {
           // User user = list.get(i);


         User user = userRepository.findByEmail(email);




            if(userRepository.findByEmail(email)!=null) {



                response.setStatus(200);

                return noteRepository.findAll();
            }

            else {

                response.setStatus(401);
                return "(\"Unauthorized\")";
                //return null;

            }



        //return null;


    }


    @PostMapping("/note")
    public String register(@RequestBody Note newNote, HttpServletResponse response, User newUser, @RequestHeader  String Authorization) {



        int index3 = Authorization.indexOf(" ");
        String code = Authorization.substring(index3+1);
        Base64 base64 = new Base64();
        BASE64Decoder decoder = new BASE64Decoder();
        String decode = null;

        try {
            decode = new String(decoder.decodeBuffer(code),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int index = decode.indexOf(":");

        String password = decode.substring(index+1);
        String email = decode.substring(0,index);


        //ArrayList<User> list = (ArrayList<User>) userRepository.findByEmail(email);

         User user = userRepository.findByEmail(email);

         if(user!=null){



                if(newNote.getTitle().equals(null) && newNote.getContent().equals(null) && newNote.getTitle().length()>=20)
                {
                    response.setStatus(400);
                    return "(\"Bad Request\")";
                }

                else{


                    //newNote.setNoteId(UUID.randomUUID());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

                    newNote.setNoteId(UUID.randomUUID().toString());

                    newNote.setCreated_on(sdf.format(new Date()));

                    // get the create date for later use
                    String createdate = newNote.getCreated_on();

                    newNote.setLast_updated_on(sdf.format(new Date()));

                    newNote.setUser(user);


                    response.setStatus(200);
                    noteRepository.save(newNote);
                    return "(\"saved\")";


                }

            }
            else {

                response.setStatus(401);
                return "(\"Unauthorized\")";
            }
    }

   //@RequestMapping(value = "/note/{id}", method = RequestMethod.GET)

//    public Object getSingleNote(@PathVariable("id") String id, HttpServletResponse response, @RequestHeader String Authorization) {
//
//       ArrayList<Note> list = (ArrayList<Note>) noteRepository.findAll();
//
//       int index3 = Authorization.indexOf(" ");
//       String code = Authorization.substring(index3+1);
//       Base64 base64 = new Base64();
//       BASE64Decoder decoder = new BASE64Decoder();
//       String decode = null;
//
//       try {
//           decode = new String(decoder.decodeBuffer(code),"UTF-8");
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
//
//
//       int index = decode.indexOf(":");
//
//       String password = decode.substring(index+1);
//       String email = decode.substring(0,index);
//
//       Note note = new Note();
//       for (Note note : list) {
//           if (note.getEmail().equals(email)){
//               if (note.getNoteId().equals(id)) {
//                   response.setStatus(200);
//                   return note;
//
//               } else {
//                   response.setStatus(404);
//                   return null;
//               }
//        }
//           response.setStatus(401);
//           return "{ \"Unaothorized\" }";
//       }
//          return null;
//   }



    @RequestMapping(value="/note/{id}",method=RequestMethod.PUT)

        public String update(@PathVariable("id") String id,@RequestBody Note note, HttpServletResponse response){
        ArrayList<Note> noteList = (ArrayList<Note>) noteRepository.findAll();

        String realId = id.substring(1,id.length()-1);


        Note note2 = new Note();
        for(int i=0;i<noteList.size();i++){
            if(realId.equals(noteList.get(i).getNoteId())) {
                note2 = noteList.get(i);

                if (note.getContent().equals(note2.getContent()) || note.getTitle().equals(note2.getTitle())) {

                    response.setStatus(406);
                    return "{\"Not Acceptable\"}";
                } else {
                    SimpleDateFormat updateTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
                note2.setLast_updated_on(updateTime.format(new Date()));
                note2.setTitle(note.getTitle());
                note2.setContent(note.getContent());
                noteRepository.save(note2);

                response.setStatus(205);
                return "{\"Reset Content\"}";
             }
            }
        }



        response.setStatus(404);
        return "{\"Not Found\"}";
    }
}
