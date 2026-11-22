package org.example.service;

import org.example.model.Link;
import org.example.model.User;
import org.example.repository.LinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkServiceTest {
    private LinkService service;
    private User testUser;

    @BeforeEach
    void setUp() {
        LinkRepository repository = new LinkRepository();
        service = new LinkService(repository);
        testUser = new User();
    }

    @Test
    void testCreateShortLink() {
        Link link = service.createShortLink("https://google.com", testUser.getId(), 5, 1);
        assertNotNull(link);
        assertEquals("https://google.com", link.getOriginalUrl());
        assertNotNull(link.getShortCode());
    }

    @Test
    void testLimitReached() {
        // Создаем ссылку с лимитом 1
        Link link = service.createShortLink("https://google.com", testUser.getId(), 1, 1);

        // Первый переход должен быть успешным
        assertEquals("https://google.com", service.goToLink(link.getShortCode()));

        // Второй переход должен вернуть ошибку
        assertTrue(service.goToLink(link.getShortCode()).startsWith("Ошибка"));
    }

    @Test
    void testDeleteOnlyByOwner() {
        Link link = service.createShortLink("https://google.com", testUser.getId(), 5, 1);
        User otherUser = new User();

        // Другой пользователь не может удалить
        assertFalse(service.deleteLink(link.getShortCode(), otherUser.getId()));

        // Владелец может удалить
        assertTrue(service.deleteLink(link.getShortCode(), testUser.getId()));
    }
}