package org.mac.swe.trafikskyltar;

import org.mac.swe.trafikskyltar.crawler.FileSupportedCrawler;
import org.mac.swe.trafikskyltar.crawler.HttpClientSupportedCrawler;
import org.mac.swe.trafikskyltar.crawler.VagmarkesforordningCrawler;
import org.mac.swe.trafikskyltar.crawler.VagtrafikdefinitionerCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppC {

    private static final Logger logger = LoggerFactory.getLogger(AppC.class);

    public static void main(String[] args) {
        logger.debug("Begin execution...");

        FileSupportedCrawler crawler = new VagtrafikdefinitionerCrawler();
        try {
            crawler.init();
            crawler.crawl();
            crawler.writeOutput();
        } catch (Exception e) {
            logger.error("error", e);
        } finally {
            crawler.shutdown();
        }


        logger.info("Execution finished.");
        System.exit(0);
    }
}
