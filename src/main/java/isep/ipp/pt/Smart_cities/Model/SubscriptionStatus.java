package isep.ipp.pt.Smart_cities.Model;

public enum SubscriptionStatus {
    SUBSCRIBED("Subscribed"),
    UNSUBSCRIBED("Unsubscribed"),
    CANCELLED("Cancelled"),
    ATTENDED("Attended");

    private final String status;

    SubscriptionStatus(String estado) {
        this.status = estado;
    }

    public String getStatus() {
        return status;
    }

}
