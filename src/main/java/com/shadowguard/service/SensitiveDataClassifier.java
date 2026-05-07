package com.shadowguard.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class SensitiveDataClassifier {

    private final EntropyCalculator entropyCalculator;

    public SensitiveDataClassifier(EntropyCalculator entropyCalculator) {
        this.entropyCalculator = entropyCalculator;
    }

    private static final Pattern AADHAAR = Pattern.compile(
            "\\b[0-9]{4}\\s?[0-9]{4}\\s?[0-9]{4}\\b"
    );

    private static final Pattern PAN = Pattern.compile(
            "\\b[A-Z]{5}[0-9]{4}[A-Z]{1}\\b"
    );

    private static final Pattern CREDIT_CARD = Pattern.compile(
            "\\b(?:\\d[ -]?){13,16}\\b"
    );

    private static final Pattern API_KEY = Pattern.compile(
            "\\b(sk-|ghp_|AIza|Bearer\\s)[A-Za-z0-9_\\-]{20,}\\b"
    );

    private static final Pattern EMAIL = Pattern.compile(
            "\\b[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}\\b"
    );

    private static final Pattern MOBILE = Pattern.compile(
            "\\b[6-9][0-9]{9}\\b"
    );

    private static final Pattern UPI = Pattern.compile(
            "\\b[a-zA-Z0-9.\\-_]{2,256}@(okaxis|okhdfcbank|okicici|oksbi|ybl|ibl|axl|waicici|paytm|apl|upi)\\b"
    );

    private static final Pattern PASSWORD = Pattern.compile(
            "(?i)(password|passwd|pwd|secret)\\s*[:=]\\s*\\S+"
    );

    private static final Pattern DB_CONNECTION = Pattern.compile(
            "(?i)(jdbc:|mongodb://|mysql://|postgres://|redis://)\\S+"
    );

    private static final Pattern SOURCE_CODE = Pattern.compile(
            "(?i)(private\\s+key|BEGIN RSA|import\\s+os|def\\s+\\w+\\(|function\\s+\\w+\\()"
    );

    public ClassificationResult classify(String text) {
        List<String> reasons = new ArrayList<>();
        int score = 0;

        if (matches(AADHAAR, text)) {
            reasons.add("Aadhaar number detected");
            score += 40;
        }
        if (matches(PAN, text)) {
            reasons.add("PAN card number detected");
            score += 35;
        }
        if (matches(CREDIT_CARD, text)) {
            reasons.add("Credit card number detected");
            score += 40;
        }
        if (matches(API_KEY, text)) {
            reasons.add("API key or secret token detected");
            score += 45;
        }
        if (matches(EMAIL, text)) {
            reasons.add("Email address detected");
            score += 15;
        }
        if (matches(MOBILE, text)) {
            reasons.add("Indian mobile number detected");
            score += 20;
        }
        if (matches(UPI, text)) {
            reasons.add("UPI ID detected");
            score += 25;
        }
        if (matches(PASSWORD, text)) {
            reasons.add("Plain text password detected");
            score += 50;
        }
        if (matches(DB_CONNECTION, text)) {
            reasons.add("Database connection string detected");
            score += 50;
        }
        if (matches(SOURCE_CODE, text)) {
            reasons.add("Source code or private key detected");
            score += 30;
        }

        // Entropy check — detect random strings like API keys
        for (String word : text.split("\\s+")) {
            if (entropyCalculator.isHighEntropy(word)) {
                reasons.add("High entropy string detected — possible API key or secret");
                score += 40;
                break;
            }
        }

        score = Math.min(score, 100);

        String verdict;
        if (score >= 50) {
            verdict = "BLOCKED";
        } else if (score >= 20) {
            verdict = "WARNING";
        } else {
            verdict = "SAFE";
        }

        if (reasons.isEmpty()) {
            reasons.add("No sensitive data detected");
        }

        return new ClassificationResult(score, verdict, reasons);
    }

    private boolean matches(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static class ClassificationResult {
        private final int score;
        private final String verdict;
        private final List<String> reasons;

        public ClassificationResult(int score, String verdict,
                                    List<String> reasons) {
            this.score = score;
            this.verdict = verdict;
            this.reasons = reasons;
        }

        public int getScore() { return score; }
        public String getVerdict() { return verdict; }
        public List<String> getReasons() { return reasons; }
    }
}