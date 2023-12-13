package com.alpha53.virtualteacher.controllers.rest;

import com.alpha53.virtualteacher.exceptions.AuthorizationException;
import com.alpha53.virtualteacher.models.WikiResult;
import com.alpha53.virtualteacher.services.WikiServiceImpl;
import com.alpha53.virtualteacher.utilities.helpers.AuthenticationHelper;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController()
@RequestMapping("api/v1/wiki-search")
public class WikiController {
    private final WikiServiceImpl wikiServiceImpl;
    private final AuthenticationHelper authenticationHelper;

    public WikiController(WikiServiceImpl wikiServiceImpl, AuthenticationHelper authenticationHelper) {
        this.wikiServiceImpl = wikiServiceImpl;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping()
    public List<WikiResult> searchWiki(@RequestHeader HttpHeaders headers,
                                       @RequestParam("search") @Size(min = 1) String searchCriteria) throws IOException, InterruptedException, URISyntaxException {
        try{
            authenticationHelper.tryGetUser(headers);
            return wikiServiceImpl.getSearchResult(searchCriteria);
        }
        catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }
}
