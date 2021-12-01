package com.davideagostini.data.repository.note

import com.davideagostini.data.models.Note
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class NoteRepositoryImpl(
    db: CoroutineDatabase
): NoteRepository {

    private val notes = db.getCollection<Note>()


    override suspend fun createNote(note: Note): Boolean {
        val noteExists = notes.findOneById(note.id) != null
        return if (noteExists) {
            notes.updateOneById(note.id, note).wasAcknowledged()
        } else {
            notes.insertOne(note).wasAcknowledged()
        }
    }

    override suspend fun getNoteDetails(noteId: String, userId: String): Note? {
        return notes.findOne(Note::id eq noteId, Note::collaborators contains userId)
    }

    override suspend fun getNotesForOwner(userId: String): List<Note> {
        return notes.find(Note::collaborators contains userId).toList()
    }

    override suspend fun addCollaboratorToNote(noteId: String, collaborator: String): Boolean {
        val collaborators = notes.findOneById(noteId)?.collaborators ?: return false
        return notes.updateOneById(noteId, setValue(Note::collaborators, collaborators + collaborator)).wasAcknowledged()
    }

    override suspend fun deleteNoteForUser(userId: String, noteId: String): Boolean {
        val note = notes.findOne(Note::id eq noteId, Note::collaborators contains userId)
        note?.let { n ->
            if (n.collaborators.size > 1) {
                val newCollaborators = note.collaborators - userId
                val update = notes.updateOne(Note::id eq note.id, setValue(Note::collaborators, newCollaborators))
                return update.wasAcknowledged()
            }
            return notes.deleteOneById(noteId).wasAcknowledged()
        } ?: return false
    }

    override suspend fun isOwnerOfNote(noteId: String, collaborator: String): Boolean {
        val note = notes.findOne(noteId) ?: return false
        return collaborator in note.collaborators
    }

}