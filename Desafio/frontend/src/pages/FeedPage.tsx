import { Link, PlusSquare } from "lucide-react";
import { ComposerLink, PostList } from "../components/features/PostList";
import { PageFrame } from "../components/layout/PageFrame";
import { useApi, CursorPage, PostResponse } from "../lib/api";

export function FeedPage() {
  const feed = useApi<CursorPage<PostResponse>>(["feed"], "/feed");
  return (
    <PageFrame title="Feed" action={<Link className="icon-button coral" to="/new-post" aria-label="Criar publicacao"><PlusSquare size={20} /></Link>}>
      <ComposerLink />
      <PostList query={feed} emptyTitle="Seu feed ainda esta vazio." />
    </PageFrame>
  );
}
