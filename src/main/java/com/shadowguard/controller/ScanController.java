//package com.shadowguard.controller;
//
//import com.shadowguard.model.ScanRequest;
//import com.shadowguard.model.ScanResult;
//import com.shadowguard.service.SensitiveDataClassifier;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//@CrossOrigin(origins = "*")
//public class ScanController {
//
//    @Autowired
//    private SensitiveDataClassifier classifier;
//
//    @PostMapping("/scan")
//    public ScanResult scan(@RequestBody ScanRequest request) {
//        SensitiveDataClassifier.ClassificationResult result =
//                classifier.classify(request.getText());
//
//        return new ScanResult(
//                request.getText(),
//                result.getScore(),
//                result.getVerdict(),
//                result.getReasons()
//        );
//    }
//}

package com.shadowguard.controller;

import com.shadowguard.model.Alert;
import com.shadowguard.model.ScanRequest;
import com.shadowguard.model.ScanResult;
import com.shadowguard.service.AlertRepository;
import com.shadowguard.service.Masking;
import com.shadowguard.service.SensitiveDataClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ScanController {

    @Autowired
    private Masking masking;
    @Autowired
    private SensitiveDataClassifier classifier;

    @Autowired
    private AlertRepository alertRepository;

    @PostMapping("/scan")
    public ScanResult scan(@RequestBody ScanRequest request) {
        SensitiveDataClassifier.ClassificationResult result =
                classifier.classify(request.getText());

        String mask = masking.masktext(request.getText());  // new

        Alert alert = new Alert(
                request.getText(),
                result.getScore(),
                result.getVerdict(),
                result.getReasons()
        );
        alertRepository.save(alert);

        return new ScanResult(
                request.getText(),
                result.getScore(),
                result.getVerdict(),
                result.getReasons(),
                mask // new
        );


    }

    @GetMapping("/alerts")
    public List<Alert> getAlerts() {
        return alertRepository.findAll();
    }
}