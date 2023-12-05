package com.alpha53.virtualteacher.models;

import lombok.Data;

@Data
public class WikiResult {

    private String title;
    private int pageid;
    private String snippet;
    private String content;
    private String fullUrl;

}
