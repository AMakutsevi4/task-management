package ru.management.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.management.service.TaskService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TagRepository tagRepository;
    private final FileSavedService fileSavedService;

    public void uploadFile(Long taskId, MultipartFile file) {
        log.info("Сохранение файла к задаче: {}", file.getName());
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Задача не найдена: " + taskId);
        }
        fileSavedService.saveFile(taskId, file);
    }

    public File downloadFile(Long taskId) throws IOException {
        log.info("Скачивание файла к задаче: {}", taskId);
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Задача не найдена: " + taskId);
        }
        return fileSavedService.loadFile(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getAllTasks(Pageable pageable) {
        log.info("Получение всех задач");
        return taskRepository.findAll(pageable).map(taskMapper::toDto);
    }

    @Override
    @Cacheable("TaskPriority")
    @Transactional(readOnly = true)
    public List<TaskPriorityResponse> getAllPriorities() {
        log.info("Получение возможных приоритетов к задаче");
        return Arrays.stream(TaskPriority.values())
                .map(priority -> new TaskPriorityResponse(priority.name(), priority.getPriority()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasksByTagId(Long tagId) {
        log.info("Получение всех задач по тегу");
        if (!tagRepository.existsById(tagId)) {
            throw new EntityNotFoundException("Тег не найден: " + tagId);
        }
        return taskMapper.entityListToResponseList(taskRepository.findTaskByTagIdOrderByPriorityByDesc(tagId));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasksBetweenDateByPriorityDesc(LocalDate startDate, LocalDate endDate) {
        log.info("Получение всех задач за период");
        LocalDateTime start = startDate.atTime(LocalTime.MIN);
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return taskRepository.findByTasksBetweenDateByPriorityDesc(start, end)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TaskResponse createTask(TaskCreateOrUpdateRequest task) {
        log.info("Сохранение задачи: {}", task.name());
        return taskMapper.toDto(taskRepository.save(taskMapper.toEntity(task)));
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long taskId, @NotNull TaskCreateOrUpdateRequest updateRequest) {
        log.info("Обновление задачи: {}", updateRequest.name());
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача не найдена id " + taskId));

        Optional.ofNullable(updateRequest.name()).ifPresent(task::setName);
        Optional.ofNullable(updateRequest.description()).ifPresent(task::setDescription);
        Optional.ofNullable(updateRequest.scheduledDate()).ifPresent(task::setScheduledDate);
        Optional.ofNullable(updateRequest.priority()).ifPresent(task::setPriority);

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        log.info("Удаление задачи: {}", taskId);
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Задача не найдена с id: " + taskId);
        }
        taskRepository.deleteById(taskId);
        log.info("Задача {} успешно удалена", taskId);
    }

    @Override
    @Transactional
    public TaskResponse addTagToTask(Long taskId, Long tagId) {
        log.info("Добавление тега {}, к задаче {}", tagId, taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача с id " + taskId + " не найдена"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Тег с id " + tagId + " не найден"));

        task.getTags().add(tag);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse removeTagFromTask(Long taskId, Long tagId) {
        log.info("Удаление тега {}, у задачи {}", tagId, taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача с id " + taskId + " не найдена"));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Тег с id " + tagId + " не найден"));

        if (!task.getTags().contains(tag)) {
            throw new IllegalStateException("У задачи не существует указанный тег");
        }
        task.getTags().remove(tag);
        return taskMapper.toDto(taskRepository.save(task));
    }
}