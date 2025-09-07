package com.application.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "translations",
        indexes = {
                @Index(name = "idx_key_locale", columnList = "translationKey, locale"),
                @Index(name = "idx_locale", columnList = "locale"),
                @Index(name = "idx_key", columnList = "translationKey")
        })
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "translation_key", nullable = false)
    private String translationKey;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 10)
    private String locale;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "translation_tags",
            joinColumns = @JoinColumn(name = "translation_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Constructors
    public Translation() {}

    public Translation(String translationKey, String content, String locale) {
        this.translationKey = translationKey;
        this.content = content;
        this.locale = locale;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTranslationKey() { return translationKey; }
    public void setTranslationKey(String translationKey) { this.translationKey = translationKey; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Set<Tag> getTags() { return tags; }
    public void setTags(Set<Tag> tags) { this.tags = tags; }

    // Utility methods
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getTranslations().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getTranslations().remove(this);
    }
}
