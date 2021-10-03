package org.mac.swe.trafikskyltar.outputhandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

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

    default void ensureDirExists(Path p) throws IOException {
        File targetDomainDirFile = p.toFile();
        if (!targetDomainDirFile.exists()) {
            targetDomainDirFile.mkdirs();
        }
    }
}
