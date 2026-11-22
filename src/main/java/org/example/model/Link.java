package org.example.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Link {
    private final String shortCode;
    private final String originalUrl;
    private final UUID ownerId;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private int maxClicks;
    private int currentClicks;

    public Link(String shortCode, String originalUrl, UUID ownerId, int maxClicks, int ttlDays) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.ownerId = ownerId;
        this.maxClicks = maxClicks;
        this.currentClicks = 0;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = createdAt.plusDays(ttlDays);
    }

    public String getShortCode() { return shortCode; }
    public String getOriginalUrl() { return originalUrl; }
    public UUID getOwnerId() { return ownerId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public int getMaxClicks() { return maxClicks; }
    public int getCurrentClicks() { return currentClicks; }

    public void setMaxClicks(int maxClicks) {
        this.maxClicks = maxClicks;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isLimitReached() {
        return currentClicks >= maxClicks;
    }

    public void incrementClicks() {
        this.currentClicks++;
    }

    @Override
    public String toString() {
        return "Link{" +
                "shortCode='" + shortCode + '\'' +
                ", originalUrl='" + originalUrl + '\'' +
                ", clicks=" + currentClicks + "/" + maxClicks +
                ", expiresAt=" + expiresAt +
                '}';
    }
}