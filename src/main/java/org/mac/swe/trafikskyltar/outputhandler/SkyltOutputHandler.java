package org.mac.swe.trafikskyltar.outputhandler;

import org.mac.swe.trafikskyltar.model.Skylt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Downloads and saves image
 */
public class SkyltOutputHandler implements CrawlDataOutputHandler<Skylt> {

    private static final Logger logger = LoggerFactory.getLogger(SkyltOutputHandler.class);
    private final String domainname;
    private final String contextname;

    public SkyltOutputHandler(String contextname, String domainname) {
        this.contextname = contextname;
        this.domainname = domainname;
    }

    @Override
    public void writeOutput(Skylt s) throws IOException {
        String href = s.getSkyltBildHref();
        if (href == null) {
            logger.warn("Image href not present on entry [entry={}]", s);
            return;
        }
        int i = href.lastIndexOf('/');
        if (i == -1) {
            return;
        }
        String filename = href.substring(i);

        URL url = new URL(s.getSkyltBildHref());
        BufferedImage img = ImageIO.read(url);
        Path targetDomainDir = Paths.get(contextname, getDomainname());
        //Create output base-dir
        this.ensureDirExists(targetDomainDir);
        File file = Paths.get(contextname, getDomainname(), filename).toFile();

        s.setDomainpath(getDomainname() + filename);
        logger.debug("Sparar fil.. [file={}, skylt={}]", file.getPath(), s);
        ImageIO.write(img, "png", file);
    }

    @Override
    public String getDomainname() {
        return this.domainname;
    }
}
