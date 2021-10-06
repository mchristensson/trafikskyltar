package org.mac.swe.trafikskyltar.model.vagmarke;

import org.mac.swe.trafikskyltar.model.LagtextStycke;

import java.util.ArrayList;
import java.util.List;

public class VagmarkesGrupp implements LagtextStycke {

    private String text;
    private String underrubrik;
    private String href;
    private String domainname;

    private final List<Vagmarke> skyltar = new ArrayList<>();

    public void addSkylt(Vagmarke vagmarke) {
        skyltar.add(vagmarke);
    }

    public List<Vagmarke> getSkyltar() {
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

    @Override
    public void setUnderrubrik(String underrubrik) {
        this.underrubrik = underrubrik;
    }

    @Override
    public String getUnderrubrik() {
        return this.underrubrik;
    }

    @Override
    public void setText(String trim) {
        this.text = trim;
    }

    @Override
    public String getText() {
        return text;
    }
}
