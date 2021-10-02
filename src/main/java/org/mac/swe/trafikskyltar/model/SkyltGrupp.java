package org.mac.swe.trafikskyltar.model;

import java.util.ArrayList;
import java.util.List;

public class SkyltGrupp {
    private String beskrivning;
    private String label;
    private String href;
    private String domainname;

    private final List<Skylt> skyltar = new ArrayList<>();

    public void addSkylt(Skylt skylt) {
        skyltar.add(skylt);
    }

    public List<Skylt> getSkyltar() {
        return this.skyltar;
    }

    public String getDomainname() {
        return this.domainname;
    }

    public void setDomainname(String domainname) {
        this.domainname = domainname;
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

    public void setBeskrivning(String trim) {
        this.beskrivning = trim;
    }

    public String getBeskrivning() {
        return beskrivning;
    }
}
