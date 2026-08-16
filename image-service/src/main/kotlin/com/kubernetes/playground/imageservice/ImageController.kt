package com.kubernetes.playground.imageservice

import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID

@RestController
@RequestMapping("/images")
class ImageController(private val repo: ImageRepository) {

    private val storageDir = File(System.getenv("STORAGE_PATH") ?: "/data/images")

    init {
        storageDir.mkdirs()
    }

    @PutMapping("/{userId}")
    fun updatePicture(@PathVariable userId: Long, @RequestParam file: MultipartFile): ResponseEntity<Any> {
        if (!repo.userExists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "User $userId does not exist"))
        }

        repo.findById(userId).ifPresent { existing ->
            File(storageDir, existing.filename).delete()
        }

        val ext = file.originalFilename?.substringAfterLast('.', "jpg") ?: "jpg"
        val filename = "${userId}-${UUID.randomUUID()}.$ext"
        file.transferTo(File(storageDir, filename))

        val saved = repo.save(ImageMetadata(userId = userId, filename = filename))
        return ResponseEntity.ok(saved)
    }

    @GetMapping("/{userId}")
    fun getMetadata(@PathVariable userId: Long): ResponseEntity<Any> =
        repo.findById(userId)
            .map { ResponseEntity.ok(it) as ResponseEntity<Any> }
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "No picture for user $userId")))

    @GetMapping("/{userId}/file", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getFile(@PathVariable userId: Long): ResponseEntity<FileSystemResource> {
        val meta = repo.findById(userId).orElse(null)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(FileSystemResource(File(storageDir, meta.filename)))
    }
}
