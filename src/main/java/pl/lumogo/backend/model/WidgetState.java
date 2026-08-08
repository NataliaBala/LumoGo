package pl.lumogo.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "widget_state")
public class WidgetState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private int streakDays = 0;

    @Column(nullable = false)
    private String nextReminder;

    @Column(columnDefinition = "TEXT")
    private String lastNotification;

    @Column(columnDefinition = "TEXT")
    private String lastMotivation;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public WidgetState() {
    }

    public WidgetState(String email, boolean active, int streakDays, String nextReminder, String lastNotification, String lastMotivation) {
        this.email = email;
        this.active = active;
        this.streakDays = streakDays;
        this.nextReminder = nextReminder;
        this.lastNotification = lastNotification;
        this.lastMotivation = lastMotivation;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }

    public String getNextReminder() {
        return nextReminder;
    }

    public void setNextReminder(String nextReminder) {
        this.nextReminder = nextReminder;
    }

    public String getLastNotification() {
        return lastNotification;
    }

    public void setLastNotification(String lastNotification) {
        this.lastNotification = lastNotification;
    }

    public String getLastMotivation() {
        return lastMotivation;
    }

    public void setLastMotivation(String lastMotivation) {
        this.lastMotivation = lastMotivation;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
