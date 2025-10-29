package com.pikolinc.domain;

public enum OfferStatus {
    OPEN("Open", "Offer is active and awaiting response"),
    ACCEPTED("Accepted", "Offer has been accepted"),
    REJECTED("Rejected", "Offer has been rejected"),
    COMPLETED("Completed", "Transaction has been completed"),
    CANCELLED("Cancelled", "Offer has been cancelled");

    private final String displayName;
    private final String description;

    OfferStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return this == OPEN;
    }

    public boolean isFinal() {
        return this == ACCEPTED || this == REJECTED ||
                this == COMPLETED || this == CANCELLED;
    }
}