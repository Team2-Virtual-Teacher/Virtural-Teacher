package com.alpha53.virtualteacher.services;

import com.alpha53.virtualteacher.models.WikiResult;
import com.alpha53.virtualteacher.services.contracts.WikiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.wikiclean.WikiClean;
import org.wikiclean.languages.English;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WikiServiceImpl implements WikiService {
    public static final String EXTRACT_CONTENT_URI = "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvslots=*&rvprop=content&formatversion=2&format=json&titles=";
    private static final String EXTRACT_PAGEID_AND_TITLE_URI = "https://en.wikipedia.org/w/api.php?action=query&list=search&format=json&srlimit=3&formatversion=2&srsearch=";
    private static final String EXTRACT_FULL_URI = "https://en.wikipedia.org/w/api.php?action=query&prop=info&inprop=url&format=json&pageids=";
    private final ObjectMapper objectMapper;

    public WikiServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<WikiResult> getSearchResult(String searchCriteria) throws URISyntaxException, IOException, InterruptedException {
        List<WikiResult> searchResultlist = new ArrayList<>();

        HttpClient httpClient = HttpClient
                .newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();


        HttpRequest request = HttpRequest
                .newBuilder(new URI(EXTRACT_PAGEID_AND_TITLE_URI.concat(URLEncoder.encode(searchCriteria, StandardCharsets.UTF_8))))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        extractPageIdAndTitle(response, searchResultlist);

        String titlesRequestParams = generateTitleParamValue(searchResultlist);

        request = HttpRequest
                .newBuilder(new URI(EXTRACT_CONTENT_URI.concat(titlesRequestParams)))
                .GET()
                .build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        extractContent(response, searchResultlist);

        String pageidsRequestParams = generatePageIdParamValue(searchResultlist);

        request=HttpRequest
                .newBuilder(new URI(EXTRACT_FULL_URI.concat(pageidsRequestParams)))
                .GET()
                .build();
        response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        extractFullUrl(response,searchResultlist);


        return searchResultlist;

    }

    private String generateTitleParamValue(List<WikiResult> searchResultlist) {
        StringBuilder sb = new StringBuilder();
        for (WikiResult wikiResult : searchResultlist) {
            sb.append(wikiResult.getTitle()).append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        return URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);

    }

    private String generatePageIdParamValue(List<WikiResult> searchResultlist) {
        StringBuilder sb = new StringBuilder();
        for (WikiResult wikiResult : searchResultlist) {
            sb.append(wikiResult.getPageid()).append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        return URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);

    }

    private void extractPageIdAndTitle(HttpResponse<String> response, List<WikiResult> resultList) throws JsonProcessingException {
        JsonNode source = objectMapper.readTree(response.body()).path("query").path("search");
        for (JsonNode jsonNode : source) {
            WikiResult w = new WikiResult();
            w.setPageid(jsonNode.get("pageid").asInt(-1));
            w.setTitle(jsonNode.get("title").asText("Not available"));
            w.setSnippet(jsonNode.get("snippet").asText("Not available"));
            resultList.add(w);
        }
    }
    private void extractFullUrl(HttpResponse<String> response, List<WikiResult> searchResultlist) throws JsonProcessingException {
        JsonNode source = objectMapper.readTree(response.body()).path("query").path("pages");
        Map<Integer,String> urlMap = new HashMap<>();
        for (JsonNode node : source) {
            urlMap.put(node.get("pageid").asInt(),node.get("fullurl").asText());
        }
        for (WikiResult wikiResult : searchResultlist) {
            wikiResult.setFullUrl(urlMap.get(wikiResult.getPageid()));
        }

    }

    private void extractContent(HttpResponse<String> response, List<WikiResult> resultList) throws JsonProcessingException {
        JsonNode source = objectMapper.readTree(response.body()).path("query").path("pages");
        Map<Integer,String> contentMap = new HashMap<>();
        for (JsonNode jsonNode : source) {
            JsonNode n = objectMapper.readTree(jsonNode.toString()).path("revisions");
            Integer pageId = jsonNode.get("pageid").asInt();
            for (JsonNode node : n) {
                contentMap.put(pageId,node.path("slots").path("main").get("content").asText("N/A"));
            }
        }

       // WikiClean wikiClean = new WikiClean.Builder().withLanguage(new English()).withTitle(false).withFooter(false).build();
        for (WikiResult wikiResult : resultList) {
           // wikiResult.setContent(wikiClean.clean(contentMap.get(wikiResult.getPageid())));
            wikiResult.setContent(contentMap.get(wikiResult.getPageid()));
        }

    }
}
