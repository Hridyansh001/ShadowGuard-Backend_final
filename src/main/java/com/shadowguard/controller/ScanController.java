package com.shadowguard.controller;

import com.shadowguard.model.ScanRequest;
import com.shadowguard.model.ScanResult;
import com.shadowguard.service.SensitiveDataClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ScanController {

    @Autowired
    private SensitiveDataClassifier classifier;

    @PostMapping("/scan")
    public ScanResult scan(@RequestBody ScanRequest request) {
        SensitiveDataClassifier.ClassificationResult result =
                classifier.classify(request.getText());

        return new ScanResult(
                request.getText(),
                result.getScore(),
                result.getVerdict(),
                result.getReasons()
        );
    }
}