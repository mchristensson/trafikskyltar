package org.mac.swe.trafikskyltar.crawler;

import org.mac.swe.trafikskyltar.model.Skylt;
import org.mac.swe.trafikskyltar.model.SkyltGrupp;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SkyltCrawler {

    private static final Logger logger = LoggerFactory.getLogger(SkyltCrawler.class);

    void hittaSkyltar(WebDriver webDriver, SkyltGrupp parent) throws Exception {
        WebElement vagmarkesGruppMain = webDriver.findElement(By.xpath("//*[@id=\"content-primary\"]/main"));
        WebElement vagmarkesGruppMainRubrik = vagmarkesGruppMain.findElement(By.tagName("h1"));
        logger.debug("Grupp [text={}]", vagmarkesGruppMainRubrik.getText());
        if (!parent.getLabel().contains(vagmarkesGruppMainRubrik.getText())) {
            logger.error("Hittade inte förväntad rubrik. [expected={}, actual={}]", vagmarkesGruppMainRubrik.getText(), parent.getLabel());
            throw new Exception("Hittade inte förväntad rubrik '" + vagmarkesGruppMainRubrik.getText() + "'");
        }

        //Beskrivning på grupp
        try {
            WebElement lead = vagmarkesGruppMain.findElement(By.className("lead"));
            parent.setBeskrivning(lead.getText().trim());
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
                Skylt skylt = new Skylt();
                skylt.setHref(relativeHref);
                skylt.setLabel(label);
                parent.addSkylt(skylt);
            }
        }

    }

    public void hittaSkylt(WebDriver webDriver, Skylt skylt, boolean hittaAlternativa) throws Exception {
        logger.debug("Hämtar data om skylt... [label={}]", skylt.getLabel());
        WebElement vagmarkesGruppMain = webDriver.findElement(By.xpath("//*[@id=\"content-primary\"]/main"));
        WebElement vagmarkesGruppMainRubrik = vagmarkesGruppMain.findElement(By.tagName("h1"));

        logger.debug("Skyltdata [rubriktext={}]", vagmarkesGruppMainRubrik.getText());
        if (skylt.getLabel() == null) {
            skylt.setLabel(vagmarkesGruppMainRubrik.getText());
        } else if (!skylt.getLabel().contains(vagmarkesGruppMainRubrik.getText())) {
            throw new Exception("Hittade inte förväntad rubrik. was " + vagmarkesGruppMainRubrik.getText());
        }

        logger.debug("Hämtar beskrivande skyltdata... [label={}]", skylt.getLabel());
        try {
            List<WebElement> lead = vagmarkesGruppMain.findElements(By.tagName("span"));
            lead.stream().filter(l -> {
                String s = l.getAttribute("class");
                String i = l.getAttribute("id");
                return (i == null || "".equals(i)) && (s == null || "".equals(s));
            }).findFirst()
                    .ifPresent(l -> skylt.setBeskrivning(l.getText().trim()));
        } catch (NoSuchElementException e) {
            logger.error("Kunde inte hitta beskrivande skyltdata. [label={}]", skylt.getLabel(), e);
        }

        logger.debug("Hämtar primär bilddata för skylt... [label={}]", skylt.getLabel());
        try {
            List<WebElement> filLista =
                    webDriver.findElements(By.xpath("//*[@id=\"FullWidthWithSubmenuContent_FullWidthContent_MainContent_RoadSignFilesPanel\"]/div/a"));
            filLista.stream()
                    .filter(f -> f.getAttribute("href")
                            .endsWith(".png"))
                    .findFirst()
                    .ifPresent(f -> skylt.setSkyltBildHref(f.getAttribute("href")));
        } catch (NoSuchElementException e) {
            logger.error("Kunde inte hitta primär bilddata för skylt. [label={}]", skylt.getLabel(), e);
        }

        logger.debug("Hämtar alternativ bilddata för skylt... [label={}]", skylt.getLabel());
        if (hittaAlternativa) {
            try {
                List<WebElement> filListaAlternativa =
                        webDriver.findElements(By.xpath("//*[@id=\"FullWidthWithSubmenuContent_FullWidthContent_MainContent_AlternativeRoadSignsPanel\"]/div"));
                for (WebElement alt : filListaAlternativa) {
                    Skylt skyltAlt = new Skylt();
                    try {
                        WebElement skyltAltLinkElement = alt.findElement(By.tagName("a"));
                        skyltAlt.setHref(skyltAltLinkElement.getAttribute("href"));
                        skylt.addSkyltAlternativa(skyltAlt);
                    } catch (NoSuchElementException ee) {
                        logger.warn("Specifik alternativ bilddata för skylt kunde inte hämtas. [label={}]", skylt.getLabel(), ee);
                    }
                }
            } catch (NoSuchElementException e) {
                logger.warn("Kunde inte hitta alternativ bilddata för skylt. [label={}]", skylt.getLabel(), e);
            }
        }
    }
}
