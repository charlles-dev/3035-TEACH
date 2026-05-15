import { Link } from "lucide-react";
import { CursorPage, PostResponse } from "../../lib/api";
import { SkeletonCard, EmptyState } from "../ui/Feedback";

export function ProfileGrid({ query, emptyTitle }: { query: { data?: CursorPage<PostResponse>; isLoading: boolean; error: unknown }; emptyTitle: string }) {
  if (query.isLoading) {
    return <><SkeletonCard /><SkeletonCard /></>;
  }
  if (query.error) {
    return <EmptyState title="Nao conseguimos carregar agora." />;
  }
  if (!query.data?.items.length) {
    return <EmptyState title={emptyTitle} />;
  }
  return (
    <div className="profile-grid">
      {query.data.items.map((post) => (
        post.media[0] ? (
          <Link key={post.id} to={`/posts/${post.id}`} className="profile-grid-item">
            <img src={post.media[0].url} alt={post.media[0].altText ?? post.title} />
          </Link>
        ) : null
      ))}
    </div>
  );
}
