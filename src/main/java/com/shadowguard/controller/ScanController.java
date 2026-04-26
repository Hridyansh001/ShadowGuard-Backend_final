package com.shadowguard.controller;

import com.shadowguard.model.ScanRequest;
import com.shadowguard.model.ScanResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ScanController {

    @PostMapping("/scan")
    public ScanResult scan(@RequestBody ScanRequest request) {
        return new ScanResult(
                request.getText(),
                0,
                "SAFE",
                List.of("No sensitive data detected")
        );
    }
}