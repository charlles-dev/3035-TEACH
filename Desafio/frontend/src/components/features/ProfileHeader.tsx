import { useQueryClient, useMutation } from "@tanstack/react-query";
import { useAuth } from "../../contexts/AuthContext";
import { ProfileSummary } from "../../lib/api";
import { ProfileAvatar } from "./ProfileAvatar";
import { apiRequest } from '../../lib/api';

export type PublicProfile = ProfileSummary & {
  stats: { posts: number; followers: number; following: number };
  viewerState: { following: boolean; ownProfile: boolean };
};
export function ProfileHeader({ profile }: { profile: PublicProfile }) {
  const { token } = useAuth();
  const queryClient = useQueryClient();
  const follow = useMutation({
    mutationFn: () => apiRequest(`/relationships/${profile.id}/follow`, token, { method: "POST" }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["profile", profile.username] }),
  });
  return (
    <section className="profile-header-card">
      <div className="profile-header-top">
        <ProfileAvatar profile={profile} large />
        <div className="profile-header-info">
          <h2>{profile.displayName}</h2>
          <span>{profile.bio || "Sem biografia"}</span>
        </div>
        <div className="profile-stats">
          <strong>{profile.stats.posts} Posts</strong>
          <span className="stats-divider">|</span>
          <strong>{profile.stats.followers} Amigos</strong>
        </div>
        {!profile.viewerState.ownProfile && (
          <button type="button" className="primary-button follow-button" onClick={() => follow.mutate()}>
            {profile.viewerState.following ? "Seguindo" : "Seguir"}
          </button>
        )}
      </div>
    </section>
  );
}
