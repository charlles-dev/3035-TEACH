CREATE TABLE user_accounts (
    id uuid PRIMARY KEY,
    email varchar(255) NOT NULL UNIQUE,
    username varchar(40) NOT NULL UNIQUE,
    password_hash varchar(255) NOT NULL,
    role varchar(30) NOT NULL,
    status varchar(30) NOT NULL,
    last_login_at timestamptz,
    deleted_at timestamptz,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE INDEX idx_user_accounts_status ON user_accounts(status);
CREATE INDEX idx_user_accounts_created_at ON user_accounts(created_at);

CREATE TABLE user_profiles (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL UNIQUE REFERENCES user_accounts(id),
    display_name varchar(120) NOT NULL,
    phone varchar(30) UNIQUE,
    bio varchar(500),
    avatar_url text,
    profile_visibility varchar(30) NOT NULL,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE TABLE refresh_tokens (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL REFERENCES user_accounts(id),
    token_hash varchar(255) NOT NULL UNIQUE,
    expires_at timestamptz NOT NULL,
    revoked_at timestamptz,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens(expires_at);

CREATE TABLE posts (
    id uuid PRIMARY KEY,
    author_id uuid NOT NULL REFERENCES user_accounts(id),
    title varchar(50) NOT NULL,
    description varchar(200),
    visibility varchar(30) NOT NULL,
    moderation_status varchar(30) NOT NULL,
    like_count integer NOT NULL DEFAULT 0,
    comment_count integer NOT NULL DEFAULT 0,
    save_count integer NOT NULL DEFAULT 0,
    deleted_at timestamptz,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE INDEX idx_posts_author_created ON posts(author_id, created_at DESC);
CREATE INDEX idx_posts_feed_public_created ON posts(visibility, created_at DESC);
CREATE INDEX idx_posts_moderation_status ON posts(moderation_status);

CREATE TABLE media_assets (
    id uuid PRIMARY KEY,
    post_id uuid NOT NULL REFERENCES posts(id),
    type varchar(30) NOT NULL,
    url text NOT NULL,
    storage_key text,
    alt_text varchar(255),
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE INDEX idx_media_assets_post_id ON media_assets(post_id);

CREATE TABLE follows (
    id uuid PRIMARY KEY,
    follower_id uuid NOT NULL REFERENCES user_accounts(id),
    followed_id uuid NOT NULL REFERENCES user_accounts(id),
    status varchar(30) NOT NULL,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL,
    CONSTRAINT uk_follows_pair UNIQUE(follower_id, followed_id),
    CONSTRAINT ck_follows_not_self CHECK (follower_id <> followed_id)
);

CREATE INDEX idx_follows_follower_id ON follows(follower_id);
CREATE INDEX idx_follows_followed_id ON follows(followed_id);

CREATE TABLE post_likes (
    id uuid PRIMARY KEY,
    post_id uuid NOT NULL REFERENCES posts(id),
    user_id uuid NOT NULL REFERENCES user_accounts(id),
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL,
    CONSTRAINT uk_post_likes_post_user UNIQUE(post_id, user_id)
);

CREATE INDEX idx_post_likes_post_id ON post_likes(post_id);
CREATE INDEX idx_post_likes_user_id ON post_likes(user_id);

CREATE TABLE post_comments (
    id uuid PRIMARY KEY,
    post_id uuid NOT NULL REFERENCES posts(id),
    author_id uuid NOT NULL REFERENCES user_accounts(id),
    content varchar(500) NOT NULL,
    deleted_at timestamptz,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE INDEX idx_post_comments_post_created ON post_comments(post_id, created_at);
CREATE INDEX idx_post_comments_author_id ON post_comments(author_id);

CREATE TABLE saved_posts (
    id uuid PRIMARY KEY,
    post_id uuid NOT NULL REFERENCES posts(id),
    user_id uuid NOT NULL REFERENCES user_accounts(id),
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL,
    CONSTRAINT uk_saved_posts_post_user UNIQUE(post_id, user_id)
);

CREATE TABLE feed_items (
    id uuid PRIMARY KEY,
    owner_id uuid NOT NULL REFERENCES user_accounts(id),
    post_id uuid NOT NULL REFERENCES posts(id),
    author_id uuid NOT NULL REFERENCES user_accounts(id),
    reason varchar(30) NOT NULL,
    score numeric(12,4) NOT NULL DEFAULT 0,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL,
    CONSTRAINT uk_feed_items_owner_post UNIQUE(owner_id, post_id)
);

CREATE INDEX idx_feed_items_owner_created ON feed_items(owner_id, created_at DESC);
CREATE INDEX idx_feed_items_owner_score ON feed_items(owner_id, score DESC);

CREATE TABLE notifications (
    id uuid PRIMARY KEY,
    recipient_id uuid NOT NULL REFERENCES user_accounts(id),
    actor_id uuid REFERENCES user_accounts(id),
    type varchar(40) NOT NULL,
    resource_type varchar(40),
    resource_id uuid,
    message varchar(255) NOT NULL,
    read_at timestamptz,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE INDEX idx_notifications_recipient_created ON notifications(recipient_id, created_at DESC);
CREATE INDEX idx_notifications_recipient_read ON notifications(recipient_id, read_at);

CREATE TABLE ai_jobs (
    id uuid PRIMARY KEY,
    user_id uuid REFERENCES user_accounts(id),
    provider varchar(50) NOT NULL,
    model varchar(120),
    type varchar(40) NOT NULL,
    status varchar(40) NOT NULL,
    input_hash varchar(128) NOT NULL,
    output_preview varchar(500),
    error_code varchar(80),
    latency_ms integer,
    completed_at timestamptz,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE INDEX idx_ai_jobs_user_created ON ai_jobs(user_id, created_at DESC);
CREATE INDEX idx_ai_jobs_status_created ON ai_jobs(status, created_at DESC);
CREATE INDEX idx_ai_jobs_type_created ON ai_jobs(type, created_at DESC);

CREATE TABLE moderation_events (
    id uuid PRIMARY KEY,
    resource_type varchar(40) NOT NULL,
    resource_id uuid NOT NULL,
    provider varchar(50) NOT NULL,
    model varchar(120),
    status varchar(40) NOT NULL,
    categories jsonb,
    raw_score numeric(8,4),
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE INDEX idx_moderation_events_resource ON moderation_events(resource_type, resource_id);
CREATE INDEX idx_moderation_events_status_created ON moderation_events(status, created_at DESC);

CREATE TABLE audit_events (
    id uuid PRIMARY KEY,
    actor_id uuid,
    action varchar(80) NOT NULL,
    resource_type varchar(40),
    resource_id uuid,
    ip_address varchar(80),
    user_agent text,
    metadata jsonb,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE INDEX idx_audit_events_actor_created ON audit_events(actor_id, created_at DESC);
CREATE INDEX idx_audit_events_action_created ON audit_events(action, created_at DESC);
CREATE INDEX idx_audit_events_resource ON audit_events(resource_type, resource_id);

CREATE TABLE outbox_events (
    id uuid PRIMARY KEY,
    aggregate_type varchar(80) NOT NULL,
    aggregate_id uuid NOT NULL,
    event_type varchar(120) NOT NULL,
    payload jsonb NOT NULL,
    status varchar(40) NOT NULL,
    attempts integer NOT NULL DEFAULT 0,
    last_error text,
    processed_at timestamptz,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL
);

CREATE INDEX idx_outbox_events_status_created ON outbox_events(status, created_at);
CREATE INDEX idx_outbox_events_aggregate ON outbox_events(aggregate_type, aggregate_id);

