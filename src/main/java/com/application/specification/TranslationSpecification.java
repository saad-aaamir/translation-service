package com.application.specification;

import com.application.dto.request.TranslationSearchRequest;
import com.application.entity.Translation;
import com.application.entity.Tag;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class TranslationSpecification {

    public static Specification<Translation> withCriteria(TranslationSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getLocale() != null && !request.getLocale().trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("locale"), request.getLocale()));
            }

            if (request.getTranslationKey() != null && !request.getTranslationKey().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("translationKey")),
                        "%" + request.getTranslationKey().toLowerCase() + "%"
                ));
            }

            if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("content")),
                        "%" + request.getContent().toLowerCase() + "%"
                ));
            }

            if (request.getTagName() != null && !request.getTagName().trim().isEmpty()) {
                Join<Translation, Tag> tagJoin = root.join("tags", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(tagJoin.get("name"), request.getTagName()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Translation> hasLocale(String locale) {
        return (root, query, criteriaBuilder) -> {
            if (locale == null || locale.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("locale"), locale);
        };
    }

    public static Specification<Translation> hasTranslationKeyLike(String key) {
        return (root, query, criteriaBuilder) -> {
            if (key == null || key.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("translationKey")),
                    "%" + key.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Translation> hasContentLike(String content) {
        return (root, query, criteriaBuilder) -> {
            if (content == null || content.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("content")),
                    "%" + content.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Translation> hasTag(String tagName) {
        return (root, query, criteriaBuilder) -> {
            if (tagName == null || tagName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Translation, Tag> tagJoin = root.join("tags", JoinType.INNER);
            return criteriaBuilder.equal(tagJoin.get("name"), tagName);
        };
    }

    public static Specification<Translation> hasAnyTag(List<String> tagNames) {
        return (root, query, criteriaBuilder) -> {
            if (tagNames == null || tagNames.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Translation, Tag> tagJoin = root.join("tags", JoinType.INNER);
            return tagJoin.get("name").in(tagNames);
        };
    }
}