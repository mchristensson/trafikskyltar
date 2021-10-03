package org.mac.swe.trafikskyltar.outputhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mac.swe.trafikskyltar.model.Skylt;
import org.mac.swe.trafikskyltar.model.SkyltGrupp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SkyltGruppOutputHandler implements CrawlDataOutputHandler<SkyltGrupp> {

    private static final Logger logger = LoggerFactory.getLogger(SkyltGruppOutputHandler.class);
    private final String domainname;

    public SkyltGruppOutputHandler(String domainname) {
        this.domainname = domainname;
    }

    @Override
    public void writeOutput(SkyltGrupp skyltGrupp) throws IOException {
        Path targetDomainDir = Paths.get(this.getDomainname(), skyltGrupp.getDomainname());
        //Create output base-dir
        this.ensureDirExists(targetDomainDir);

        //Download and save images
        CrawlDataOutputHandler<Skylt> skyltOutputHandler = new SkyltOutputHandler(this.getDomainname(), skyltGrupp.getDomainname());
        for (Skylt s : skyltGrupp.getSkyltar()) {
            logger.debug("Saving primary image...");
            skyltOutputHandler.writeOutput(s);
            if (!s.skyltAlternativa.isEmpty()) {
                for (Skylt alt : s.skyltAlternativa) {
                    logger.debug("Saving alternate image...");
                    skyltOutputHandler.writeOutput(alt);
                }
            }
        }

        logger.info("Saving entity data as JSON...");
        Path path = Paths.get(this.getDomainname(), skyltGrupp.getDomainname(), "data.json");
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(path.toFile(), skyltGrupp);

    }

    @Override
    public String getDomainname() {
        return this.domainname;
    }

}

