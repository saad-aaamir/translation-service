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

    Optional<Translation> findByTranslationKeyAndLocale(String translationKey, String locale);

    List<Translation> findByLocale(String locale);

    @Query("SELECT t FROM Translation t JOIN t.tags tag WHERE tag.name = :tagName")
    List<Translation> findByTagName(@Param("tagName") String tagName);

    long countByLocale(String locale);

    @Modifying
    void deleteByLocale(String locale);

    @Query(value = "SELECT * FROM translations WHERE MATCH(content) AGAINST(:searchTerm IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<Translation> fullTextSearch(@Param("searchTerm") String searchTerm);

}