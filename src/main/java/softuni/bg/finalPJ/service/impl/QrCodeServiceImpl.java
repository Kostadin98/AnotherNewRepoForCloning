package softuni.bg.finalPJ.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import softuni.bg.finalPJ.models.entities.UserEntity;
import softuni.bg.finalPJ.repositories.UserRepository;
import softuni.bg.finalPJ.service.QrCodeService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class QrCodeServiceImpl implements QrCodeService {

    private final String qrCodePath = "src/main/resources/static/images/qr-codes/";

    private final UserRepository userRepository;

    public QrCodeServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Generating QR code for each user
    @Override
    public String generateQRCodeImage(String text, Long userId) throws IOException, WriterException {
        File directory = new File(qrCodePath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it does not exist
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250);

        String fileName = "qr_code_" + userId + "_qr.png";
        Path filePath = Paths.get(qrCodePath, fileName);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

        return "/images/qr-codes/" + fileName;
    }

    @Override
    public void saveQrIfHasNoExisting(UserEntity user) throws IOException, WriterException {
        if (user.getQrCodePath() == null || user.getQrCodePath().isEmpty()) {
            String qrCodePath = generateQRCodeImage("http://localhost:8080/profile/" + user.getId(), user.getId());
            user.setQrCodePath(qrCodePath);
            userRepository.save(user);
        }
    }
}
