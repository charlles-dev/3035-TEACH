import { Sparkles } from "lucide-react";
import { Link } from "react-router-dom";
import { CursorPage, PostResponse } from "../../lib/api";
import { SkeletonCard, EmptyState } from "../ui/Feedback";
import { PostCard } from "./PostCard";

export function ComposerLink() {
  return (
    <Link to="/new-post" className="composer-link">
      <span>Compartilhe uma publicacao</span>
      <Sparkles size={18} />
    </Link>
  );
}
export function PostList({ query, emptyTitle }: { query: { data?: CursorPage<PostResponse>; isLoading: boolean; error: unknown }; emptyTitle: string }) {
  if (query.isLoading) {
    return <><SkeletonCard /><SkeletonCard /></>;
  }
  if (query.error) {
    return <EmptyState title="Nao conseguimos carregar agora." />;
  }
  if (!query.data?.items.length) {
    return <EmptyState title={emptyTitle} />;
  }
  return <>{query.data.items.map((post) => <PostCard key={post.id} post={post} />)}</>;
}
