import { Link } from "react-router-dom";
import { ProfileSummary } from "../../lib/api";
import { SkeletonCard, EmptyState } from "../ui/Feedback";
import { ProfileAvatar } from "./ProfileAvatar";

export function UserList({ title, users, loading = false }: { title: string; users: ProfileSummary[]; loading?: boolean }) {
  return (
    <section className="content-card list-card">
      <h2>{title}</h2>
      {loading && <SkeletonCard />}
      {!loading && users.length === 0 && <EmptyState title="Nenhum resultado encontrado." compact />}
      {users.map((user) => <UserListItem key={user.id} user={user} />)}
    </section>
  );
}
export function UserListItem({ user }: { user: ProfileSummary }) {
  return (
    <div className="user-list-item">
      <ProfileAvatar profile={user} />
      <div className="user-list-info">
        <strong>@{user.username}</strong>
        <span>{user.displayName}</span>
      </div>
      <Link to={`/u/${user.username}`} className="secondary-button coral-text view-profile-btn">
        Ver perfil
      </Link>
    </div>
  );
}
