import { useParams } from "react-router-dom";
import { ProfileGrid } from "../components/features/ProfileGrid";
import { PublicProfile, ProfileHeader } from "../components/features/ProfileHeader";
import { PageFrame } from "../components/layout/PageFrame";
import { useApi, CursorPage, PostResponse } from "../lib/api";

export function ProfilePage() {
  const { username } = useParams();
  const profile = useApi<PublicProfile>(["profile", username], `/users/${username}`, Boolean(username));
  const posts = useApi<CursorPage<PostResponse>>(["profile-posts", username], `/users/${username}/posts`, Boolean(username));
  return (
    <PageFrame title={profile.data?.displayName ?? "Perfil"}>
      {profile.data && <ProfileHeader profile={profile.data} />}
      <ProfileGrid query={posts} emptyTitle="Nenhuma publicacao por aqui." />
    </PageFrame>
  );
}
