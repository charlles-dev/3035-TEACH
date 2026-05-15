import { ArrowLeft } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

export function SettingsAccountPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const displayName = user?.displayName ?? "Maria da Silva";
  const email = user?.email ?? "maria@email.com";

  return (
    <main className="figma-settings-screen">
      <button type="button" className="figma-back-button settings-back" onClick={() => navigate(-1)} aria-label="Voltar">
        <ArrowLeft size={28} strokeWidth={3} />
      </button>
      <section className="settings-account-form">
        <h1>Configurações da conta</h1>
        <dl className="settings-readonly-list">
          <div>
            <dt>Nome</dt>
            <dd>{displayName}</dd>
          </div>
          <div>
            <dt>Email</dt>
            <dd>{email}</dd>
          </div>
          <div>
            <dt>Celular</dt>
            <dd>55 51 99948 5500</dd>
          </div>
          <div>
            <dt>Senha</dt>
            <dd>****************</dd>
          </div>
        </dl>
        <button type="button" className="figma-small-primary">Salvar</button>
      </section>
    </main>
  );
}
