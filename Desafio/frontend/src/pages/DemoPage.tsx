import { useQuery } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import { PageFrame } from "../components/layout/PageFrame";
import { DEMO_EMAIL } from "../contexts/AuthContext";
import { API_BASE_URL } from "../lib/api";

export function DemoPage() {
  const health = useQuery({
    queryKey: ["health"],
    queryFn: () => fetch(`${API_BASE_URL.replace("/api/v1", "")}/actuator/health`).then((response) => response.json()),
  });
  return (
    <PageFrame title="Modo demo">
      <section className="content-card demo-card">
        <h2>Credenciais demo</h2>
        <p><strong>Email:</strong> {DEMO_EMAIL}</p>
        <p><strong>Senha:</strong> Demo@123456</p>
        <p><strong>Status API:</strong> {health.data?.status ?? "verificando"}</p>
        <Link className="secondary-button coral-text" to="/login">Abrir login</Link>
      </section>
    </PageFrame>
  );
}
