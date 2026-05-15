import { PropsWithChildren } from "react";
import { TeachgramLogo } from "./TeachgramLogo";

export function AuthShell({ children }: PropsWithChildren) {
  return (
    <main className="auth-shell">
      <section className="auth-panel">
        <TeachgramLogo />
        {children}
      </section>
      <section className="auth-art" aria-hidden>
        <img src="https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800&q=80" alt="Amigos tirando foto" className="auth-art-image" />
        <div className="auth-art-decor" />
      </section>
    </main>
  );
}
