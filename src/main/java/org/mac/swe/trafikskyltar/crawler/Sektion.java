package org.mac.swe.trafikskyltar.crawler;

import java.util.List;

class Sektion {
    private String code;
    private String label;
    private String beskrivning;
    private List<Foreskrift> foreskriftList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public List<Foreskrift> getForeskriftList() {
        return foreskriftList;
    }

    public void setForeskriftList(List<Foreskrift> foreskriftList) {
        this.foreskriftList = foreskriftList;
    }
}
