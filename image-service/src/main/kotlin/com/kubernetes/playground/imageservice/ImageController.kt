package com.kubernetes.playground.imageservice

import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
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

    @PostMapping("/{userId}")
    fun upload(@PathVariable userId: String, @RequestParam file: MultipartFile): ImageMetadata {
        val ext = file.originalFilename?.substringAfterLast('.', "jpg") ?: "jpg"
        val filename = "${userId}-${UUID.randomUUID()}.$ext"
        val dest = File(storageDir, filename)
        file.transferTo(dest)
        return repo.save(ImageMetadata(userId = userId, filename = filename))
    }

    @GetMapping("/{userId}")
    fun getLatest(@PathVariable userId: String): ImageMetadata? =
        repo.findTopByUserIdOrderByUploadedAtDesc(userId)

    @GetMapping("/{userId}/file", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getFile(@PathVariable userId: String): FileSystemResource {
        val meta = repo.findTopByUserIdOrderByUploadedAtDesc(userId)
            ?: throw RuntimeException("No image for user $userId")
        return FileSystemResource(File(storageDir, meta.filename))
    }
}
