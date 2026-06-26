package com.shadowguard.service;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileScannerService
{
    private final Tika tika = new Tika();

    public String extractText(MultipartFile file) throws Exception
    {
        return tika.parseToString(file.getInputStream());
    }
}
