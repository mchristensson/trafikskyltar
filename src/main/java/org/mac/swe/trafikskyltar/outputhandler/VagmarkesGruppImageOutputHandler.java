package org.mac.swe.trafikskyltar.outputhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mac.swe.trafikskyltar.model.LagtextStycke;
import org.mac.swe.trafikskyltar.model.vagmarke.Vagmarke;
import org.mac.swe.trafikskyltar.model.vagmarke.VagmarkesGrupp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VagmarkesGruppImageOutputHandler implements CrawlDataOutputHandler<VagmarkesGrupp> {

    private static final Logger logger = LoggerFactory.getLogger(VagmarkesGruppImageOutputHandler.class);
    private final String domainname;

    public VagmarkesGruppImageOutputHandler(String domainname) {
        this.domainname = domainname;
    }

    @Override
    public void writeOutput(VagmarkesGrupp vagmarkesGrupp) throws IOException {
        Path targetDomainDir = Paths.get(this.getDomainname(), vagmarkesGrupp.getDomainname());
        //Create output base-dir
        this.ensureDirExists(targetDomainDir);

        //Download and save images
        CrawlDataOutputHandler<Vagmarke> skyltOutputHandler = new VagmarkesImageOutputHandler(this.getDomainname(), vagmarkesGrupp.getDomainname());
        for (Vagmarke s : vagmarkesGrupp.getSkyltar()) {
            logger.debug("Saving primary image...");
            skyltOutputHandler.writeOutput(s);
            if (!s.vagmarkeAlternativa.isEmpty()) {
                for (Vagmarke alt : s.vagmarkeAlternativa) {
                    logger.debug("Saving alternate image...");
                    skyltOutputHandler.writeOutput(alt);
                }
            }
        }

        CrawlDataOutputHandler<LagtextStycke> outputHandler = new LagtextOutputHandler(this.getDomainname(), vagmarkesGrupp.getDomainname());
        outputHandler.writeOutput(vagmarkesGrupp);

        /*
        logger.info("Saving entity data as JSON...");
        Path path = Paths.get(this.getDomainname(), vagmarkesGrupp.getDomainname(), "data.json");
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(path.toFile(), vagmarkesGrupp);

         */

    }

    @Override
    public String getDomainname() {
        return this.domainname;
    }

}

