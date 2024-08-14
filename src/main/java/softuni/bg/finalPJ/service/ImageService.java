package softuni.bg.finalPJ.service;

import org.springframework.web.multipart.MultipartFile;
import softuni.bg.finalPJ.models.entities.Image;

import java.io.IOException;
import java.util.List;

public interface ImageService {

     void saveImage(MultipartFile file, Long userId);

     void saveAvatarImage(MultipartFile file, Long userId);

     List<Image> findImagesByUserId(Long id);
}
