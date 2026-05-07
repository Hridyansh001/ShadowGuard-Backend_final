package com.shadowguard.service;

import org.springframework.stereotype.Service;

@Service
public class EntropyCalculator {

    public double calculate(String text) {
        if (text == null || text.isEmpty()) return 0;

        int[] frequency = new int[256];
        for (char c : text.toCharArray()) {
            frequency[c]++;
        }

        double entropy = 0;
        int length = text.length();

        for (int count : frequency) {
            if (count > 0) {
                double probability = (double) count / length;
                entropy -= probability * (Math.log(probability) / Math.log(2));
            }
        }

        return entropy;
    }

    public boolean isHighEntropy(String text) {
        // Strings longer than 20 chars with entropy above 4.0
        // are likely API keys, tokens or passwords
        return text.length() > 20 && calculate(text) > 4.0;
    }
}