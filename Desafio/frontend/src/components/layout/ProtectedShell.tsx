import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import { LoadingScreen } from "../ui/Feedback";
import { SidebarNav } from "./SidebarNav";

export function ProtectedShell() {
  const { user, loading, logout } = useAuth();
  const location = useLocation();
  if (loading) {
    return <LoadingScreen />;
  }
  if (!user) {
    return <Navigate to="/login" replace />;
  }

  const standalone = location.pathname.startsWith("/settings") || location.pathname === "/profile-photo";
  if (standalone) {
    return <Outlet />;
  }

  const withTopbar =
    location.pathname === "/friends" ||
    location.pathname === "/relationships/add" ||
    location.pathname.startsWith("/u/");

  return (
    <div className={`social-shell ${withTopbar ? "with-topbar" : ""}`}>
      <SidebarNav onLogout={logout} user={user} withTopbar={withTopbar} />
      <main className="social-main">
        <Outlet />
      </main>
    </div>
  );
}
