import { Link, Bell } from "lucide-react";
import { ProfileAvatar } from "../components/features/ProfileAvatar";
import { PageFrame } from "../components/layout/PageFrame";
import { SkeletonCard, EmptyState } from "../components/ui/Feedback";
import { useApi, ProfileSummary } from "../lib/api";
import { relativeTime } from "../lib/format";

export function NotificationsPage() {
  const notifications = useApi<{ items: NotificationResponse[] }>(["notifications"], "/notifications");
  return (
    <PageFrame title="Notificacoes">
      <section className="content-card list-card">
        {notifications.isLoading && <SkeletonCard />}
        {notifications.data?.items.length === 0 && <EmptyState title="Nada novo por aqui." />}
        {notifications.data?.items.map((item) => <NotificationCard key={item.id} item={item} />)}
      </section>
    </PageFrame>
  );
}
export type NotificationResponse = {
  id: string;
  type: string;
  message: string;
  read: boolean;
  resourceType?: string;
  resourceId?: string;
  actor?: ProfileSummary;
  createdAt: string;
};
export function NotificationCard({ item }: { item: NotificationResponse }) {
  return (
    <Link className={`notification-card ${item.read ? "" : "unread"}`} to={item.resourceType === "POST" ? `/posts/${item.resourceId}` : "/notifications"}>
      {item.actor ? <ProfileAvatar profile={item.actor} /> : <Bell size={22} />}
      <div>
        <strong>{item.message}</strong>
        <p>{relativeTime(item.createdAt)}</p>
      </div>
    </Link>
  );
}
