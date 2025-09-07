package com.application.repository;

import com.application.entity.Translation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long>, JpaSpecificationExecutor<Translation> {

    // Find by key and locale
    Optional<Translation> findByTranslationKeyAndLocale(String translationKey, String locale);

    // Find all by locale
    List<Translation> findByLocale(String locale);
    Page<Translation> findByLocale(String locale, Pageable pageable);

    // Find by key
    List<Translation> findByTranslationKey(String translationKey);
    Page<Translation> findByTranslationKey(String translationKey, Pageable pageable);

    // Search by content containing
    List<Translation> findByContentContainingIgnoreCase(String content);
    Page<Translation> findByContentContainingIgnoreCase(String content, Pageable pageable);

    // Find by tags
    @Query("SELECT t FROM Translation t JOIN t.tags tag WHERE tag.name = :tagName")
    List<Translation> findByTagName(@Param("tagName") String tagName);

    @Query("SELECT t FROM Translation t JOIN t.tags tag WHERE tag.name = :tagName")
    Page<Translation> findByTagName(@Param("tagName") String tagName, Pageable pageable);

    // Find by multiple tags
    @Query("SELECT DISTINCT t FROM Translation t JOIN t.tags tag WHERE tag.name IN :tagNames")
    List<Translation> findByTagNameIn(@Param("tagNames") List<String> tagNames);

    // Complex search query
    @Query("SELECT DISTINCT t FROM Translation t LEFT JOIN t.tags tag WHERE " +
            "(:locale IS NULL OR t.locale = :locale) AND " +
            "(:key IS NULL OR t.translationKey LIKE %:key%) AND " +
            "(:content IS NULL OR LOWER(t.content) LIKE LOWER(CONCAT('%', :content, '%'))) AND " +
            "(:tagName IS NULL OR tag.name = :tagName)")
    Page<Translation> searchTranslations(@Param("locale") String locale,
                                         @Param("key") String key,
                                         @Param("content") String content,
                                         @Param("tagName") String tagName,
                                         Pageable pageable);

    // Count by locale
    long countByLocale(String locale);

    // Bulk update
    @Modifying
    @Query("UPDATE Translation t SET t.content = :newContent WHERE t.translationKey = :key AND t.locale = :locale")
    int updateContentByKeyAndLocale(@Param("key") String key,
                                    @Param("locale") String locale,
                                    @Param("newContent") String newContent);

    // Bulk delete by locale
    @Modifying
    void deleteByLocale(String locale);

    // Full-text search (MySQL specific)
    @Query(value = "SELECT * FROM translations WHERE MATCH(content) AGAINST(:searchTerm IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<Translation> fullTextSearch(@Param("searchTerm") String searchTerm);

    // Export query - optimized for large datasets
    @Query("SELECT t FROM Translation t LEFT JOIN FETCH t.tags WHERE t.locale = :locale")
    List<Translation> findAllByLocaleWithTags(@Param("locale") String locale);
}