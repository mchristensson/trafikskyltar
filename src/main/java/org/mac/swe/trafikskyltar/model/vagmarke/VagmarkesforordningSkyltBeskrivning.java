package org.mac.swe.trafikskyltar.model.vagmarke;

import org.mac.swe.trafikskyltar.model.LagtextStycke;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VagmarkesforordningSkyltBeskrivning implements LagtextStycke {
    private String code = "";
    private String underrubrik = "";
    private String text = "";

    private static final Pattern detectCodePattern = Pattern.compile("^\\w\\d+\\s");

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code != null ? code.trim() : code;
    }

    @Override
    public String getUnderrubrik() {
        return underrubrik;
    }

    @Override
    public void setUnderrubrik(String underrubrik) {
        String rawLabel = underrubrik != null ? underrubrik.trim() : underrubrik;
        if (this.underrubrik == null || this.underrubrik.isBlank()) {
            Matcher m = detectCodePattern.matcher(rawLabel);
            if (m.find()) {
                setCode(rawLabel.substring(0, m.end()).trim());
                this.underrubrik = rawLabel.substring(m.end()).trim();
            } else {
                this.underrubrik = rawLabel;
            }
        } else {
            this.underrubrik = rawLabel;
        }
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text != null ? text.trim() : text;
    }
}
