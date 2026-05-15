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
      title: "Publicação",
      description: editing ? "Hoje foi dia de passeio" : "",
      visibility: "PUBLIC",
      mediaUrl: figmaAssets.feedPhotoSecondary,
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
    <>
      <section className="page-frame feed-frame new-post-feed-backdrop" aria-hidden>
        <article className="post-card">
          <header className="post-header">
            <div className="post-author-info">
              <img className="avatar" src={figmaAssets.feedAvatarPrimary} alt="" />
              <div className="post-author-name"><strong>@JennieRubyJane</strong><span>há 5 min</span></div>
            </div>
          </header>
          <p className="post-description">Hoje foi dia de tomar uma vitamina refrescante</p>
          <img className="post-media" src={figmaAssets.feedPhotoPrimary} alt="" />
        </article>
      </section>
      <div className="modal-overlay figma-new-post-overlay">
        <form className="figma-post-modal" onSubmit={submit}>
          <header>
            <button type="button" className="figma-modal-close" onClick={() => step === 2 && !editing ? setStep(1) : navigate(-1)}>
              {step === 2 && !editing ? "←" : "×"}
            </button>
            <h1>{editing ? "Editar publicação" : "Criar nova publicação"}</h1>
            {step === 1 && !editing ? (
              <button type="button" className="figma-modal-action" onClick={() => setStep(2)}>Avançar</button>
            ) : (
              <button type="submit" className="figma-modal-action">{editing ? "Salvar" : "Compartilhar"}</button>
            )}
          </header>
          <input type="hidden" {...form.register("mediaUrl")} />
          <input type="hidden" {...form.register("title")} />
          <input type="hidden" {...form.register("visibility")} />
          <img className="figma-post-modal-image" src={mediaUrlValue || figmaAssets.feedPhotoSecondary} alt="" />
          {(step === 2 || editing) && (
            <textarea className="figma-post-caption" placeholder="Escreva uma legenda..." {...form.register("description")} />
          )}
          {createPost.error && <InlineError message={messageFromError(createPost.error)} />}
        </form>
      </div>
    </>
  );
}
