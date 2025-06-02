package ru.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.management.dto.task.TaskCreateOrUpdateRequest;
import ru.management.dto.task.TaskResponse;
import ru.management.entity.Task;

import java.util.List;

/**
 * Маппер для задач
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    TaskResponse toDto(Task task);

    Task toEntity(TaskCreateOrUpdateRequest taskCreateOrUpdateRequest);

    List<TaskResponse> entityListToResponseList(List<Task> tasks);
}