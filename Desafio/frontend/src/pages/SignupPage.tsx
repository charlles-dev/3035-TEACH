import { zodResolver } from "@hookform/resolvers/zod";
import { Link } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import z from "zod";
import { AuthShell } from "../components/layout/AuthShell";
import { AuthInput, InlineError } from "../components/ui/AuthInput";
import { PrimaryButton } from "../components/ui/Button";
import { useAuth } from "../contexts/AuthContext";
import { messageFromError } from '../lib/api';

export const signupSchema = z.object({
  name: z.string().min(2).max(120),
  username: z.string().regex(/^[a-zA-Z0-9._]{3,40}$/),
  email: z.string().email(),
  phone: z.string().max(30).optional(),
  password: z.string().min(8),
  bio: z.string().max(500).optional(),
  avatarUrl: z.string().url().optional().or(z.literal("")),
});
export type SignupPayload = z.infer<typeof signupSchema>;
export function SignupPage() {
  const { signup } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const form = useForm<SignupPayload>({
    resolver: zodResolver(signupSchema),
    defaultValues: { name: "", username: "", email: "", phone: "", password: "", bio: "", avatarUrl: "" },
  });
  const submit = form.handleSubmit(async (values) => {
    setError("");
    try {
      await signup(values);
      navigate("/");
    } catch (exception) {
      setError(messageFromError(exception));
    }
  });
  return (
    <AuthShell>
      <form className="auth-form signup-form" onSubmit={submit}>
        <h1>Criar conta</h1>
        <div className="two-col">
          <AuthInput label="Nome" {...form.register("name")} error={form.formState.errors.name?.message} />
          <AuthInput label="Username" {...form.register("username")} error={form.formState.errors.username?.message} />
        </div>
        <AuthInput label="Email" {...form.register("email")} error={form.formState.errors.email?.message} />
        <AuthInput label="Telefone" {...form.register("phone")} error={form.formState.errors.phone?.message} />
        <AuthInput label="Senha" type="password" {...form.register("password")} error={form.formState.errors.password?.message} />
        <AuthInput label="Foto de perfil URL" {...form.register("avatarUrl")} error={form.formState.errors.avatarUrl?.message} />
        <label className="field">
          <span>Bio</span>
          <textarea rows={3} {...form.register("bio")} />
        </label>
        {error && <InlineError message={error} />}
        <PrimaryButton loading={form.formState.isSubmitting}>Cadastrar</PrimaryButton>
        <p className="auth-link-row single"><Link to="/login">Ja tenho conta</Link></p>
      </form>
    </AuthShell>
  );
}
