package ru.management.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.management.dto.tag.TagCreateOrUpdateRequest;
import ru.management.dto.tag.TagResponse;
import ru.management.entity.Tag;
import ru.management.entity.Task;
import ru.management.entity.exception.TagExistException;
import ru.management.mapper.TagMapper;
import ru.management.repository.TagRepository;
import ru.management.repository.TaskRepository;

import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование бизнес логики тегов")
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag testTag;
    private Task testTask;
    private TagCreateOrUpdateRequest createRequest;
    private TagCreateOrUpdateRequest updateRequest;
    private TagResponse tagResponse;

    @BeforeEach
    void setUp() {
        testTag = Tag.builder()
                .id(1L)
                .name("#backend")
                .tasks(new ArrayList<>())
                .build();

        testTask = Task.builder()
                .id(1L)
                .name("Fix bug")
                .tags(new HashSet<>(Set.of(testTag)))
                .build();

        testTag.getTasks().add(testTask);

        createRequest = new TagCreateOrUpdateRequest("#backend");
        updateRequest = new TagCreateOrUpdateRequest("#frontend");
        tagResponse = new TagResponse(1L, "#backend");
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Получить тег с задачами")
    void givenValidRequest_whenGetTagsWithTasks_thenSuccess() {
        when(tagRepository.getAllTagsForTasks()).thenReturn(List.of(testTag));
        when(tagMapper.entityListToResponseList(anyList())).thenReturn(List.of(tagResponse));

        List<TagResponse> result = tagService.getTagsForTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("#backend", result.get(0).name());
        verify(tagRepository, times(1)).getAllTagsForTasks();
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Создание тега")
    void givenValidRequest_whenCreateTag_thenSuccess() {
        when(tagMapper.toEntity(createRequest)).thenReturn(testTag);
        when(tagRepository.save(testTag)).thenReturn(testTag);
        when(tagMapper.toDto(testTag)).thenReturn(tagResponse);

        TagResponse result = tagService.createTag(createRequest);

        assertNotNull(result);
        assertEquals("#backend", result.name());
        verify(tagRepository, times(1)).save(testTag);
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Обновление тега")
    void givenValidRequest_whenUpdateTag_thenSuccess() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));
        when(tagRepository.save(testTag)).thenReturn(testTag);
        when(tagMapper.toDto(testTag)).thenReturn(new TagResponse(1L, "#frontend"));

        TagResponse result = tagService.updateTag(1L, updateRequest);

        assertNotNull(result);
        assertEquals("#frontend", result.name());
        verify(tagRepository, times(1)).save(testTag);
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Удаление тега с одной задачей")
    void givenValidRequest_whenDeleteTag_thenSuccess() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));

        tagService.deleteTagAndTasks(1L);

        verify(tagRepository, times(1)).delete(testTag);
    }

    @Test
    @org.junit.jupiter.api.Tag("Негативный")
    @DisplayName("Создание тега без символа")
    void givenInvalidName_whenCreateTag_thenThrowException() {
        TagCreateOrUpdateRequest invalidRequest = new TagCreateOrUpdateRequest("backend");

        assertThrows(TagExistException.class,
                () -> tagService.createTag(invalidRequest));
    }

    @Test
    @org.junit.jupiter.api.Tag("Негативный")
    @DisplayName("Создание существующего тега")
    void givenExistingTag_whenCreateTag_thenThrowException() {
        when(tagRepository.existsByName("#backend")).thenReturn(true);

        assertThrows(TagExistException.class,
                () -> tagService.createTag(createRequest));
    }

    @Test
    @org.junit.jupiter.api.Tag("Негативный")
    @DisplayName("Обновление несуществующего тега")
    void givenNonExistentTag_whenUpdateTag_thenThrowException() {
        when(tagRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> tagService.updateTag(99L, updateRequest));
    }

    @Test
    @org.junit.jupiter.api.Tag("Негативный")
    @DisplayName("Удаление несуществующего тега")
    void givenNonExistentTag_whenDeleteTag_thenThrowException() {
        when(tagRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> tagService.deleteTagAndTasks(99L));
    }
}