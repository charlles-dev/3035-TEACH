export interface GithubUser {
  login: string;
  id: number;
  avatar_url: string;
  html_url: string;
  name: string;
  bio: string | null;
  public_repos: number;
  followers: number;
  following: number;
  location: string | null;
}

export interface GithubRepo {
  id: number;
  name: string;
  full_name: string;
  private: boolean;
  html_url: string;
  description: string | null;
  language: string | null;
  visibility: string;
  stargazers_count: number;
  forks_count: number;
  updated_at: string;
}

export enum ViewState {
  SEARCH = 'SEARCH',
  PROFILE = 'PROFILE'
}

export enum AITab {
  THINKING = 'THINKING',
  IMAGE_GEN = 'IMAGE_GEN',
  IMAGE_EDIT = 'IMAGE_EDIT'
}

export interface ImageGenConfig {
  size: '1K' | '2K' | '4K';
}

export type Language = 'pt' | 'en' | 'es';
export type Theme = 'system' | 'dark' | 'light';