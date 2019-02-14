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




    public User authorizeUser( String Authorization){
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

    @GetMapping(path = "/note")
    public Object getAllNote(@RequestHeader String Authorization, Note newNote, HttpServletResponse response) {

        //Authorization authorization = new Authorization();

        //User user = authorization.authorizeUser(Authorization);

        User user = authorizeUser(Authorization);


            if(user!=null) {


                List<Note> noteList = noteRepository.findAllByUser(user);
                List resultList = new ArrayList();


                if(noteList.isEmpty()){
                    return new ArrayList<>();
                }else {

                    response.setStatus(200);
                    return noteList;
                }
            }

            else {

                response.setStatus(401);
                return "(\"Unauthorized\")";
            }

    }


    @PostMapping("/note")
    public String register(@RequestBody Note newNote, HttpServletResponse response, User newUser, @RequestHeader  String Authorization) {


        //Authorization authorization = new Authorization();

        //User user = authorization.authorizeUser(Authorization);

        User user = authorizeUser(Authorization);

         if(user!=null){



                if(newNote.getTitle().equals("") || newNote.getContent().equals("") || newNote.getTitle().length()>=20)
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

    @RequestMapping(value="/note/{id}",method=RequestMethod.PUT)

        public String update(@PathVariable("id") String id,@RequestBody Note note, HttpServletResponse response,@RequestHeader String Authorization){
        User user = authorizeUser(Authorization);


        if(user==null){
            response.setStatus(404);
            return "{\"Not Found\"}";
        }else{
        List<Note> noteList = noteRepository.findAllByUser(user);
        String realId = id.substring(1,id.length()-1);

        if(realId.equals("")){
            response.setStatus(400);
            return "{\"Bad Request\"}";
        }else{
        Note note2 = new Note();
        for(int i=0;i<noteList.size();i++) {
            if (realId.equals(noteList.get(i).getNoteId())) {
                note2 = noteList.get(i);

                if (note.getContent().equals(note2.getContent()) && note.getTitle().equals(note2.getTitle())) {

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
        return "{\"Note Not Found\"}";
    }

    @DeleteMapping("delete/{id}")
    public @ResponseBody
    String deleteNote(@PathVariable("id") String id, HttpServletResponse response,
                      @RequestHeader String Authorization) {

        User user = authorizeUser(Authorization);

        if (user == null) {
            response.setStatus(401);
            return ("unauthorized user");
        } else {
            List<Note> list = noteRepository.findAllByUser(user);
            if (list.size() < 1) {
                response.setStatus(404);
                return ("there is no note");
            } else {
                for (Note note : list) {
                    if (note.getNoteId().equals(id)) {
                        noteRepository.delete(note);
                        //response.setStatus(200);
                        response.setStatus(204);
                        return ("note deleted");
                    } else {
                        response.setStatus(404);
                        return ("the note does not exist");
                    }
                }
            }
            return null;
        }
    }

}

