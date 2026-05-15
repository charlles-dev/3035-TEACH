import { Link } from "lucide-react";
import { ProfileAvatar } from "../components/features/ProfileAvatar";
import { PageFrame } from "../components/layout/PageFrame";
import { useAuth } from "../contexts/AuthContext";

export function SettingsAccountPage() {
  const { user, logout } = useAuth();
  return (
    <PageFrame title="Configuracoes">
      <section className="content-card settings-card">
        <ProfileAvatar profile={{ id: user?.id ?? "", username: user?.username ?? "", displayName: user?.displayName ?? "Usuario", avatarUrl: user?.avatarUrl }} large />
        <div>
          <h2>{user?.displayName ?? user?.username}</h2>
          <p>{user?.email}</p>
        </div>
        <Link className="secondary-button" to="/settings/profile">Editar perfil</Link>
        <Link className="secondary-button danger-text" to="/settings/delete">Excluir conta</Link>
        <button className="secondary-button" type="button" onClick={logout}>Sair</button>
      </section>
    </PageFrame>
  );
}
