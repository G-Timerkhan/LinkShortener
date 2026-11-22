package org.example.repository;

import org.example.model.Link;

import java.util.HashMap;
import java.util.Map;

public class LinkRepository {
    // Хранилище: короткий код -> объект Link
    private final Map<String, Link> links = new HashMap<>();

    public void save(Link link) {
        links.put(link.getShortCode(), link);
    }

    public Link findByShortCode(String shortCode) {
        return links.get(shortCode);
    }

    public void delete(String shortCode) {
        links.remove(shortCode);
    }

    public Map<String, Link> getAllLinks() {
        return links;
    }
}