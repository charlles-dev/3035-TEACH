import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search, Github, Loader2, Clock, Trash2, ChevronRight, Settings } from 'lucide-react';
import { getSearchHistory, clearSearchHistory, fetchGithubUser, addToSearchHistory } from '../services/githubService';
import { GithubUser } from '../types';
import { useLanguage } from '../components/contexts/LanguageContext';

export function Home() {
    const navigate = useNavigate();
    const [query, setQuery] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [history, setHistory] = useState<GithubUser[]>([]);

    // Translations
    const { t } = useLanguage();

    useEffect(() => {
        setHistory(getSearchHistory());
    }, []);

    const handleSearch = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!query.trim()) return;

        setLoading(true);
        setError(null);

        try {
            // Pre-fetch basic user data to validate existence before navigating
            const userData = await fetchGithubUser(query);
            addToSearchHistory(userData);
            navigate(`/profile/${query}`);
        } catch (err: any) {
            setError(err.message || t.userNotFound);
        } finally {
            setLoading(false);
        }
    };

    const handleClearHistory = () => {
        clearSearchHistory();
        setHistory([]);
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen px-4 py-20 bg-slate-50 dark:bg-slate-950 transition-colors duration-300">
            <div className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 p-8 rounded-2xl shadow-2xl w-full max-w-md backdrop-blur-sm relative overflow-hidden flex flex-col transition-colors duration-300">
                {/* Decorative gradient blob */}
                <div className="absolute -top-20 -right-20 w-40 h-40 bg-blue-500/10 rounded-full blur-3xl pointer-events-none"></div>

                <div className="text-center mb-8 relative mt-4 flex-shrink-0">
                    <Github size={64} className="mx-auto text-slate-900 dark:text-white mb-4 transition-colors duration-300" />
                    <h1 className="text-3xl font-bold text-slate-900 dark:text-white mb-2 transition-colors duration-300">{t.heroTitle}</h1>
                    <p className="text-slate-500 dark:text-slate-400 transition-colors duration-300">{t.heroSubtitle}</p>
                </div>

                <form onSubmit={handleSearch} className="space-y-4 relative flex-shrink-0">
                    <div className="relative group">
                        <Search className="absolute left-3 top-3.5 text-slate-400 dark:text-slate-500 group-focus-within:text-blue-500 transition-colors" size={20} />
                        <input
                            id="search-input"
                            type="text"
                            value={query}
                            onChange={(e) => setQuery(e.target.value)}
                            placeholder={t.searchPlaceholder}
                            className="w-full bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-white pl-10 pr-4 py-3 rounded-lg border border-slate-200 dark:border-slate-700 focus:border-blue-500 focus:ring-2 focus:ring-blue-500/20 outline-none transition-all placeholder-slate-400 dark:placeholder-slate-600"
                        />
                    </div>

                    {error && (
                        <div className="p-3 bg-red-50 dark:bg-red-900/30 border border-red-200 dark:border-red-800 text-red-600 dark:text-red-200 text-sm rounded-lg text-center animate-in slide-in-from-top-2">
                            {error}
                        </div>
                    )}

                    <div className="flex gap-2">
                        <button
                            type="submit"
                            disabled={loading}
                            className="flex-1 bg-blue-600 hover:bg-blue-500 text-white font-semibold py-3 px-4 rounded-lg transition-all active:scale-[0.98] disabled:opacity-70 disabled:cursor-not-allowed flex justify-center items-center gap-2 shadow-lg shadow-blue-500/20 dark:shadow-blue-900/20"
                        >
                            {loading ? <Loader2 className="animate-spin" /> : t.searchButton}
                        </button>
                    </div>

                    <button
                        id="favorites-link"
                        type="button"
                        onClick={() => navigate('/favorites')}
                        className="w-full bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700 text-slate-700 dark:text-slate-300 font-semibold py-3 px-4 rounded-lg transition-all active:scale-[0.98] flex justify-center items-center gap-2"
                    >
                        {t.viewFavorites}
                    </button>
                </form>

                {/* Recent History Section */}
                {history.length > 0 && (
                    <div className="mt-8 pt-6 border-t border-slate-200 dark:border-slate-800 animate-in slide-in-from-bottom-2 flex-1 overflow-hidden flex flex-col">
                        <div className="flex items-center justify-between mb-4 flex-shrink-0">
                            <h3 className="text-sm font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider flex items-center gap-2">
                                <Clock size={14} /> {t.recentSearches}
                            </h3>
                            <button
                                onClick={handleClearHistory}
                                className="text-xs text-slate-500 hover:text-red-500 dark:hover:text-red-400 transition-colors flex items-center gap-1"
                            >
                                <Trash2 size={12} /> Clear
                            </button>
                        </div>

                        <div className="space-y-2 overflow-y-auto pr-2 custom-scrollbar flex-1 max-h-48">
                            {history.map((hUser) => (
                                <button
                                    key={hUser.login}
                                    onClick={() => navigate(`/profile/${hUser.login}`)}
                                    className="w-full flex items-center gap-3 p-2 rounded-lg hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors group text-left border border-transparent hover:border-slate-200 dark:hover:border-slate-700"
                                >
                                    <img src={hUser.avatar_url} alt={hUser.login} className="w-8 h-8 rounded-full border border-slate-200 dark:border-slate-700" />
                                    <div className="flex-1 min-w-0">
                                        <p className="text-sm font-medium text-slate-700 dark:text-slate-200 truncate group-hover:text-blue-500 dark:group-hover:text-blue-400 transition-colors">
                                            {hUser.name || hUser.login}
                                        </p>
                                        <p className="text-xs text-slate-500 truncate">@{hUser.login}</p>
                                    </div>
                                    <ChevronRight size={14} className="text-slate-400 dark:text-slate-600 group-hover:text-slate-600 dark:group-hover:text-slate-400 group-hover:translate-x-0.5 transition-all" />
                                </button>
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
