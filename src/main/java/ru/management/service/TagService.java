package ru.management.service;

import ru.management.dto.tag.TagCreateOrUpdateRequest;
import ru.management.dto.tag.TagResponse;

import java.util.List;

public interface TagService {

    /**
     * Создать тег
     *
     * @param tagCreateOrUpdateRequest - создаваемый тег
     * @return - созданный тег
     */
    TagResponse createTag(TagCreateOrUpdateRequest tagCreateOrUpdateRequest);

    /**
     * Обновить тег
     *
     * @param tagId - id тега
     * @param tag - обновление тега
     * @return - обновленный тег
     */
    TagResponse updateTag(Long tagId, TagCreateOrUpdateRequest tag);

    /**
     * Удаление тега
     *
     * @param tagId - id тега
     */
    void deleteTagAndTasks(Long tagId);

    /**
     * Получить список всех тегов, у которых есть задачи
     *
     * @return - список тегов
     */
    List<TagResponse> getTagsForTasks();
}