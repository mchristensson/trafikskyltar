package org.mac.swe.trafikskyltar.model.vagtrafik;

import java.util.Arrays;
import java.util.Optional;

public enum VagtrafikGruppMappning {

    A00001(VagtrafikGrupp.SNOSKOTER, VagtrafikGrupp.TERRANGSKOTER),
    A00002(VagtrafikGrupp.TERRANGHJULING, VagtrafikGrupp.TERRANGSKOTER),
    A00003(VagtrafikGrupp.TERRANGSKOTER, VagtrafikGrupp.TERRANGMOTORFORDON),
    A00004(VagtrafikGrupp.TERRANGSLAP, VagtrafikGrupp.SLAPFORDON),
    A00005(VagtrafikGrupp.TERRANGMOTORFORDON, VagtrafikGrupp.MOTORDRIVET_FORDON),
    A00006(VagtrafikGrupp.TERRANGVAGN, VagtrafikGrupp.TERRANGMOTORFORDON),
    A00007(VagtrafikGrupp.TUNG_TERRANGVAGN, VagtrafikGrupp.TERRANGVAGN),
    A00008(VagtrafikGrupp.BIL, VagtrafikGrupp.MOTORFORDON),
    A00009(VagtrafikGrupp.MOTORREDSKAP, VagtrafikGrupp.MOTORDRIVET_FORDON),
    A00010(VagtrafikGrupp.MOTORFORDON, VagtrafikGrupp.MOTORDRIVET_FORDON),
    A000011(VagtrafikGrupp.SLAPFORDON, VagtrafikGrupp.FORDON),
    A000012(VagtrafikGrupp.MOTORCYKEL, VagtrafikGrupp.MOTORFORDON),
    A000013(VagtrafikGrupp.LASTBIL, VagtrafikGrupp.BIL),
    A000014(VagtrafikGrupp.TRAKTOR, VagtrafikGrupp.MOTORDRIVET_FORDON),
    A000015(VagtrafikGrupp.BUSS, VagtrafikGrupp.BIL),
    A000016(VagtrafikGrupp.MOTORDRIVET_FORDON, VagtrafikGrupp.FORDON),
    //A000017(Grupp.AXELTRYCK, Grupp.STORHET),
    //A000018(Grupp.BOGGI, Grupp.INRATTNING),
    //A000019(Grupp.BOGGITRYCK, Grupp.STORHET),
    //A000020(Grupp.BRUTTOVIKT, Grupp.STORHET),
    //A000021(Grupp.BARIGHETSKLASS, Grupp.STORHET),
    A000022(VagtrafikGrupp.CYKEL, VagtrafikGrupp.FORDON),
    A000023(VagtrafikGrupp.CYKELKARRA, VagtrafikGrupp.SLAPKARRA),
    A000024(VagtrafikGrupp.DOLLY, VagtrafikGrupp.SLAPKARRA),
    A000025(VagtrafikGrupp.EFTERFORDON, VagtrafikGrupp.FORDON),
    A000026(VagtrafikGrupp.EG_MOBILKRAN, VagtrafikGrupp.TUNG_LASTBIL),
    // A000027(Grupp.FORDON, Grupp.ENTITET),
    A000028(VagtrafikGrupp.FORDONSTAG, VagtrafikGrupp.MOTORDRIVET_FORDON),
    A000029(VagtrafikGrupp.HASTFORDON, VagtrafikGrupp.FORDON),
    A000030(VagtrafikGrupp.LEKFORDON, VagtrafikGrupp.FORDON),
    A000031(VagtrafikGrupp.LATT_BUSS, VagtrafikGrupp.BUSS),
    A000032(VagtrafikGrupp.LATT_LASTBIL, VagtrafikGrupp.LASTBIL),
    A000033(VagtrafikGrupp.LATT_MOTORCYKEL, VagtrafikGrupp.MOTORCYKEL),
    A000034(VagtrafikGrupp.LATT_SLAPFORDON, VagtrafikGrupp.SLAPFORDON),
    A000035(VagtrafikGrupp.LATT_TERRANGVAGN, VagtrafikGrupp.TERRANGVAGN),
    //A000036(Grupp.MAXLAST_FORDON_AA, Grupp.STORHET),
    A000037(VagtrafikGrupp.MOPED_KLASS_1, VagtrafikGrupp.MOTORFORDON),
    A000038(VagtrafikGrupp.MOPED_KLASS_2, VagtrafikGrupp.MOTORFORDON),
    A000039(VagtrafikGrupp.MOTORREDSKAP_KLASS_1, VagtrafikGrupp.MOTORREDSKAP),
    A000040(VagtrafikGrupp.MOTORREDSKAP_KLASS_2, VagtrafikGrupp.MOTORREDSKAP),
    A000041(VagtrafikGrupp.PERSONBIL, VagtrafikGrupp.BIL),
    A000042(VagtrafikGrupp.PERSONBIL_KLASS_1, VagtrafikGrupp.PERSONBIL),
    A000043(VagtrafikGrupp.PERSONBIL_KLASS_2, VagtrafikGrupp.PERSONBIL),
    A000044(VagtrafikGrupp.PAHANGSVAGN, VagtrafikGrupp.SLAPVAGN),
    A000045(VagtrafikGrupp.SIDVAGN, VagtrafikGrupp.FORDON),
    A000046(VagtrafikGrupp.SLAPKARRA, VagtrafikGrupp.SLAPVAGN),
    A000047(VagtrafikGrupp.SLAPSLADE, VagtrafikGrupp.SLAPFORDON),
    A000048(VagtrafikGrupp.SLAPVAGN, VagtrafikGrupp.SLAPFORDON),
    // A000049(Grupp.SLAPVAGNSVIKT, Grupp.STORHET),
    // A000050(Grupp.TJANSTEVIKT_BIL_MFL, Grupp.STORHET),
    // A000051(Grupp.TJANSTEVIKT_MC_MFL, Grupp.STORHET),
    //  A000052(Grupp.TJANSTEVIKT_TERRANGMOTORFORDON, Grupp.STORHET),
    //  A000053(Grupp.TOTALVIKT_BIL_MFL,Grupp.STORHET),
    //  A000054(Grupp.TOTALVIKT_MC_MFL, Grupp.STORHET),
    A000055(VagtrafikGrupp.TRAKTOR_A, VagtrafikGrupp.TRAKTOR),
    A000056(VagtrafikGrupp.TRAKTOR_B, VagtrafikGrupp.TRAKTOR),
    //  A000057(Grupp.TRAKTOR_SKATTEKLASS_1, Grupp.STORHET),
    //  A000058(Grupp.TRAKTOR_SKATTEKLASS_2, Grupp.STORHET),
    //  A000059(Grupp.TRAKTORTAG, Grupp.TILLSTAND),
    // A000060(Grupp.TRIPPELAXEL, Grupp.INRATTNING),
    //  A000061(Grupp.TRIPPELAXELTRYCK, Grupp.STORHET),
    A000062(VagtrafikGrupp.TUNG_BUSS, VagtrafikGrupp.BUSS),
    A000063(VagtrafikGrupp.TUNG_LASTBIL, VagtrafikGrupp.LASTBIL),
    A000064(VagtrafikGrupp.TUNG_SLAPFORDON, VagtrafikGrupp.SLAPFORDON),
    // A000065(Grupp.HJULINRATTNING, Grupp.FUNKTION),
    //   A000066(Grupp.VIKTMATT, Grupp.STORHET),
    A000067(VagtrafikGrupp.TUNG_MOTORCYKEL, VagtrafikGrupp.MOTORCYKEL);

    private final VagtrafikGrupp child;
    private final VagtrafikGrupp parent;

    VagtrafikGruppMappning(VagtrafikGrupp child, VagtrafikGrupp parent) {
        this.child = child;
        this.parent = parent;
    }

    public static Optional<VagtrafikGruppMappning> lookup(VagtrafikGrupp child) {
        return Arrays.stream(VagtrafikGruppMappning.values()).filter(g -> g.child == child).findFirst();
    }

    public VagtrafikGrupp getParent() {
        return parent;
    }
}
