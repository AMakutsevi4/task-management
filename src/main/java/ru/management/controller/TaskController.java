package ru.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;
import ru.management.dto.task.TaskCreateOrUpdateRequest;
import ru.management.dto.task.TaskPriorityResponse;
import ru.management.dto.task.TaskResponse;
import ru.management.service.TaskService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
@Tag(name = "Задачи", description = "Контроллер по управлению задачами")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Загрузить файл для задачи",
            description = "Загружает файл к задаче и возвращает обновленную информацию.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Файл успешно загружен"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void createFileField(@RequestParam Long id,
                                @RequestParam("file") MultipartFile file) {
        taskService.uploadFile(id, file);
    }

    @Operation(summary = "Получить файл для задачи",
            description = "Возвращает полный путь к файлу, связанный с задачей по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Файл успешно получен"),
            @ApiResponse(responseCode = "404", description = "Файл или задача не найдены"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    @GetMapping("/downloadFile/{id}")
    public File downloadFile(@PathVariable Long id) throws IOException {
        return taskService.downloadFile(id);
    }

    @GetMapping
    @Operation(summary = "Получить список всех задач с пагинацией",
            description = "Возвращает статус 200 и список задач")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public Page<TaskResponse> getAllTasks(@PageableDefault(sort = "scheduledDate") Pageable pageable) {
        return taskService.getAllTasks(pageable);
    }

    @GetMapping("/get-priority")
    @Operation(summary = "Получить список типов задач с уровнем приоритета",
            description = "Возвращает статус 200 и список типов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<TaskPriorityResponse> getAllPriorities() {
        return taskService.getAllPriorities();
    }

    @GetMapping("/tag/{tag_id}")
    @Operation(summary = "Получить список задач по тегу, отсортированные по приоритету",
            description = "Возвращает статус 200 и список задач")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<TaskResponse> getTasksByTagSorted(@PathVariable Long tag_id) {
        return taskService.getAllTasksByTagId(tag_id);
    }

    @GetMapping("/get-period")
    @Operation(summary = "Получить список задач за указанную дату с сортировкой по приоритету",
            description = "Возвращает статус 200 и список задач")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<TaskResponse> getAllForPeriodDate(@RequestParam("start") @DateTimeFormat LocalDate start,
                                                  @RequestParam("end") @DateTimeFormat LocalDate end) {
        return taskService.getAllTasksBetweenDateByPriorityDesc(start, end);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новую задачу",
            description = "Создаёт новую задачу и возвращает её данные")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public TaskResponse save(@RequestBody @Valid TaskCreateOrUpdateRequest request) {
        return taskService.createTask(request);
    }

    @PutMapping("/{task_id}")
    @Operation(summary = "Обновить задачу",
            description = "Обновляет существующую задачу по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public TaskResponse update(@PathVariable Long task_id,
                               @RequestBody @Valid TaskCreateOrUpdateRequest request) {
        return taskService.updateTask(task_id, request);
    }

    @DeleteMapping("/{task_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить задачу",
            description = "Удаляет задачу по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public void delete(@PathVariable Long task_id) {
        taskService.deleteTask(task_id);
    }

    @PostMapping("/{task_id}/{tag_id}")
    @Operation(summary = "Добавить тег к задаче")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тег добавлен"),
            @ApiResponse(responseCode = "404", description = "Задача или тег не найдены")
    })
    public TaskResponse addTagToTask(
            @PathVariable Long task_id,
            @PathVariable Long tag_id) {
        return taskService.addTagToTask(task_id, tag_id);
    }

    @DeleteMapping("/{task_id}/{tag_id}")
    @Operation(summary = "Удалить тег из задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Тег успешно удален"),
            @ApiResponse(responseCode = "404", description = "Тег или Задача не найдена")
    })
    public TaskResponse removeTagFromTask(
            @PathVariable Long task_id,
            @PathVariable Long tag_id) {
        return taskService.removeTagFromTask(task_id, tag_id);
    }
}