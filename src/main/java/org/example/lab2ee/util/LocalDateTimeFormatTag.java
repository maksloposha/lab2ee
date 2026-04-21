package org.example.lab2ee.util;


import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatTag extends SimpleTagSupport {

    private LocalDateTime value;
    private String pattern = "dd.MM.yyyy HH:mm";

    public void setValue(LocalDateTime value) {
        this.value = value;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void doTag() throws JspException, IOException {
        var out = getJspContext().getOut();

        if (value == null) {
            out.write("—");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        out.write(value.format(formatter));
    }
}