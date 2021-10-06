package org.mac.swe.trafikskyltar.crawler.impl;

import org.mac.swe.trafikskyltar.model.vagmarke.Vagmarke;
import org.mac.swe.trafikskyltar.model.vagmarke.VagmarkesGrupp;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SkyltCrawler {

    private static final Logger logger = LoggerFactory.getLogger(SkyltCrawler.class);

    public void hittaSkyltar(WebDriver webDriver, VagmarkesGrupp parent) throws Exception {
        WebElement vagmarkesGruppMain = webDriver.findElement(By.xpath("//*[@id=\"content-primary\"]/main"));
        WebElement vagmarkesGruppMainRubrik = vagmarkesGruppMain.findElement(By.tagName("h1"));
        logger.debug("Grupp [text={}]", vagmarkesGruppMainRubrik.getText());
        if (!parent.getUnderrubrik().contains(vagmarkesGruppMainRubrik.getText())) {
            logger.error("Hittade inte förväntad rubrik. [expected={}, actual={}]", vagmarkesGruppMainRubrik.getText(), parent.getUnderrubrik());
            throw new Exception("Hittade inte förväntad rubrik '" + vagmarkesGruppMainRubrik.getText() + "'");
        }

        //Beskrivning på grupp
        try {
            WebElement lead = vagmarkesGruppMain.findElement(By.className("lead"));
            parent.setText(lead.getText().trim());
        } catch (NoSuchElementException e) {
            logger.error("Kunde inte hitta beskrivning om grupp {}", vagmarkesGruppMainRubrik.getText());
        }

        //Iterera skyltar
        List<WebElement> subDivs = vagmarkesGruppMain.findElements(By.xpath("//*[@id=\"content-primary\"]/main/div"));
        for (WebElement element : subDivs) {
            WebElement signDiv;
            try {
                signDiv = element.findElement(By.xpath("div"));
            } catch (NoSuchElementException e) {
                continue;
            }

            if (signDiv.getAttribute("class").contains("roadsign")) {
                WebElement signLink = signDiv.findElement(By.tagName("a"));
                String relativeHref = signLink.getAttribute("href");
                WebElement signText = signLink.findElement(By.xpath("span"));
                String label = (signText.getText().replaceAll("\\n", " "));
                Vagmarke vagmarke = new Vagmarke();
                vagmarke.setHref(relativeHref);
                vagmarke.setLabel(label);
                parent.addSkylt(vagmarke);
            }
        }

    }

    public void hittaSkylt(WebDriver webDriver, Vagmarke vagmarke, boolean hittaAlternativa) throws Exception {
        logger.debug("Hämtar data om skylt... [label={}]", vagmarke.getLabel());
        WebElement vagmarkesGruppMain = webDriver.findElement(By.xpath("//*[@id=\"content-primary\"]/main"));
        WebElement vagmarkesGruppMainRubrik = vagmarkesGruppMain.findElement(By.tagName("h1"));

        logger.debug("Skyltdata [rubriktext={}]", vagmarkesGruppMainRubrik.getText());
        if (vagmarke.getLabel() == null) {
            vagmarke.setLabel(vagmarkesGruppMainRubrik.getText());
        } else if (!vagmarke.getLabel().contains(vagmarkesGruppMainRubrik.getText())) {
            throw new Exception("Hittade inte förväntad rubrik. was " + vagmarkesGruppMainRubrik.getText());
        }

        logger.debug("Hämtar beskrivande skyltdata... [label={}]", vagmarke.getLabel());
        try {
            List<WebElement> lead = vagmarkesGruppMain.findElements(By.tagName("span"));
            lead.stream().filter(l -> {
                String s = l.getAttribute("class");
                String i = l.getAttribute("id");
                return (i == null || "".equals(i)) && (s == null || "".equals(s));
            }).findFirst()
                    .ifPresent(l -> vagmarke.setBeskrivning(l.getText().trim()));
        } catch (NoSuchElementException e) {
            logger.error("Kunde inte hitta beskrivande skyltdata. [label={}]", vagmarke.getLabel(), e);
        }

        logger.debug("Hämtar primär bilddata för skylt... [label={}]", vagmarke.getLabel());
        try {
            List<WebElement> filLista =
                    webDriver.findElements(By.xpath("//*[@id=\"FullWidthWithSubmenuContent_FullWidthContent_MainContent_RoadSignFilesPanel\"]/div/a"));
            filLista.stream()
                    .filter(f -> f.getAttribute("href")
                            .endsWith(".png"))
                    .findFirst()
                    .ifPresent(f -> vagmarke.setSkyltBildHref(f.getAttribute("href")));
        } catch (NoSuchElementException e) {
            logger.error("Kunde inte hitta primär bilddata för skylt. [label={}]", vagmarke.getLabel(), e);
        }

        logger.debug("Hämtar alternativ bilddata för skylt... [label={}]", vagmarke.getLabel());
        if (hittaAlternativa) {
            try {
                List<WebElement> filListaAlternativa =
                        webDriver.findElements(By.xpath("//*[@id=\"FullWidthWithSubmenuContent_FullWidthContent_MainContent_AlternativeRoadSignsPanel\"]/div"));
                for (WebElement alt : filListaAlternativa) {
                    Vagmarke vagmarkeAlt = new Vagmarke();
                    try {
                        WebElement skyltAltLinkElement = alt.findElement(By.tagName("a"));
                        vagmarkeAlt.setHref(skyltAltLinkElement.getAttribute("href"));
                        vagmarke.addSkyltAlternativa(vagmarkeAlt);
                    } catch (NoSuchElementException ee) {
                        logger.warn("Specifik alternativ bilddata för skylt kunde inte hämtas. [label={}]", vagmarke.getLabel(), ee);
                    }
                }
            } catch (NoSuchElementException e) {
                logger.warn("Kunde inte hitta alternativ bilddata för skylt. [label={}]", vagmarke.getLabel(), e);
            }
        }
    }
}
