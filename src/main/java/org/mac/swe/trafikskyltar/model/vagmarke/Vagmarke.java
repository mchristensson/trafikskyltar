package org.mac.swe.trafikskyltar.model.vagmarke;

import java.util.ArrayList;
import java.util.List;

public class Vagmarke {

    private String href;
    private String label;
    private String beskrivning;
    private String skyltBildHref;
    private String domainpath;

    public List<Vagmarke> vagmarkeAlternativa = new ArrayList<>();

    public void addSkyltAlternativa(Vagmarke href) {
        vagmarkeAlternativa.add(href);
    }

    public List<Vagmarke> getSkyltAlternativa() {
        return this.vagmarkeAlternativa;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHref() {
        return this.href;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public String getSkyltBildHref() {
        return skyltBildHref;
    }

    public void setSkyltBildHref(String skyltBildHref) {
        this.skyltBildHref = skyltBildHref;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    @Override
    public String toString() {
        return "org.mac.swe.trafikskyltar.model.Skylt{" +
                "href='" + href + '\'' +
                ", label='" + label + '\'' +
                ", beskrivning='" + beskrivning + '\'' +
                ", skyltBildHref='" + skyltBildHref + '\'' +
                ", skyltAlternativa=" + vagmarkeAlternativa +
                '}';
    }


    public String getDomainpath() {
        return domainpath;
    }

    public void setDomainpath(String domainpath) {
        this.domainpath = domainpath;
    }
}
