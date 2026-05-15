import { useQueryClient, useMutation } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { PageFrame } from "../components/layout/PageFrame";
import { AuthInput } from "../components/ui/AuthInput";
import { PrimaryButton } from "../components/ui/Button";
import { useAuth } from "../contexts/AuthContext";
import { apiRequest } from '../lib/api';
import { queryClient } from '../lib/queryClient';

export function SettingsProfilePage({ photoFocus = false }: { photoFocus?: boolean }) {
  const { token, user } = useAuth();
  const queryClient = useQueryClient();
  const form = useForm({
    defaultValues: { displayName: user?.displayName ?? "", username: user?.username ?? "", email: user?.email ?? "", phone: "", bio: "", avatarUrl: user?.avatarUrl ?? "" },
  });
  const mutation = useMutation({
    mutationFn: (values: unknown) => apiRequest("/users/me", token, { method: "PATCH", body: JSON.stringify(values) }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["me"] }),
  });
  return (
    <PageFrame title={photoFocus ? "Foto de perfil" : "Editar perfil"}>
      <form className="content-card form-card" onSubmit={form.handleSubmit((values) => mutation.mutate(values))}>
        <AuthInput label="Nome" {...form.register("displayName")} />
        <AuthInput label="Username" {...form.register("username")} />
        <AuthInput label="Email" {...form.register("email")} />
        <AuthInput label="Telefone" {...form.register("phone")} />
        <AuthInput label="Foto URL" {...form.register("avatarUrl")} />
        <label className="field">
          <span>Bio</span>
          <textarea rows={4} {...form.register("bio")} />
        </label>
        {mutation.isSuccess && <div className="success-card">Perfil atualizado.</div>}
        <PrimaryButton loading={mutation.isPending}>Salvar</PrimaryButton>
      </form>
    </PageFrame>
  );
}
