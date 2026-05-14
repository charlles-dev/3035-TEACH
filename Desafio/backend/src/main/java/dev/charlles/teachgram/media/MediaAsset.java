package dev.charlles.teachgram.media;

import dev.charlles.teachgram.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "media_assets")
public class MediaAsset extends BaseEntity {

    @Column(nullable = false)
    private UUID postId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MediaType type;

    @Column(nullable = false, columnDefinition = "text")
    private String url;

    @Column(columnDefinition = "text")
    private String storageKey;

    @Column(length = 255)
    private String altText;

    protected MediaAsset() {
    }

    public MediaAsset(UUID postId, MediaType type, String url, String altText) {
        this.postId = postId;
        this.type = type;
        this.url = url;
        this.altText = altText;
    }

    public MediaType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getAltText() {
        return altText;
    }
}
