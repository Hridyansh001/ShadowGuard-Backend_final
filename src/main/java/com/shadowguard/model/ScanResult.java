package com.shadowguard.model;

import java.util.List;

public class ScanResult {
    private String text;
    private int riskScore;
    private String verdict;
    private List<String> topReasons;
    private String mask; //new

    public ScanResult(String text, int riskScore,
                      String verdict, List<String> topReasons, String mask) {
        this.text = text;
        this.riskScore = riskScore;
        this.verdict = verdict;
        this.topReasons = topReasons;
        this.mask=mask;
    }

    public String getText() { return text; }
    public int getRiskScore() { return riskScore; }
    public String getVerdict() { return verdict; }
    public List<String> getTopReasons() { return topReasons; }
    public String getMask(){ return mask;}
}