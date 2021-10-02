package org.mac.swe.trafikskyltar;

import org.mac.swe.trafikskyltar.crawler.TrafikskylteCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.debug("Begin execution...");

        TrafikskylteCrawler trafikskylteCrawler = new TrafikskylteCrawler();
        try {
            trafikskylteCrawler.init();
            trafikskylteCrawler.crawl();
            trafikskylteCrawler.writeOutput();
        } catch (Exception e) {
            logger.error("error", e);
        } finally {
            trafikskylteCrawler.shutdown();
        }


        logger.info("Execution finished.");
        System.exit(0);

    }
}
