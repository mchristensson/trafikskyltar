package org.mac.swe.trafikskyltar.crawler.impl;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.mac.swe.trafikskyltar.crawler.WebDriverSupportedCrawler;
import org.mac.swe.trafikskyltar.model.vagmarke.Vagmarke;
import org.mac.swe.trafikskyltar.model.vagmarke.VagmarkesGrupp;
import org.mac.swe.trafikskyltar.outputhandler.CrawlDataOutputHandler;
import org.mac.swe.trafikskyltar.outputhandler.VagmarkesGruppImageOutputHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TrafikskylteCrawler implements WebDriverSupportedCrawler {

    private static final Logger logger = LoggerFactory.getLogger(TrafikskylteCrawler.class);

    private WebDriver webDriver;
    private final String basUrl = "https://www.transportstyrelsen.se/sv/vagtrafik/Vagmarken";
    private List<VagmarkesGrupp> skyltgrupper;

    private final Function<String, String> domainResolver = url -> {
        String result = url.substring(basUrl.length());
        if (result.length() > 0) {
            Optional<String> q = Arrays.stream(Arrays.copyOfRange(result.split("/"), 1, 2)).findFirst();
            if (q.isPresent()) {
                return q.get().toLowerCase();
            }
        }
        return "missingdomain";
    };

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
    public void writeOutput() {
        CrawlDataOutputHandler<VagmarkesGrupp> outputHandler = new VagmarkesGruppImageOutputHandler("./target/vagmarken");
        if (this.skyltgrupper != null) {
            try {
                for (VagmarkesGrupp s : this.skyltgrupper) {
                    outputHandler.writeOutput(s);
                }
            } catch (Exception e) {
                logger.error("Unable to write output of object", e);
            }
        }
    }


    public void crawl() throws Exception {
        logger.info("Requesting base web-page...");
        this.webDriver.get(basUrl);

        List<VagmarkesGrupp> skyltgrupper = hittaSkyltgrupper(0);
        logger.info("Hittar skyltar i grupp...");
        skyltgrupper.forEach(this::hittaSkyltar);
        logger.info("L??gger till data om skyltarna i respektive grupp...");
        skyltgrupper.forEach(s -> s.getSkyltar().forEach(skylt -> hittaSkyltData(skylt, true)));
        logger.info("L??gger till alternativbilddata om skyltarna i respektive grupp...");
        skyltgrupper.forEach(s -> s.getSkyltar().forEach(this::hittaSkyltAlternativaData));

        this.skyltgrupper = skyltgrupper;
    }

    /**
     * H??mtar alternativa data om skyltar
     *
     * @param vagmarke Skylt
     */
    private void hittaSkyltAlternativaData(Vagmarke vagmarke) {
        try {
            Thread.sleep(300);
            logger.trace("Requesting web-page... [url={}]", vagmarke.getHref());
            this.webDriver.get(vagmarke.getHref());
            Thread.sleep(150);
            if (!vagmarke.getSkyltAlternativa().isEmpty()) {
                vagmarke.getSkyltAlternativa().forEach(s -> hittaSkyltData(s, false));
            }
        } catch (Exception e) {
            logger.error("Kunde inte bearbeta alternativa skyltar", e);
        }
    }

    /**
     * H??mtar data om skyltar
     *
     * @param vagmarke            Skylt
     * @param hittaAlternativa om altarnativ data ska h??mtas eller inte
     */
    private void hittaSkyltData(Vagmarke vagmarke, boolean hittaAlternativa) {
        SkyltCrawler skyltCrawler = new SkyltCrawler();
        try {
            Thread.sleep(300);
            logger.trace("Requesting web-page... [url={}]", vagmarke.getHref());
            this.webDriver.get(vagmarke.getHref());
            Thread.sleep(150);
            skyltCrawler.hittaSkylt(webDriver, vagmarke, hittaAlternativa);
        } catch (Exception e) {
            logger.error("Kunde inte bearbeta skylt", e);
        }
    }

    private void hittaSkyltar(VagmarkesGrupp vagmarkesGrupp) {
        SkyltCrawler skyltCrawler = new SkyltCrawler();
        try {
            Thread.sleep(300);
            logger.trace("Requesting web-page... [url={}]", vagmarkesGrupp.getHref());
            this.webDriver.get(vagmarkesGrupp.getHref());
            Thread.sleep(150);
            skyltCrawler.hittaSkyltar(webDriver, vagmarkesGrupp);
        } catch (Exception e) {
            logger.error("Kunde inte h??mta de skyltar som ing??r i gruppen [grupp={}]", vagmarkesGrupp, e);
        }
    }


    /**
     * Identifierar och samlar ihop de tillg??ngliga skyltgrupper som ska analyseras
     *
     * @param limit Konfigurationsparameter f??r att begr??nsa maximalt antal skyltgrupper som ska analyseras
     * @return Lista med skyltgrupper
     * @throws Exception Om listan som inneh??ller de skyltgrupper vi letar efter inte kunde hittas
     */
    private List<VagmarkesGrupp> hittaSkyltgrupper(int limit) throws Exception {
        List<VagmarkesGrupp> skyltgrupper = new ArrayList<>();
        WebElement vagmarkesGruppMain = webDriver.findElement(By.xpath("//*[@id=\"content-primary\"]/main"));
        WebElement vagmarkesGruppMainRubrik = vagmarkesGruppMain.findElement(By.tagName("h1"));
        logger.debug("Kontrollerar gruppnamn... [namn={}]", vagmarkesGruppMainRubrik.getText());
        if (!vagmarkesGruppMainRubrik.getText().equals("V??gm??rken")) {
            throw new Exception("Hittade inte f??rv??ntad rubrik");
        }

        //Antal skyltgrupper
        String navMainVagmarkenElement = "//*[@id=\"nav-main\"]/ul[2]/li[12]/a";
        Thread.sleep(200);
        WebElement vagmarkenElement = this.webDriver.findElement(By.xpath(navMainVagmarkenElement));
        List<WebElement> vagmarkesGrupperMenuItems = vagmarkenElement.findElements(By.xpath("following-sibling::ul/li/a"));
        int antalGrupper = vagmarkesGrupperMenuItems.size();
        logger.debug("Antal hittade skyltgrupper: {}", antalGrupper);

        logger.debug("Samlar in data om skyltgrupper...");
        int groupCounter = 0;
        int index = 0;
        while (groupCounter < antalGrupper) {
            WebElement subDiv = vagmarkesGruppMain.findElement(By.xpath("//*[@id=\"content-primary\"]/main/div[" + (index + 4) + "]"));
            index++;
            WebElement signDiv;
            try {
                signDiv = subDiv.findElement(By.xpath("div"));
                groupCounter++;
            } catch (NoSuchElementException e) {
                logger.warn("Kunde inte bearbeta grupp");
                continue;
            }

            WebElement signLink = signDiv.findElement(By.tagName("a"));
            String relativeHref = signLink.getAttribute("href");
            WebElement signText = signLink.findElement(By.xpath("span"));
            String label = (signText.getText().replaceAll("\\n", " "));
            VagmarkesGrupp vagmarkesGrupp = new VagmarkesGrupp();
            vagmarkesGrupp.setHref(relativeHref);
            vagmarkesGrupp.setDomainname(domainResolver.apply(relativeHref));
            vagmarkesGrupp.setUnderrubrik(label);
            skyltgrupper.add(vagmarkesGrupp);
            if (signText.getText().contains("Y.")) {
                break;
            }
            if (limit > 0 && index == limit) {
                logger.warn("Limit '{}' reached", limit);
                break;
            }
        }
        logger.info("Data om '{}' skyltgrupper insamlat.", skyltgrupper.size());
        return skyltgrupper;
    }

}
