import { PropsWithChildren } from "react";
import { figmaAssets } from "../../assets/figma/figmaAssets";
import { TeachgramLogo } from "./TeachgramLogo";

export function AuthShell({ children }: PropsWithChildren) {
  return (
    <main className="auth-shell">
      <section className="auth-panel">
        <TeachgramLogo />
        {children}
      </section>
      <section className="auth-art" aria-hidden>
        <img src={figmaAssets.authHero} alt="" className="auth-art-image" />
        <img src={figmaAssets.authOrnament} alt="" className="auth-art-decor auth-art-decor-one" />
        <img src={figmaAssets.authOrnament} alt="" className="auth-art-decor auth-art-decor-two" />
      </section>
    </main>
  );
}
