import { useQueryClient, useMutation } from "@tanstack/react-query";
import { Settings, LogOut } from "lucide-react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { figmaAssets } from "../assets/figma/figmaAssets";
import { useApi, CursorPage, PostResponse, apiRequest } from "../lib/api";
import { useAuth } from "../contexts/AuthContext";
import { SmartImage } from "../components/ui/SmartImage";

export function ProfilePage() {
  const { username } = useParams();
  const { user, token, logout } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const profile = useApi<any>(["profile", username], `/users/${username}`, Boolean(username));
  const posts = useApi<CursorPage<PostResponse>>(["profile-posts", username], `/users/${username}/posts`, Boolean(username));
  
  const data = profile.data;
  const isOwnProfile = user?.username === username;

  const followMutation = useMutation({
    mutationFn: () => apiRequest(`/users/${data?.id}/follow`, token, { 
      method: data?.viewerState?.following ? "DELETE" : "POST" 
    }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["profile", username] });
    }
  });

  if (profile.isLoading) {
    return <div className="flex items-center justify-center h-screen text-coral-500">Carregando perfil...</div>;
  }

  if (!data) {
    return <div className="flex items-center justify-center h-screen">Perfil não encontrado</div>;
  }

  const postMedia = posts.data?.items.flatMap((post) => post.media[0]?.url ? [post.media[0].url] : []) ?? [];
  const gridItems = postMedia.length ? postMedia : [];

  return (
    <section className="official-profile-page">
      <header className="official-profile-header">
        <div className="relative">
          <img 
            className="official-profile-avatar" 
            src={data.avatarUrl || figmaAssets.profileAvatarBruno} 
            alt={data.displayName} 
            loading="lazy"
          />
        </div>
        <div className="official-profile-copy">
          <div className="official-profile-title-row">
            <h1>{data.displayName || data.username}</h1>
            {isOwnProfile ? (
              <div className="flex gap-2">
                <button onClick={() => navigate("/settings/profile")} className="p-1 hover:bg-gray-100 rounded-full">
                  <Settings size={26} strokeWidth={1.7} />
                </button>
                <button onClick={() => logout()} className="p-1 hover:bg-red-50 rounded-full text-red-500">
                  <LogOut size={26} strokeWidth={1.7} />
                </button>
              </div>
            ) : (
              <Settings size={26} strokeWidth={1.7} />
            )}
          </div>
          <p className="text-gray-500 font-medium">{isOwnProfile ? "Seu Perfil" : "Estudante"}</p>
          <p className="mt-1">{data.bio || "Sem biografia ainda."}</p>
          
          {!isOwnProfile && (
            <button 
              type="button" 
              className={`official-friend-select ${data.viewerState?.following ? "!bg-gray-100 !text-gray-800" : ""}`}
              onClick={() => followMutation.mutate()}
              disabled={followMutation.isPending}
            >
              {followMutation.isPending ? "Processando..." : (data.viewerState?.following ? "Seguindo" : "Seguir")}
            </button>
          )}
          
          {isOwnProfile && (
            <div className="flex gap-2 mt-4">
              <button 
                type="button" 
                onClick={() => navigate("/settings/profile")}
                className="official-friend-select"
              >
                Editar Perfil
              </button>
              <button 
                type="button" 
                onClick={() => logout()}
                className="official-friend-select !bg-red-50 !text-red-500 !border-red-100"
              >
                Sair
              </button>
            </div>
          )}
        </div>
      </header>
      
      <div className="official-profile-stats">
        <div><strong>{data.stats?.posts ?? 0}</strong><span>Posts</span></div>
        <span className="official-stats-divider" />
        <div><strong>{data.stats?.followers ?? 0}</strong><span>Amigos</span></div>
      </div>

      <div className="official-profile-grid">
        {gridItems.length > 0 ? (
          gridItems.slice(0, 9).map((src, index) => (
            <SmartImage key={`${src}-${index}`} src={src} alt="" className="aspect-square object-cover rounded-lg" />
          ))
        ) : (
          <div className="col-span-3 py-20 text-center text-gray-400">
            Nenhuma publicação ainda.
          </div>
        )}
      </div>
    </section>
  );
}

