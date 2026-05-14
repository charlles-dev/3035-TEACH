package dev.charlles.teachgram.ai;

import dev.charlles.teachgram.identity.UserAccount;
import dev.charlles.teachgram.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "ai_jobs")
public class AiJob extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(nullable = false, length = 50)
    private String provider;

    @Column(length = 120)
    private String model;

    @Column(nullable = false, length = 40)
    private String type;

    @Column(nullable = false, length = 40)
    private String status;

    @Column(nullable = false, length = 128)
    private String inputHash;

    @Column(length = 500)
    private String outputPreview;

    @Column(length = 80)
    private String errorCode;

    private Integer latencyMs;

    private Instant completedAt;

    protected AiJob() {
    }

    public AiJob(UserAccount user, String provider, String model, String type, String status,
                 String inputHash, String outputPreview, String errorCode, Integer latencyMs) {
        this.user = user;
        this.provider = provider;
        this.model = model;
        this.type = type;
        this.status = status;
        this.inputHash = inputHash;
        this.outputPreview = outputPreview;
        this.errorCode = errorCode;
        this.latencyMs = latencyMs;
        this.completedAt = Instant.now();
    }
}

