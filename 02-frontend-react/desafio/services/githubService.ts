/// <reference types="vite/client" />
import { GithubUser, GithubRepo } from '../types';

const BASE_URL = 'https://api.github.com/users';
const CACHE_PREFIX = 'devhub_cache_';
const HISTORY_KEY = 'devhub_search_history';
const CACHE_DURATION = 10 * 60 * 1000; // 10 minutes

interface CacheEntry<T> {
  timestamp: number;
  data: T;
}

const getFromCache = <T>(key: string): T | null => {
  try {
    const cached = localStorage.getItem(CACHE_PREFIX + key);
    if (!cached) return null;

    const entry: CacheEntry<T> = JSON.parse(cached);
    const now = Date.now();

    if (now - entry.timestamp > CACHE_DURATION) {
      localStorage.removeItem(CACHE_PREFIX + key);
      return null;
    }

    return entry.data;
  } catch (e) {
    console.warn('Failed to parse cache', e);
    return null;
  }
};

const saveToCache = <T>(key: string, data: T): void => {
  try {
    const entry: CacheEntry<T> = {
      timestamp: Date.now(),
      data
    };
    localStorage.setItem(CACHE_PREFIX + key, JSON.stringify(entry));
  } catch (e) {
    console.warn('Failed to save to cache', e);
  }
};

// --- History Management ---

export const getSearchHistory = (): GithubUser[] => {
  try {
    const history = localStorage.getItem(HISTORY_KEY);
    return history ? JSON.parse(history) : [];
  } catch {
    return [];
  }
};

export const addToSearchHistory = (user: GithubUser): void => {
  try {
    const history = getSearchHistory();
    // Remove if exists to move to top (most recent)
    const filtered = history.filter(u => u.login !== user.login);
    // Add to front
    filtered.unshift(user);
    // Keep max 6 items
    const trimmed = filtered.slice(0, 6);
    localStorage.setItem(HISTORY_KEY, JSON.stringify(trimmed));
  } catch (e) {
    console.warn('Failed to update history', e);
  }
};

export const clearSearchHistory = (): void => {
  localStorage.removeItem(HISTORY_KEY);
};

// --- API Calls ---

const getHeaders = (): HeadersInit => {
  // Uses the developer's token from environment variables
  const token = import.meta.env.VITE_GITHUB_TOKEN;
  return token ? { Authorization: `token ${token}` } : {};
};

const handleResponse = async (response: Response, context: string) => {
  if (!response.ok) {
    if (response.status === 404) {
      throw new Error(`${context} not found`);
    }
    if (response.status === 403) {
      throw new Error(`GitHub API rate limit exceeded. Please try again later.`);
    }
    throw new Error(`GitHub API Error (${response.status}): ${response.statusText || 'Unknown error'}`);
  }
  return response.json();
};

export const fetchGithubUser = async (username: string): Promise<GithubUser> => {
  const cacheKey = `user_${username.toLowerCase()}`;
  const cachedData = getFromCache<GithubUser>(cacheKey);

  if (cachedData) {
    return cachedData;
  }

  try {
    const response = await fetch(`${BASE_URL}/${username}`, {
      headers: getHeaders()
    });
    const data = await handleResponse(response, 'User');
    saveToCache(cacheKey, data);
    return data;
  } catch (error: any) {
    throw error;
  }
};

export const fetchGithubRepos = async (username: string): Promise<GithubRepo[]> => {
  const cacheKey = `repos_${username.toLowerCase()}`;
  const cachedData = getFromCache<GithubRepo[]>(cacheKey);

  if (cachedData) {
    return cachedData;
  }

  try {
    // Sort by updated to show most active projects first
    const response = await fetch(`${BASE_URL}/${username}/repos?sort=updated&per_page=100`, {
      headers: getHeaders()
    });
    const data = await handleResponse(response, 'Repositories');
    saveToCache(cacheKey, data);
    return data;
  } catch (error: any) {
    throw error;
  }
};