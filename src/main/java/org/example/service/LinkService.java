package org.example.service;

import org.example.config.Config;
import org.example.model.Link;
import org.example.repository.LinkRepository;

import java.util.Random;
import java.util.UUID;

public class LinkService {
    private final LinkRepository repository;
    private final Random random = new Random();

    public LinkService(LinkRepository repository) {
        this.repository = repository;
    }

    // 1. Сокращение ссылки
    public Link createShortLink(String originalUrl, UUID userId, Integer maxClicks, Integer ttlDays) {
        // Если параметры не заданы, берем из конфига
        int actualMaxClicks = (maxClicks != null) ? maxClicks : Config.getDefaultMaxClicks();
        int actualTtlDays = (ttlDays != null) ? ttlDays : Config.getDefaultTtlDays();

        // Генерируем уникальный короткий код для КАЖДОГО пользователя
        String shortCode;
        do {
            shortCode = generateCode();
        } while (repository.findByShortCode(shortCode) != null);

        Link link = new Link(shortCode, originalUrl, userId, actualMaxClicks, actualTtlDays);
        repository.save(link);
        return link;
    }

    // 2. Переход по ссылке (проверка лимита и TTL)
    public String goToLink(String shortCode) {
        Link link = repository.findByShortCode(shortCode);

        if (link == null) {
            return "Ошибка: Ссылка не найдена.";
        }
        if (link.isExpired()) {
            repository.delete(shortCode); // Автоудаление по TTL
            return "Ошибка: Время жизни ссылки истекло. Ссылка удалена.";
        }
        if (link.isLimitReached()) {
            return "Ошибка: Лимит переходов исчерпан. Ссылка заблокирована.";
        }

        // Все ок - увеличиваем счетчик и возвращаем оригинальный URL
        link.incrementClicks();
        return link.getOriginalUrl();
    }

    // 3. Удаление ссылки (только владелец)
    public boolean deleteLink(String shortCode, UUID userId) {
        Link link = repository.findByShortCode(shortCode);
        if (link == null) return false;
        if (!link.getOwnerId().equals(userId)) {
            System.out.println("Ошибка: Вы не являетесь владельцем этой ссылки.");
            return false;
        }
        repository.delete(shortCode);
        return true;
    }

    // 4. Редактирование лимита (только владелец)
    public boolean editLinkLimit(String shortCode, UUID userId, int newLimit) {
        Link link = repository.findByShortCode(shortCode);
        if (link == null) return false;
        if (!link.getOwnerId().equals(userId)) {
            System.out.println("Ошибка: Вы не являетесь владельцем этой ссылки.");
            return false;
        }
        if (newLimit <= 0) {
            System.out.println("Ошибка: Лимит должен быть больше 0.");
            return false;
        }
        link.setMaxClicks(newLimit);
        return true;
    }

    // Генератор случайного кода (6 символов)
    private String generateCode() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}