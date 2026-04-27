package org.example.lab2ee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private List<String> violations;

    public ApiError() { this.timestamp = LocalDateTime.now(); }

    public ApiError(int status, String error, String message) {
        this(); this.status = status; this.error = error; this.message = message;
    }

    public ApiError(int status, String error, String message, List<String> violations) {
        this(status, error, message); this.violations = violations;
    }

    public int getStatus() { return status; }              public void setStatus(int s) { this.status = s; }
    public String getError() { return error; }             public void setError(String e) { this.error = e; }
    public String getMessage() { return message; }         public void setMessage(String m) { this.message = m; }
    public LocalDateTime getTimestamp() { return timestamp; } public void setTimestamp(LocalDateTime t) { this.timestamp = t; }
    public List<String> getViolations() { return violations; } public void setViolations(List<String> v) { this.violations = v; }
}
