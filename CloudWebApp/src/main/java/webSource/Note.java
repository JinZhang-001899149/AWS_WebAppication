package webSource;

import javax.persistence.Id;
import javax.persistence.Entity;
import java.util.Date;

public class Note {
  @Id
  private Integer noteId;
  private String content;
  private String title;
  private Date created_on;
  private Date last_updated_on;
  private String email;

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public Date getLast_updated_on() {
        return last_updated_on;
    }

    public void setLast_updated_on(Date last_updated_on) {
        this.last_updated_on = last_updated_on;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
