package org.mac.swe.trafikskyltar.model.vagtrafik;

public enum VagtrafikKategori {

    ENTITET("Entitet"),
    FUNKTION("Funktion"),
    INRATTNING("Inrättning"),
    TILLSTAND("Tillstånd"),
    STORHET("Storhet");

    private final String beskrivning;

    VagtrafikKategori(String beskrivning) {
        this.beskrivning = beskrivning;
    }


    public String getBeskrivning() {
        return beskrivning;
    }
}
