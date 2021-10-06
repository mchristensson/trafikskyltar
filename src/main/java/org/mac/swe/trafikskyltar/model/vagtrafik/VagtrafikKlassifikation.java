package org.mac.swe.trafikskyltar.model.vagtrafik;

import org.mac.swe.trafikskyltar.model.LagtextStycke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VagtrafikKlassifikation implements LagtextStycke {

    private String underrubrik = "";
    private String text = "";
    private VagtrafikKategori typ;
    private VagtrafikGrupp vagtrafikGrupp;
    private VagtrafikGrupp kategori;
    private List<VagtrafikGrupp> appliesTo = new ArrayList<>();
    private final List<VagtrafikKlassifikation> children = new ArrayList<>();

    public void setAppliesTo(VagtrafikGrupp... grupper) {
        this.appliesTo.addAll(Arrays.asList(grupper));
    }

    @Override
    public String getUnderrubrik() {
        return underrubrik;
    }

    @Override
    public void setUnderrubrik(String underrubrik) {
        this.underrubrik = underrubrik != null ? underrubrik.trim() : underrubrik;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text != null ? text.trim() : text;
    }

    public void setTyp(VagtrafikKategori vagtrafikKategori) {
        this.typ = vagtrafikKategori;
    }

    public VagtrafikKategori getTyp() {
        return typ;
    }

    public VagtrafikGrupp getGrupp() {
        return vagtrafikGrupp;
    }

    public void setGrupp(VagtrafikGrupp vagtrafikGrupp) {
        this.vagtrafikGrupp = vagtrafikGrupp;
    }

    public VagtrafikGrupp getKategori() {
        return kategori;
    }

    public void setKategori(VagtrafikGrupp kategori) {
        this.kategori = kategori;
    }

    public void addChild(VagtrafikKlassifikation def) {
        this.children.add(def);
    }

    public List<VagtrafikKlassifikation> getChildren() {
        return this.children;
    }

    @Override
    public String toString() {
        return "Definition{" +
                "label='" + underrubrik + '\'' +
                ", beskrivning='" + text + '\'' +
                ", typ=" + typ +
                ", grupp=" + vagtrafikGrupp +
                ", kategori=" + kategori +
                '}';
    }
}
