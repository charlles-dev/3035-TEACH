import { Link } from "lucide-react";
import { useState } from "react";
import { AuthShell } from "../components/layout/AuthShell";
import { AuthInput } from "../components/ui/AuthInput";
import { PrimaryButton } from "../components/ui/Button";

export function ForgotPasswordPage() {
  const [sent, setSent] = useState(false);
  return (
    <AuthShell>
      <form className="auth-form" onSubmit={(event) => { event.preventDefault(); setSent(true); }}>
        <h1>Recuperar senha</h1>
        <AuthInput label="Email" type="email" required />
        {sent && <div className="success-card">Se existir uma conta com esse e-mail, enviaremos as instrucoes.</div>}
        <PrimaryButton>Enviar instrucao</PrimaryButton>
        <p className="auth-link-row single"><Link to="/login">Voltar para login</Link></p>
      </form>
    </AuthShell>
  );
}
