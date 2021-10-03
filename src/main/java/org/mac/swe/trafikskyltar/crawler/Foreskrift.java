package org.mac.swe.trafikskyltar.crawler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Foreskrift {
    private String code = "";
    private String label = "";
    private String beskrivning = "";

    private static final Pattern detectCodePattern = Pattern.compile("^\\w\\d+\\s");

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code != null ? code.trim() : code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        String rawLabel = label != null ? label.trim() : label;
        if (this.label == null || this.label.isBlank()) {
            Matcher m = detectCodePattern.matcher(rawLabel);
            if (m.find()) {
                setCode(rawLabel.substring(0, m.end()).trim());
                this.label = rawLabel.substring(m.end()).trim();
            } else {
                this.label = rawLabel;
            }
        } else {
            this.label = rawLabel;
        }
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning != null ? beskrivning.trim() : beskrivning;
    }
}
