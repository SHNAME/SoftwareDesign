package studio.aroundhub.opensourceproject.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class CafeteriaService {

    private static final String BASE_URL = "https://www.yu.ac.kr";
    private static final String VIEW_URL_FORMAT = BASE_URL + "/_attach/f/view.jsp?attach_no=";
    private static final String TARGET_URL = BASE_URL + "/main/life/cafeteria-menu.do?mode=list&&articleLimit=10&article.offset=0";

    public Map<String, String> getCafeteria() throws IOException {
        Map<String, String> previewLinks = extractLatestPdfPreviewLinks();
        if (previewLinks.isEmpty()) {
            log.error(" 최신 PDF 미리보기 페이지를 찾을 수 없습니다.");
            return Collections.emptyMap();
        }

        Map<String, String> images = new HashMap<>();

        for (Map.Entry<String, String> entry : previewLinks.entrySet()) {
            String previewUrl = entry.getValue();
            log.info(" 미리보기 페이지 접근 중: {}", previewUrl);

            String imageUrl = extractMenuImageUrl(previewUrl);
            if (imageUrl != null) {
                byte[] imageData = downloadImage(imageUrl);
                if (imageData != null) {
                    images.put(entry.getKey(), encodeToBase64(imageData));
                }
            } else {
                log.error("Not find Image URL.", entry.getKey());
            }
        }

        log.info("총 이미지 개수: {}", images.size());
        return images;
    }

    private Map<String, String> extractLatestPdfPreviewLinks() {
        Map<String, String> previewLinks = new HashMap<>();

        try {
            Document doc = Jsoup.connect(TARGET_URL)
                    .userAgent("Mozilla/5.0")
                    .get();

            Elements rows = doc.select("tbody tr:not([style*='display: none'])");

            for (Element row : rows) {
                Elements pdfLinksElements = row.select("a.b-file-preview");

                for (Element link : pdfLinksElements) {
                    String title = link.attr("title").toLowerCase().trim();
                    String href = link.attr("href");

                    String attachNo = extractAttachNo(href);
                    if (attachNo == null) continue;

                    String previewUrl = VIEW_URL_FORMAT + attachNo;

                    if (title.contains("자연계") && !previewLinks.containsKey("자연계식당")) {
                        previewLinks.put("natural", previewUrl);
                    }
                    if (title.contains("학생회관") && !previewLinks.containsKey("학생회관")) {
                        previewLinks.put("student", previewUrl);
                    }

                    if (previewLinks.size() >= 2) break;
                }
                if (previewLinks.size() >= 2) break;
            }
        } catch (Exception e) {
            log.error(" 페이지 URL을 가져오는 중 오류 발생", e);
        }
        return previewLinks;
    }

    private String extractAttachNo(String url) {
        if (url.contains("attachNo=")) {
            return url.substring(url.indexOf("attachNo=") + 9);
        }
        return null;
    }

    private String extractMenuImageUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String scriptContent = doc.select("script").html();
        String gDocname = null;

        for (String line : scriptContent.split("\n")) {
            if (line.contains("g_docname")) {
                gDocname = line.split("=")[1].trim().replace("'", "").replace(";", "");
                break;
            }
        }

        if (gDocname == null) {
            log.error("값을 찾을 수 없습니다!");
            return null;
        }

        return BASE_URL + "/_attach/flexer" + gDocname + ".files/00001.png";
    }

    private byte[] downloadImage(String imageUrl) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response = restTemplate.getForEntity(imageUrl, byte[].class);
            return response.getBody();
        } catch (Exception e) {
            log.error(" 이미지 다운로드 실패: {}", e.getMessage());
            return null;
        }
    }

    private String encodeToBase64(byte[] imageBytes) {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }
}
