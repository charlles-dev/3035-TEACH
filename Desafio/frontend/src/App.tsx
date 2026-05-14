import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery, useQueryClient, QueryClient, QueryClientProvider } from "@tanstack/react-query";
import {
  Bell,
  Bookmark,
  Camera,
  Heart,
  Home,
  LogOut,
  MessageCircle,
  PlusSquare,
  Search,
  Settings,
  Sparkles,
  Trash2,
  UserPlus,
  Users,
} from "lucide-react";
import { createContext, PropsWithChildren, useContext, useEffect, useMemo, useState } from "react";
import type { InputHTMLAttributes, ReactNode } from "react";
import { useForm } from "react-hook-form";
import {
  BrowserRouter,
  Link,
  Navigate,
  NavLink,
  Outlet,
  Route,
  Routes,
  useNavigate,
  useParams,
} from "react-router-dom";
import { z } from "zod";

const queryClient = new QueryClient();
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081/api/v1";
const DEMO_EMAIL = import.meta.env.VITE_DEMO_EMAIL ?? "demo@teachgram.pro";

type AuthUser = {
  id: string;
  username: string;
  email: string;
  displayName?: string;
  avatarUrl?: string;
  role?: string;
};

type AuthResponse = {
  user: AuthUser;
  tokens: { accessToken: string; tokenType: string; expiresIn: number };
};

type ProfileSummary = {
  id: string;
  username: string;
  displayName: string;
  avatarUrl?: string;
  bio?: string;
};

type PostResponse = {
  id: string;
  author: ProfileSummary;
  title: string;
  description?: string;
  visibility: "PUBLIC" | "PRIVATE" | "FOLLOWERS";
  moderationStatus: "PENDING" | "APPROVED" | "REVIEW_REQUIRED" | "HIDDEN";
  media: { id: string; type: "IMAGE" | "VIDEO"; url: string; altText?: string }[];
  stats: { likes: number; comments: number; saves: number };
  viewerState: { liked: boolean; saved: boolean; owner: boolean };
  createdAt: string;
};

type CursorPage<T> = { items: T[]; nextCursor?: string; hasNext: boolean };

type ApiErrorBody = {
  title?: string;
  detail?: string;
  traceId?: string;
  errors?: Record<string, string[]>;
};

class ApiError extends Error {
  status: number;
  body?: ApiErrorBody;

  constructor(status: number, body?: ApiErrorBody) {
    super(body?.detail ?? body?.title ?? "Erro inesperado");
    this.status = status;
    this.body = body;
  }
}

type AuthContextValue = {
  token: string | null;
  user: AuthUser | null;
  loading: boolean;
  login: (identifier: string, password: string) => Promise<void>;
  signup: (payload: SignupPayload) => Promise<void>;
  logout: () => Promise<void>;
  setAuth: (auth: AuthResponse) => void;
};

const AuthContext = createContext<AuthContextValue | null>(null);

function useAuth() {
  const value = useContext(AuthContext);
  if (!value) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return value;
}

async function apiRequest<T>(path: string, token: string | null, options: RequestInit = {}): Promise<T> {
  const headers = new Headers(options.headers);
  if (!(options.body instanceof FormData)) {
    headers.set("Content-Type", "application/json");
  }
  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
    credentials: "include",
  });
  if (!response.ok) {
    let body: ApiErrorBody | undefined;
    try {
      body = await response.json();
    } catch {
      body = undefined;
    }
    throw new ApiError(response.status, body);
  }
  if (response.status === 204) {
    return undefined as T;
  }
  return response.json() as Promise<T>;
}

function AuthProvider({ children }: PropsWithChildren) {
  const [token, setToken] = useState<string | null>(null);
  const [user, setUser] = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(true);

  const setAuth = (auth: AuthResponse) => {
    setToken(auth.tokens.accessToken);
    setUser(auth.user);
  };

  useEffect(() => {
    apiRequest<AuthResponse>("/auth/refresh", null, { method: "POST" })
      .then(setAuth)
      .catch(() => undefined)
      .finally(() => setLoading(false));
  }, []);

  const value = useMemo<AuthContextValue>(() => ({
    token,
    user,
    loading,
    setAuth,
    login: async (identifier, password) => {
      const auth = await apiRequest<AuthResponse>("/auth/login", null, {
        method: "POST",
        body: JSON.stringify({ identifier, password }),
      });
      setAuth(auth);
    },
    signup: async (payload) => {
      const auth = await apiRequest<AuthResponse>("/auth/signup", null, {
        method: "POST",
        body: JSON.stringify(payload),
      });
      setAuth(auth);
    },
    logout: async () => {
      await apiRequest<void>("/auth/logout", token, { method: "POST" }).catch(() => undefined);
      setToken(null);
      setUser(null);
      queryClient.clear();
    },
  }), [loading, token, user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

function useApi<T>(key: unknown[], path: string, enabled = true) {
  const { token } = useAuth();
  return useQuery({
    queryKey: key,
    enabled: enabled && Boolean(token),
    queryFn: () => apiRequest<T>(path, token),
  });
}

function AppProviders({ children }: PropsWithChildren) {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>{children}</AuthProvider>
    </QueryClientProvider>
  );
}

export function App() {
  return (
    <AppProviders>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/forgot-password" element={<ForgotPasswordPage />} />
          <Route path="/demo" element={<DemoPage />} />
          <Route element={<ProtectedShell />}>
            <Route path="/" element={<FeedPage />} />
            <Route path="/new-post" element={<NewPostPage />} />
            <Route path="/posts/:postId" element={<PostDetailPage />} />
            <Route path="/posts/:postId/edit" element={<NewPostPage editing />} />
            <Route path="/friends" element={<ConnectionsPage />} />
            <Route path="/relationships/add" element={<SearchPage relationshipMode />} />
            <Route path="/u/:username" element={<ProfilePage />} />
            <Route path="/settings" element={<SettingsAccountPage />} />
            <Route path="/settings/account" element={<SettingsAccountPage />} />
            <Route path="/settings/profile" element={<SettingsProfilePage />} />
            <Route path="/settings/delete" element={<DeleteAccountPage />} />
            <Route path="/search" element={<SearchPage />} />
            <Route path="/notifications" element={<NotificationsPage />} />
            <Route path="/saved" element={<SavedPage />} />
            <Route path="/ai/studio" element={<AiStudioPage />} />
            <Route path="/profile-photo" element={<SettingsProfilePage photoFocus />} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AppProviders>
  );
}

function ProtectedShell() {
  const { user, loading, logout } = useAuth();
  if (loading) {
    return <LoadingScreen />;
  }
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  return (
    <div className="social-shell">
      <SidebarNav onLogout={logout} user={user} />
      <main className="social-main">
        <Outlet />
      </main>
    </div>
  );
}

function PageFrame({ title, children, action }: PropsWithChildren<{ title: string; action?: ReactNode }>) {
  return (
    <section className="page-frame">
      <header className="page-header">
        <div>
          <p className="eyebrow">TeachGram</p>
          <h1>{title}</h1>
        </div>
        {action}
      </header>
      {children}
      <BottomNav />
    </section>
  );
}

function SidebarNav({ onLogout, user }: { onLogout: () => void; user: AuthUser }) {
  const items = [
    ["/", Home, "Feed"],
    ["/search", Search, "Buscar"],
    ["/new-post", PlusSquare, "Publicar"],
    ["/notifications", Bell, "Notificacoes"],
    ["/saved", Bookmark, "Salvos"],
    ["/friends", Users, "Amigos"],
    [`/u/${user.username}`, Camera, "Perfil"],
    ["/settings/account", Settings, "Ajustes"],
  ] as const;
  return (
    <aside className="sidebar">
      <TeachgramLogo />
      <nav className="sidebar-nav">
        {items.map(([href, Icon, label]) => (
          <NavLink key={href} to={href} className={({ isActive }) => `sidebar-item ${isActive ? "active" : ""}`}>
            <Icon size={22} aria-hidden />
            <span>{label}</span>
          </NavLink>
        ))}
      </nav>
      <button className="sidebar-item logout" onClick={onLogout} type="button">
        <LogOut size={22} aria-hidden />
        <span>Sair</span>
      </button>
    </aside>
  );
}

function BottomNav() {
  return (
    <nav className="bottom-nav" aria-label="Navegacao principal">
      <NavLink to="/"><Home size={21} /></NavLink>
      <NavLink to="/search"><Search size={21} /></NavLink>
      <NavLink to="/new-post"><PlusSquare size={23} /></NavLink>
      <NavLink to="/notifications"><Bell size={21} /></NavLink>
      <NavLink to="/settings/account"><Settings size={21} /></NavLink>
    </nav>
  );
}

function TeachgramLogo() {
  return (
    <Link to="/" className="logo" aria-label="TeachGram">
      <span className="logo-mark">T</span>
      <span>Teachgram</span>
    </Link>
  );
}

function AuthShell({ children }: PropsWithChildren) {
  return (
    <main className="auth-shell">
      <section className="auth-panel">
        <TeachgramLogo />
        {children}
      </section>
      <section className="auth-art" aria-hidden>
        <div className="auth-art-card">
          <span />
          <strong>Teachgram</strong>
        </div>
      </section>
    </main>
  );
}

const loginSchema = z.object({
  identifier: z.string().min(3),
  password: z.string().min(8),
});

type LoginPayload = z.infer<typeof loginSchema>;

function LoginPage() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const form = useForm<LoginPayload>({
    resolver: zodResolver(loginSchema),
    defaultValues: { identifier: DEMO_EMAIL, password: "Demo@123456" },
  });
  if (user) {
    return <Navigate to="/" replace />;
  }
  const submit = form.handleSubmit(async (values) => {
    setError("");
    try {
      await login(values.identifier, values.password);
      navigate("/");
    } catch (exception) {
      setError(messageFromError(exception));
    }
  });
  return (
    <AuthShell>
      <form className="auth-form" onSubmit={submit}>
        <h1>Entrar</h1>
        <AuthInput label="Email ou username" {...form.register("identifier")} error={form.formState.errors.identifier?.message} />
        <AuthInput label="Senha" type="password" {...form.register("password")} error={form.formState.errors.password?.message} />
        {error && <InlineError message={error} />}
        <PrimaryButton loading={form.formState.isSubmitting}>Entrar</PrimaryButton>
        <button type="button" className="ghost-button" onClick={() => form.reset({ identifier: DEMO_EMAIL, password: "Demo@123456" })}>
          Usar usuario demo
        </button>
        <p className="auth-link-row">
          <Link to="/forgot-password">Esqueci minha senha</Link>
          <Link to="/signup">Criar conta</Link>
        </p>
      </form>
    </AuthShell>
  );
}

const signupSchema = z.object({
  name: z.string().min(2).max(120),
  username: z.string().regex(/^[a-zA-Z0-9._]{3,40}$/),
  email: z.string().email(),
  phone: z.string().max(30).optional(),
  password: z.string().min(8),
  bio: z.string().max(500).optional(),
  avatarUrl: z.string().url().optional().or(z.literal("")),
});

type SignupPayload = z.infer<typeof signupSchema>;

function SignupPage() {
  const { signup } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const form = useForm<SignupPayload>({
    resolver: zodResolver(signupSchema),
    defaultValues: { name: "", username: "", email: "", phone: "", password: "", bio: "", avatarUrl: "" },
  });
  const submit = form.handleSubmit(async (values) => {
    setError("");
    try {
      await signup(values);
      navigate("/");
    } catch (exception) {
      setError(messageFromError(exception));
    }
  });
  return (
    <AuthShell>
      <form className="auth-form signup-form" onSubmit={submit}>
        <h1>Criar conta</h1>
        <div className="two-col">
          <AuthInput label="Nome" {...form.register("name")} error={form.formState.errors.name?.message} />
          <AuthInput label="Username" {...form.register("username")} error={form.formState.errors.username?.message} />
        </div>
        <AuthInput label="Email" {...form.register("email")} error={form.formState.errors.email?.message} />
        <AuthInput label="Telefone" {...form.register("phone")} error={form.formState.errors.phone?.message} />
        <AuthInput label="Senha" type="password" {...form.register("password")} error={form.formState.errors.password?.message} />
        <AuthInput label="Foto de perfil URL" {...form.register("avatarUrl")} error={form.formState.errors.avatarUrl?.message} />
        <label className="field">
          <span>Bio</span>
          <textarea rows={3} {...form.register("bio")} />
        </label>
        {error && <InlineError message={error} />}
        <PrimaryButton loading={form.formState.isSubmitting}>Cadastrar</PrimaryButton>
        <p className="auth-link-row single"><Link to="/login">Ja tenho conta</Link></p>
      </form>
    </AuthShell>
  );
}

function ForgotPasswordPage() {
  const [sent, setSent] = useState(false);
  return (
    <AuthShell>
      <form className="auth-form" onSubmit={(event) => { event.preventDefault(); setSent(true); }}>
        <h1>Recuperar senha</h1>
        <AuthInput label="Email" type="email" required />
        {sent && <div className="success-card">Se existir uma conta com esse e-mail, enviaremos as instrucoes.</div>}
        <PrimaryButton>Enviar instrucao</PrimaryButton>
        <p className="auth-link-row single"><Link to="/login">Voltar para login</Link></p>
      </form>
    </AuthShell>
  );
}

function FeedPage() {
  const feed = useApi<CursorPage<PostResponse>>(["feed"], "/feed");
  return (
    <PageFrame title="Feed" action={<Link className="icon-button coral" to="/new-post" aria-label="Criar publicacao"><PlusSquare size={20} /></Link>}>
      <ComposerLink />
      <PostList query={feed} emptyTitle="Seu feed ainda esta vazio." />
    </PageFrame>
  );
}

function ComposerLink() {
  return (
    <Link to="/new-post" className="composer-link">
      <span>Compartilhe uma publicacao</span>
      <Sparkles size={18} />
    </Link>
  );
}

const postSchema = z.object({
  title: z.string().min(1).max(50),
  description: z.string().max(200).optional(),
  visibility: z.enum(["PUBLIC", "PRIVATE", "FOLLOWERS"]),
  mediaUrl: z.string().url().optional().or(z.literal("")),
});

type PostPayload = z.infer<typeof postSchema>;

function NewPostPage({ editing = false }: { editing?: boolean }) {
  const { token } = useAuth();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [aiSuggestion, setAiSuggestion] = useState("");
  const [aiState, setAiState] = useState<"idle" | "loading" | "fallback" | "success" | "error">("idle");
  const form = useForm<PostPayload>({
    resolver: zodResolver(postSchema),
    defaultValues: { title: "", description: "", visibility: "PUBLIC", mediaUrl: "" },
  });
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
    <PageFrame title={editing ? "Editar publicacao" : "Nova publicacao"}>
      <form className="content-card form-card" onSubmit={form.handleSubmit((values) => createPost.mutate(values))}>
        <AuthInput label="Titulo" {...form.register("title")} error={form.formState.errors.title?.message} />
        <label className="field">
          <span>Legenda</span>
          <textarea rows={5} {...form.register("description")} />
        </label>
        <AuthInput label="Imagem URL" {...form.register("mediaUrl")} error={form.formState.errors.mediaUrl?.message} />
        <label className="field">
          <span>Privacidade</span>
          <select {...form.register("visibility")}>
            <option value="PUBLIC">Publico</option>
            <option value="FOLLOWERS">Seguidores</option>
            <option value="PRIVATE">Privado</option>
          </select>
        </label>
        <AiAssistPanel state={aiState} suggestion={aiSuggestion} onAsk={askAi} onApply={() => form.setValue("description", aiSuggestion)} />
        {createPost.error && <InlineError message={messageFromError(createPost.error)} />}
        <PrimaryButton loading={createPost.isPending}>Publicar</PrimaryButton>
      </form>
    </PageFrame>
  );
}

function AiAssistPanel({ state, suggestion, onAsk, onApply }: {
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

function PostDetailPage() {
  const { postId } = useParams();
  const post = useApi<PostResponse>(["post", postId], `/posts/${postId}`, Boolean(postId));
  const comments = useApi<{ items: CommentResponse[] }>(["comments", postId], `/posts/${postId}/comments`, Boolean(postId));
  return (
    <PageFrame title="Publicacao">
      {post.data && <PostCard post={post.data} expanded />}
      {post.isLoading && <SkeletonCard />}
      {post.error && <EmptyState title="Publicacao nao encontrada." />}
      <CommentComposer postId={postId ?? ""} />
      <div className="comments-list">
        {comments.data?.items.map((comment) => <CommentItem key={comment.id} comment={comment} />)}
      </div>
    </PageFrame>
  );
}

type CommentResponse = { id: string; content: string; author: ProfileSummary; createdAt: string };

function CommentComposer({ postId }: { postId: string }) {
  const { token } = useAuth();
  const queryClient = useQueryClient();
  const [content, setContent] = useState("");
  const mutation = useMutation({
    mutationFn: () => apiRequest(`/posts/${postId}/comments`, token, { method: "POST", body: JSON.stringify({ content }) }),
    onSuccess: () => {
      setContent("");
      queryClient.invalidateQueries({ queryKey: ["comments", postId] });
      queryClient.invalidateQueries({ queryKey: ["post", postId] });
    },
  });
  return (
    <form className="comment-composer" onSubmit={(event) => { event.preventDefault(); if (content.trim()) mutation.mutate(); }}>
      <input value={content} onChange={(event) => setContent(event.target.value)} placeholder="Escreva um comentario" />
      <button type="submit">Comentar</button>
    </form>
  );
}

function CommentItem({ comment }: { comment: CommentResponse }) {
  return (
    <article className="comment-item">
      <ProfileAvatar profile={comment.author} />
      <div>
        <strong>@{comment.author.username}</strong>
        <p>{comment.content}</p>
      </div>
    </article>
  );
}

function ProfilePage() {
  const { username } = useParams();
  const profile = useApi<PublicProfile>(["profile", username], `/users/${username}`, Boolean(username));
  const posts = useApi<CursorPage<PostResponse>>(["profile-posts", username], `/users/${username}/posts`, Boolean(username));
  return (
    <PageFrame title={profile.data?.displayName ?? "Perfil"}>
      {profile.data && <ProfileHeader profile={profile.data} />}
      <PostList query={posts} emptyTitle="Nenhuma publicacao por aqui." />
    </PageFrame>
  );
}

type PublicProfile = ProfileSummary & {
  stats: { posts: number; followers: number; following: number };
  viewerState: { following: boolean; ownProfile: boolean };
};

function ProfileHeader({ profile }: { profile: PublicProfile }) {
  const { token } = useAuth();
  const queryClient = useQueryClient();
  const follow = useMutation({
    mutationFn: () => apiRequest(`/relationships/${profile.id}/follow`, token, { method: "POST" }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["profile", profile.username] }),
  });
  return (
    <section className="profile-header-card">
      <ProfileAvatar profile={profile} large />
      <div>
        <h2>{profile.displayName}</h2>
        <p>@{profile.username}</p>
        <span>{profile.bio}</span>
      </div>
      {!profile.viewerState.ownProfile && (
        <button type="button" className="secondary-button coral-text" onClick={() => follow.mutate()}>
          {profile.viewerState.following ? "Seguindo" : "Seguir"}
        </button>
      )}
    </section>
  );
}

function ConnectionsPage() {
  const following = useApi<{ items: ProfileSummary[] }>(["following"], "/relationships/me/following");
  const followers = useApi<{ items: ProfileSummary[] }>(["followers"], "/relationships/me/followers");
  return (
    <PageFrame title="Amigos">
      <section className="split-list">
        <UserList title="Seguindo" users={following.data?.items ?? []} loading={following.isLoading} />
        <UserList title="Seguidores" users={followers.data?.items ?? []} loading={followers.isLoading} />
      </section>
    </PageFrame>
  );
}

function SearchPage({ relationshipMode = false }: { relationshipMode?: boolean }) {
  const [term, setTerm] = useState("");
  const [type, setType] = useState("all");
  const search = useApi<{ users: ProfileSummary[]; posts: PostResponse[] }>(
    ["search", term, type],
    `/search?q=${encodeURIComponent(term)}&type=${type}`,
    term.trim().length >= 2,
  );
  return (
    <PageFrame title={relationshipMode ? "Adicionar" : "Buscar"}>
      <section className="content-card search-panel">
        <input value={term} onChange={(event) => setTerm(event.target.value)} placeholder="Busque por usuarios, posts ou hashtags" />
        <div className="segmented">
          {["all", "users", "posts"].map((item) => (
            <button key={item} type="button" className={type === item ? "active" : ""} onClick={() => setType(item)}>
              {item === "all" ? "Tudo" : item === "users" ? "Usuarios" : "Posts"}
            </button>
          ))}
        </div>
      </section>
      {term.trim().length < 2 && <EmptyState title="Digite ao menos dois caracteres." />}
      {search.data && <UserList title="Usuarios" users={search.data.users} />}
      {search.data?.posts.map((post) => <PostCard key={post.id} post={post} />)}
      {search.isError && <EmptyState title="Nao conseguimos carregar agora." />}
    </PageFrame>
  );
}

function NotificationsPage() {
  const notifications = useApi<{ items: NotificationResponse[] }>(["notifications"], "/notifications");
  return (
    <PageFrame title="Notificacoes">
      <section className="content-card list-card">
        {notifications.isLoading && <SkeletonCard />}
        {notifications.data?.items.length === 0 && <EmptyState title="Nada novo por aqui." />}
        {notifications.data?.items.map((item) => <NotificationCard key={item.id} item={item} />)}
      </section>
    </PageFrame>
  );
}

type NotificationResponse = {
  id: string;
  type: string;
  message: string;
  read: boolean;
  resourceType?: string;
  resourceId?: string;
  actor?: ProfileSummary;
  createdAt: string;
};

function NotificationCard({ item }: { item: NotificationResponse }) {
  return (
    <Link className={`notification-card ${item.read ? "" : "unread"}`} to={item.resourceType === "POST" ? `/posts/${item.resourceId}` : "/notifications"}>
      {item.actor ? <ProfileAvatar profile={item.actor} /> : <Bell size={22} />}
      <div>
        <strong>{item.message}</strong>
        <p>{relativeTime(item.createdAt)}</p>
      </div>
    </Link>
  );
}

function SavedPage() {
  const saved = useApi<CursorPage<PostResponse>>(["saved"], "/saved");
  return (
    <PageFrame title="Salvos">
      <PostList query={saved} emptyTitle="Posts salvos vao aparecer aqui." />
    </PageFrame>
  );
}

function SettingsAccountPage() {
  const { user, logout } = useAuth();
  return (
    <PageFrame title="Configuracoes">
      <section className="content-card settings-card">
        <ProfileAvatar profile={{ id: user?.id ?? "", username: user?.username ?? "", displayName: user?.displayName ?? "Usuario", avatarUrl: user?.avatarUrl }} large />
        <div>
          <h2>{user?.displayName ?? user?.username}</h2>
          <p>{user?.email}</p>
        </div>
        <Link className="secondary-button" to="/settings/profile">Editar perfil</Link>
        <Link className="secondary-button danger-text" to="/settings/delete">Excluir conta</Link>
        <button className="secondary-button" type="button" onClick={logout}>Sair</button>
      </section>
    </PageFrame>
  );
}

function SettingsProfilePage({ photoFocus = false }: { photoFocus?: boolean }) {
  const { token, user } = useAuth();
  const queryClient = useQueryClient();
  const form = useForm({
    defaultValues: { displayName: user?.displayName ?? "", username: user?.username ?? "", email: user?.email ?? "", phone: "", bio: "", avatarUrl: user?.avatarUrl ?? "" },
  });
  const mutation = useMutation({
    mutationFn: (values: unknown) => apiRequest("/users/me", token, { method: "PATCH", body: JSON.stringify(values) }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["me"] }),
  });
  return (
    <PageFrame title={photoFocus ? "Foto de perfil" : "Editar perfil"}>
      <form className="content-card form-card" onSubmit={form.handleSubmit((values) => mutation.mutate(values))}>
        <AuthInput label="Nome" {...form.register("displayName")} />
        <AuthInput label="Username" {...form.register("username")} />
        <AuthInput label="Email" {...form.register("email")} />
        <AuthInput label="Telefone" {...form.register("phone")} />
        <AuthInput label="Foto URL" {...form.register("avatarUrl")} />
        <label className="field">
          <span>Bio</span>
          <textarea rows={4} {...form.register("bio")} />
        </label>
        {mutation.isSuccess && <div className="success-card">Perfil atualizado.</div>}
        <PrimaryButton loading={mutation.isPending}>Salvar</PrimaryButton>
      </form>
    </PageFrame>
  );
}

function DeleteAccountPage() {
  const { token, logout, user } = useAuth();
  const [confirm, setConfirm] = useState("");
  const mutation = useMutation({
    mutationFn: () => apiRequest("/users/me", token, { method: "DELETE" }),
    onSuccess: logout,
  });
  return (
    <PageFrame title="Excluir conta">
      <section className="content-card form-card danger-zone">
        <Trash2 size={32} />
        <h2>Excluir conta</h2>
        <p>Digite seu username para confirmar.</p>
        <input value={confirm} onChange={(event) => setConfirm(event.target.value)} />
        <button className="danger-button" disabled={confirm !== user?.username || mutation.isPending} onClick={() => mutation.mutate()} type="button">
          Excluir definitivamente
        </button>
      </section>
    </PageFrame>
  );
}

function AiStudioPage() {
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

function DemoPage() {
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

function PostList({ query, emptyTitle }: { query: { data?: CursorPage<PostResponse>; isLoading: boolean; error: unknown }; emptyTitle: string }) {
  if (query.isLoading) {
    return <><SkeletonCard /><SkeletonCard /></>;
  }
  if (query.error) {
    return <EmptyState title="Nao conseguimos carregar agora." />;
  }
  if (!query.data?.items.length) {
    return <EmptyState title={emptyTitle} />;
  }
  return <>{query.data.items.map((post) => <PostCard key={post.id} post={post} />)}</>;
}

function PostCard({ post, expanded = false }: { post: PostResponse; expanded?: boolean }) {
  const { token } = useAuth();
  const queryClient = useQueryClient();
  const like = useMutation({
    mutationFn: () => apiRequest(`/posts/${post.id}/likes`, token, { method: post.viewerState.liked ? "DELETE" : "POST" }),
    onSuccess: () => queryClient.invalidateQueries(),
  });
  const save = useMutation({
    mutationFn: () => apiRequest(`/posts/${post.id}/saves`, token, { method: post.viewerState.saved ? "DELETE" : "POST" }),
    onSuccess: () => queryClient.invalidateQueries(),
  });
  return (
    <article className={`post-card ${expanded ? "expanded" : ""}`}>
      <header>
        <ProfileAvatar profile={post.author} />
        <Link to={`/u/${post.author.username}`}>
          <strong>{post.author.displayName}</strong>
          <span>@{post.author.username} · {relativeTime(post.createdAt)}</span>
        </Link>
        {post.viewerState.owner && <Link className="icon-button" to={`/posts/${post.id}/edit`} aria-label="Editar publicacao"><Settings size={18} /></Link>}
      </header>
      {post.media[0] && (
        <img className="post-media" src={post.media[0].url} alt={post.media[0].altText ?? post.title} />
      )}
      <Link to={`/posts/${post.id}`} className="post-content">
        <h2>{post.title}</h2>
        {post.description && <p>{post.description}</p>}
      </Link>
      {post.moderationStatus !== "APPROVED" && <ModerationNotice status={post.moderationStatus} />}
      <footer>
        <button type="button" onClick={() => like.mutate()} className={post.viewerState.liked ? "active-action" : ""}>
          <Heart size={19} /> {post.stats.likes}
        </button>
        <Link to={`/posts/${post.id}`}><MessageCircle size={19} /> {post.stats.comments}</Link>
        <button type="button" onClick={() => save.mutate()} className={post.viewerState.saved ? "active-action" : ""}>
          <Bookmark size={19} /> {post.stats.saves}
        </button>
      </footer>
    </article>
  );
}

function ModerationNotice({ status }: { status: string }) {
  return <div className="moderation-notice">{status === "HIDDEN" ? "Este post foi ocultado por seguranca." : "Em analise"}</div>;
}

function UserList({ title, users, loading = false }: { title: string; users: ProfileSummary[]; loading?: boolean }) {
  return (
    <section className="content-card list-card">
      <h2>{title}</h2>
      {loading && <SkeletonCard />}
      {!loading && users.length === 0 && <EmptyState title="Nenhum resultado encontrado." compact />}
      {users.map((user) => <UserListItem key={user.id} user={user} />)}
    </section>
  );
}

function UserListItem({ user }: { user: ProfileSummary }) {
  return (
    <Link to={`/u/${user.username}`} className="user-list-item">
      <ProfileAvatar profile={user} />
      <div>
        <strong>{user.displayName}</strong>
        <span>@{user.username}</span>
      </div>
      <UserPlus size={18} />
    </Link>
  );
}

function ProfileAvatar({ profile, large = false }: { profile: Partial<ProfileSummary>; large?: boolean }) {
  return profile.avatarUrl ? (
    <img className={`avatar ${large ? "large" : ""}`} src={profile.avatarUrl} alt={profile.displayName ?? profile.username ?? "Avatar"} />
  ) : (
    <span className={`avatar fallback ${large ? "large" : ""}`}>{(profile.displayName ?? profile.username ?? "T").charAt(0).toUpperCase()}</span>
  );
}

type AuthInputProps = InputHTMLAttributes<HTMLInputElement> & { label: string; error?: string };

function AuthInput({ label, error, ...props }: AuthInputProps) {
  return (
    <label className="field">
      <span>{label}</span>
      <input {...props} />
      {error && <small>{error}</small>}
    </label>
  );
}

function PrimaryButton({ children, loading = false }: PropsWithChildren<{ loading?: boolean }>) {
  return <button className="primary-button" disabled={loading} type="submit">{loading ? "Carregando..." : children}</button>;
}

function EmptyState({ title, compact = false }: { title: string; compact?: boolean }) {
  return <section className={`empty-state ${compact ? "compact" : ""}`}><p>{title}</p></section>;
}

function SkeletonCard() {
  return <section className="skeleton-card" />;
}

function LoadingScreen() {
  return <main className="loading-screen"><TeachgramLogo /><p>Carregando...</p></main>;
}

function InlineError({ message }: { message: string }) {
  return <div className="inline-error">{message}</div>;
}

function messageFromError(exception: unknown) {
  if (exception instanceof ApiError) {
    return exception.body?.detail ?? exception.message;
  }
  if (exception instanceof Error) {
    return exception.message;
  }
  return "Nao conseguimos completar essa acao.";
}

function aiStateText(state: string) {
  if (state === "loading") return "Gerando sugestao...";
  if (state === "fallback") return "IA indisponivel agora. Geramos uma sugestao simples para voce continuar.";
  if (state === "success") return "Sugestao pronta para revisar.";
  if (state === "error") return "Nao conseguimos gerar a sugestao. Tente novamente mais tarde.";
  return "";
}

function relativeTime(value: string) {
  const diff = Date.now() - new Date(value).getTime();
  const minutes = Math.max(1, Math.round(diff / 60000));
  if (minutes < 60) return `${minutes}min`;
  const hours = Math.round(minutes / 60);
  if (hours < 24) return `${hours}h`;
  return `${Math.round(hours / 24)}d`;
}
