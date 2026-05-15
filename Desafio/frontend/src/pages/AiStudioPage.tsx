import { useState } from "react";
import { PageFrame } from "../components/layout/PageFrame";
import { AuthInput } from "../components/ui/AuthInput";
import { useAuth } from "../contexts/AuthContext";
import { apiRequest } from '../lib/api';

export function AiStudioPage() {
  const [topic, setTopic] = useState("projeto fullstack com Spring e React");
  const [result, setResult] = useState("");
  const { token } = useAuth();
  const ask = async () => {
    const response = await apiRequest<{ suggestions: string[] }>("/ai/caption", token, {
      method: "POST",
      body: JSON.stringify({ topic, tone: "profissional", language: "pt-BR" }),
    });
    setResult(response.suggestions[0] ?? "");
  };
  return (
    <PageFrame title="Laboratorio IA">
      <section className="content-card form-card">
        <AuthInput label="Tema" value={topic} onChange={(event) => setTopic(event.target.value)} />
        <button className="secondary-button coral-text" type="button" onClick={ask}>Gerar legenda</button>
        {result && <blockquote className="ai-result">{result}</blockquote>}
      </section>
    </PageFrame>
  );
}
