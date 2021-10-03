package org.mac.swe.trafikskyltar.outputhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mac.swe.trafikskyltar.crawler.Definition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class VagmarkesforordningOutputHandler implements CrawlDataOutputHandler<Definition> {

    private static final Logger logger = LoggerFactory.getLogger(SkyltGruppOutputHandler.class);
    private final String domainname;

    public VagmarkesforordningOutputHandler(String domainname) {
        this.domainname = domainname;
    }

    @Override
    public void writeOutput(Definition data) throws IOException {
        logger.info("Saving entity data as JSON...");
        //Create output base-dir
        this.ensureDirExists(Paths.get(getDomainname()));

        Path path = Paths.get(this.getDomainname(), "data.json");
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(path.toFile(), data);
    }

    @Override
    public String getDomainname() {
        return this.domainname;
    }
}
