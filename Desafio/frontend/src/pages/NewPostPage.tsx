import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import z from "zod";
import { figmaAssets } from "../assets/figma/figmaAssets";
import { InlineError } from "../components/ui/AuthInput";
import { useAuth } from "../contexts/AuthContext";
import { apiRequest, messageFromError, PostResponse } from "../lib/api";

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
  const form = useForm<PostPayload>({
    resolver: zodResolver(postSchema),
    defaultValues: {
      title: "Nova Publicação",
      description: "",
      visibility: "PUBLIC",
      mediaUrl: "",
    },
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

  const submit = form.handleSubmit((values) => createPost.mutate(values));

  return (
    <div className="modal-overlay figma-new-post-overlay">
      <form className="figma-post-modal" onSubmit={submit}>
        <header>
          <button type="button" className="figma-modal-close" onClick={() => step === 2 && !editing ? setStep(1) : navigate(-1)}>
            {step === 2 && !editing ? "←" : "×"}
          </button>
          <h1>{editing ? "Editar publicação" : "Criar nova publicação"}</h1>
          {step === 1 && !editing ? (
            <button type="button" className="figma-modal-action" onClick={() => setStep(2)} disabled={!mediaUrlValue}>Avançar</button>
          ) : (
            <button type="submit" className="figma-modal-action" disabled={createPost.isPending}>
              {createPost.isPending ? "..." : (editing ? "Salvar" : "Compartilhar")}
            </button>
          )}
        </header>

        {step === 1 && !editing ? (
          <div className="p-6 flex flex-col gap-4">
            <p className="text-sm text-gray-500 text-center">Cole o link de uma imagem para começar</p>
            <input 
              className="figma-line-input coral-line-input !static !w-full" 
              placeholder="https://exemplo.com/imagem.jpg" 
              {...form.register("mediaUrl")} 
            />
            {mediaUrlValue && (
              <img className="figma-post-modal-image !static !max-h-60 object-contain rounded-lg" src={mediaUrlValue} alt="Preview" />
            )}
          </div>
        ) : (
          <div className="flex flex-col">
            <img className="figma-post-modal-image !static !max-h-60 object-cover" src={mediaUrlValue || figmaAssets.feedPhotoSecondary} alt="" />
            <textarea className="figma-post-caption" placeholder="Escreva uma legenda..." {...form.register("description")} autoFocus />
            <div className="px-6 pb-6">
               <label className="text-xs text-gray-400 block mb-1">Visibilidade</label>
               <select {...form.register("visibility")} className="w-full bg-gray-50 border-none rounded p-2 text-sm">
                  <option value="PUBLIC">Público</option>
                  <option value="FOLLOWERS">Amigos</option>
                  <option value="PRIVATE">Privado</option>
               </select>
            </div>
          </div>
        )}
        {createPost.error && <div className="px-6 pb-4"><InlineError message={messageFromError(createPost.error)} /></div>}
      </form>
    </div>
  );
}

