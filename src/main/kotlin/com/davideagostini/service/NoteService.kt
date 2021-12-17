package com.davideagostini.service

import com.davideagostini.data.models.Note
import com.davideagostini.data.repository.note.NoteRepository
import com.davideagostini.data.requests.CreateNoteRequest

class NoteService(
    private val noteRepository: NoteRepository
) {

    suspend fun createNote(request: CreateNoteRequest): Boolean {
        if(request.id != null) {
            return noteRepository.createNote(
                Note(
                    title = request.title,
                    description = request.description,
                    date = request.date,
                    collaborators = request.collaborators ?: listOf(),
                    categories = request.categories ?: listOf(),
                    id = request.id
                )
            )
        } else {
            return noteRepository.createNote(
                Note(
                    title = request.title,
                    description = request.description,
                    date = request.date,
                    collaborators = request.collaborators ?: listOf(),
                    categories = request.categories ?: listOf(),
                )
            )
        }
    }

    suspend fun getNoteDetails(noteId: String, email: String): Note? {
        return noteRepository.getNoteDetails(noteId, email)
    }

    suspend fun getNotesForOwner(userId: String): List<Note> {
        return noteRepository.getNotesForOwner(userId)
    }

    suspend fun deleteNote(email: String, noteId: String): Boolean {
        return noteRepository.deleteNoteForUser(email, noteId)
    }

    suspend fun addCollaboratorToNote(noteId: String, email: String): Boolean {
        return noteRepository.addCollaboratorToNote(noteId, email)
    }

    suspend fun isOwnerNote(noteId: String, email: String): Boolean {
        return noteRepository.isOwnerOfNote(noteId = noteId, email = email)
    }

    suspend fun checkIfUserExists(email: String): Boolean {
        return noteRepository.checkIfUserExists(email = email)
    }

}