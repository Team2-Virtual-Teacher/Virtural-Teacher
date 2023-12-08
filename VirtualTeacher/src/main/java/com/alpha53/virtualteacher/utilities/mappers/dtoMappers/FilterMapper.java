package com.alpha53.virtualteacher.utilities.mappers.dtoMappers;

import com.alpha53.virtualteacher.models.FilterOptions;
import com.alpha53.virtualteacher.models.dtos.FilterOptionDto;

import java.util.Optional;

public class FilterMapper {
    public static FilterOptions fromFilterOptionsDtoToFilterOptions(FilterOptionDto filterOptionDto) {
        FilterOptions filterOption = new FilterOptions();
        filterOption.setTitle(Optional.ofNullable(filterOptionDto.getTitle()));
        filterOption.setRating(Optional.ofNullable(filterOptionDto.getRating()));
        filterOption.setTeacher(Optional.ofNullable(filterOptionDto.getTeacher()));
        filterOption.setTopic(Optional.ofNullable(filterOptionDto.getTopic()));
        filterOption.setSortBy(Optional.ofNullable(filterOptionDto.getSortBy()));
        filterOption.setIsPublic(Optional.ofNullable(filterOptionDto.getIsPublic()));
        filterOption.setSortOrder(Optional.ofNullable(filterOptionDto.getSortOrder()));
        return filterOption;
    }
}
