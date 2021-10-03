package org.mac.swe.trafikskyltar.crawler;

public interface InterfaceSupportedCrawler {
    void init();

    void shutdown();

    void crawl() throws Exception;

    void writeOutput();
}
