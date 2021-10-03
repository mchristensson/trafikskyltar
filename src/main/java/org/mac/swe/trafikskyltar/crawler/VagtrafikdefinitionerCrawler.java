package org.mac.swe.trafikskyltar.crawler;

import org.mac.swe.trafikskyltar.outputhandler.VagmarkesforordningOutputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class VagtrafikdefinitionerCrawler implements FileSupportedCrawler {
    private static final Logger logger = LoggerFactory.getLogger(VagtrafikdefinitionerCrawler.class);

    private List<Definition> foreskriftList;
    private Definition rot;
    @Override
    public void init() {
        logger.info("Init completed");
    }

    @Override
    public void shutdown() {
        logger.info("Shutdown completed");
    }

    @Override
    public void crawl() throws Exception {
        this.foreskriftList = readFromResourceFile();

        kategorisera();
    }

    private void kategorisera() {
        Map<Grupp, Definition> index = new HashMap<>();

        int i = 0;
        List<Definition> items = this.foreskriftList;
        while (!items.isEmpty() && i < 10) {
            items = buildIndex(index, items);
            i++;
        }
        logger.info("Återstod efter indexering: {} [iter={}]", items, i);
        this.rot = index.get(Grupp.FORDON);
    }

    private static List<Definition> buildIndex(Map<Grupp, Definition> index, List<Definition> items) {
        List<Definition> wasAddedList = new ArrayList<>();
        List<Definition> tmpList = new ArrayList<>(items);
        for (Definition def : tmpList) {
            Optional<Grupp> grp = Grupp.lookup(def.getLabel());
            if (grp.isPresent()) {
                def.setKategori(grp.get());
            } else {
                logger.warn("Kategori '{}' stöds ej!", def.getLabel());
            }

            if (def.getKategori() == null || def.getKategori().kategori != Kategori.ENTITET) {
                //TODO: Vi plockar av dessa tills vidare
                wasAddedList.add(def);
            } else if (def.getKategori() == Grupp.FORDON) {
                logger.info("Indexerar kategori... {}", def.getKategori());
                index.putIfAbsent(def.getKategori(), def);
                wasAddedList.add(def);
            } else if (def.getKategori() != null) {
                Optional<GruppMappning> mapping = GruppMappning.lookup(def.getKategori());
                if (mapping.isPresent()) {
                    Definition indexedEntry = index.get(mapping.get().parent);
                    if (indexedEntry != null) {
                        logger.info("Indexerar kategori... {}", def.getKategori());
                        indexedEntry.addChild(def);
                        index.putIfAbsent(def.getKategori(), def);
                        wasAddedList.add(def);
                    }
                } else {
                    logger.warn("Kategori '{}' still awaits being indexed", def.getKategori());
                }
            }
        }
        tmpList.removeAll(wasAddedList);
        logger.info("Entries left : {}", tmpList.size());
        return tmpList;
    }

    enum GruppMappning {

        A00001(Grupp.SNOSKOTER, Grupp.TERRANGSKOTER),
        A00002(Grupp.TERRANGHJULING, Grupp.TERRANGSKOTER),
        A00003(Grupp.TERRANGSKOTER, Grupp.TERRANGMOTORFORDON),
        A00004(Grupp.TERRANGSLAP, Grupp.SLAPFORDON),
        A00005(Grupp.TERRANGMOTORFORDON, Grupp.MOTORDRIVET_FORDON),
        A00006(Grupp.TERRANGVAGN, Grupp.TERRANGMOTORFORDON),
        A00007(Grupp.TUNG_TERRANGVAGN, Grupp.TERRANGVAGN),
        A00008(Grupp.BIL, Grupp.MOTORFORDON),
        A00009(Grupp.MOTORREDSKAP, Grupp.MOTORDRIVET_FORDON),
        A00010(Grupp.MOTORFORDON, Grupp.MOTORDRIVET_FORDON),
        A000011(Grupp.SLAPFORDON, Grupp.FORDON),
        A000012(Grupp.MOTORCYKEL, Grupp.MOTORFORDON),
        A000013(Grupp.LASTBIL, Grupp.BIL),
        A000014(Grupp.TRAKTOR, Grupp.MOTORDRIVET_FORDON),
        A000015(Grupp.BUSS, Grupp.BIL),
        A000016(Grupp.MOTORDRIVET_FORDON, Grupp.FORDON),
        //A000017(Grupp.AXELTRYCK, Grupp.STORHET),
        //A000018(Grupp.BOGGI, Grupp.INRATTNING),
        //A000019(Grupp.BOGGITRYCK, Grupp.STORHET),
        //A000020(Grupp.BRUTTOVIKT, Grupp.STORHET),
        //A000021(Grupp.BARIGHETSKLASS, Grupp.STORHET),
        A000022(Grupp.CYKEL, Grupp.FORDON),
        A000023(Grupp.CYKELKARRA, Grupp.SLAPKARRA),
        A000024(Grupp.DOLLY, Grupp.SLAPKARRA),
        A000025(Grupp.EFTERFORDON, Grupp.FORDON),
        A000026(Grupp.EG_MOBILKRAN, Grupp.TUNG_LASTBIL),
        // A000027(Grupp.FORDON, Grupp.ENTITET),
        A000028(Grupp.FORDONSTAG, Grupp.MOTORDRIVET_FORDON),
        A000029(Grupp.HASTFORDON, Grupp.FORDON),
        A000030(Grupp.LEKFORDON, Grupp.FORDON),
        A000031(Grupp.LATT_BUSS, Grupp.BUSS),
        A000032(Grupp.LATT_LASTBIL, Grupp.LASTBIL),
        A000033(Grupp.LATT_MOTORCYKEL, Grupp.MOTORCYKEL),
        A000034(Grupp.LATT_SLAPFORDON, Grupp.SLAPFORDON),
        A000035(Grupp.LATT_TERRANGVAGN, Grupp.TERRANGVAGN),
        //A000036(Grupp.MAXLAST_FORDON_AA, Grupp.STORHET),
        A000037(Grupp.MOPED_KLASS_1, Grupp.MOTORFORDON),
        A000038(Grupp.MOPED_KLASS_2, Grupp.MOTORFORDON),
        A000039(Grupp.MOTORREDSKAP_KLASS_1, Grupp.MOTORREDSKAP),
        A000040(Grupp.MOTORREDSKAP_KLASS_2, Grupp.MOTORREDSKAP),
        A000041(Grupp.PERSONBIL, Grupp.BIL),
        A000042(Grupp.PERSONBIL_KLASS_1, Grupp.PERSONBIL),
        A000043(Grupp.PERSONBIL_KLASS_2, Grupp.PERSONBIL),
        A000044(Grupp.PAHANGSVAGN, Grupp.SLAPVAGN),
        A000045(Grupp.SIDVAGN, Grupp.FORDON),
        A000046(Grupp.SLAPKARRA, Grupp.SLAPVAGN),
        A000047(Grupp.SLAPSLADE, Grupp.SLAPFORDON),
        A000048(Grupp.SLAPVAGN, Grupp.SLAPFORDON),
        // A000049(Grupp.SLAPVAGNSVIKT, Grupp.STORHET),
        // A000050(Grupp.TJANSTEVIKT_BIL_MFL, Grupp.STORHET),
        // A000051(Grupp.TJANSTEVIKT_MC_MFL, Grupp.STORHET),
        //  A000052(Grupp.TJANSTEVIKT_TERRANGMOTORFORDON, Grupp.STORHET),
        //  A000053(Grupp.TOTALVIKT_BIL_MFL,Grupp.STORHET),
        //  A000054(Grupp.TOTALVIKT_MC_MFL, Grupp.STORHET),
        A000055(Grupp.TRAKTOR_A, Grupp.TRAKTOR),
        A000056(Grupp.TRAKTOR_B, Grupp.TRAKTOR),
        //  A000057(Grupp.TRAKTOR_SKATTEKLASS_1, Grupp.STORHET),
        //  A000058(Grupp.TRAKTOR_SKATTEKLASS_2, Grupp.STORHET),
        //  A000059(Grupp.TRAKTORTAG, Grupp.TILLSTAND),
        // A000060(Grupp.TRIPPELAXEL, Grupp.INRATTNING),
        //  A000061(Grupp.TRIPPELAXELTRYCK, Grupp.STORHET),
        A000062(Grupp.TUNG_BUSS, Grupp.BUSS),
        A000063(Grupp.TUNG_LASTBIL, Grupp.LASTBIL),
        A000064(Grupp.TUNG_SLAPFORDON, Grupp.SLAPFORDON),
        // A000065(Grupp.HJULINRATTNING, Grupp.FUNKTION),
        //   A000066(Grupp.VIKTMATT, Grupp.STORHET),
        A000067(Grupp.TUNG_MOTORCYKEL, Grupp.MOTORCYKEL);

        private final Grupp child;
        private final Grupp parent;

        GruppMappning(Grupp child, Grupp parent) {
            this.child = child;
            this.parent = parent;
        }

        public static Optional<GruppMappning> lookup(Grupp child) {
            return Arrays.stream(GruppMappning.values()).filter(g -> g.child == child).findFirst();
        }
    }

    enum Grupp {
        SNOSKOTER("Snöskoter", Kategori.ENTITET),
        TERRANGHJULING("Terränghjuling", Kategori.ENTITET),
        TERRANGSKOTER("Terrängskoter", Kategori.ENTITET),
        TERRANGSLAP("Terrängsläp", Kategori.ENTITET),
        TERRANGMOTORFORDON("Terrängmotorfordon", Kategori.ENTITET),
        TERRANGVAGN("Terrängvagn", Kategori.ENTITET),
        TUNG_TERRANGVAGN("Tung terrängvagn", Kategori.ENTITET),
        BIL("Bil", Kategori.ENTITET),
        MOTORREDSKAP("Motorredskap", Kategori.ENTITET),
        MOTORFORDON("Motorfordon", Kategori.ENTITET),
        SLAPFORDON("Släpfordon", Kategori.ENTITET),
        MOTORCYKEL("Motorcykel", Kategori.ENTITET),
        LASTBIL("Lastbil", Kategori.ENTITET),
        TRAKTOR("Traktor", Kategori.ENTITET),
        BUSS("Buss", Kategori.ENTITET),
        MOTORDRIVET_FORDON("Motordrivet fordon", Kategori.ENTITET),
        AXELTRYCK("Axeltryck", Kategori.STORHET),
        BOGGI("Boggi", Kategori.INRATTNING),
        BOGGITRYCK("Boggitryck", Kategori.STORHET),
        BRUTTOVIKT("Bruttovikt på fordon", Kategori.STORHET),
        BARIGHETSKLASS("Bärighetsklass", Kategori.STORHET),
        CYKEL("Cykel", Kategori.ENTITET),
        CYKELKARRA("Cykelkärra", Kategori.ENTITET),
        DOLLY("Dolly", Kategori.ENTITET),
        EFTERFORDON("Efterfordon", Kategori.ENTITET),
        EG_MOBILKRAN("EG-mobilkran", Kategori.ENTITET),
        FORDON("Fordon", Kategori.ENTITET),
        FORDONSTAG("Fordonståg", Kategori.ENTITET),
        HASTFORDON("Hästfordon", Kategori.ENTITET),
        LEKFORDON("Lekfordon", Kategori.ENTITET),
        LATT_BUSS("Lätt buss", Kategori.ENTITET),
        LATT_LASTBIL("Lätt lastbil", Kategori.ENTITET),
        LATT_MOTORCYKEL("Lätt motorcykel", Kategori.ENTITET),
        LATT_SLAPFORDON("Lätt släpfordon", Kategori.ENTITET),
        LATT_TERRANGVAGN("Lätt terrängvagn", Kategori.ENTITET),
        MAXLAST_FORDON_AA("Maximilast för ett motorfordon, en traktor , ett motorredskap, ett  terrängmotorfordon,  ett släpfordon eller  en sidvagn Moped", Kategori.STORHET),
        MOPED_KLASS_1("Moped klass I", Kategori.ENTITET),
        MOPED_KLASS_2("Moped klass II", Kategori.ENTITET),
        MOTORREDSKAP_KLASS_1("Motorredskap klass I", Kategori.ENTITET),
        MOTORREDSKAP_KLASS_2("Motorredskap klass II", Kategori.ENTITET),
        PERSONBIL("Personbil", Kategori.ENTITET),
        PERSONBIL_KLASS_1("Personbil klass I", Kategori.ENTITET),
        PERSONBIL_KLASS_2("Personbil klass II", Kategori.ENTITET),
        PAHANGSVAGN("Påhängsvagn", Kategori.ENTITET),
        SIDVAGN("Sidvagn", Kategori.ENTITET),
        SLAPKARRA("Släpkärra", Kategori.ENTITET),
        SLAPSLADE("Släpsläde", Kategori.ENTITET),
        SLAPVAGN("Släpvagn", Kategori.ENTITET),
        SLAPVAGNSVIKT("Släpvagnsvikt", Kategori.STORHET),
        TJANSTEVIKT_BIL_MFL("Tjänstevikt för en bil, en traktor eller ett motorredskap", Kategori.STORHET),
        TJANSTEVIKT_MC_MFL("Tjänstevikt för en motorcykel eller en moped Tjänstevikt för ett släpfordon, ett terrängsläp eller en sidvagn", Kategori.STORHET),
        TJANSTEVIKT_TERRANGMOTORFORDON("Tjänstevikt för ett terräng- motorfordon", Kategori.STORHET),
        TOTALVIKT_BIL_MFL("Totalvikt för en bil, en traktor, ett motorredskap eller en tung terrängvagn", Kategori.STORHET),
        TOTALVIKT_MC_MFL("Totalvikt för en motorcykel, ett terrängfordon utom en tung terrängvagn, ett släpfordon eller en sidvagn", Kategori.STORHET),
        TRAKTOR_A("Traktor a", Kategori.ENTITET),
        TRAKTOR_B("Traktor b", Kategori.ENTITET),
        TRAKTOR_SKATTEKLASS_1("Traktor skatteklass I", Kategori.STORHET),
        TRAKTOR_SKATTEKLASS_2("Traktor skatteklass II", Kategori.STORHET),
        TRAKTORTAG("Traktortåg", Kategori.TILLSTAND),
        TRIPPELAXEL("Trippelaxel", Kategori.INRATTNING),
        TRIPPELAXELTRYCK("Trippelaxeltryck", Kategori.STORHET),
        TUNG_BUSS("Tung buss", Kategori.ENTITET),
        TUNG_LASTBIL("Tung lastbil", Kategori.ENTITET),
        TUNG_SLAPFORDON("Tungt släpfordon", Kategori.ENTITET),
        HJULINRATTNING("Inrattning", Kategori.FUNKTION),

        VIKTMATT("Vikt", Kategori.STORHET),
        TUNG_MOTORCYKEL("Tung motorcykel", Kategori.ENTITET),
        OVRIGA_FORDON("Övriga fordon", Kategori.ENTITET);

        private final String beskrivning;
        private final Kategori kategori;

        Grupp(String beskrivning, Kategori kategori) {
            this.beskrivning = beskrivning;
            this.kategori = kategori;
        }

        public static Optional<Grupp> lookup(String label) {
            return Arrays.stream(Grupp.values()).filter(g -> g.beskrivning.equalsIgnoreCase(label)).findFirst();
        }
    }

    enum Kategori {
        ENTITET("Entitet"),
        FUNKTION("Funktion"),
        INRATTNING("Inrattning"),
        TILLSTAND("Tillstånd"),
        STORHET("Storhet");

        private final String beskrivning;

        Kategori(String beskrivning) {
            this.beskrivning = beskrivning;
        }


    }

    private List<Definition> readFromResourceFile() throws Exception {
        List<Definition> foreskriftList = new ArrayList<>();
        InputStream inputStream = null;
        try {
            Class clazz = VagtrafikdefinitionerCrawler.class;
            inputStream = clazz.getResourceAsStream("/vagtrafikdefinitioner.txt");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line = reader.readLine();

                Definition current = new Definition();
                while (line != null) {
                    logger.trace("Läser rad... {}", line);
                    line = reader.readLine();
                    if (line == null) {
                        logger.trace("line is null");
                        continue;
                    }
                    if (line.length() == 0) {
                        foreskriftList.add(current);
                        current = new Definition();
                        continue;
                    }

                    String a;
                    String b;
                    if (line.length() > 24) {
                        a = line.substring(0, 24);
                        b = line.substring(24);
                    } else {
                        a = line;
                        b = null;
                    }
                    current.setLabel(current.getLabel().concat(" " + a));
                    if (b != null) {
                        current.setBeskrivning(current.getBeskrivning().concat(" " + b));
                    }
                }
            } catch (IOException ee) {
                logger.error("Kunde inte läsa fil", ee);
            }

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Kunde inte stäng inputstream", e);
                }

            }
        }
        return foreskriftList;
    }

    @Override
    public void writeOutput() {
        if (this.foreskriftList != null) {
            try {
                VagmarkesforordningOutputHandler outputHandler = new VagmarkesforordningOutputHandler("./target/vagtrafikdefinintioner/");
                outputHandler.writeOutput(this.rot);
            } catch (Exception e) {
                logger.error("Unable to write output of object", e);
            }
        }
    }
}
