package softuni.bg.finalPJ.service;

import com.google.zxing.WriterException;
import softuni.bg.finalPJ.models.entities.UserEntity;

import java.io.IOException;

public interface QrCodeService {

    String generateQRCodeImage(String text, Long userId) throws IOException, WriterException;

    void saveQrIfHasNoExisting(UserEntity user) throws IOException, WriterException;
}
