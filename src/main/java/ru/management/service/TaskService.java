package ru.management.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.management.dto.task.TaskCreateOrUpdateRequest;
import ru.management.dto.task.TaskPriorityResponse;
import ru.management.dto.task.TaskResponse;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    /**
     * Создать задачу
     *
     * @param task - создаваемый тег
     * @return - созданная задача
     */
    TaskResponse createTask(TaskCreateOrUpdateRequest task);

    /**
     * Обновить задачу
     *
     * @param taskId - id задачи
     * @param task   - обновление задачи
     * @return - обновленный обновленная задача
     */
    TaskResponse updateTask(Long taskId, TaskCreateOrUpdateRequest task);

    /**
     * Удаление задачи
     *
     * @param taskId - id тега
     */
    void deleteTask(Long taskId);

    /**
     * Добавить тег к задаче
     *
     * @param taskId - id задачи
     * @param tagId  - id ега
     * @return - задача с добавленным тегом
     */
    TaskResponse addTagToTask(Long taskId, Long tagId);

    /**
     * Удалить тег у задачи
     *
     * @param taskId - id задачи
     * @param tagId  - id тега
     * @return - задача с удаленным тегом
     */
    TaskResponse removeTagFromTask(Long taskId, Long tagId);

    /**
     * Получить список типов задач, с уровнем приоритета
     *
     * @return - список задач
     */
    List<TaskPriorityResponse> getAllPriorities();

    /**
     * Получить список задач за промежуток времени
     *
     * @param startDate - начальная дата
     * @param endDate   - конец периода
     * @return - список задач
     */
    List<TaskResponse> getAllTasksBetweenDateByPriorityDesc(LocalDate startDate, LocalDate endDate);

    /**
     * Получить список задач по тегу
     *
     * @param tagId - id тега
     * @return - список задач
     */
    List<TaskResponse> getAllTasksByTagId(Long tagId);

    /**
     * Получить список всех задач
     *
     * @param pageable - пагинация для списка задач
     * @return - список задач
     */
    Page<TaskResponse> getAllTasks(Pageable pageable);

    /**
     * Загрузить файл для задачи
     *
     * @param taskId - id задачи
     * @param file   -прикрепляемый файл
     */
    void uploadFile(Long taskId, MultipartFile file);

    /**
     * Скачать файл определенной задачи
     *
     * @param taskId - id задачи
     */
    File downloadFile(Long taskId) throws IOException;
}