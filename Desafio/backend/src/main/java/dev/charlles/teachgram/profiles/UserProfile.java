package dev.charlles.teachgram.profiles;

import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_profiles")
public class UserProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserAccount user;

    @Column(nullable = false, length = 120)
    private String displayName;

    @Column(unique = true, length = 30)
    private String phone;

    @Column(length = 500)
    private String bio;

    @Column(columnDefinition = "text")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProfileVisibility profileVisibility = ProfileVisibility.PUBLIC;

    protected UserProfile() {
    }

    public UserProfile(UserAccount user, String displayName, String phone, String bio, String avatarUrl) {
        this.user = user;
        this.displayName = displayName;
        this.phone = phone;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
    }

    public UserAccount getUser() {
        return user;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhone() {
        return phone;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void update(String displayName, String phone, String bio, String avatarUrl) {
        if (displayName != null && !displayName.isBlank()) {
            this.displayName = displayName.trim();
        }
        this.phone = blankToNull(phone);
        this.bio = blankToNull(bio);
        this.avatarUrl = blankToNull(avatarUrl);
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}

