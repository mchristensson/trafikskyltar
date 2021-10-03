package org.mac.swe.trafikskyltar;

import org.mac.swe.trafikskyltar.crawler.HttpClientSupportedCrawler;
import org.mac.swe.trafikskyltar.crawler.VagmarkesforordningCrawler;
import org.mac.swe.trafikskyltar.crawler.VagmarkesforordningCrawler2;
import org.mac.swe.trafikskyltar.crawler.WebDriverSupportedCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppB {

    private static final Logger logger = LoggerFactory.getLogger(AppB.class);

    public static void main(String[] args) {
        logger.debug("Begin execution...");


        HttpClientSupportedCrawler crawler = new VagmarkesforordningCrawler2();
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
