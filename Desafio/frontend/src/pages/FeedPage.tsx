import { PostList } from "../components/features/PostList";
import { BottomNav } from "../components/layout/BottomNav";
import { useApi, CursorPage, PostResponse } from "../lib/api";

export function FeedPage() {
  const feed = useApi<CursorPage<PostResponse>>(["feed"], "/feed");
  return (
    <section className="page-frame feed-frame">
      <PostList query={feed} emptyTitle="Seu feed ainda esta vazio." />
      <BottomNav />
    </section>
  );
}
