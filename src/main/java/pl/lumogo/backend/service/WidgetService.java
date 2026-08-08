package pl.lumogo.backend.service;

import org.springframework.stereotype.Service;
import pl.lumogo.backend.dto.WidgetStatusResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WidgetService {

    private static final List<String> MOTIVATION_MESSAGES = List.of(
        "Dzisiaj dasz radę! Zrób kolejny krok do celu.",
        "Mały postęp to nadal postęp. Jesteś bliżej swojego celu niż wczoraj.",
        "Wykonaj dziś zadanie, a jutro będzie łatwiej.",
        "Pamiętaj: regularność buduje zwyczaje. Kontynuuj!"
    );

    private static final List<String> NOTIFICATIONS = List.of(
        "Sprawdź swój plan treningowy na dziś.",
        "Uzupełnij profil, by otrzymywać lepsze propozycje.",
        "Masz nowe pomysły na aktywność — zobacz je teraz.",
        "Zrób przegląd swoich zainteresowań i ustaw cel na dziś."
    );

    private static final List<String> SUGGESTIONS = List.of(
        "Wybierz nowy cel na dziś",
        "Sprawdź rekomendowane zainteresowania",
        "Zacznij sesję treningową",
        "Dodaj notatkę do swojego planu"
    );

    public WidgetStatusResponse getWidgetStatus(String email) {
        LocalDateTime now = LocalDateTime.now();
        String nextReminder = now.plusHours(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        int variation = Math.abs((email == null ? 0 : email.hashCode())) % MOTIVATION_MESSAGES.size();

        return new WidgetStatusResponse(
            "success",
            MOTIVATION_MESSAGES.get((now.getDayOfYear() + variation) % MOTIVATION_MESSAGES.size()),
            NOTIFICATIONS.get((now.getHour() + variation) % NOTIFICATIONS.size()),
            nextReminder,
            1 + ((now.getDayOfMonth() + variation) % 7),
            SUGGESTIONS,
            true,
            email == null ? null : email.trim().toLowerCase()
        );
    }
}
