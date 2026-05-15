import { useQueryClient, useMutation } from "@tanstack/react-query";
import { Bookmark, MessageCircle } from "lucide-react";
import { useState } from "react";
import { Link } from "react-router-dom";
import { figmaAssets } from "../../assets/figma/figmaAssets";
import { useAuth } from "../../contexts/AuthContext";
import { PostResponse } from "../../lib/api";
import { relativeTime } from "../../lib/format";
import { ModerationNotice } from "../ui/Feedback";
import { ProfileAvatar } from "./ProfileAvatar";
import { apiRequest } from "../../lib/api";
import { SmartImage } from "../ui/SmartImage";

export function PostCard({ post, expanded = false }: { post: PostResponse; expanded?: boolean }) {
  const { token } = useAuth();
  const queryClient = useQueryClient();
  const [menuOpen, setMenuOpen] = useState(false);
  const [confirmDeleteOpen, setConfirmDeleteOpen] = useState(false);
  const like = useMutation({
    mutationFn: () => apiRequest(`/posts/${post.id}/likes`, token, { method: post.viewerState.liked ? "DELETE" : "POST" }),
    onSuccess: () => queryClient.invalidateQueries(),
  });
  const save = useMutation({
    mutationFn: () => apiRequest(`/posts/${post.id}/saves`, token, { method: post.viewerState.saved ? "DELETE" : "POST" }),
    onSuccess: () => queryClient.invalidateQueries(),
  });
  const removePost = useMutation({
    mutationFn: () => apiRequest(`/posts/${post.id}`, token, { method: "DELETE" }),
    onSuccess: () => {
      setConfirmDeleteOpen(false);
      setMenuOpen(false);
      queryClient.invalidateQueries();
    },
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
            <button className="post-menu-button" type="button" aria-label="Mais opções" onClick={() => setMenuOpen((open) => !open)}>
              <img className="post-more-icon" src={figmaAssets.feedMoreIcon} alt="" aria-hidden />
            </button>
          ) : (
            <button className="post-menu-button" type="button" aria-label="Mais opções">
              <img className="post-more-icon" src={figmaAssets.feedMoreIcon} alt="" aria-hidden />
            </button>
          )}
          {post.viewerState.owner && menuOpen && (
            <div className="post-owner-menu" role="menu">
              <Link to={`/posts/${post.id}/edit`} role="menuitem">Editar</Link>
              <button
                type="button"
                role="menuitem"
                onClick={() => setConfirmDeleteOpen(true)}
              >
                Excluir
              </button>
            </div>
          )}
        </div>
      </header>

      <Link to={`/posts/${post.id}`} className="post-content">
        <p className="post-description">{post.description || post.title}</p>
      </Link>

      {post.media[0] && (
        <SmartImage 
          className="post-media w-full h-auto max-h-[500px] object-cover" 
          src={post.media[0].url} 
          alt={post.media[0].altText ?? post.title} 
        />
      )}
      
      {post.moderationStatus !== "APPROVED" && <ModerationNotice status={post.moderationStatus} />}
      
      <footer className="post-footer">
        <button type="button" onClick={() => like.mutate()} className={`like-button ${post.viewerState.liked ? "active-action" : ""}`}>
          <img className="post-heart-icon" src={figmaAssets.feedHeartIcon} alt="" aria-hidden />
          <span className="likes-count">{post.stats.likes} curtidas</span>
        </button>
        <div className="post-secondary-actions">
          <Link className="post-action-icon" to={`/posts/${post.id}`} aria-label="Ver comentarios"><MessageCircle size={20} /></Link>
          <button type="button" onClick={() => save.mutate()} className={`post-action-icon ${post.viewerState.saved ? "active-action" : ""}`} aria-label="Salvar publicacao">
            <Bookmark size={20} fill={post.viewerState.saved ? "var(--color-brand-coral)" : "none"} />
          </button>
        </div>
      </footer>
      {confirmDeleteOpen && (
        <div className="figma-delete-post-overlay" role="presentation">
          <section className="figma-delete-post-dialog" role="dialog" aria-modal="true" aria-labelledby={`delete-post-${post.id}`}>
            <h1 id={`delete-post-${post.id}`}>Excluir publicação ?</h1>
            <div className="figma-delete-post-actions">
              <button type="button" className="figma-delete-cancel" onClick={() => setConfirmDeleteOpen(false)}>Cancelar</button>
              <button type="button" className="figma-delete-confirm" onClick={() => removePost.mutate()} disabled={removePost.isPending}>
                Confirmar
              </button>
            </div>
          </section>
        </div>
      )}
    </article>
  );
}
