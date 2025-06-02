package ru.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.management.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий задач
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.scheduledDate BETWEEN :startDate AND :endDate ORDER BY t.priority DESC")
    List<Task> findByTasksBetweenDateByPriorityDesc(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT task FROM Task task JOIN task.tags tag WHERE tag.id = :tagId ORDER BY task.priority DESC")
    List<Task> findTaskByTagIdOrderByPriorityByDesc(@Param("tagId")Long tagId);
}