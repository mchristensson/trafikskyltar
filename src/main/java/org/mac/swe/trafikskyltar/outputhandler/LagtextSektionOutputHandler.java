package org.mac.swe.trafikskyltar.outputhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mac.swe.trafikskyltar.model.common.LagtextSektion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LagtextSektionOutputHandler implements CrawlDataOutputHandler<List<LagtextSektion>> {

    private static final Logger logger = LoggerFactory.getLogger(LagtextSektionOutputHandler.class);

    private final String domainname;

    public LagtextSektionOutputHandler(String domainname) {
        this.domainname = domainname;
    }

    @Override
    public void writeOutput(List<LagtextSektion> data) throws IOException {
        logger.info("Saving entity data as JSON...");
        Path targetDomainDir = Paths.get(getDomainname());
        //Create output base-dir
        this.ensureDirExists(targetDomainDir);

        Path path = Paths.get(getDomainname(), "lagtexter.json");
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(path.toFile(), data);
    }

    @Override
    public String getDomainname() {
        return this.domainname;
    }
}
