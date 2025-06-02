package ru.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.management.dto.tag.TagCreateOrUpdateRequest;
import ru.management.dto.tag.TagResponse;
import ru.management.service.TagService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
@Tag(name = "Теги", description = "Контроллер для управления тегами")
public class TagController {

    private final TagService tagService;

    @GetMapping("/tag-for-tasks")
    @Operation(summary = "Получить список тегов, у которых есть задачи",
            description = "Возвращает статус 200 и список тегов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Запрос прошел успешно"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    public List<TagResponse> getTagsWithTasks() {
        return tagService.getTagsForTasks();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать тег")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Тег успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public TagResponse save(@RequestBody @Valid TagCreateOrUpdateRequest request) {
        return tagService.createTag(request);
    }

    @PutMapping("/{tag_id}")
    @Operation(summary = "Обновить тег",
            description = "Обновляет существующий тег по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тег успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Тег не найден")
    })
    public TagResponse update(@PathVariable Long tag_id,
                               @RequestBody @Valid TagCreateOrUpdateRequest request) {
        return tagService.updateTag(tag_id, request);
    }

    @DeleteMapping("/{tag_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить тег и все связанные задачи.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Тег удалён"),
            @ApiResponse(responseCode = "404", description = "Тег не найден")
    })
    public void deleteTagWithTasks(@PathVariable Long tag_id) {
        tagService.deleteTagAndTasks(tag_id);
    }
}