package com.shadowguard.controller;

import com.shadowguard.model.Alert;
import com.shadowguard.model.ScanResult;
import com.shadowguard.service.AlertRepository;
import com.shadowguard.service.FileScannerService;
import com.shadowguard.service.Masking;
import com.shadowguard.service.SensitiveDataClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FileScanController {

    @Autowired
    private SensitiveDataClassifier classifier;
    @Autowired
    private FileScannerService fileScannerService;
    @Autowired
    private Masking masking;
    @Autowired
    private AlertRepository alertRepository;

    @PostMapping("/scan/file")
    public ScanResult scanfile(@RequestParam("file")MultipartFile file) throws Exception
    {
        if(file.isEmpty())
        {
            throw new RuntimeException("no file uploaded");
        }
        String text = fileScannerService.extractText(file);
        SensitiveDataClassifier.ClassificationResult result= classifier.classify(text);

        String maskedText = masking.masktext(text);

        if( !result.getVerdict().equals("SAFE")) {
            Alert alert = new Alert(maskedText, result.getScore(), result.getVerdict(), result.getReasons());
            alertRepository.save(alert);
        }
        return  new ScanResult(
                maskedText,

                result.getScore(),

                result.getVerdict(),

                result.getReasons(),

                maskedText
        );

    }
}
