import { useMutation } from "@tanstack/react-query";
import { Trash2 } from "lucide-react";
import { useState } from "react";
import { PageFrame } from "../components/layout/PageFrame";
import { useAuth } from "../contexts/AuthContext";
import { apiRequest } from '../lib/api';

export function DeleteAccountPage() {
  const { token, logout, user } = useAuth();
  const [confirm, setConfirm] = useState("");
  const mutation = useMutation({
    mutationFn: () => apiRequest("/users/me", token, { method: "DELETE" }),
    onSuccess: logout,
  });
  return (
    <PageFrame title="Excluir conta">
      <section className="content-card form-card danger-zone">
        <Trash2 size={32} />
        <h2>Excluir conta</h2>
        <p>Digite seu username para confirmar.</p>
        <input value={confirm} onChange={(event) => setConfirm(event.target.value)} />
        <button className="danger-button" disabled={confirm !== user?.username || mutation.isPending} onClick={() => mutation.mutate()} type="button">
          Excluir definitivamente
        </button>
      </section>
    </PageFrame>
  );
}
