package org.mac.swe.trafikskyltar.outputhandler;

import java.io.IOException;

public interface CrawlDataOutputHandler<T> {

    /**
     * Store data to some output
     *
     * @param data
     * @throws IOException
     */
    void writeOutput(T data) throws IOException;

    /**
     * Provides some domain-identifier for the entry
     *
     * @return domain-identifier
     */
    String getDomainname();

}
