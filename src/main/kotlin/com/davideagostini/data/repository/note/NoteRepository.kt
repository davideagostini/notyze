package com.davideagostini.data.repository.note

import com.davideagostini.data.models.Note

interface NoteRepository {

    suspend fun createNote(note: Note): Boolean

    suspend fun getNoteDetails(noteId: String, userId: String): Note?

    suspend fun getNotesForOwner(userId: String): List<Note>

    suspend fun addCollaboratorToNote(noteId: String, collaborator: String): Boolean

    suspend fun deleteNoteForUser(userId: String, noteId: String): Boolean

    suspend fun isOwnerOfNote(noteId: String, collaborator: String): Boolean
}