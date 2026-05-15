import { useQueryClient, useMutation } from "@tanstack/react-query";
import { Link, MoreHorizontal, Heart, MessageCircle, Bookmark } from "lucide-react";
import { useAuth } from "../../contexts/AuthContext";
import { PostResponse } from "../../lib/api";
import { relativeTime } from "../../lib/format";
import { ModerationNotice } from "../ui/Feedback";
import { ProfileAvatar } from "./ProfileAvatar";
import { apiRequest } from '../../lib/api';
import { queryClient } from '../../lib/queryClient';

export function PostCard({ post, expanded = false }: { post: PostResponse; expanded?: boolean }) {
  const { token } = useAuth();
  const queryClient = useQueryClient();
  const like = useMutation({
    mutationFn: () => apiRequest(`/posts/${post.id}/likes`, token, { method: post.viewerState.liked ? "DELETE" : "POST" }),
    onSuccess: () => queryClient.invalidateQueries(),
  });
  const save = useMutation({
    mutationFn: () => apiRequest(`/posts/${post.id}/saves`, token, { method: post.viewerState.saved ? "DELETE" : "POST" }),
    onSuccess: () => queryClient.invalidateQueries(),
  });
  return (
    <article className={`post-card ${expanded ? "expanded" : ""}`}>
      <header className="post-header">
        <div className="post-author-info">
          <ProfileAvatar profile={post.author} />
          <Link to={`/u/${post.author.username}`} className="post-author-name">
            <strong>@{post.author.username}</strong>
            <span>{relativeTime(post.createdAt)}</span>
          </Link>
        </div>
        <div className="post-actions-menu">
          {post.viewerState.owner ? (
            <Link className="icon-button" to={`/posts/${post.id}/edit`} aria-label="Editar publicacao" style={{ border: 'none', minHeight: 'auto', padding: '5px' }}>
              <MoreHorizontal size={20} color="var(--color-text-muted)" />
            </Link>
          ) : (
            <button className="icon-button" type="button" style={{ border: 'none', minHeight: 'auto', padding: '5px' }}>
              <MoreHorizontal size={20} color="var(--color-text-muted)" />
            </button>
          )}
        </div>
      </header>

      <Link to={`/posts/${post.id}`} className="post-content">
        <p className="post-description">{post.description || post.title}</p>
      </Link>

      {post.media[0] && (
        <img className="post-media" src={post.media[0].url} alt={post.media[0].altText ?? post.title} />
      )}
      
      {post.moderationStatus !== "APPROVED" && <ModerationNotice status={post.moderationStatus} />}
      
      <footer className="post-footer">
        <button type="button" onClick={() => like.mutate()} className={`like-button ${post.viewerState.liked ? "active-action" : ""}`}>
          <Heart size={22} fill={post.viewerState.liked ? "var(--color-brand-coral)" : "none"} /> 
          <span className="likes-count">{post.stats.likes} curtidas</span>
        </button>
        <div className="post-secondary-actions">
          <Link to={`/posts/${post.id}`} style={{ color: "var(--color-text-muted)" }}><MessageCircle size={20} /></Link>
          <button type="button" onClick={() => save.mutate()} className={post.viewerState.saved ? "active-action" : ""} style={{ background: "transparent", border: "none", color: "var(--color-text-muted)", padding: 0 }}>
            <Bookmark size={20} fill={post.viewerState.saved ? "var(--color-brand-coral)" : "none"} />
          </button>
        </div>
      </footer>
    </article>
  );
}
