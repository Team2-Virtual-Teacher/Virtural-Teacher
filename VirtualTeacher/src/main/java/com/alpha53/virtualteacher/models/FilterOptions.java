package com.alpha53.virtualteacher.models;

import lombok.*;

import java.util.Optional;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FilterOptions {

    private Optional<String> title;
    private Optional<String> topic;
    private Optional<String> teacher;
    private Optional<Double> rating;
    private Optional<Boolean> isPublic;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptions(String title, String topic,String teacher, Double rating, Boolean isPublic, String sortBy, String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.topic = Optional.ofNullable(topic);
        this.teacher = Optional.ofNullable(teacher);
        this.rating = Optional.ofNullable(rating);
        this.isPublic = Optional.ofNullable(isPublic);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }
    public FilterOptions() {
        this.title = Optional.empty();
        this.topic = Optional.empty();
        this.teacher = Optional.empty();
        this.rating = Optional.empty();
        this.isPublic = Optional.empty();
        this.sortBy = Optional.empty();
        this.sortOrder = Optional.empty();
    }

}
