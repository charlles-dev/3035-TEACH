import { Link } from "react-router-dom";
import { figmaAssets } from "../assets/figma/figmaAssets";
import { ProfileSummary, useApi } from "../lib/api";

const fallbackFriends: ProfileSummary[] = [
  { id: "1", username: "jennierubyjane", displayName: "Jennie Ruby Jane", avatarUrl: figmaAssets.feedAvatarPrimary },
  { id: "2", username: "adrianadossantos", displayName: "Adriana dos Santos", avatarUrl: figmaAssets.profileAvatarMaria },
  { id: "3", username: "annadias", displayName: "Anna Lorena Dias", avatarUrl: figmaAssets.profileEditAvatar },
  { id: "4", username: "brunoprado", displayName: "Bruno Westmann Prado", avatarUrl: figmaAssets.profileAvatarBruno },
];

export function ConnectionsPage() {
  const following = useApi<{ items: ProfileSummary[] }>(["following"], "/relationships/me/following");
  const users = following.data?.items.length ? following.data.items : fallbackFriends;

  return (
    <section className="official-profile-page official-profile-page-dimmed">
      <header className="official-profile-header official-profile-header-maria">
        <img className="official-profile-avatar" src={figmaAssets.profileAvatarMaria} alt="" />
        <div className="official-profile-copy">
          <h1>Maria</h1>
          <p>Fotógrafa</p>
          <p>O melhor de mim ainda está por vir. 🌹</p>
        </div>
      </header>
      <div className="official-profile-grid official-profile-grid-background">
        {figmaAssets.profileGrid.slice(0, 6).map((src, index) => (
          <img key={`${src}-${index}`} src={src} alt="" />
        ))}
      </div>
      <div className="friends-dialog" role="dialog" aria-modal="true" aria-labelledby="friends-title">
        <button type="button" className="friends-dialog-close" aria-label="Fechar">×</button>
        <h1 id="friends-title">Amigos</h1>
        <div className="friends-dialog-line" />
        <div className="friends-dialog-list">
          {users.slice(0, 4).map((user) => (
            <div key={user.id} className="friends-dialog-item">
              <img src={user.avatarUrl || figmaAssets.profileAvatarBruno} alt="" />
              <div>
                <strong>{user.username}</strong>
                <span>{user.displayName}</span>
              </div>
              <Link to={`/u/${user.username}`}>Ver perfil</Link>
            </div>
          ))}
        </div>
        <div className="friends-pagination">
          <button type="button" disabled>←</button>
          <button type="button" className="active">1</button>
          <button type="button">2</button>
          <button type="button">3</button>
          <button type="button">4</button>
          <button type="button">→</button>
        </div>
      </div>
    </section>
  );
}
