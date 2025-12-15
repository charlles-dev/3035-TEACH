import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { Language } from '../../types';

type Translations = {
    [key in Language]: {
        searchPlaceholder: string;
        searchButton: string;
        recentSearches: string;
        backToSearch: string;
        saved: string;
        save: string;
        viewOnGithub: string;
        repositories: string;
        noReposFound: string;
        followers: string;
        following: string;
        repos: string;
        favorites: string;
        noFavorites: string;
        noFavoritesDesc: string;
        viewProfile: string;
        error: string;
        userNotFound: string;
        showing: string;
        of: string;
        footer: string;
        topLanguages: string;
        sortBy: string;
        filterByLanguage: string;
        stars: string;
        forks: string;
        updated: string;
        lastUpdated: string;
        allLanguages: string;
        noDescription: string;
        visibility: string;
        heroTitle: string;
        heroSubtitle: string;
        viewFavorites: string;
    }
};

const translations: Translations = {
    en: {
        searchPlaceholder: "Search for a GitHub user...",
        searchButton: "Search",
        recentSearches: "Recent Searches",
        backToSearch: "Back to Search",
        saved: "Saved",
        save: "Save",
        viewOnGithub: "View on GitHub",
        repositories: "Repositories",
        noReposFound: "No repositories match your filters.",
        followers: "followers",
        following: "following",
        repos: "repos",
        favorites: "Favorites",
        noFavorites: "No favorites yet",
        noFavoritesDesc: "Search for users and click the heart icon to save them here.",
        viewProfile: "View Profile",
        error: "Error",
        userNotFound: "User not found",
        showing: "Showing",
        of: "of",
        footer: "Made with ❤️ by",
        topLanguages: "Top Languages",
        sortBy: "Sort by",
        filterByLanguage: "Language",
        stars: "Stars",
        forks: "Forks",
        updated: "Updated",
        lastUpdated: "Last Updated",
        allLanguages: "All Languages",
        noDescription: "No description provided.",
        visibility: "Visibility",
        heroTitle: "DevHub Explorer",
        heroSubtitle: "Search for GitHub users and explore repositories",
        viewFavorites: "View Favorites"
    },
    pt: {
        searchPlaceholder: "Buscar usuário do GitHub...",
        searchButton: "Buscar",
        recentSearches: "Buscas Recentes",
        backToSearch: "Voltar para Busca",
        saved: "Salvo",
        save: "Salvar",
        viewOnGithub: "Ver no GitHub",
        repositories: "Repositórios",
        noReposFound: "Nenhum repositório encontrado com os filtros.",
        followers: "seguidores",
        following: "seguindo",
        repos: "repos",
        favorites: "Favoritos",
        noFavorites: "Nenhum favorito ainda",
        noFavoritesDesc: "Busque usuários e clique no coração para salvá-los.",
        viewProfile: "Ver Perfil",
        error: "Erro",
        userNotFound: "Usuário não encontrado",
        showing: "Mostrando",
        of: "de",
        footer: "Feito com ❤️ por",
        topLanguages: "Linguagens Principais",
        sortBy: "Ordenar por",
        filterByLanguage: "Linguagem",
        stars: "Estrelas",
        forks: "Forks",
        updated: "Atualizado",
        lastUpdated: "Última Atualização",
        allLanguages: "Todas as Linguagens",
        noDescription: "Nenhuma descrição fornecida.",
        visibility: "Visibilidade",
        heroTitle: "DevHub Explorer",
        heroSubtitle: "Busque usuários do GitHub e explore repositórios",
        viewFavorites: "Ver Favoritos"
    },
    es: {
        searchPlaceholder: "Buscar usuario de GitHub...",
        searchButton: "Buscar",
        recentSearches: "Búsquedas Recientes",
        backToSearch: "Volver a la Búsqueda",
        saved: "Guardado",
        save: "Guardar",
        viewOnGithub: "Ver en GitHub",
        repositories: "Repositorios",
        noReposFound: "No se encontraron repositorios con los filtros.",
        followers: "seguidores",
        following: "siguiendo",
        repos: "repos",
        favorites: "Favoritos",
        noFavorites: "Aún no hay favoritos",
        noFavoritesDesc: "Busca usuarios y haz clic en el corazón para guardarlos.",
        viewProfile: "Ver Perfil",
        error: "Error",
        userNotFound: "Usuario no encontrado",
        showing: "Mostrando",
        of: "de",
        footer: "Hecho con ❤️ por",
        topLanguages: "Idiomas Principales",
        sortBy: "Ordenar por",
        filterByLanguage: "Idioma",
        stars: "Estrellas",
        forks: "Forks",
        updated: "Actualizado",
        lastUpdated: "Última Actualización",
        allLanguages: "Todos los Idiomas",
        noDescription: "Sin descripción.",
        visibility: "Visibilidad",
        heroTitle: "DevHub Explorer",
        heroSubtitle: "Busca usuarios de GitHub y explora repositorios",
        viewFavorites: "Ver Favoritos"
    }
};

interface LanguageContextType {
    language: Language;
    setLanguage: (lang: Language) => void;
    t: Translations['en'];
}

const LanguageContext = createContext<LanguageContextType | undefined>(undefined);

export function LanguageProvider({ children }: { children: ReactNode }) {
    const [language, setLanguage] = useState<Language>('pt');

    useEffect(() => {
        const saved = localStorage.getItem('devhub_lang') as Language;
        if (saved) setLanguage(saved);
    }, []);

    const handleSetLanguage = (lang: Language) => {
        setLanguage(lang);
        localStorage.setItem('devhub_lang', lang);
    };

    return (
        <LanguageContext.Provider value={{ language, setLanguage: handleSetLanguage, t: translations[language] }}>
            {children}
        </LanguageContext.Provider>
    );
}

export const useLanguage = () => {
    const context = useContext(LanguageContext);
    if (!context) {
        throw new Error('useLanguage must be used within a LanguageProvider');
    }
    return context;
};
