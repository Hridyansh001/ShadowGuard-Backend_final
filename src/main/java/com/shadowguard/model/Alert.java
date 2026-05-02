package com.shadowguard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private int riskScore;
    private String verdict;
    private LocalDateTime timestamp;

    @ElementCollection
    private List<String> topReasons;

    public Alert() {}

    public Alert(String text, int riskScore, String verdict,
                 List<String> topReasons) {
        this.text = text;
        this.riskScore = riskScore;
        this.verdict = verdict;
        this.topReasons = topReasons;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public int getRiskScore() { return riskScore; }
    public String getVerdict() { return verdict; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<String> getTopReasons() { return topReasons; }
}