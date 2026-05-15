import { useParams } from "react-router-dom";
import { CommentComposer, CommentItem } from "../components/features/CommentItem";
import { PostCard } from "../components/features/PostCard";
import { PageFrame } from "../components/layout/PageFrame";
import { SkeletonCard, EmptyState } from "../components/ui/Feedback";
import { useApi, PostResponse, ProfileSummary } from "../lib/api";

export function PostDetailPage() {
  const { postId } = useParams();
  const post = useApi<PostResponse>(["post", postId], `/posts/${postId}`, Boolean(postId));
  const comments = useApi<{ items: CommentResponse[] }>(["comments", postId], `/posts/${postId}/comments`, Boolean(postId));
  return (
    <PageFrame title="Publicacao">
      {post.data && <PostCard post={post.data} expanded />}
      {post.isLoading && <SkeletonCard />}
      {post.error && <EmptyState title="Publicacao nao encontrada." />}
      <CommentComposer postId={postId ?? ""} />
      <div className="comments-list">
        {comments.data?.items.map((comment) => <CommentItem key={comment.id} comment={comment} />)}
      </div>
    </PageFrame>
  );
}
export type CommentResponse = { id: string; content: string; author: ProfileSummary; createdAt: string };
