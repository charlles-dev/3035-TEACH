import { zodResolver } from "@hookform/resolvers/zod";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import z from "zod";
import { AuthShell } from "../components/layout/AuthShell";
import { AuthInput, InlineError } from "../components/ui/AuthInput";
import { PrimaryButton } from "../components/ui/Button";
import { useAuth } from "../contexts/AuthContext";
import { messageFromError } from "../lib/api";

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
        <h1>Crie sua conta</h1>
        <AuthInput label="Nome" placeholder="Digite seu nome" {...form.register("name")} error={form.formState.errors.name?.message} />
        <AuthInput label="E-mail" placeholder="Digite seu E-mail" {...form.register("email")} error={form.formState.errors.email?.message} />
        <AuthInput label="Username" placeholder="@ seu_username" {...form.register("username")} error={form.formState.errors.username?.message} />
        <AuthInput label="Descrição" placeholder="Faça uma descrição" {...form.register("bio")} error={form.formState.errors.bio?.message} />
        <AuthInput label="Celular" placeholder="Digite se número de celular" {...form.register("phone")} error={form.formState.errors.phone?.message} />
        <AuthInput label="Senha" type="password" placeholder="Digite sua senha" {...form.register("password")} error={form.formState.errors.password?.message} />
        <input type="hidden" {...form.register("avatarUrl")} />
        {error && <InlineError message={error} />}
        <PrimaryButton loading={form.formState.isSubmitting}>Próximo</PrimaryButton>
        <p className="auth-link-row single auth-muted-link-row">
          Já possui conta? <Link className="auth-strong-link" to="/login">Entrar</Link>
        </p>
      </form>
    </AuthShell>
  );
}
