package com.alpha53.virtualteacher.models;

import lombok.*;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FilterOptions {

    private Optional<String> title;
    private Optional<String> topic;
    private Optional<String> teacher;
    private Optional<String> rating;
    private Optional<Boolean> isPublic;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptions(String title, String topic,String teacher, String rating, Boolean isPublic, String sortBy, String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.topic = Optional.ofNullable(topic);
        this.teacher = Optional.ofNullable(teacher);
        this.rating = Optional.ofNullable(rating);
        this.isPublic = Optional.ofNullable(isPublic);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

}
