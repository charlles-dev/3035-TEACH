import { PropsWithChildren } from "react";

export function PrimaryButton({ children, loading = false }: PropsWithChildren<{ loading?: boolean }>) {
  return <button className="primary-button" disabled={loading} type="submit">{loading ? "Carregando..." : children}</button>;
}
