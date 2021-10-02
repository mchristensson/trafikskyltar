package org.mac.swe.trafikskyltar.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.mac.swe.trafikskyltar.model.SkyltGrupp;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class VagmarkesforordningCrawler implements WebDriverSupportedCrawler {

    private static final Logger logger = LoggerFactory.getLogger(TrafikskylteCrawler.class);

    private WebDriver webDriver;
    private final String basUrl = "https://rkrattsbaser.gov.se/sfst?bet=2007:90";
private List<Sektion> paragrafer;
    @Override
    public void init() {
        logger.debug("Initializing chrome webdriver for selenium...");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        this.webDriver = new ChromeDriver(options);
        logger.debug("Webdriver initialization complete.");
    }

    @Override
    public void shutdown() {
        if (this.webDriver != null) {
            logger.debug("Shutting down webdriver...");
            webDriver.quit();
            logger.debug("Webdriver was shut-down.");
        }
    }

    @Override
    public void crawl() throws Exception {
        logger.info("Requesting base web-page...");
        this.webDriver.get(basUrl);

        logger.info("Hittar skyltar i grupp...");
        this.paragrafer = hittaStycke();

        logger.info("Klar.");
    }

    @Override
    public void writeOutput() {
        if (this.paragrafer != null) {
            try {
                logger.info("Saving entity data as JSON...");
                Path path = Paths.get("./target/data", "paragrafer.json");
                ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
                objectMapper.writeValue(path.toFile(), this.paragrafer);
            } catch (Exception e) {
                logger.error("Unable to write output of object", e);
            }
        }

    }

    private final Predicate<String> isAnyFirstFiveSections = s -> s.substring(0,2).matches("[1234]\\s");

    private List<Sektion> hittaStycke() throws Exception {
        Thread.sleep(300);
        WebElement element = this.webDriver.findElement(By.xpath("/html/body/div/div[3]/div/div[2]/div/div[2]/div[2]/div[8]/div"));
        if (element != null) {
            String text = element.getText();
            logger.info("Text lästes ok.");
            String kapitelText = hittaKapitelText(text);
            logger.info("Kapitel lästes ok.");
            List<String> paragrafer = hittaSektioner(kapitelText);

            return paragrafer.stream().filter(isAnyFirstFiveSections.negate()).map(this::skapaSektion).collect(Collectors.toList());

        } else {
            throw new Exception("Kunde inte läsa sidan");
        }
    }

    private Sektion skapaSektion(String s) {
        Sektion sektion = new Sektion();
        try {
            int i = s.indexOf('\n');
            String[] labelParts =  s.substring(0, i).split("§");
            sektion.setCode(labelParts[0].trim());
            sektion.setLabel(labelParts[1].trim());

            //Föreskrifter
            sektion.setForeskriftList(skapaForeskrifter(s.substring(i)));
        } catch (Exception e) {
            logger.error("Kunde inte bearbeta sektion",e);
        }
        return sektion;
    }

    private List<Foreskrift> skapaForeskrifter(String text) {
        List<Foreskrift> foreskriftList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line = reader.readLine();
            Foreskrift current = new Foreskrift();
            while (line != null) {
                line = reader.readLine();
                if (line == null) {
                    logger.warn("line is null");
                    continue;
                }
                if (line.startsWith("E22")) {
                    logger.info("hello");
                }
                if (line.length() == 0) {
                    //Tom rad, ny ROW kommer
                    continue;
                } else if (!line.substring(0,1).isBlank()) {
                    if (line.matches("\\w\\d*\\s.*")) {
                        //New försekrift
                        foreskriftList.add(current);
                        current = new Foreskrift();
                    }

                }
                //Continue on existing
                String[] tryTabSplit = line.split("[\\t]");
                String a;
                String b;
                if (tryTabSplit.length == 2) {
                    a = tryTabSplit[0].trim();
                    b = tryTabSplit[1].trim();
                } else if (line.length() > 31){
                    a = line.substring(0,32);
                    b = line.substring(32);
                } else {
                    a = line;
                    b = null;
                }
                current.setCode(current.getCode().concat(" " + a));
                if (b != null) {
                    current.setBeskrivning(current.getBeskrivning().concat(" " + b));
                }
            }
        } catch (IOException exc) {
            logger.error("Kunde inte tolka data",exc);
        }
        return  foreskriftList;
    }

    /**
     *
     * @param text
     * @return
     * @throws Exception
     */
    private String hittaKapitelText(String text) throws Exception {
      int kapitelStart = text.indexOf(System.lineSeparator() + System.lineSeparator() + "2 kap. Vägmärken och tilläggstavlor");
        int nextKapitelStart = text.indexOf(System.lineSeparator() + System.lineSeparator() +"3 kap. Trafiksignaler");
        if (kapitelStart == -1 || nextKapitelStart == -1) {
            logger.error("Kunde inte hitta kapitel [start={}, end={}]", kapitelStart, nextKapitelStart);
            throw new Exception("Kunde inte hitta distinkt kapitel");
        }
        return text.substring(kapitelStart, nextKapitelStart);
    }

    private List<String> hittaSektioner(String text) {
        List<String> sections = new ArrayList<>();
        int i = 1;
        int sectionStart = 0;
        int nextSectionStart = 0;

        while(sectionStart > -1 ) {
            sectionStart = text.indexOf(System.lineSeparator() + System.lineSeparator() + i + " §");
            nextSectionStart = text.indexOf(System.lineSeparator() + System.lineSeparator() + (i+1) + " §");
            if (sectionStart == -1) {
                break;
            } else if (nextSectionStart == -1) {
                nextSectionStart = text.length();
            }
            sections.add(text.substring(sectionStart, nextSectionStart).trim());

            //Finally
            text = text.substring(sectionStart);
            i++;
        }



        logger.info("Kapitel lästes ok.");
        return sections;
    }

}
