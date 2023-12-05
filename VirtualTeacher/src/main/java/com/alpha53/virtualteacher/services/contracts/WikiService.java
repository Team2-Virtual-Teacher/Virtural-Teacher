package com.alpha53.virtualteacher.services.contracts;

import com.alpha53.virtualteacher.models.WikiResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface WikiService {
    List<WikiResult> getSearchResult(String searchCriteria) throws URISyntaxException, IOException, InterruptedException;
}
