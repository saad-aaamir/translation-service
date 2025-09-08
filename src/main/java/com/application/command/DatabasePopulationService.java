package com.application.command;

import com.application.entity.Tag;
import com.application.entity.Translation;
import com.application.repository.TagRepository;
import com.application.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabasePopulationService {

    private final TranslationRepository translationRepository;
    private final TagRepository tagRepository;

    private static final String[] LOCALES = {"en", "fr", "es", "de", "it", "pt", "ru", "ja", "zh", "ar"};
    private static final String[] TAG_NAMES = {"mobile", "desktop", "web", "api", "ui", "error", "success", "warning", "info", "navigation"};
    private static final String[] KEY_PREFIXES = {"button", "label", "message", "title", "description", "placeholder", "tooltip", "alert", "notification", "menu"};

    @Transactional
    public void populateDatabase(int recordCount) {
        log.info("Starting database population with {} records...", recordCount);

        long startTime = System.currentTimeMillis();

        // Clear existing data if needed
        // translationRepository.deleteAll();
        // tagRepository.deleteAll();

        // Create tags first
        List<Tag> tags = createTagsIfNotExist();

        // Create translations in batches for better performance
        createTranslationsInBatches(tags, recordCount);

        long endTime = System.currentTimeMillis();
        log.info("Database population completed in {} ms", endTime - startTime);
        log.info("Total translations: {}", translationRepository.count());
        log.info("Total tags: {}", tagRepository.count());
    }

    private List<Tag> createTagsIfNotExist() {
        log.info("Creating/fetching tags...");
        List<Tag> tags = new ArrayList<>();

        for (String tagName : TAG_NAMES) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = Tag.builder()
                                .name(tagName)
                                .description("Tag for " + tagName + " context")
                                .build();
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }

        return tags;
    }

    private void createTranslationsInBatches(List<Tag> tags, int totalRecords) {
        int batchSize = 1000;
        int numberOfBatches = (totalRecords + batchSize - 1) / batchSize; // Ceiling division

        log.info("Creating {} translations in {} batches of {}", totalRecords, numberOfBatches, batchSize);

        for (int batchIndex = 0; batchIndex < numberOfBatches; batchIndex++) {
            int startIndex = batchIndex * batchSize;
            int endIndex = Math.min(startIndex + batchSize, totalRecords);
            int currentBatchSize = endIndex - startIndex;

            List<Translation> batchTranslations = createTranslationBatch(tags, startIndex, currentBatchSize);

            // Use batch insert with JPA
            translationRepository.saveAll(batchTranslations);

            // Log progress every 10 batches
            if ((batchIndex + 1) % 10 == 0) {
                log.info("Completed batch {}/{} ({} records)",
                        batchIndex + 1, numberOfBatches, endIndex);
            }
        }
    }

    private List<Translation> createTranslationBatch(List<Tag> availableTags, int startIndex, int batchSize) {
        return IntStream.range(0, batchSize)
                .mapToObj(i -> createTranslation(startIndex + i, availableTags))
                .toList();
    }

    private Translation createTranslation(int recordNumber, List<Tag> availableTags) {
        Random random = ThreadLocalRandom.current();

        // Generate unique translation key
        String prefix = KEY_PREFIXES[random.nextInt(KEY_PREFIXES.length)];
        String translationKey = String.format("%s.%s.%06d",
                prefix,
                generateRandomWord(),
                recordNumber);

        // Generate locale-specific content
        String locale = LOCALES[random.nextInt(LOCALES.length)];
        String content = generateTranslationContent(translationKey, locale, random);

        Translation translation = new Translation(translationKey, content, locale);

        // Add 1-3 random tags
        int tagCount = random.nextInt(3) + 1;
        Set<Tag> selectedTags = selectRandomTags(availableTags, tagCount, random);
        translation.setTags(selectedTags);

        return translation;
    }

    private Set<Tag> selectRandomTags(List<Tag> availableTags, int count, Random random) {
        Set<Tag> selectedTags = new HashSet<>();
        while (selectedTags.size() < count && selectedTags.size() < availableTags.size()) {
            Tag randomTag = availableTags.get(random.nextInt(availableTags.size()));
            selectedTags.add(randomTag);
        }
        return selectedTags;
    }

    private String generateTranslationContent(String key, String locale, Random random) {
        // Generate realistic content based on key type and locale
        if (key.contains("button")) {
            return generateButtonText(locale, random);
        } else if (key.contains("message") || key.contains("alert")) {
            return generateMessageText(locale, random);
        } else if (key.contains("title")) {
            return generateTitleText(locale, random);
        } else if (key.contains("label")) {
            return generateLabelText(locale, random);
        } else {
            return generateGenericText(locale, random);
        }
    }

    private String generateButtonText(String locale, Random random) {
        Map<String, String[]> buttonTexts = Map.of(
                "en", new String[]{"Save", "Cancel", "Submit", "Delete", "Edit", "View"},
                "fr", new String[]{"Enregistrer", "Annuler", "Soumettre", "Supprimer", "Éditer", "Voir"},
                "es", new String[]{"Guardar", "Cancelar", "Enviar", "Eliminar", "Editar", "Ver"},
                "de", new String[]{"Speichern", "Abbrechen", "Senden", "Löschen", "Bearbeiten", "Anzeigen"}
        );

        String[] texts = buttonTexts.getOrDefault(locale, buttonTexts.get("en"));
        return texts[random.nextInt(texts.length)];
    }

    private String generateMessageText(String locale, Random random) {
        Map<String, String[]> messageTexts = Map.of(
                "en", new String[]{"Operation completed", "Error occurred", "Please fill required fields"},
                "fr", new String[]{"Opération terminée", "Erreur survenue", "Veuillez remplir les champs requis"},
                "es", new String[]{"Operación completada", "Error ocurrido", "Complete los campos requeridos"},
                "de", new String[]{"Vorgang abgeschlossen", "Fehler aufgetreten", "Pflichtfelder ausfüllen"}
        );

        String[] texts = messageTexts.getOrDefault(locale, messageTexts.get("en"));
        return texts[random.nextInt(texts.length)];
    }

    private String generateTitleText(String locale, Random random) {
        String[] titles = {"Dashboard", "Settings", "Profile", "Reports", "Analytics"};
        return titles[random.nextInt(titles.length)];
    }

    private String generateLabelText(String locale, Random random) {
        String[] labels = {"Name", "Email", "Phone", "Address", "Company"};
        return labels[random.nextInt(labels.length)];
    }

    private String generateGenericText(String locale, Random random) {
        String[] texts = {"Loading...", "Welcome", "Search", "Filter", "Sort"};
        return texts[random.nextInt(texts.length)];
    }

    private String generateRandomWord() {
        String[] words = {"home", "user", "admin", "product", "order", "payment", "profile", "search"};
        return words[ThreadLocalRandom.current().nextInt(words.length)];
    }

}