import { useState } from "react";
import { Settings } from "lucide-react";
import { figmaAssets } from "../assets/figma/figmaAssets";
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

  if (relationshipMode) {
    return (
      <PageFrame title="Adicionar Conexões">
        <section className="content-card search-panel">
          <p className="mb-4 text-gray-500">Busque por pessoas para expandir sua rede.</p>
          <input value={term} onChange={(event) => setTerm(event.target.value)} placeholder="Digite o nome ou @username" />
        </section>
        {term.trim().length < 2 && <EmptyState title="Digite ao menos dois caracteres para buscar amigos." />}
        {search.data && <UserList title="Pessoas encontradas" users={search.data.users} />}
        {search.isError && <EmptyState title="Erro ao buscar conexões." />}
      </PageFrame>
    );
  }

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
