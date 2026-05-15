import { Settings } from "lucide-react";
import { useParams } from "react-router-dom";
import { figmaAssets } from "../assets/figma/figmaAssets";
import { PublicProfile } from "../components/features/ProfileHeader";
import { useApi, CursorPage, PostResponse } from "../lib/api";

const fallbackProfile: PublicProfile = {
  id: "bruno",
  username: "brunoprado",
  displayName: "Bruno Prado",
  avatarUrl: figmaAssets.profileAvatarBruno,
  bio: "Durma com ideias, acorde com atitudes.",
  stats: { posts: 50, followers: 100, following: 0 },
  viewerState: { following: true, ownProfile: false },
};

export function ProfilePage() {
  const { username } = useParams();
  const profile = useApi<PublicProfile>(["profile", username], `/users/${username}`, Boolean(username));
  const posts = useApi<CursorPage<PostResponse>>(["profile-posts", username], `/users/${username}/posts`, Boolean(username));
  const data = profile.data ?? fallbackProfile;
  const postMedia = posts.data?.items.flatMap((post) => post.media[0]?.url ? [post.media[0].url] : []) ?? [];
  const gridItems = postMedia.length ? postMedia : figmaAssets.profileGrid;

  return (
    <section className="official-profile-page">
      <header className="official-profile-header">
        <img className="official-profile-avatar" src={data.avatarUrl || figmaAssets.profileAvatarBruno} alt="" />
        <div className="official-profile-copy">
          <div className="official-profile-title-row">
            <h1>{data.displayName}</h1>
            <Settings size={26} strokeWidth={1.7} />
          </div>
          <p>Empresário</p>
          <p>{data.bio || fallbackProfile.bio}</p>
          <button type="button" className="official-friend-select">{data.viewerState.following ? "Amigos" : "Adicionar"}</button>
        </div>
      </header>
      <div className="official-profile-stats">
        <div><strong>{data.stats.posts || 50}</strong><span>Posts</span></div>
        <span className="official-stats-divider" />
        <div><strong>{data.stats.followers || 100}</strong><span>Amigos</span></div>
      </div>
      <div className="official-profile-grid">
        {gridItems.slice(0, 6).map((src, index) => (
          <img key={`${src}-${index}`} src={src} alt="" />
        ))}
      </div>
    </section>
  );
}
