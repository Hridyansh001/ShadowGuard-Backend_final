package com.shadowguard.service;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Service
public class ImageScannerService {
    private final Tesseract tesseract;

    public ImageScannerService()
    {
        tesseract = new Tesseract();
        String tessDataPath = System.getenv("TESSDATA_PREFIX");
        if (tessDataPath != null && !tessDataPath.isBlank()) {
            tesseract.setDatapath(tessDataPath);
        }
        tesseract.setLanguage("eng");
    }
    public String extractText(MultipartFile file) throws Exception{
        BufferedImage image = ImageIO.read(file.getInputStream());
        return tesseract.doOCR(image);
    }
}