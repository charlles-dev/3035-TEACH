import { useMutation } from "@tanstack/react-query";
import { ArrowLeft, ChevronRight } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import { apiRequest } from "../lib/api";

export function DeleteAccountPage() {
  const { token, logout } = useAuth();
  const navigate = useNavigate();
  const mutation = useMutation({
    mutationFn: () => apiRequest("/users/me", token, { method: "DELETE" }),
    onSuccess: logout,
  });

  return (
    <main className="figma-settings-screen settings-delete-screen">
      <button type="button" className="figma-back-button settings-back" onClick={() => navigate(-1)} aria-label="Voltar">
        <ArrowLeft size={28} strokeWidth={3} />
      </button>
      <nav className="settings-link-menu" aria-label="Configurações">
        <Link to="/settings/account">Configurações da conta <ChevronRight size={24} /></Link>
        <Link to="/settings/profile">Editar perfil <ChevronRight size={24} /></Link>
        <Link className="danger-text" to="/settings/delete">Excluir conta</Link>
      </nav>
      <div className="figma-modal-scrim" />
      <section className="delete-account-dialog" role="dialog" aria-modal="true" aria-labelledby="delete-account-title">
        <h1 id="delete-account-title">Excluir conta</h1>
        <div className="delete-account-dialog-body">
          <p>Todos os seus dados serão excluídos.</p>
          <div className="delete-account-actions">
            <button type="button" className="figma-small-secondary" onClick={() => navigate(-1)}>Cancelar</button>
            <button type="button" className="figma-small-primary" disabled={mutation.isPending} onClick={() => mutation.mutate()}>
              Confirmar
            </button>
          </div>
        </div>
      </section>
    </main>
  );
}
