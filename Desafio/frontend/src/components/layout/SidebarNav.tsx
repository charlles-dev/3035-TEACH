import { ArrowLeft, LogOut, Sparkles } from "lucide-react";
import { NavLink, useNavigate } from "react-router-dom";
import { figmaAssets } from "../../assets/figma/figmaAssets";
import { AuthUser } from "../../lib/api";
import { ProfileAvatar } from "../features/ProfileAvatar";
import { TeachgramLogo } from "./TeachgramLogo";

export function SidebarNav({ onLogout, user, withTopbar = false }: { onLogout: () => void; user: AuthUser; withTopbar?: boolean }) {
  const navigate = useNavigate();
  const items = [
    ["/", figmaAssets.homeIcon, "Feed"],
    ["/ai/studio", "sparkles", "IA Studio"],
    ["/friends", figmaAssets.friendsIcon, "Amigos"],
    [`/u/${user.username}`, "avatar", "Perfil"],
    ["/settings/account", figmaAssets.settingsIcon, "Configurações"],
  ] as const;

  return (
    <aside className="sidebar">
      {withTopbar ? (
        <div className="sidebar-topbar">
          <button type="button" className="figma-back-button" onClick={() => navigate(-1)} aria-label="Voltar">
            <ArrowLeft size={26} strokeWidth={3} />
          </button>
          <TeachgramLogo />
        </div>
      ) : (
        <TeachgramLogo />
      )}
      <nav className="sidebar-nav">
        {items.map(([href, icon, label]) => (
          <NavLink key={href} to={href} className={({ isActive }) => `sidebar-item ${icon === "avatar" ? "sidebar-item-profile" : ""} ${isActive ? "active" : ""}`}>
            {icon === "avatar" ? (
              <div className="sidebar-avatar-wrapper"><ProfileAvatar profile={user} /></div>
            ) : icon === "sparkles" ? (
              <div className="sidebar-icon-img flex items-center justify-center"><Sparkles size={24} /></div>
            ) : (
              <img className="sidebar-icon-img" src={icon} alt="" aria-hidden />
            )}
            <span>{label}</span>
          </NavLink>
        ))}
        <NavLink to="/new-post" className={({ isActive }) => `sidebar-item sidebar-item-create ${isActive ? "active" : ""}`}>
          <span className="create-icon" aria-hidden>
            <img className="create-icon-square" src={figmaAssets.createIconSquare} alt="" />
            <img className="create-icon-line create-icon-line-horizontal" src={figmaAssets.createIconStroke} alt="" />
            <img className="create-icon-line create-icon-line-vertical" src={figmaAssets.createIconStroke} alt="" />
          </span>
          <span>Criar</span>
        </NavLink>
      </nav>
      <button className="sidebar-item logout" onClick={onLogout} type="button">
        <LogOut size={26} strokeWidth={1.6} aria-hidden />
        <span>Sair</span>
      </button>
    </aside>
  );
}
