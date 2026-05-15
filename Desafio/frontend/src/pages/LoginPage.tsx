import { zodResolver } from "@hookform/resolvers/zod";
import { Link } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, Navigate } from "react-router-dom";
import z from "zod";
import { AuthShell } from "../components/layout/AuthShell";
import { AuthInput, InlineError } from "../components/ui/AuthInput";
import { PrimaryButton } from "../components/ui/Button";
import { useAuth, DEMO_EMAIL } from "../contexts/AuthContext";
import { messageFromError } from '../lib/api';

export const loginSchema = z.object({
  identifier: z.string().min(3),
  password: z.string().min(8),
});
export type LoginPayload = z.infer<typeof loginSchema>;
export function LoginPage() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const form = useForm<LoginPayload>({
    resolver: zodResolver(loginSchema),
    defaultValues: { identifier: DEMO_EMAIL, password: "Demo@123456" },
  });
  if (user) {
    return <Navigate to="/" replace />;
  }
  const submit = form.handleSubmit(async (values) => {
    setError("");
    try {
      await login(values.identifier, values.password);
      navigate("/");
    } catch (exception) {
      setError(messageFromError(exception));
    }
  });
  return (
    <AuthShell>
      <form className="auth-form" onSubmit={submit}>
        <h1>Faça seu login</h1>
        <AuthInput label="E-mail" placeholder="Digite seu E-mail" {...form.register("identifier")} error={form.formState.errors.identifier?.message} />
        <AuthInput label="Senha" type="password" placeholder="Digite sua senha" {...form.register("password")} error={form.formState.errors.password?.message} />
        
        <div className="login-options">
          <label className="checkbox-label">
            <input type="checkbox" />
            <span>Lembra senha</span>
          </label>
          <Link className="forgot-link" to="/forgot-password">Esqueci minha senha</Link>
        </div>

        {error && <InlineError message={error} />}
        <PrimaryButton loading={form.formState.isSubmitting}>Entrar</PrimaryButton>
        
        <p className="auth-link-row single" style={{ color: "var(--color-text-muted)" }}>
          Não possui conta? <Link to="/signup" style={{ color: "var(--color-brand-coral)", fontWeight: 600 }}>Cadastre-se</Link>
        </p>

        <div className="social-divider">
          <hr /><span>Entrar com</span><hr />
        </div>

        <div className="social-buttons-grid">
          <button type="button" className="ghost-button social-button">
            <img src="https://www.svgrepo.com/show/475656/google-color.svg" alt="Google" width="20" height="20" />
            Entrar com Google
          </button>
          <button type="button" className="ghost-button social-button">
            <img src="https://www.svgrepo.com/show/512317/github-142.svg" alt="Apple" width="20" height="20" />
            Entrar com Apple
          </button>
        </div>

        {/* Botão de demo sutil */}
        <button type="button" className="ghost-button demo-button-tiny" onClick={() => form.reset({ identifier: DEMO_EMAIL, password: "Demo@123456" })}>
          Autopreencher Demo
        </button>
      </form>
    </AuthShell>
  );
}
