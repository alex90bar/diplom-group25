package ru.skillbox.diplom.group25.microservice.post.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group25.microservice.post.dto.PhotoDto;

/**
 * StorageService
 *
 * @author alex90bar
 */


@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

  private final Cloudinary cloudinary;

  public ResponseEntity<PhotoDto> uploadPhoto(MultipartFile file) {

    log.info("uploadPhoto starts");
    Map uploadResult = null;
    try {
      uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("transformation",
          new Transformation()
              .height(400).width(400).crop("pad")));
    } catch (IOException e) {
      e.printStackTrace();
      log.error("Error uploading: {}", e);
      return ResponseEntity.badRequest().body(null);
    }

    log.info("Image uploaded successfully");
    return ResponseEntity.ok(new PhotoDto((String) uploadResult.get("url")));

  }

}


