package softuni.bg.finalPJ.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import softuni.bg.finalPJ.models.entities.Image;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.AvatarImageRepository;
import softuni.bg.finalPJ.repositories.ImageRepository;
import softuni.bg.finalPJ.service.ImageService;
import softuni.bg.finalPJ.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private final String SAVE_DIRECTORY_ROUTE_IMAGES = "src/main/resources/static/images/";
    private final String SAVE_DIRECTORY_ROUTE_AVATARS = "src/main/resources/static/images/avatars/";
    private final ImageRepository imageRepository;
    private final AvatarImageRepository avatarImageRepository;
    private final UserService userService;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,
                            AvatarImageRepository avatarImageRepository,
                            UserService userService) {
        this.imageRepository = imageRepository;
        this.avatarImageRepository = avatarImageRepository;
        this.userService = userService;
    }


    @Override
    public List<Image> findImagesByUserId(Long userId) {
        return imageRepository.findImagesByUserId(userId);
    }


    @Override
    public void saveImage(MultipartFile file, Long userId)  {
        UserEntity user = userService.findById(userId);
        Image image = new Image();

        try{
            String userDirectory = SAVE_DIRECTORY_ROUTE_IMAGES + "user_" + userId + "/";
            Files.createDirectories(Paths.get(userDirectory));

            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(userDirectory + fileName);
            Files.write(filePath, file.getBytes());

            image.setFileName(fileName);

            //Correct Path to set
            String correctPath = userDirectory + fileName;
            correctPath = correctPath.replace("src/main/resources/static","");
            filePath = Paths.get(correctPath);
            image.setFilePath(filePath.toString());
        }catch (IOException ex){
            throw new RuntimeException("Image cannot be saved.");
        }


        image.setFileType(file.getContentType());
        image.setUser(user);


        imageRepository.save(image);
    }

    @Override
    public void saveAvatarImage(MultipartFile file, Long userId) {

        UserEntity user = userService.findById(userId);
        Image avatarImage = new Image();

        try {
            String userDirectory = SAVE_DIRECTORY_ROUTE_AVATARS + "user_" + userId + "/";
            Files.createDirectories(Paths.get(userDirectory));


            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(userDirectory + fileName);
            Files.write(filePath, file.getBytes());


            avatarImage.setFileName(fileName);

            //Correct Path to set
            String correctPath = userDirectory + fileName;
            correctPath = correctPath.replace("src/main/resources/static","");
            filePath = Paths.get(correctPath);
            avatarImage.setFilePath(filePath.toString());

        }catch (IOException ex){
            throw new RuntimeException("Avatar cannot be saved");
        }

        avatarImage.setFileType(file.getContentType());
        avatarImage.setUser(user);

        avatarImageRepository.save(avatarImage);

        user.setAvatarImage(avatarImage);
        userService.save(user);
    }
}
