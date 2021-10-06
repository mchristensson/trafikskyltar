package org.mac.swe.trafikskyltar.outputhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mac.swe.trafikskyltar.model.LagtextStycke;
import org.mac.swe.trafikskyltar.model.common.LagtextSektion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LagtextOutputHandler implements CrawlDataOutputHandler<LagtextStycke> {

    private static final Logger logger = LoggerFactory.getLogger(LagtextOutputHandler.class);

    private final String domainname;
    private final  String subdomain;

    public LagtextOutputHandler(String domainname, String subdomain) {
        this.domainname = domainname;
        this.subdomain = subdomain;
    }

    @Override
    public void writeOutput(LagtextStycke data) throws IOException {
        logger.info("Saving entity data as JSON...");
        Path targetDomainDir = Paths.get(getDomainname());
        //Create output base-dir
        this.ensureDirExists(targetDomainDir);

        Path path = Paths.get(getDomainname(), this.subdomain, "lagtexter.json");
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(path.toFile(), data);
    }

    @Override
    public String getDomainname() {
        return this.domainname;
    }
}
