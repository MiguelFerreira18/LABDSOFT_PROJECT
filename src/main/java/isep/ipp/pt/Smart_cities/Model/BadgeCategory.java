package isep.ipp.pt.Smart_cities.Model;

public enum BadgeCategory {
    // Defining constants for badge categories
    BEGINNER_MILESTONES("Beginner Milestones"),
    SOCIAL_INTERACTION("Social Interaction"),
    FREQUENCY_LOYALTY("Frequency Loyalty");
    private final String category;

    BadgeCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}


