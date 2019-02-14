package com.cloud.assignment.assignment.Note;

import com.cloud.assignment.assignment.webSource.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends CrudRepository<Note,Integer>{

    Note save(Note note);

    List<Note>findAllByUser(User user);


}
