import { PostList } from "../components/features/PostList";
import { PageFrame } from "../components/layout/PageFrame";
import { useApi, CursorPage, PostResponse } from "../lib/api";

export function SavedPage() {
  const saved = useApi<CursorPage<PostResponse>>(["saved"], "/saved");
  return (
    <PageFrame title="Salvos">
      <PostList query={saved} emptyTitle="Posts salvos vao aparecer aqui." />
    </PageFrame>
  );
}
