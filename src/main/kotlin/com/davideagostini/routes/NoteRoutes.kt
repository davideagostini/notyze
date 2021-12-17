package com.davideagostini.routes

import com.davideagostini.data.requests.AddCollaboratorRequest
import com.davideagostini.data.requests.CreateNoteRequest
import com.davideagostini.data.responses.BasicApiResponse
import com.davideagostini.data.util.ApiResponseMessages.INTERNAL_SERVER_ERROR
import com.davideagostini.data.util.ApiResponseMessages.USER_ALREADY_OWNER
import com.davideagostini.data.util.ApiResponseMessages.USER_NOT_FOUND
import com.davideagostini.service.NoteService
import email
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import userId

fun Route.createNote(noteService: NoteService) {
    authenticate {
        post("/api/note/create") {
            val request = call.receiveOrNull<CreateNoteRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val createNoteAcknowledged = noteService.createNote(request)
            if (createNoteAcknowledged) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = INTERNAL_SERVER_ERROR
                    )
                )
            }
        }
    }
}

fun Route.getNotesForOwner(noteService: NoteService) {
    authenticate {
        get("api/note/all") {
            val notes = noteService.getNotesForOwner(call.email)
            call.respond(HttpStatusCode.OK, notes)
        }
    }
}

fun Route.getNoteDetails(noteService: NoteService) {
    authenticate {
        get("/api/note/details/{noteId}") {
            val noteId = call.parameters["noteId"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val note = noteService.getNoteDetails(noteId = noteId, email = call.email) ?: kotlin.run {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }


            if (!noteService.isOwnerNote(noteId = noteId, email = call.email)) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            call.respond(
                HttpStatusCode.OK,
                note
            )
        }
    }
}

fun Route.addCollaboratorToNote(noteService: NoteService) {
    authenticate {
        post("/api/note/{noteId}/add-collaborator") {
            val noteId = call.parameters["noteId"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val request = call.receiveOrNull<AddCollaboratorRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (!noteService.checkIfUserExists(email = request.email)) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = USER_NOT_FOUND
                    )
                )
                return@post
            }

            if (noteService.isOwnerNote(noteId = noteId, email = request.email)) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = USER_ALREADY_OWNER
                    )
                )
                return@post
            }

            val acknowledged = noteService.addCollaboratorToNote(noteId, request.email)
            if (acknowledged) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = INTERNAL_SERVER_ERROR
                    )
                )
            }
        }
    }
}

fun Route.deleteNote(noteService: NoteService) {
    authenticate {
        delete("/api/note/{noteId}") {
            val noteId = call.parameters["noteId"] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val acknowledged = noteService.deleteNote(email = call.email, noteId = noteId)
            if (acknowledged) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = INTERNAL_SERVER_ERROR
                    )
                )
            }
        }
    }
}