

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class App {

    private WebDriver webDriver;
    private String basUrl = "https://www.transportstyrelsen.se/sv/vagtrafik/Vagmarken";
    private List<SkyltGrupp> skyltgrupper;

    public static void main(String[] args) {
        System.out.println("hello world");
        App app = new App();
        try {

            app.init();
            app.crawl();

            app.writeOutput();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            app.shutdown();
        }

        System.exit(0);
    }

    public void init() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        this.webDriver = new ChromeDriver(options);
    }

    public void shutdown() {
        if (this.webDriver != null) {
            webDriver.quit();
        }

    }

    public void writeOutput() {
      if (  this.skyltgrupper != null) {
          try {
              for (SkyltGrupp s: this.skyltgrupper
                   ) {
                  writeOutput(s);
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
    }
    private void writeOutput(SkyltGrupp skyltGrupp) throws IOException {
        Path targetDomainDir = Paths.get("./target/data", skyltGrupp.getDomainname());
        //Skapa domaänkatalog
        File targetDomainDirFile = targetDomainDir.toFile();
        if (!targetDomainDirFile.exists()) {
            targetDomainDirFile.mkdirs();
        }

        //Ladda ner bilder
        for (Skylt s: skyltGrupp.getSkyltar()
             ) {
            saveSkyltImage(skyltGrupp, s);
            if (!s.skyltAlternativa.isEmpty()) {
                for (Skylt alt: s.skyltAlternativa
                     ) {
                    saveSkyltImage(skyltGrupp, alt);
                }
            }
        }

        //Skriv en JSON-fil...
        Path path = Paths.get("./target/data", skyltGrupp.getDomainname(), "data.json");
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(path.toFile(), skyltGrupp);

    }

    private void saveSkyltImage(SkyltGrupp skyltGrupp, Skylt s) throws IOException {
        String href = s.getSkyltBildHref();
        if (href == null ) {
            System.out.println("Skylt saknas: " + s.toString());
            return;
        }
        int i = href.lastIndexOf('/');
        if (i == -1 ) {
            return;
        }
        String filename = href.substring(i);

        URL url = new URL(s.getSkyltBildHref());
        BufferedImage img = ImageIO.read(url);
        File file = Paths.get("./target/data", skyltGrupp.getDomainname(), filename).toFile();
        s.setDomainpath(skyltGrupp.getDomainname() + filename);
        ImageIO.write(img, "png", file);
    }

    private String domainnameResolver(String url) {
        String result = url.substring(basUrl.length());
        if (result.length() > 0) {
            Optional<String> q = Arrays.stream(Arrays.copyOfRange(result.split("/"), 1, 2)).findFirst();
            if (q.isPresent()) {
                return q.get().toLowerCase();
            }
        }
        return "missingdomain";
    }

    private void crawl() throws Exception {
        this.webDriver.get(basUrl);

        List<SkyltGrupp> skyltgrupper = hittaSkyltgrupper(0);
        System.out.println("Hittar skyltar i grupp...");
        skyltgrupper.forEach(this::hittaSkyltarIGrupp);
        System.out.println("Lägger till data om skyltarna i respektive grupp...");
        skyltgrupper.forEach(this::hittaSkyltarData);
        ////*[@id="content-primary"]/main/span[2]
        skyltgrupper.forEach(this::hittaSkyltarAlternativaData);
        System.out.println("aklrt");
        this.skyltgrupper = skyltgrupper;
    }

    private void hittaSkyltarData(SkyltGrupp s) {
        s.getSkyltar().forEach(skylt -> hittaSkyltData(skylt, true));
    }

    private void hittaSkyltarAlternativaData(SkyltGrupp s) {
        s.getSkyltar().forEach(this::hittaSkyltAlternativaData);
    }

    private void hittaSkyltAlternativaData(Skylt s) {
        try {
            Thread.sleep(300);
            this.webDriver.get(s.getHref());
            Thread.sleep(150);
            if (!s.getSkyltAlternativa().isEmpty()) {
                s.getSkyltAlternativa().forEach(skylt -> hittaSkyltData(skylt, false));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hittaSkyltData(Skylt s, boolean hittaAlternativa) {
        try {
            Thread.sleep(300);
            this.webDriver.get(s.getHref());
            Thread.sleep(150);
            hittaSkylt(s, hittaAlternativa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hittaSkyltarIGrupp(SkyltGrupp s) {
        try {
            Thread.sleep(300);
            this.webDriver.get(s.getHref());
            Thread.sleep(150);
            hittaSkyltar(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hittaSkylt(Skylt skylt, boolean hittaAlternativa) throws Exception {
        System.out.println("(1) Undersöker skylt " + skylt.getLabel());
        WebElement vagmarkesGruppMain = webDriver.findElement(By.xpath("//*[@id=\"content-primary\"]/main"));
        WebElement vagmarkesGruppMainRubrik = vagmarkesGruppMain.findElement(By.tagName("h1"));
        System.out.println("Text: " + vagmarkesGruppMainRubrik.getText());
        if (skylt.getLabel() == null) {
            skylt.setLabel(vagmarkesGruppMainRubrik.getText());
        } else if (!skylt.getLabel().contains(vagmarkesGruppMainRubrik.getText())) {
            throw new Exception("Hittade inte förväntad rubrik. was " + vagmarkesGruppMainRubrik.getText());
        }
        System.out.println("(2) Undersöker skylt " + skylt.getLabel());
        //Beskrivning på skylt
        try {
            // //*[@id="content-primary"]/main/span[2]
            List<WebElement> lead = vagmarkesGruppMain.findElements(By.tagName("span"));
            lead.stream().filter(l -> {
                String s = l.getAttribute("class");
                String i = l.getAttribute("id");
                return (i == null || "".equals(i)) && (s == null || "".equals(s));
            }).findFirst()
                    .ifPresent(l -> skylt.setBeskrivning(l.getText().trim()));
        } catch (NoSuchElementException e) {

        }
        System.out.println("(3) Undersöker skylt " + skylt.getLabel());
        //Bildlänkar ordinarie
        try {
            List<WebElement> filLista =
                    webDriver.findElements(By.xpath("//*[@id=\"FullWidthWithSubmenuContent_FullWidthContent_MainContent_RoadSignFilesPanel\"]/div/a"));
            filLista.stream()
                    .filter(f -> f.getAttribute("href")
                            .endsWith(".png"))
                    .findFirst()
                    .ifPresent(f -> skylt.setSkyltBildHref(f.getAttribute("href")));
        } catch (NoSuchElementException e) {
            System.err.println("Kunde inte hitta filer för skylt");
        }

        //Bildlänkar alternativa
        ////*[@id="FullWidthWithSubmenuContent_FullWidthContent_MainContent_AlternativeRoadSignsPanel"]/div
        if (hittaAlternativa) {
            try {
                ////*[@id="FullWidthWithSubmenuContent_FullWidthContent_MainContent_AlternativeRoadSignsPanel"]/div[7]/a
                List<WebElement> filListaAlternativa =
                        webDriver.findElements(By.xpath("//*[@id=\"FullWidthWithSubmenuContent_FullWidthContent_MainContent_AlternativeRoadSignsPanel\"]/div"));
                for (WebElement alt : filListaAlternativa) {
                    Skylt skyltAlt = new Skylt();
                    try {
                        WebElement skyltAltLinkElement = alt.findElement(By.tagName("a"));
                        skyltAlt.setHref(skyltAltLinkElement.getAttribute("href"));
                        skylt.addSkyltAlternativa(skyltAlt);
                    } catch (NoSuchElementException ee) {

                    }
                }
            } catch (NoSuchElementException e) {
                System.err.println("Kunde inte hitta filer för skylt");
            }
        }
    }

    private void hittaSkyltar(SkyltGrupp parent) throws Exception {
        WebElement vagmarkesGruppMain = webDriver.findElement(By.xpath("//*[@id=\"content-primary\"]/main"));
        WebElement vagmarkesGruppMainRubrik = vagmarkesGruppMain.findElement(By.tagName("h1"));
        System.out.println("Text: " + vagmarkesGruppMainRubrik.getText());
        if (!parent.getLabel().contains(vagmarkesGruppMainRubrik.getText())) {
            throw new Exception("Hittade inte förväntad rubrik. was " + vagmarkesGruppMainRubrik.getText());
        }

        //Beskrivning på grupp
        try {
            WebElement lead = vagmarkesGruppMain.findElement(By.className("lead"));
            parent.setBeskrivning(lead.getText().trim());
        } catch (NoSuchElementException e) {

        }

        //Iterera skyltar
        List<WebElement> subDivs = vagmarkesGruppMain.findElements(By.xpath("//*[@id=\"content-primary\"]/main/div"));
        for (WebElement element : subDivs) {
            WebElement signDiv;
            try {
                signDiv = element.findElement(By.xpath("div"));
            } catch (NoSuchElementException e) {
                continue;
            }

            if (signDiv.getAttribute("class").contains("roadsign")) {
                WebElement signLink = signDiv.findElement(By.tagName("a"));
                String relativeHref = signLink.getAttribute("href");
                WebElement signText = signLink.findElement(By.xpath("span"));
                String label = (signText.getText().replaceAll("\\n", " "));
                Skylt skylt = new Skylt();
                skylt.setHref(relativeHref);
                skylt.setLabel(label);
                parent.addSkylt(skylt);
            }
        }

    }

    private List<SkyltGrupp> hittaSkyltgrupper(int limit) throws Exception {
        List<SkyltGrupp> skyltgrupper = new ArrayList<>();
        WebElement vagmarkesGruppMain = webDriver.findElement(By.xpath("//*[@id=\"content-primary\"]/main"));
        WebElement vagmarkesGruppMainRubrik = vagmarkesGruppMain.findElement(By.tagName("h1"));
        System.out.println("Text: " + vagmarkesGruppMainRubrik.getText());
        if (!vagmarkesGruppMainRubrik.getText().equals("Vägmärken")) {
            throw new Exception("Hittade inte förväntad rubrik");
        }
        ///html/body/form/div[3]/div[1]/div[2]/div/div[1]/main/div[4]/div

        //Antal skyltgrupper
        String navMainVagmarkenElement = "//*[@id=\"nav-main\"]/ul[2]/li[12]/a";
        Thread.sleep(200);
        WebElement vagmarkenElement = this.webDriver.findElement(By.xpath(navMainVagmarkenElement));
        List<WebElement> vagmarkesGrupperMenuItems = vagmarkenElement.findElements(By.xpath("following-sibling::ul/li/a"));
        int antalGrupper = vagmarkesGrupperMenuItems.size();
        System.out.println("Antal grupper: " + antalGrupper);

        int groupCounter = 0;
        int index = 0;
        while (groupCounter < antalGrupper) {
            WebElement subDiv = vagmarkesGruppMain.findElement(By.xpath("//*[@id=\"content-primary\"]/main/div[" + (index + 4) + "]"));
            index++;
            WebElement signDiv;
            try {
                signDiv = subDiv.findElement(By.xpath("div"));
                groupCounter++;
            } catch (NoSuchElementException e) {
                continue;
            }

            WebElement signLink = signDiv.findElement(By.tagName("a"));
            String relativeHref = signLink.getAttribute("href");
            WebElement signText = signLink.findElement(By.xpath("span"));
            String label = (signText.getText().replaceAll("\\n", " "));
            SkyltGrupp skyltGrupp = new SkyltGrupp();
            skyltGrupp.setHref(relativeHref);
            skyltGrupp.setDomainname(domainnameResolver(relativeHref));
            skyltGrupp.setLabel(label);
            skyltgrupper.add(skyltGrupp);
            if (signText.getText().contains("Y.")) {
                break;
            }
            if (limit > 0 && index == limit) {
                System.out.println("Limit '" + limit + "' reached");
                break;
            }
        }
        return skyltgrupper;
    }

}
