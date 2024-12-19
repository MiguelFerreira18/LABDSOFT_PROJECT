package isep.ipp.pt.Smart_cities.Model;

import java.time.LocalDate;

public class Milestone {
    private String name;
    private String description;
    private BadgeCategory category;
    private LocalDate completionDate;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BadgeCategory getCategory() {
        return category;
    }

    public void setCategory(BadgeCategory category) {
        this.category = category;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
}