package org.mac.swe.trafikskyltar;

import org.mac.swe.trafikskyltar.crawler.InterfaceSupportedCrawler;
import org.mac.swe.trafikskyltar.crawler.impl.TrafikskylteCrawler;
import org.mac.swe.trafikskyltar.crawler.impl.VagmarkesforordningCrawler;
import org.mac.swe.trafikskyltar.crawler.impl.VagtrafikdefinitionerCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.debug("Begin execution...");

        List<InterfaceSupportedCrawler> crawlers = new ArrayList<>();
        crawlers.add(new TrafikskylteCrawler());
        crawlers.add(new VagmarkesforordningCrawler());
        crawlers.add(new VagtrafikdefinitionerCrawler());

        runEach(crawlers.stream());


        logger.info("Execution finished.");
        System.exit(0);

    }

    private static void runEach(Stream<InterfaceSupportedCrawler> crawlers) {
        crawlers.forEach(crawler -> {
            try {
                crawler.init();
                crawler.crawl();
                crawler.writeOutput();
            } catch (Exception e) {
                logger.error("error", e);
            } finally {
                crawler.shutdown();
            }
        });
    }
}
