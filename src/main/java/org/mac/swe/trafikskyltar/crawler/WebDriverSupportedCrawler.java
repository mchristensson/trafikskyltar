package org.mac.swe.trafikskyltar.crawler;

public interface WebDriverSupportedCrawler {

    void init();

    void shutdown();

    void crawl() throws Exception;

    void writeOutput();
}
