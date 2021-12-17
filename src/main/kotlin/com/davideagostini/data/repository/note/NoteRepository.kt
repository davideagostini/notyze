package com.davideagostini.data.repository.note

import com.davideagostini.data.models.Note

interface NoteRepository {

    suspend fun createNote(note: Note): Boolean

    suspend fun getNoteDetails(noteId: String, email: String): Note?

    suspend fun getNotesForOwner(userId: String): List<Note>

    suspend fun addCollaboratorToNote(noteId: String, collaborator: String): Boolean

    suspend fun deleteNoteForUser(email: String, noteId: String): Boolean

    suspend fun isOwnerOfNote(noteId: String, email: String): Boolean

    suspend fun checkIfUserExists(email: String): Boolean
}