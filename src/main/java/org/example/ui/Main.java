package org.example.ui;

import org.example.model.Link;
import org.example.model.User;
import org.example.repository.LinkRepository;
import org.example.service.LinkService;

import java.awt.Desktop;
import java.net.URI;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        LinkRepository repository = new LinkRepository();
        LinkService service = new LinkService(repository);

        // Имитация пользователя (UUID генерируется один раз при запуске)
        User currentUser = new User();
        System.out.println("Ваш UUID: " + currentUser.getId());

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Меню ---");
            System.out.println("1. Сократить ссылку");
            System.out.println("2. Перейти по короткой ссылке");
            System.out.println("3. Редактировать лимит ссылки (только владелец)");
            System.out.println("4. Удалить ссылку (только владелец)");
            System.out.println("5. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Введите длинный URL: ");
                    String url = scanner.nextLine();
                    System.out.print("Максимум переходов (Enter - по умолчанию): ");
                    String maxStr = scanner.nextLine();
                    Integer maxClicks = maxStr.isEmpty() ? null : Integer.parseInt(maxStr);

                    Link link = service.createShortLink(url, currentUser.getId(), maxClicks, null);
                    System.out.println("Короткая ссылка создана: " + link.getShortCode());
                    break;

                case "2":
                    System.out.print("Введите короткий код: ");
                    String shortCode = scanner.nextLine();
                    String targetUrl = service.goToLink(shortCode);

                    if (targetUrl.startsWith("Ошибка")) {
                        System.out.println(targetUrl);
                    } else {
                        System.out.println("Переход по ссылке: " + targetUrl);
                        // Автоматическое открытие в браузере
                        try {
                            Desktop.getDesktop().browse(new URI(targetUrl));
                        } catch (Exception e) {
                            System.out.println("Не удалось открыть браузер автоматически.");
                        }
                    }
                    break;

                case "3":
                    System.out.print("Введите короткий код: ");
                    String editCode = scanner.nextLine();
                    System.out.print("Новый лимит переходов: ");
                    int newLimit = Integer.parseInt(scanner.nextLine());
                    if (service.editLinkLimit(editCode, currentUser.getId(), newLimit)) {
                        System.out.println("Лимит успешно изменен.");
                    }
                    break;

                case "4":
                    System.out.print("Введите короткий код: ");
                    String delCode = scanner.nextLine();
                    if (service.deleteLink(delCode, currentUser.getId())) {
                        System.out.println("Ссылка успешно удалена.");
                    }
                    break;

                case "5":
                    running = false;
                    System.out.println("Выход из программы.");
                    break;

                default:
                    System.out.println("Неверная команда.");
            }
        }
        scanner.close();
    }
}