export function AiAssistPanel({ state, suggestion, onAsk, onApply }: {
  state: string;
  suggestion: string;
  onAsk: () => void;
  onApply: () => void;
}) {
  return (
    <section className={`ai-panel ${state}`}>
      <div>
        <strong>Assistente IA</strong>
        <p>{state === "idle" ? "Use IA para sugerir uma legenda, ou escreva manualmente." : aiStateText(state)}</p>
      </div>
      {suggestion && <blockquote>{suggestion}</blockquote>}
      <div className="inline-actions">
        <button type="button" className="secondary-button" onClick={onAsk}>Sugerir legenda</button>
        {suggestion && <button type="button" className="secondary-button coral-text" onClick={onApply}>Aplicar</button>}
      </div>
    </section>
  );
}
export function aiStateText(state: string) {
  if (state === "loading") return "Gerando sugestao...";
  if (state === "fallback") return "IA indisponivel agora. Geramos uma sugestao simples para voce continuar.";
  if (state === "success") return "Sugestao pronta para revisar.";
  if (state === "error") return "Nao conseguimos gerar a sugestao. Tente novamente mais tarde.";
  return "";
}
