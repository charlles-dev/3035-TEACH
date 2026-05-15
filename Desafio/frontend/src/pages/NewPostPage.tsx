import { zodResolver } from "@hookform/resolvers/zod";
import { useQueryClient, useMutation } from "@tanstack/react-query";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import z from "zod";
import { AiAssistPanel } from "../components/features/AiAssistPanel";
import { AuthInput, InlineError } from "../components/ui/AuthInput";
import { useAuth } from "../contexts/AuthContext";
import { PostResponse } from "../lib/api";
import { apiRequest, messageFromError } from '../lib/api';
import { queryClient } from '../lib/queryClient';

export const postSchema = z.object({
  title: z.string().min(1).max(50),
  description: z.string().max(200).optional(),
  visibility: z.enum(["PUBLIC", "PRIVATE", "FOLLOWERS"]),
  mediaUrl: z.string().url().optional().or(z.literal("")),
});
export type PostPayload = z.infer<typeof postSchema>;
export function NewPostPage({ editing = false }: { editing?: boolean }) {
  const { token } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [step, setStep] = useState<1 | 2>(editing ? 2 : 1);
  const [aiSuggestion, setAiSuggestion] = useState("");
  const [aiState, setAiState] = useState<"idle" | "loading" | "fallback" | "success" | "error">("idle");
  const form = useForm<PostPayload>({
    resolver: zodResolver(postSchema),
    defaultValues: { title: "Publicação", description: "", visibility: "PUBLIC", mediaUrl: "" },
  });
  
  const mediaUrlValue = form.watch("mediaUrl");
  
  const createPost = useMutation({
    mutationFn: (values: PostPayload) => apiRequest<PostResponse>("/posts", token, {
      method: "POST",
      body: JSON.stringify({
        title: values.title,
        description: values.description,
        visibility: values.visibility,
        media: values.mediaUrl ? [{ type: "IMAGE", url: values.mediaUrl, altText: values.title }] : [],
      }),
    }),
    onSuccess: (post) => {
      queryClient.invalidateQueries({ queryKey: ["feed"] });
      navigate(`/posts/${post.id}`);
    },
  });

  const askAi = async () => {
    setAiState("loading");
    try {
      const response = await apiRequest<{ fallbackUsed: boolean; suggestions: string[] }>("/ai/caption", token, {
        method: "POST",
        body: JSON.stringify({ topic: form.getValues("title") || "novo post", tone: "casual", language: "pt-BR" }),
      });
      setAiSuggestion(response.suggestions[0] ?? "");
      setAiState(response.fallbackUsed ? "fallback" : "success");
    } catch {
      setAiState("error");
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content new-post-modal">
        <header className="modal-header">
           <button type="button" onClick={() => step === 2 && !editing ? setStep(1) : navigate(-1)}>Voltar</button>
           <h3>{editing ? "Editar publicação" : "Criar nova publicação"}</h3>
           {step === 1 && !editing ? (
             <button type="button" className="coral-text" onClick={() => setStep(2)}>Avançar</button>
           ) : (
             <button type="button" className="coral-text" onClick={form.handleSubmit((values) => createPost.mutate(values))}>Compartilhar</button>
           )}
        </header>

        <div className="modal-body">
           {step === 1 && !editing ? (
              <div className="step-1-content">
                 <AuthInput label="Cole a URL da sua foto" {...form.register("mediaUrl")} error={form.formState.errors.mediaUrl?.message} />
                 {mediaUrlValue && <img src={mediaUrlValue} alt="Preview" className="media-preview" />}
              </div>
           ) : (
              <div className="step-2-content">
                 <div className="step-2-media">
                   {mediaUrlValue ? <img src={mediaUrlValue} alt="Preview" /> : <div className="placeholder-media" />}
                 </div>
                 <div className="step-2-form">
                    <textarea placeholder="Escreva uma legenda..." rows={4} {...form.register("description")} className="clean-textarea" />
                    
                    <AiAssistPanel state={aiState} suggestion={aiSuggestion} onAsk={askAi} onApply={() => form.setValue("description", aiSuggestion)} />
                    
                    <select {...form.register("visibility")} className="clean-select" style={{ marginTop: "auto" }}>
                      <option value="PUBLIC">Público</option>
                      <option value="FOLLOWERS">Seguidores</option>
                      <option value="PRIVATE">Privado</option>
                    </select>
                    
                    {createPost.error && <InlineError message={messageFromError(createPost.error)} />}
                 </div>
              </div>
           )}
        </div>
      </div>
    </div>
  );
}
