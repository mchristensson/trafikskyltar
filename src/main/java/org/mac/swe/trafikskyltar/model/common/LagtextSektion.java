package org.mac.swe.trafikskyltar.model.common;

import org.mac.swe.trafikskyltar.model.LagtextStycke;

import java.util.List;

public class LagtextSektion implements LagtextStycke {

    private String code;
    private String underrubrik;
    private String text;
    private List<LagtextStycke> lagtextStyckeList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnderrubrik() {
        return underrubrik;
    }

    public void setUnderrubrik(String underrubrik) {
        this.underrubrik = underrubrik;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<LagtextStycke> getLagtextStyckeList() {
        return lagtextStyckeList;
    }

    public void setLagtextStyckeList(List<LagtextStycke> lagtextStyckeList) {
        this.lagtextStyckeList = lagtextStyckeList;
    }
}
