package org.mac.swe.trafikskyltar.crawler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Definition {
    private String label = "";
    private String beskrivning = "";
    private VagtrafikdefinitionerCrawler.Kategori typ;
    private VagtrafikdefinitionerCrawler.Grupp grupp;
    private VagtrafikdefinitionerCrawler.Grupp kategori;
    private List<VagtrafikdefinitionerCrawler.Grupp> appliesTo = new ArrayList<>();
    private final List<Definition> children = new ArrayList<>();

    public void setAppliesTo(VagtrafikdefinitionerCrawler.Grupp... grupper) {
        this.appliesTo.addAll(Arrays.asList(grupper));
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label != null ? label.trim() : label;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning != null ? beskrivning.trim() : beskrivning;
    }

    public void setTyp(VagtrafikdefinitionerCrawler.Kategori kategori) {
        this.typ = kategori;
    }

    public VagtrafikdefinitionerCrawler.Kategori getTyp() {
        return typ;
    }

    public VagtrafikdefinitionerCrawler.Grupp getGrupp() {
        return grupp;
    }

    public void setGrupp(VagtrafikdefinitionerCrawler.Grupp grupp) {
        this.grupp = grupp;
    }

    public VagtrafikdefinitionerCrawler.Grupp getKategori() {
        return kategori;
    }

    public void setKategori(VagtrafikdefinitionerCrawler.Grupp kategori) {
        this.kategori = kategori;
    }

    public void addChild(Definition def) {
        this.children.add(def);
    }

    public List<Definition> getChildren() {
        return this.children;
    }
    @Override
    public String toString() {
        return "Definition{" +
                "label='" + label + '\'' +
                ", beskrivning='" + beskrivning + '\'' +
                ", typ=" + typ +
                ", grupp=" + grupp +
                ", kategori=" + kategori +
                '}';
    }
}
