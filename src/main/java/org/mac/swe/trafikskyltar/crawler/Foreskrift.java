package org.mac.swe.trafikskyltar.crawler;

public class Foreskrift {
    private String code = "";
    private String label = "";
    private String beskrivning = "";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code != null? code.trim() : code;
        /*
        if (this.code == null) {
            //Splitta upp code och label
            String[] data = rawData.split("\\.");
            if (data.length > 1) {

            }
        }       else {
               setLabel();
        }
            */
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        String rawLabel = label != null? label.trim() : label;
        if (this.label == null) {
                String[] data = rawLabel.split("\\.");
            if (data.length > 1) {
                setCode(data[0]);
                this.label = data[1].trim();
            } else {
                this.label = rawLabel;
            }
        }   else {
             this.label = rawLabel;
        }
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning != null? beskrivning.trim() : beskrivning;
    }
}
