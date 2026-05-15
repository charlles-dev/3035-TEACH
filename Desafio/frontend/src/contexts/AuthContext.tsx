import { createContext, useContext, PropsWithChildren, useState, useEffect, useMemo } from "react";
import { AuthUser, AuthResponse } from "../lib/api";
import { SignupPayload } from "../pages/SignupPage";
import { apiRequest } from '../lib/api';
import { queryClient } from '../lib/queryClient';

export const DEMO_EMAIL = import.meta.env.VITE_DEMO_EMAIL ?? "demo@teachgram.pro";
export type AuthContextValue = {
  token: string | null;
  user: AuthUser | null;
  loading: boolean;
  login: (identifier: string, password: string) => Promise<void>;
  signup: (payload: SignupPayload) => Promise<void>;
  logout: () => Promise<void>;
  setAuth: (auth: AuthResponse) => void;
};
export const AuthContext = createContext<AuthContextValue | null>(null);
export function useAuth() {
  const value = useContext(AuthContext);
  if (!value) {
    throw new Error("useAuth must be used inside AuthProvider");
  }
  return value;
}
export function AuthProvider({ children }: PropsWithChildren) {
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
