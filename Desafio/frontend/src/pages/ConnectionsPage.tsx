import { UserList } from "../components/features/UserList";
import { PageFrame } from "../components/layout/PageFrame";
import { useApi, ProfileSummary } from "../lib/api";

export function ConnectionsPage() {
  const following = useApi<{ items: ProfileSummary[] }>(["following"], "/relationships/me/following");
  const followers = useApi<{ items: ProfileSummary[] }>(["followers"], "/relationships/me/followers");
  
  // Como o Figma pede um modal simplificado, consolidamos aqui na tela cheia como se fosse o modal,
  // ou misturamos tudo na mesma lista caso seja a intencao de "Amigos" (Seguindo + Seguidores).
  // Vamos usar apenas 1 lista combinada ou duas sessoes limpas.
  
  return (
    <PageFrame title="Amigos">
      <section className="friends-container">
        <UserList title="Seus Amigos (Seguindo)" users={following.data?.items ?? []} loading={following.isLoading} />
        <UserList title="Seguidores" users={followers.data?.items ?? []} loading={followers.isLoading} />
      </section>
    </PageFrame>
  );
}
