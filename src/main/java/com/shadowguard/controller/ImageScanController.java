package com.shadowguard.controller;


import com.shadowguard.model.Alert;
import com.shadowguard.model.ScanResult;
import com.shadowguard.service.AlertRepository;
import com.shadowguard.service.ImageScannerService;
import com.shadowguard.service.Masking;
import com.shadowguard.service.SensitiveDataClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ImageScanController {

    @Autowired
     private ImageScannerService service;
    @Autowired
    private Masking masking;
    @Autowired
    private SensitiveDataClassifier classifier;
    @Autowired
    AlertRepository alertRepository;

    @PostMapping("/scan/image")
    public ScanResult scanImage(@RequestParam("file") MultipartFile file) throws Exception
    {
        String text = service.extractText(file);
        SensitiveDataClassifier.ClassificationResult result= classifier.classify(text);

        String maskedtext= masking.masktext(text);
        if(!result.getVerdict().equals("SAFE"))
        {
            Alert alert = new Alert(maskedtext, result.getScore(), result.getVerdict(), result.getReasons());
            alertRepository.save(alert);
        }
       return new ScanResult(maskedtext,result.getScore(),result.getVerdict(),result.getReasons(),maskedtext);
    }
}
