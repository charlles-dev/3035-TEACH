import { TeachgramLogo } from "../layout/TeachgramLogo";

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
  return <main className="loading-screen"><TeachgramLogo /><p>Carregando...</p></main>;
}
