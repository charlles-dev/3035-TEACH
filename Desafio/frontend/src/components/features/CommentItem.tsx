import { useQueryClient, useMutation } from "@tanstack/react-query";
import { useState } from "react";
import { useAuth } from "../../contexts/AuthContext";
import { CommentResponse } from "../../pages/PostDetailPage";
import { ProfileAvatar } from "./ProfileAvatar";
import { apiRequest } from '../../lib/api';
import { queryClient } from '../../lib/queryClient';

export function CommentComposer({ postId }: { postId: string }) {
  const { token } = useAuth();
  const queryClient = useQueryClient();
  const [content, setContent] = useState("");
  const mutation = useMutation({
    mutationFn: () => apiRequest(`/posts/${postId}/comments`, token, { method: "POST", body: JSON.stringify({ content }) }),
    onSuccess: () => {
      setContent("");
      queryClient.invalidateQueries({ queryKey: ["comments", postId] });
      queryClient.invalidateQueries({ queryKey: ["post", postId] });
    },
  });
  return (
    <form className="comment-composer" onSubmit={(event) => { event.preventDefault(); if (content.trim()) mutation.mutate(); }}>
      <input value={content} onChange={(event) => setContent(event.target.value)} placeholder="Escreva um comentario" />
      <button type="submit">Comentar</button>
    </form>
  );
}
export function CommentItem({ comment }: { comment: CommentResponse }) {
  return (
    <article className="comment-item">
      <ProfileAvatar profile={comment.author} />
      <div>
        <strong>@{comment.author.username}</strong>
        <p>{comment.content}</p>
      </div>
    </article>
  );
}
