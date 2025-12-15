import { useState, useEffect } from 'react';
import { GithubUser } from '../types';

const FAVORITES_KEY = 'devhub_favorites';

export const useFavorites = () => {
    const [favorites, setFavorites] = useState<GithubUser[]>([]);

    useEffect(() => {
        const stored = localStorage.getItem(FAVORITES_KEY);
        if (stored) {
            try {
                setFavorites(JSON.parse(stored));
            } catch (e) {
                console.error('Failed to parse favorites', e);
            }
        }
    }, []);

    const addFavorite = (user: GithubUser) => {
        const updated = [...favorites, user];
        setFavorites(updated);
        localStorage.setItem(FAVORITES_KEY, JSON.stringify(updated));
    };

    const removeFavorite = (userId: number) => {
        const updated = favorites.filter(u => u.id !== userId);
        setFavorites(updated);
        localStorage.setItem(FAVORITES_KEY, JSON.stringify(updated));
    };

    const isFavorite = (userId: number) => {
        return favorites.some(u => u.id === userId);
    };

    return { favorites, addFavorite, removeFavorite, isFavorite };
};
