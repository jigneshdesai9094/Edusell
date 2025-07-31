package com.example.EducationSell.Repository;

import com.example.EducationSell.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository  extends JpaRepository<Note,Integer> {
}
