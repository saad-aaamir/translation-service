package com.application.command;

import com.application.entity.Tag;
import com.application.entity.Translation;
import com.application.repository.TagRepository;
import com.application.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabasePopulationCommand implements CommandLineRunner {

    private final TranslationRepository translationRepository;
    private final TagRepository tagRepository;

    private static final String[] LOCALES = {"en", "fr", "es", "de", "it", "pt", "ru", "ja", "zh", "ar"};
    private static final String[] TAG_NAMES = {"mobile", "desktop", "web", "api", "ui", "error", "success", "warning", "info", "navigation"};
    private static final String[] KEY_PREFIXES = {"button", "label", "message", "title", "description", "placeholder", "tooltip", "alert", "notification", "menu"};

    @Override
    public void run(String... args) throws Exception {
        // Check if we should populate data (you can control this via application properties or command line args)
        if (args.length > 0 && "populate".equals(args[0])) {
            log.info("Starting database population with 100k+ records...");
            populateDatabase();
            log.info("Database population completed!");
        }
    }

    @Transactional
    public void populateDatabase() {
        // Clear existing data
        translationRepository.deleteAll();
        tagRepository.deleteAll();

        // Create tags first
        List<Tag> tags = createTags();

        // Create translations in batches for better performance
        createTranslationsInBatches(tags, 100000);

        log.info("Total translations created: {}", translationRepository.count());
        log.info("Total tags created: {}", tagRepository.count());
    }

    private List<Tag> createTags() {
        log.info("Creating tags...");
        List<Tag> tags = new ArrayList<>();

        for (String tagName : TAG_NAMES) {
            Tag tag = Tag.builder()
                    .name(tagName)
                    .description("Tag for " + tagName + " context")
                    .build();
            tags.add(tag);
        }

        return tagRepository.saveAll(tags);
    }

    private void createTranslationsInBatches(List<Tag> tags, int totalRecords) {
        int batchSize = 1000;
        int batches = totalRecords / batchSize;

        log.info("Creating {} translations in {} batches of {}", totalRecords, batches, batchSize);

        for (int batch = 0; batch < batches; batch++) {
            List<Translation> translations = new ArrayList<>();

            for (int i = 0; i < batchSize; i++) {
                int recordNumber = batch * batchSize + i;
                Translation translation = createTranslation(recordNumber, tags);
                translations.add(translation);
            }

            // Save batch
            translationRepository.saveAll(translations);

            // Log progress
            if ((batch + 1) % 10 == 0) {
                log.info("Completed batch {}/{} ({} records)", batch + 1, batches, (batch + 1) * batchSize);
            }

            // Clear the list to free memory
            translations.clear();
        }
    }

    private Translation createTranslation(int recordNumber, List<Tag> availableTags) {
        Random random = ThreadLocalRandom.current();

        // Generate translation key
        String prefix = KEY_PREFIXES[random.nextInt(KEY_PREFIXES.length)];
        String translationKey = prefix + "." + generateRandomWord() + "." + recordNumber;

        // Generate content
        String content = generateTranslationContent(translationKey, random);

        // Random locale
        String locale = LOCALES[random.nextInt(LOCALES.length)];

        Translation translation = new Translation(translationKey, content, locale);

        // Add random tags (1-3 tags per translation)
        int tagCount = random.nextInt(3) + 1;
        Set<Tag> selectedTags = new HashSet<>();

        while (selectedTags.size() < tagCount) {
            Tag randomTag = availableTags.get(random.nextInt(availableTags.size()));
            selectedTags.add(randomTag);
        }

        translation.setTags(selectedTags);

        return translation;
    }

    private String generateTranslationContent(String key, Random random) {
        // Generate realistic translation content based on key
        if (key.contains("button")) {
            return generateButtonText(random);
        } else if (key.contains("message") || key.contains("alert")) {
            return generateMessageText(random);
        } else if (key.contains("title")) {
            return generateTitleText(random);
        } else if (key.contains("label")) {
            return generateLabelText(random);
        } else {
            return generateGenericText(random);
        }
    }

    private String generateButtonText(Random random) {
        String[] buttons = {"Save", "Cancel", "Submit", "Delete", "Edit", "View", "Download", "Upload", "Login", "Logout"};
        return buttons[random.nextInt(buttons.length)];
    }

    private String generateMessageText(Random random) {
        String[] messages = {
                "Operation completed successfully",
                "An error occurred while processing your request",
                "Please fill in all required fields",
                "Your changes have been saved",
                "File uploaded successfully",
                "Invalid username or password",
                "Session expired, please login again",
                "Data updated successfully"
        };
        return messages[random.nextInt(messages.length)];
    }

    private String generateTitleText(Random random) {
        String[] titles = {
                "User Management", "Dashboard", "Settings", "Profile", "Reports",
                "Analytics", "Notifications", "Security", "System Configuration", "Help"
        };
        return titles[random.nextInt(titles.length)];
    }

    private String generateLabelText(Random random) {
        String[] labels = {
                "First Name", "Last Name", "Email Address", "Phone Number", "Date of Birth",
                "Address", "City", "Country", "Postal Code", "Company Name"
        };
        return labels[random.nextInt(labels.length)];
    }

    private String generateGenericText(Random random) {
        String[] texts = {
                "Welcome to our application",
                "Please wait while we process your request",
                "Loading...",
                "No data available",
                "Search results",
                "Filter options",
                "Sort by",
                "Items per page"
        };
        return texts[random.nextInt(texts.length)];
    }

    private String generateRandomWord() {
        String[] words = {"home", "user", "admin", "product", "order", "payment", "profile", "search", "filter", "sort"};
        return words[ThreadLocalRandom.current().nextInt(words.length)];
    }
}