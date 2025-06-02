package ru.management.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.management.dto.task.TaskCreateOrUpdateRequest;
import ru.management.dto.task.TaskPriorityResponse;
import ru.management.dto.task.TaskResponse;
import ru.management.entity.Tag;
import ru.management.entity.Task;
import ru.management.enums.TaskPriority;
import ru.management.mapper.TaskMapper;
import ru.management.repository.TagRepository;
import ru.management.repository.TaskRepository;

import java.time.LocalDateTime;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование бизнес логики задач")
class TaskServiceImplTest {

    @Mock private TaskRepository taskRepository;
    @Mock private TagRepository tagRepository;
    @Mock private TaskMapper taskMapper;
    @Mock private FileSavedService fileSavedService;

    @InjectMocks private TaskServiceImpl taskService;

    private Task testTask;
    private Tag testTag;
    private TaskCreateOrUpdateRequest createRequest;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        testTag = Tag.builder().id(1L).name("#bug").build();
        testTask = Task.builder()
                .id(1L)
                .name("Fix login bug")
                .description("User can't login")
                .priority(TaskPriority.IMPORTANT)
                .tags(new HashSet<>())
                .build();

        createRequest = new TaskCreateOrUpdateRequest(
                "Fix login bug", "User can't login",
                LocalDateTime.now(), TaskPriority.IMPORTANT);

        taskResponse = new TaskResponse(
                1L, "Fix login bug", "User can't login",
                LocalDateTime.now(), TaskPriority.IMPORTANT, Set.of());
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Создание задачи")
    void givenValidRequest_whenCreateTask_thenSuccess() {
        when(taskMapper.toEntity(createRequest)).thenReturn(testTask);
        when(taskRepository.save(testTask)).thenReturn(testTask);
        when(taskMapper.toDto(testTask)).thenReturn(taskResponse);

        TaskResponse result = taskService.createTask(createRequest);
        assertNotNull(result);
        assertEquals("Fix login bug", result.name());
        verify(taskRepository).save(testTask);
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Обновление задачи")
    void givenValidRequest_whenUpdateTask_thenSuccess() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(testTask)).thenReturn(testTask);
        when(taskMapper.toDto(testTask)).thenReturn(taskResponse);

        TaskResponse result = taskService.updateTask(1L, createRequest);
        assertNotNull(result);
        verify(taskRepository).save(testTask);
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Удаление задачи")
    void givenExistingTask_whenDelete_thenSuccess() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        taskService.deleteTask(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Получение всех приоритетов задач")
    void whenGetAllPriorities_thenReturnAll() {
        List<TaskPriorityResponse> expected = List.of(
                new TaskPriorityResponse("LOW", 1),
                new TaskPriorityResponse("MEDIUM", 2),
                new TaskPriorityResponse("HIGH", 3)
        );
        List<TaskPriorityResponse> actual = taskService.getAllPriorities();
        assertEquals(3, actual.size());
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Загрузка файла")
    void givenExistingTask_whenUploadFile_thenSuccess() {
        MultipartFile file = mock(MultipartFile.class);
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.uploadFile(1L, file);
        verify(fileSavedService).saveFile(1L, file);
    }


    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Получение задач по тегу")
    void givenValidTagId_whenGetTasksByTag_thenSuccess() {
        when(tagRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findTaskByTagIdOrderByPriorityByDesc(1L)).thenReturn(List.of(testTask));
        when(taskMapper.entityListToResponseList(anyList())).thenReturn(List.of(taskResponse));

        List<TaskResponse> result = taskService.getAllTasksByTagId(1L);
        assertEquals(1, result.size());
        verify(taskRepository).findTaskByTagIdOrderByPriorityByDesc(1L);
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Добавление тега к задаче")
    void givenValidIds_whenAddTag_thenSuccess() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));
        testTask.setTags(new HashSet<>());
        when(taskRepository.save(testTask)).thenReturn(testTask);
        when(taskMapper.toDto(testTask)).thenReturn(taskResponse);

        TaskResponse result = taskService.addTagToTask(1L, 1L);
        assertNotNull(result);
        verify(taskRepository).save(testTask);
    }

    @Test
    @org.junit.jupiter.api.Tag("Позитивный")
    @DisplayName("Удаление тега у задачи")
    void givenValidIds_whenRemoveTag_thenSuccess() {
        testTask.setTags(new HashSet<>(Set.of(testTag)));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));
        when(taskRepository.save(testTask)).thenReturn(testTask);
        when(taskMapper.toDto(testTask)).thenReturn(taskResponse);

        TaskResponse result = taskService.removeTagFromTask(1L, 1L);
        assertNotNull(result);
        verify(taskRepository).save(testTask);
    }

    @Test
    @org.junit.jupiter.api.Tag("Негативный")
    @DisplayName("Обновление несуществующей задачи")
    void givenNonExistentTask_whenUpdateTask_thenThrowException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> taskService.updateTask(99L, createRequest));
    }

    @Test
    @org.junit.jupiter.api.Tag("Негативный")
    @DisplayName("Удаление несуществующей задачи")
    void givenNonExistingTask_whenDelete_thenThrowException() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> taskService.deleteTask(99L));
    }


    @Test
    @org.junit.jupiter.api.Tag("Негативный")
    @DisplayName("Загрузка файла к несуществующей задаче")
    void givenNonExistentTask_whenUploadFile_thenThrowException() {
        when(taskRepository.existsById(1L)).thenReturn(false);
        MultipartFile file = mock(MultipartFile.class);

        assertThrows(EntityNotFoundException.class,
                () -> taskService.uploadFile(1L, file));
    }


    @Test
    @org.junit.jupiter.api.Tag("Негативный")
    @DisplayName("Получение задач по несуществующему тегу")
    void givenInvalidTagId_whenGetTasksByTag_thenThrowException() {
        when(tagRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> taskService.getAllTasksByTagId(99L));
    }

    @Test
    @org.junit.jupiter.api.Tag("Негативный")
    @DisplayName("Удаление несуществующего тега у задачи")
    void givenTagNotAttached_whenRemoveTag_thenThrowException() {
        testTask.setTags(new HashSet<>());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));

        assertThrows(IllegalStateException.class,
                () -> taskService.removeTagFromTask(1L, 1L));
    }
}