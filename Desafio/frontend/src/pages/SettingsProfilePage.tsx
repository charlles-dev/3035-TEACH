import { useMutation, useQueryClient } from "@tanstack/react-query";
import { ArrowLeft } from "lucide-react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { figmaAssets } from "../assets/figma/figmaAssets";
import { AuthShell } from "../components/layout/AuthShell";
import { AuthInput, InlineError } from "../components/ui/AuthInput";
import { useAuth } from "../contexts/AuthContext";
import { apiRequest, messageFromError } from "../lib/api";

type ProfileForm = {
  displayName: string;
  username: string;
  email: string;
  phone: string;
  bio: string;
  avatarUrl: string;
};

export function SettingsProfilePage({ photoFocus = false }: { photoFocus?: boolean }) {
  const { token, user } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const form = useForm<ProfileForm>({
    defaultValues: {
      displayName: user?.displayName ?? "",
      username: user?.username ?? "",
      email: user?.email ?? "",
      phone: "",
      bio: user?.bio ?? "",
      avatarUrl: user?.avatarUrl ?? "",
    },
  });
  const mutation = useMutation({
    mutationFn: (values: ProfileForm) => apiRequest("/users/me", token, { method: "PATCH", body: JSON.stringify(values) }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["me"] }),
  });

  if (photoFocus) {
    return (
      <AuthShell>
        <button type="button" className="figma-back-button profile-photo-back" onClick={() => navigate(-1)} aria-label="Voltar">
          <ArrowLeft size={28} strokeWidth={3} />
        </button>
        <form className="profile-photo-form" onSubmit={form.handleSubmit((values) => mutation.mutate(values))}>
          <h1>Insira o link da sua foto de perfil</h1>
          <AuthInput label="Link" placeholder="Insira seu link" {...form.register("avatarUrl")} />
          {mutation.error && <InlineError message={messageFromError(mutation.error)} />}
          <button type="submit" className="primary-button">Salvar</button>
        </form>
      </AuthShell>
    );
  }

  const avatarPreview = form.watch("avatarUrl") || user?.avatarUrl || figmaAssets.profileEditAvatar;

  return (
    <main className="figma-settings-screen">
      <button type="button" className="figma-back-button settings-back" onClick={() => navigate(-1)} aria-label="Voltar">
        <ArrowLeft size={28} strokeWidth={3} />
      </button>
      <form className="settings-edit-profile" onSubmit={form.handleSubmit((values) => mutation.mutate(values))}>
        <h1>Editar Perfil</h1>
        <img className="settings-edit-avatar" src={avatarPreview} alt="" />
        <label>
          <span>Foto de perfil</span>
          <input className="figma-line-input coral-line-input" {...form.register("avatarUrl")} placeholder="https://www.google.com/search?sca_..." />
        </label>
        <label>
          <span>Nome</span>
          <input className="figma-line-input" {...form.register("displayName")} />
        </label>
        <label>
          <span>Nome do usuário</span>
          <input className="figma-line-input" {...form.register("username")} />
        </label>
        <label>
          <span>Bio</span>
          <input className="figma-line-input" {...form.register("bio")} />
        </label>
        {mutation.error && <InlineError message={messageFromError(mutation.error)} />}
        <div className="figma-form-actions">
          <button type="button" className="figma-small-secondary" onClick={() => navigate(-1)}>Cancelar</button>
          <button type="submit" className="figma-small-primary">Atualizar</button>
        </div>
      </form>
    </main>
  );
}
