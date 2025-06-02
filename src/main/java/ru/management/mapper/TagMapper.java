package ru.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.management.dto.tag.TagCreateOrUpdateRequest;
import ru.management.dto.tag.TagResponse;
import ru.management.entity.Tag;

import java.util.List;

/**
 * Маппер для тегов
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {
    TagResponse toDto(Tag tag);

    Tag toEntity(TagCreateOrUpdateRequest tagCreateOrUpdateRequest);

    List<TagResponse> entityListToResponseList(List<Tag> tags);
}
