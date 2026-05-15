import { useQuery } from "@tanstack/react-query";
import { useAuth } from "../contexts/AuthContext";

export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8081/api/v1";
export type AuthUser = {
  id: string;
  username: string;
  email: string;
  displayName?: string;
  avatarUrl?: string;
  role?: string;
};
export type AuthResponse = {
  user: AuthUser;
  tokens: { accessToken: string; tokenType: string; expiresIn: number };
};
export type ProfileSummary = {
  id: string;
  username: string;
  displayName: string;
  avatarUrl?: string;
  bio?: string;
};
export type PostResponse = {
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
export type CursorPage<T> = { items: T[]; nextCursor?: string; hasNext: boolean };
export type ApiErrorBody = {
  title?: string;
  detail?: string;
  traceId?: string;
  errors?: Record<string, string[]>;
};
export class ApiError extends Error {
  status: number;
  body?: ApiErrorBody;

  constructor(status: number, body?: ApiErrorBody) {
    super(body?.detail ?? body?.title ?? "Erro inesperado");
    this.status = status;
    this.body = body;
  }
}
export async function apiRequest<T>(path: string, token: string | null, options: RequestInit = {}): Promise<T> {
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
export function useApi<T>(key: unknown[], path: string, enabled = true) {
  const { token } = useAuth();
  return useQuery({
    queryKey: key,
    enabled: enabled && Boolean(token),
    queryFn: () => apiRequest<T>(path, token),
  });
}

export function messageFromError(error: unknown): string {
  if (error instanceof ApiError) return error.body?.detail || error.body?.title || error.message;
  if (error instanceof Error) return error.message;
  return String(error);
}
