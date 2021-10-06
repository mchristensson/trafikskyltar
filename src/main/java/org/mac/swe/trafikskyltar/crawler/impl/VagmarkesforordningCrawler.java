package org.mac.swe.trafikskyltar.crawler.impl;

import org.apache.commons.text.StringEscapeUtils;
import org.mac.swe.trafikskyltar.model.LagtextStycke;
import org.mac.swe.trafikskyltar.model.vagmarke.VagmarkesforordningSkyltBeskrivning;
import org.mac.swe.trafikskyltar.httpclient.HttpClientInvoker;
import org.mac.swe.trafikskyltar.crawler.HttpClientSupportedCrawler;
import org.mac.swe.trafikskyltar.model.common.LagtextSektion;
import org.mac.swe.trafikskyltar.outputhandler.LagtextSektionOutputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class VagmarkesforordningCrawler implements HttpClientSupportedCrawler {

    private static final Logger logger = LoggerFactory.getLogger(TrafikskylteCrawler.class);

    private final String basUrl = "https://rkrattsbaser.gov.se/sfst?bet=2007:90";
    private List<LagtextSektion> paragrafer;
    private HttpClientInvoker httpClientInvoker;

    @Override
    public void init() {
        logger.debug("Initializing...");
        this.httpClientInvoker = new HttpClientInvoker();
        logger.debug("Initialization complete.");
    }

    @Override
    public void shutdown() {
        logger.debug("Shutting down webdriver...");
        logger.debug("Shut-down complete.");
    }

    @Override
    public void crawl() throws Exception {
        logger.info("Requesting base web-page...");

        logger.info("Hämtar och Tolkar text...");
        this.paragrafer = hittaStycke();

        logger.info("Klar.");
    }

    @Override
    public void writeOutput() {
        if (this.paragrafer != null) {
            LagtextSektionOutputHandler outputHandler = new LagtextSektionOutputHandler("./target/vagmarkesforordning");
            try {
                outputHandler.writeOutput(this.paragrafer);
            } catch (Exception e) {
                logger.error("Unable to write output of object", e);
            }
        }

    }

    private final Function<String, Node> nodeResolver = data -> {
        String unescaped = StringEscapeUtils.unescapeHtml4(data);

        int bodyStart = unescaped.indexOf("<body>");
        int bodyEnd = unescaped.indexOf("</body>") + 7;

        unescaped = unescaped.substring(bodyStart, bodyEnd);


        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setNamespaceAware(true);
        logger.info(unescaped);

        try {
            // parse XML
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(unescaped));
            Document doc = builder.parse(is);
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/body/div/div[3]/div/div[2]/div/div[2]/div[2]/div[8]/div";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            logger.info("Antal noder: {}", nodeList.getLength());
            Node node = nodeList.item(0);

            return node;
        } catch (Exception e) {
            logger.error("Kunde inte läsa datat", e);
            throw new RuntimeException(e);
        }
    };

    private List<LagtextSektion> hittaStycke() throws Exception {
        Thread.sleep(300);
        //WebElement element = this.webDriver.findElement(By.xpath("/html/body/div/div[3]/div/div[2]/div/div[2]/div[2]/div[8]/div"));
        Node element = this.httpClientInvoker.get(basUrl, this.nodeResolver);
        if (element != null) {
            String text = element.getFirstChild().getNodeValue();
            logger.info("Text lästes ok.");
            String kapitelText = hittaKapitelText(text);
            logger.info("Kapitel lästes ok.");
            List<String> paragrafer = hittaSektioner(kapitelText);

            return paragrafer.stream().filter(isAnyFirstFiveSections.negate()).map(this::skapaSektion).collect(Collectors.toList());

        } else {
            throw new Exception("Kunde inte läsa sidan");
        }
    }


    private final Predicate<String> isAnyFirstFiveSections = s -> s.substring(0, 2).matches("[1234]\\s");


    private LagtextSektion skapaSektion(String s) {
        LagtextSektion lagtextSektion = new LagtextSektion();
        try {
            int i = s.indexOf('\n');
            String[] labelParts = s.substring(0, i).split("§");
            lagtextSektion.setCode(labelParts[0].trim());
            lagtextSektion.setUnderrubrik(labelParts[1].trim());

            //Föreskrifter
            lagtextSektion.setLagtextStyckeList(skapaForeskrifter(s.substring(i)));
        } catch (Exception e) {
            logger.error("Kunde inte bearbeta sektion", e);
        }
        return lagtextSektion;
    }

    private List<LagtextStycke> skapaForeskrifter(String text) {
        List<LagtextStycke> vagmarkesforordningSkyltBeskrivningList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line = reader.readLine();
            VagmarkesforordningSkyltBeskrivning current = new VagmarkesforordningSkyltBeskrivning();
            while (line != null) {
                line = reader.readLine();
                if (line == null) {
                    logger.trace("line is null");
                    continue;
                }
                if (line.length() == 0) {
                    continue;
                } else if (!line.substring(0, 1).isBlank()) {
                    if (line.matches("\\w\\d*\\s.*")) {
                        vagmarkesforordningSkyltBeskrivningList.add(current);
                        current = new VagmarkesforordningSkyltBeskrivning();
                    }

                }

                //Continue on existing
                String[] tryTabSplit = line.split("[\\t]");
                String a;
                String b;
                if (tryTabSplit.length > 1) {
                    a = tryTabSplit[0].trim();
                    b = tryTabSplit[tryTabSplit.length - 1].trim();
                } else if (line.length() > 31) {
                    a = line.substring(0, 32);
                    b = line.substring(32);
                } else {
                    a = line;
                    b = null;
                }
                current.setUnderrubrik(current.getUnderrubrik().concat(" " + a));
                if (b != null) {
                    current.setText(current.getText().concat(" " + b));
                }
            }
        } catch (IOException exc) {
            logger.error("Kunde inte tolka data", exc);
        }
        return vagmarkesforordningSkyltBeskrivningList;
    }

    /**
     * @param text
     * @return
     * @throws Exception
     */
    private String hittaKapitelText(String text) throws Exception {
        int kapitelStart = text.indexOf(System.lineSeparator() + System.lineSeparator() + "2 kap. Vägmärken och tilläggstavlor");
        int nextKapitelStart = text.indexOf(System.lineSeparator() + System.lineSeparator() + "3 kap. Trafiksignaler");
        if (kapitelStart == -1 || nextKapitelStart == -1) {
            logger.error("Kunde inte hitta kapitel [start={}, end={}]", kapitelStart, nextKapitelStart);
            throw new Exception("Kunde inte hitta distinkt kapitel");
        }
        return text.substring(kapitelStart, nextKapitelStart);
    }

    private List<String> hittaSektioner(String text) {
        List<String> sections = new ArrayList<>();
        int i = 1;
        int sectionStart = 0;
        int nextSectionStart = 0;

        while (sectionStart > -1) {
            sectionStart = text.indexOf(System.lineSeparator() + System.lineSeparator() + i + " §");
            nextSectionStart = text.indexOf(System.lineSeparator() + System.lineSeparator() + (i + 1) + " §");
            if (sectionStart == -1) {
                break;
            } else if (nextSectionStart == -1) {
                nextSectionStart = text.length();
            }
            sections.add(text.substring(sectionStart, nextSectionStart).trim());

            //Finally
            text = text.substring(sectionStart);
            i++;
        }


        logger.info("Kapitel lästes ok.");
        return sections;
    }

}
