package com.example.mobileapp.service

import com.example.mobileapp.model.Notes
import com.example.mobileapp.repository.NotesRepository

class NotesService {
    private val repository = NotesRepository()

    suspend fun getNotesBySubject(subject: String?, notesList: List<Notes?>?) {
        repository.getNotes(subject, notesList)
    }

    suspend fun getNotesByFilter(subject: String?, filters: List<Int>, notesList: List<Notes?>?) {
        repository.getNotesByFilter(subject, filters, notesList)
    }
}