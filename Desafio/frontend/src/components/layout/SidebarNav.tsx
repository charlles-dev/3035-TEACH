import { Home, Users, Settings, PlusSquare, LogOut } from "lucide-react";
import { NavLink } from "react-router-dom";
import { AuthUser } from "../../lib/api";
import { ProfileAvatar } from "../features/ProfileAvatar";
import { TeachgramLogo } from "./TeachgramLogo";

export function SidebarNav({ onLogout, user }: { onLogout: () => void; user: AuthUser }) {
  const items = [
    ["/", Home, "Feed"],
    ["/friends", Users, "Amigos"],
    [`/u/${user.username}`, "avatar", "Perfil"],
    ["/settings/account", Settings, "Configurações"],
  ] as const;
  return (
    <aside className="sidebar">
      <TeachgramLogo />
      <nav className="sidebar-nav">
        {items.map(([href, Icon, label]) => (
          <NavLink key={href} to={href} className={({ isActive }) => `sidebar-item ${isActive ? "active" : ""}`}>
            {Icon === "avatar" ? (
              <div className="sidebar-avatar-wrapper"><ProfileAvatar profile={user} /></div>
            ) : (
              <Icon size={22} aria-hidden />
            )}
            <span>{label}</span>
          </NavLink>
        ))}
        <NavLink to="/new-post" className={({ isActive }) => `sidebar-item ${isActive ? "active" : ""}`}>
          <PlusSquare size={22} aria-hidden />
          <span>Criar</span>
        </NavLink>
      </nav>
      <button className="sidebar-item logout" onClick={onLogout} type="button">
        <LogOut size={22} aria-hidden />
        <span>Sair</span>
      </button>
    </aside>
  );
}
