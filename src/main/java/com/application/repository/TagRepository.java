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

    // Find by name
    Optional<Tag> findByName(String name);

    // Find by name containing
    List<Tag> findByNameContainingIgnoreCase(String name);

    Page<Tag> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Check if tag exists
    boolean existsByName(String name);

    // Find tags by names
    List<Tag> findByNameIn(List<String> names);

    // Find tags used in translations
    @Query("SELECT DISTINCT t FROM Tag t JOIN t.translations")
    List<Tag> findTagsWithTranslations();

    // Count translations per tag
    @Query("SELECT t.name, COUNT(tr) FROM Tag t LEFT JOIN t.translations tr GROUP BY t.id, t.name")
    List<Object[]> countTranslationsByTag();

    // Find popular tags (most used)
    @Query("SELECT t FROM Tag t LEFT JOIN t.translations tr GROUP BY t ORDER BY COUNT(tr) DESC")
    Page<Tag> findMostUsedTags(Pageable pageable);
}