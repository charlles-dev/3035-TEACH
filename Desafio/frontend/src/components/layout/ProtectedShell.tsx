import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import { LoadingScreen } from "../ui/Feedback";
import { SidebarNav } from "./SidebarNav";

export function ProtectedShell() {
  const { user, loading, logout } = useAuth();
  if (loading) {
    return <LoadingScreen />;
  }
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  return (
    <div className="social-shell">
      <SidebarNav onLogout={logout} user={user} />
      <main className="social-main">
        <Outlet />
      </main>
    </div>
  );
}
