import { ProfileSummary } from "../../lib/api";

export function ProfileAvatar({ profile, large = false }: { profile: Partial<ProfileSummary>; large?: boolean }) {
  return profile.avatarUrl ? (
    <img className={`avatar ${large ? "large" : ""}`} src={profile.avatarUrl} alt={profile.displayName ?? profile.username ?? "Avatar"} />
  ) : (
    <span className={`avatar fallback ${large ? "large" : ""}`}>{(profile.displayName ?? profile.username ?? "T").charAt(0).toUpperCase()}</span>
  );
}
