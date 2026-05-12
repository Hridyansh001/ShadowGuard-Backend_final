package com.shadowguard.service;

import org.springframework.stereotype.Service;
// adding masking
@Service
public class Masking {

    public String masktext(String text)
    {
        text = text.replaceAll("\\b(sk-|ghp_|AIza|Bearer\\s)[A-Za-z0-9_\\-]{20,}\\b" , "$1 ************");
        return text;
    }

}
