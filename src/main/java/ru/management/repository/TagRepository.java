package ru.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.management.entity.Tag;

import java.util.List;

/**
 * Репозиторий тегов
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Проверка на уникаольность тега
     *
     * @param name - новый тек
     * @return - результат проверки (true / false)
     */
    boolean existsByName(String name);

    /**
     * Получить список всех тегов, у которых есть задачи
     *
     * @return - список актуальных тегов
     */
    @Query("SELECT t FROM Tag t WHERE SIZE(t.tasks) > 0")
    List<Tag> getAllTagsForTasks();
}
