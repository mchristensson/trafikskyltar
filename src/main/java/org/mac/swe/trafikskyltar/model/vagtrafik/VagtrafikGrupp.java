package org.mac.swe.trafikskyltar.model.vagtrafik;

import java.util.Arrays;
import java.util.Optional;

public enum VagtrafikGrupp {
    SNOSKOTER("Snöskoter", VagtrafikKategori.ENTITET),
    TERRANGHJULING("Terränghjuling", VagtrafikKategori.ENTITET),
    TERRANGSKOTER("Terrängskoter", VagtrafikKategori.ENTITET),
    TERRANGSLAP("Terrängsläp", VagtrafikKategori.ENTITET),
    TERRANGMOTORFORDON("Terrängmotorfordon", VagtrafikKategori.ENTITET),
    TERRANGVAGN("Terrängvagn", VagtrafikKategori.ENTITET),
    TUNG_TERRANGVAGN("Tung terrängvagn", VagtrafikKategori.ENTITET),
    BIL("Bil", VagtrafikKategori.ENTITET),
    MOTORREDSKAP("Motorredskap", VagtrafikKategori.ENTITET),
    MOTORFORDON("Motorfordon", VagtrafikKategori.ENTITET),
    SLAPFORDON("Släpfordon", VagtrafikKategori.ENTITET),
    MOTORCYKEL("Motorcykel", VagtrafikKategori.ENTITET),
    LASTBIL("Lastbil", VagtrafikKategori.ENTITET),
    TRAKTOR("Traktor", VagtrafikKategori.ENTITET),
    BUSS("Buss", VagtrafikKategori.ENTITET),
    MOTORDRIVET_FORDON("Motordrivet fordon", VagtrafikKategori.ENTITET),
    AXELTRYCK("Axeltryck", VagtrafikKategori.STORHET),
    BOGGI("Boggi", VagtrafikKategori.INRATTNING),
    BOGGITRYCK("Boggitryck", VagtrafikKategori.STORHET),
    BRUTTOVIKT("Bruttovikt på fordon", VagtrafikKategori.STORHET),
    BARIGHETSKLASS("Bärighetsklass", VagtrafikKategori.STORHET),
    CYKEL("Cykel", VagtrafikKategori.ENTITET),
    CYKELKARRA("Cykelkärra", VagtrafikKategori.ENTITET),
    DOLLY("Dolly", VagtrafikKategori.ENTITET),
    EFTERFORDON("Efterfordon", VagtrafikKategori.ENTITET),
    EG_MOBILKRAN("EG-mobilkran", VagtrafikKategori.ENTITET),
    FORDON("Fordon", VagtrafikKategori.ENTITET),
    FORDONSTAG("Fordonståg", VagtrafikKategori.ENTITET),
    HASTFORDON("Hästfordon", VagtrafikKategori.ENTITET),
    LEKFORDON("Lekfordon", VagtrafikKategori.ENTITET),
    LATT_BUSS("Lätt buss", VagtrafikKategori.ENTITET),
    LATT_LASTBIL("Lätt lastbil", VagtrafikKategori.ENTITET),
    LATT_MOTORCYKEL("Lätt motorcykel", VagtrafikKategori.ENTITET),
    LATT_SLAPFORDON("Lätt släpfordon", VagtrafikKategori.ENTITET),
    LATT_TERRANGVAGN("Lätt terrängvagn", VagtrafikKategori.ENTITET),
    MAXLAST_FORDON_AA("Maximilast för ett motorfordon, en traktor , ett motorredskap, ett  terrängmotorfordon,  ett släpfordon eller  en sidvagn Moped", VagtrafikKategori.STORHET),
    MOPED_KLASS_1("Moped klass I", VagtrafikKategori.ENTITET),
    MOPED_KLASS_2("Moped klass II", VagtrafikKategori.ENTITET),
    MOTORREDSKAP_KLASS_1("Motorredskap klass I", VagtrafikKategori.ENTITET),
    MOTORREDSKAP_KLASS_2("Motorredskap klass II", VagtrafikKategori.ENTITET),
    PERSONBIL("Personbil", VagtrafikKategori.ENTITET),
    PERSONBIL_KLASS_1("Personbil klass I", VagtrafikKategori.ENTITET),
    PERSONBIL_KLASS_2("Personbil klass II", VagtrafikKategori.ENTITET),
    PAHANGSVAGN("Påhängsvagn", VagtrafikKategori.ENTITET),
    SIDVAGN("Sidvagn", VagtrafikKategori.ENTITET),
    SLAPKARRA("Släpkärra", VagtrafikKategori.ENTITET),
    SLAPSLADE("Släpsläde", VagtrafikKategori.ENTITET),
    SLAPVAGN("Släpvagn", VagtrafikKategori.ENTITET),
    SLAPVAGNSVIKT("Släpvagnsvikt", VagtrafikKategori.STORHET),
    TJANSTEVIKT_BIL_MFL("Tjänstevikt för en bil, en traktor eller ett motorredskap", VagtrafikKategori.STORHET),
    TJANSTEVIKT_MC_MFL("Tjänstevikt för en motorcykel eller en moped Tjänstevikt för ett släpfordon, ett terrängsläp eller en sidvagn", VagtrafikKategori.STORHET),
    TJANSTEVIKT_TERRANGMOTORFORDON("Tjänstevikt för ett terräng- motorfordon", VagtrafikKategori.STORHET),
    TOTALVIKT_BIL_MFL("Totalvikt för en bil, en traktor, ett motorredskap eller en tung terrängvagn", VagtrafikKategori.STORHET),
    TOTALVIKT_MC_MFL("Totalvikt för en motorcykel, ett terrängfordon utom en tung terrängvagn, ett släpfordon eller en sidvagn", VagtrafikKategori.STORHET),
    TRAKTOR_A("Traktor a", VagtrafikKategori.ENTITET),
    TRAKTOR_B("Traktor b", VagtrafikKategori.ENTITET),
    TRAKTOR_SKATTEKLASS_1("Traktor skatteklass I", VagtrafikKategori.STORHET),
    TRAKTOR_SKATTEKLASS_2("Traktor skatteklass II", VagtrafikKategori.STORHET),
    TRAKTORTAG("Traktortåg", VagtrafikKategori.TILLSTAND),
    TRIPPELAXEL("Trippelaxel", VagtrafikKategori.INRATTNING),
    TRIPPELAXELTRYCK("Trippelaxeltryck", VagtrafikKategori.STORHET),
    TUNG_BUSS("Tung buss", VagtrafikKategori.ENTITET),
    TUNG_LASTBIL("Tung lastbil", VagtrafikKategori.ENTITET),
    TUNG_SLAPFORDON("Tungt släpfordon", VagtrafikKategori.ENTITET),
    HJULINRATTNING("Inrattning", VagtrafikKategori.FUNKTION),

    VIKTMATT("Vikt", VagtrafikKategori.STORHET),
    TUNG_MOTORCYKEL("Tung motorcykel", VagtrafikKategori.ENTITET),
    OVRIGA_FORDON("Övriga fordon", VagtrafikKategori.ENTITET);

    private final String beskrivning;
    private final VagtrafikKategori vagtrafikKategori;

    VagtrafikGrupp(String beskrivning, VagtrafikKategori vagtrafikKategori) {
        this.beskrivning = beskrivning;
        this.vagtrafikKategori = vagtrafikKategori;
    }

    public static Optional<VagtrafikGrupp> lookup(String label) {
        return Arrays.stream(VagtrafikGrupp.values()).filter(g -> g.beskrivning.equalsIgnoreCase(label)).findFirst();
    }

    public VagtrafikKategori getKategori() {
        return vagtrafikKategori;
    }
}
