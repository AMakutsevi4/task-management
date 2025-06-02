package ru.management.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.management.dto.tag.TagCreateOrUpdateRequest;
import ru.management.dto.tag.TagResponse;
import ru.management.entity.Tag;
import ru.management.entity.Task;
import ru.management.entity.exception.TagExistException;
import ru.management.mapper.TagMapper;
import ru.management.repository.TagRepository;
import ru.management.repository.TaskRepository;
import ru.management.service.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;
    private final TagMapper tagMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> getTagsForTasks() {
        return tagMapper.entityListToResponseList(tagRepository.getAllTagsForTasks());
    }

    @Override
    @Transactional
    public TagResponse createTag(TagCreateOrUpdateRequest tagCreateOrUpdateRequest) {
        log.info("Сохранение тега: {}", tagCreateOrUpdateRequest.name());
        if (!tagCreateOrUpdateRequest.name().startsWith("#")) {
            throw new TagExistException("Имя тега должно начинаться с символа #");
        }

        if (tagRepository.existsByName(tagCreateOrUpdateRequest.name())) {
            throw new TagExistException("Тег " + tagCreateOrUpdateRequest.name() + " уже существует");
        }
        return tagMapper.toDto(tagRepository.save(tagMapper.toEntity(tagCreateOrUpdateRequest)));
    }

    @Override
    @Transactional
    public TagResponse updateTag(Long tagId, @NotNull TagCreateOrUpdateRequest updateRequest) {
        log.info("Обновление тега: {}", updateRequest.name());
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Тег не найден"));
        Optional.ofNullable(updateRequest.name()).ifPresent(tag::setName);
        return tagMapper.toDto(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public void deleteTagAndTasks(Long tagId) {
        log.info("Удаление тега: {}", tagId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Тег не найден: " + tagId));

        List<Task> tasksToDelete = new ArrayList<>(tag.getTasks());

        for (Task task : tasksToDelete) {
            if (task.getTags().size() > 1) {
                throw new TagExistException("Удаление невозможно! задача: " + task.getName() + ","
                        + " содержит более одного тега " + task.getTags().stream().map(Tag::getName).toList());
            }
        }
        taskRepository.deleteAll(tasksToDelete);
        tagRepository.delete(tag);
        log.info("Тег {} успешно удален", tagId);
    }
}