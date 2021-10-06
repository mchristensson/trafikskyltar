package org.mac.swe.trafikskyltar.crawler.impl;

import org.mac.swe.trafikskyltar.crawler.FileSupportedCrawler;
import org.mac.swe.trafikskyltar.model.vagtrafik.VagtrafikKlassifikation;
import org.mac.swe.trafikskyltar.model.vagtrafik.VagtrafikGrupp;
import org.mac.swe.trafikskyltar.model.vagtrafik.VagtrafikGruppMappning;
import org.mac.swe.trafikskyltar.model.vagtrafik.VagtrafikKategori;
import org.mac.swe.trafikskyltar.outputhandler.VagtrafikKlassifikationOutputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class VagtrafikdefinitionerCrawler implements FileSupportedCrawler {
    private static final Logger logger = LoggerFactory.getLogger(VagtrafikdefinitionerCrawler.class);

    private List<VagtrafikKlassifikation> foreskriftList;
    private VagtrafikKlassifikation rot;
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
        Map<VagtrafikGrupp, VagtrafikKlassifikation> index = new HashMap<>();

        int i = 0;
        List<VagtrafikKlassifikation> items = this.foreskriftList;
        while (!items.isEmpty() && i < 10) {
            items = buildIndex(index, items);
            i++;
        }
        logger.info("Återstod efter indexering: {} [iter={}]", items, i);
        this.rot = index.get(VagtrafikGrupp.FORDON);
    }

    private static List<VagtrafikKlassifikation> buildIndex(Map<VagtrafikGrupp, VagtrafikKlassifikation> index, List<VagtrafikKlassifikation> items) {
        List<VagtrafikKlassifikation> wasAddedList = new ArrayList<>();
        List<VagtrafikKlassifikation> tmpList = new ArrayList<>(items);
        for (VagtrafikKlassifikation def : tmpList) {
            Optional<VagtrafikGrupp> grp = VagtrafikGrupp.lookup(def.getUnderrubrik());
            if (grp.isPresent()) {
                def.setKategori(grp.get());
            } else {
                logger.warn("Kategori '{}' stöds ej!", def.getUnderrubrik());
            }

            if (def.getKategori() == null || def.getKategori().getKategori() != VagtrafikKategori.ENTITET) {
                //TODO: Vi plockar av dessa tills vidare
                wasAddedList.add(def);
            } else if (def.getKategori() == VagtrafikGrupp.FORDON) {
                logger.info("Indexerar kategori... {}", def.getKategori());
                index.putIfAbsent(def.getKategori(), def);
                wasAddedList.add(def);
            } else if (def.getKategori() != null) {
                Optional<VagtrafikGruppMappning> mapping = VagtrafikGruppMappning.lookup(def.getKategori());
                if (mapping.isPresent()) {
                    VagtrafikKlassifikation indexedEntry = index.get(mapping.get().getParent());
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

    private List<VagtrafikKlassifikation> readFromResourceFile() throws Exception {
        List<VagtrafikKlassifikation> foreskriftList = new ArrayList<>();
        InputStream inputStream = null;
        try {
            Class clazz = VagtrafikdefinitionerCrawler.class;
            inputStream = clazz.getResourceAsStream("/vagtrafikdefinitioner.txt");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line = reader.readLine();

                VagtrafikKlassifikation current = new VagtrafikKlassifikation();
                while (line != null) {
                    logger.trace("Läser rad... {}", line);
                    line = reader.readLine();
                    if (line == null) {
                        logger.trace("line is null");
                        continue;
                    }
                    if (line.length() == 0) {
                        foreskriftList.add(current);
                        current = new VagtrafikKlassifikation();
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
                    current.setUnderrubrik(current.getUnderrubrik().concat(" " + a));
                    if (b != null) {
                        current.setText(current.getText().concat(" " + b));
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
                VagtrafikKlassifikationOutputHandler outputHandler = new VagtrafikKlassifikationOutputHandler("./target/vagtrafikdefinintioner");
                outputHandler.writeOutput(this.rot);
            } catch (Exception e) {
                logger.error("Unable to write output of object", e);
            }
        }
    }
}
