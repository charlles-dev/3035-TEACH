import { figmaAssets } from "../../assets/figma/figmaAssets";

export function ModerationNotice({ status }: { status: string }) {
  return <div className="moderation-notice">{status === "HIDDEN" ? "Este post foi ocultado por seguranca." : "Em analise"}</div>;
}
export function EmptyState({ title, compact = false }: { title: string; compact?: boolean }) {
  return <section className={`empty-state ${compact ? "compact" : ""}`}><p>{title}</p></section>;
}
export function SkeletonCard() {
  return <section className="skeleton-card" />;
}
export function LoadingScreen() {
  return (
    <main className="loading-screen">
      <span className="loading-logo-mark" aria-hidden>
        <img className="loading-logo-bg" src={figmaAssets.logoMark} alt="" />
        <img className="loading-logo-glyph" src={figmaAssets.logoGlyph} alt="" />
      </span>
      <p>Carregando...</p>
    </main>
  );
}
