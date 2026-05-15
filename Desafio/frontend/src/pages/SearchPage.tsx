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
      <section className="official-profile-page">
        <header className="official-profile-header">
          <img className="official-profile-avatar" src={figmaAssets.profileAvatarCarlos} alt="" />
          <div className="official-profile-copy">
            <div className="official-profile-title-row">
              <h1>Carlos Tozeli</h1>
              <Settings size={26} strokeWidth={1.7} />
            </div>
            <p>Empresário</p>
            <p>Durma com ideias, acorde com atitudes.</p>
            <button type="button" className="official-add-button">Adicionar</button>
          </div>
        </header>
        <div className="official-profile-stats">
          <div><strong>50</strong><span>Posts</span></div>
          <span className="official-stats-divider" />
          <div><strong>100</strong><span>Amigos</span></div>
        </div>
        <div className="official-profile-grid">
          {figmaAssets.profileGrid.slice(0, 6).map((src, index) => (
            <img key={`${src}-${index}`} src={src} alt="" />
          ))}
        </div>
      </section>
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
