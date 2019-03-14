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
                        response.setStatus(200);
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


