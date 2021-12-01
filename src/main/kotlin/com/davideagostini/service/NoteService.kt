package com.davideagostini.service

import com.davideagostini.data.models.Note
import com.davideagostini.data.repository.note.NoteRepository
import com.davideagostini.data.requests.CreateNoteRequest

class NoteService(
    private val noteRepository: NoteRepository
) {

    suspend fun createNote(request: CreateNoteRequest, userId: String): ValidationEvent {
        request.apply {
            if (title.isBlank() || description.isBlank()) {
                return ValidationEvent.ErrorFieldEmpty
            }
            if (request.id != null) {
                noteRepository.createNote(
                    Note(
                        title = request.title,
                        description = request.description,
                        date = request.date,
                        userId = userId,
                        id = request.id
                    )
                )
            } else {
                noteRepository.createNote(
                    Note(
                        title = request.title,
                        description = request.description,
                        date = request.date,
                        userId = userId
                    )
                )
            }
        }
        return ValidationEvent.Success
    }

    suspend fun getNoteDetails(noteId: String, userId: String): Note? {
        return noteRepository.getNoteDetails(noteId, userId)
    }

    suspend fun getNotesForOwner(userId: String): List<Note> {
        return noteRepository.getNotesForOwner(userId)
    }

    suspend fun deleteNote(userId: String, noteId: String): Boolean {
        return noteRepository.deleteNoteForUser(userId, noteId)
    }

    suspend fun addCollaboratorToNote(noteId: String, collaborator: String): Boolean {
        return noteRepository.addCollaboratorToNote(noteId, collaborator)
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty: ValidationEvent()
        object Success: ValidationEvent()
    }

}