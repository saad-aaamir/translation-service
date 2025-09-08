package com.application.repository;

import com.application.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    List<Tag> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
    @Query("SELECT t FROM Tag t LEFT JOIN t.translations tr GROUP BY t ORDER BY COUNT(tr) DESC")
    Page<Tag> findMostUsedTags(Pageable pageable);
}