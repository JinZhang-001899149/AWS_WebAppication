package com.cloud.assignment.assignment.Note;
//

import com.cloud.assignment.assignment.webSource.BCrypt;
import com.cloud.assignment.assignment.webSource.User;
import com.cloud.assignment.assignment.webSource.UserRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


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
        /*int index3 = Authorization.indexOf(" ");
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

*/
        User user = authorizeUser(Authorization);


            if(user!=null) {


                List<Note> noteList = noteRepository.findAllByUser(user);
                List resultList = new ArrayList();


                if(noteList.isEmpty()){
                    return new ArrayList<>();
                }else {
                   /* for(int i=0;i<noteList.size();i++){
                        String str= "id:"+noteList.get(i).getNoteId()+",content:"+noteList.get(i).getContent()
                                +",title:"+noteList.get(i).getTitle()+",create_on:"+noteList.get(i).getCreated_on()
                                +",last_updated_on:"+noteList.get(i).getLast_updated_on();
                        resultList.add(str);
                    }*/
                    response.setStatus(200);
                   // String json = JSON.toJSONString(noteList);

                    return noteList;
                }
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



       /* int index3 = Authorization.indexOf(" ");
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

*/
        User user = authorizeUser(Authorization);

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

   @RequestMapping(value = "/note/{id}", method = RequestMethod.GET)

    public Object getSingleNote(@RequestHeader String Authorization,@PathVariable("id") String id, HttpServletResponse response) {
         User user = authorizeUser(Authorization);
         String realId = id.substring(1,id.length()-1);
         if (user == null){
             response.setStatus(401);
             return "{\"Unauthorized\"}";
         }else {
             if (realId.equals("")){
                 response.setStatus(400);
                 return"{\"Bad Request\"}";
             }else{
                 List<Note> list = noteRepository.findAllByUser(user);
                 for(int i = 0; i<list.size();i++){
                     if(list.get(i).getNoteId().equals(realId)){
                         response.setStatus(200);
                         return list.get(i);
                     }
                     /*else{
                        /*if (i == list.size()-1){
                             response.setStatus(404);
                             return"{\"Not Found\"}";
                         }

                         return "Not Found";
                     }*/
                 }
             }
         }
         response.setStatus(404);
       return "{\"Not Found\"}";
   }



    @RequestMapping(value="/note/{id}",method=RequestMethod.PUT)

        public String update(@PathVariable("id") String id,@RequestBody Note note, HttpServletResponse response,@RequestHeader String Authorization){

        User user = authorizeUser(Authorization);

        if(user==null){
            response.setStatus(404);
            return "{\"Not Found\"}";
        }else {



            List<Note> noteList =noteRepository.findAllByUser(user);


            String realId = id.substring(1, id.length() - 1);

            if(realId.equals("")){
                response.setStatus(400);
                return "{\"Bad Request\"}";
            }else{

            Note note2 = new Note();
            for (int i = 0; i < noteList.size(); i++) {
                if (realId.equals(noteList.get(i).getNoteId())) {
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
            }


        }
        response.setStatus(404);
        return "{\"Not Found\"}";
    }

    private User authorizeUser(String Authorization){
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

        User user = userRepository.findByEmail(email);
        if(user == null){
            return null;
        }else{
            if(BCrypt.checkpw(password,user.getPassword())){
                return user;
            }
        }
        return null;
    }

}
