package com.kubernetes.playground.imageservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.UUID

@RestController
@RequestMapping("/images")
class ImageController(
    private val repo: ImageRepository,
    private val s3: S3Client
) {

    @Value("\${minio.bucket}")
    lateinit var bucket: String

    @PutMapping("/{userId}")
    fun updatePicture(@PathVariable userId: Long, @RequestParam file: MultipartFile): ResponseEntity<Any> {
        if (!repo.userExists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "User $userId does not exist"))
        }

        val existing = repo.findById(userId).orElse(null)

        // delete the old object from MinIO if this user already had a picture
        existing?.let {
            s3.deleteObject(
                DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(it.filename)
                    .build()
            )
        }

        val ext = file.originalFilename?.substringAfterLast('.', "jpg") ?: "jpg"
        val objectKey = "${userId}-${UUID.randomUUID()}.$ext"

        s3.putObject(
            PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(file.contentType ?: "image/jpeg")
                .build(),
            RequestBody.fromInputStream(file.inputStream, file.size)
        )

        val entity = ImageMetadata(userId = userId, filename = objectKey)
        if (existing != null) {
            entity.markAsExisting()
        }

        val saved = repo.save(entity)
        return ResponseEntity.ok(saved)
    }

    @GetMapping("/{userId}")
    fun getMetadata(@PathVariable userId: Long): ResponseEntity<Any> =
        repo.findById(userId)
            .map { ResponseEntity.ok(it) as ResponseEntity<Any> }
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "No picture for user $userId")))

    @GetMapping("/{userId}/file", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getFile(@PathVariable userId: Long): ResponseEntity<ByteArray> {
        val meta = repo.findById(userId).orElse(null)
            ?: return ResponseEntity.notFound().build()

        val objectBytes = s3.getObject(
            GetObjectRequest.builder()
                .bucket(bucket)
                .key(meta.filename)
                .build()
        ).readAllBytes()

        return ResponseEntity.ok(objectBytes)
    }
}
