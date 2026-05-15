import { useState } from "react";
import { PostCard } from "../components/features/PostCard";
import { UserList } from "../components/features/UserList";
import { PageFrame } from "../components/layout/PageFrame";
import { EmptyState } from "../components/ui/Feedback";
import { useApi, ProfileSummary, PostResponse } from "../lib/api";

export function SearchPage({ relationshipMode = false }: { relationshipMode?: boolean }) {
  const [term, setTerm] = useState("");
  const [type, setType] = useState("all");
  const search = useApi<{ users: ProfileSummary[]; posts: PostResponse[] }>(
    ["search", term, type],
    `/search?q=${encodeURIComponent(term)}&type=${type}`,
    term.trim().length >= 2,
  );
  return (
    <PageFrame title={relationshipMode ? "Adicionar" : "Buscar"}>
      <section className="content-card search-panel">
        <input value={term} onChange={(event) => setTerm(event.target.value)} placeholder="Busque por usuarios, posts ou hashtags" />
        <div className="segmented">
          {["all", "users", "posts"].map((item) => (
            <button key={item} type="button" className={type === item ? "active" : ""} onClick={() => setType(item)}>
              {item === "all" ? "Tudo" : item === "users" ? "Usuarios" : "Posts"}
            </button>
          ))}
        </div>
      </section>
      {term.trim().length < 2 && <EmptyState title="Digite ao menos dois caracteres." />}
      {search.data && <UserList title="Usuarios" users={search.data.users} />}
      {search.data?.posts.map((post) => <PostCard key={post.id} post={post} />)}
      {search.isError && <EmptyState title="Nao conseguimos carregar agora." />}
    </PageFrame>
  );
}
