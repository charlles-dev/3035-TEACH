import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, MapPin, Users, Book, ExternalLink, Heart, Star, GitFork } from 'lucide-react';
import { fetchGithubUser, fetchGithubRepos } from '../services/githubService';
import { GithubUser, GithubRepo } from '../types';
import RepoConfig, { SortOption } from '../components/RepoConfig';
import StatsChart from '../components/StatsChart';
import RepoModal from '../components/RepoModal';
import { useFavorites } from '../hooks/useFavorites';
import { useLanguage } from '../components/contexts/LanguageContext';

export function Profile() {
    const { username } = useParams<{ username: string }>();
    const [user, setUser] = useState<GithubUser | null>(null);
    const [repos, setRepos] = useState<GithubRepo[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Translations
    const { t } = useLanguage();

    // Repo UI State
    const [selectedRepo, setSelectedRepo] = useState<GithubRepo | null>(null);
    const [sortOption, setSortOption] = useState<SortOption>('updated');
    const [languageFilter, setLanguageFilter] = useState<string>('ALL');

    // Favorites
    const { isFavorite, addFavorite, removeFavorite } = useFavorites();

    useEffect(() => {
        if (!username) return;

        const loadData = async () => {
            setLoading(true);
            try {
                const [u, r] = await Promise.all([
                    fetchGithubUser(username),
                    fetchGithubRepos(username)
                ]);
                setUser(u);
                setRepos(r);
            } catch (err: any) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        loadData();
    }, [username]);

    // Derived State for Filtering/Sorting
    const processedRepos = (() => {
        let result = [...repos];

        // Filter
        if (languageFilter !== 'ALL') {
            result = result.filter(r => r.language === languageFilter);
        }

        // Sort
        result.sort((a, b) => {
            if (sortOption === 'stars') return b.stargazers_count - a.stargazers_count;
            if (sortOption === 'forks') return b.forks_count - a.forks_count;
            return new Date(b.updated_at).getTime() - new Date(a.updated_at).getTime();
        });

        return result;
    })();

    const availableLanguages = Array.from(new Set(repos.map(r => r.language).filter(Boolean) as string[]));
    const isFav = user ? isFavorite(user.id) : false;

    const toggleFavorite = () => {
        if (!user) return;
        if (isFav) removeFavorite(user.id);
        else addFavorite(user);
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-slate-50 dark:bg-slate-950">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
            </div>
        );
    }

    if (error || !user) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-slate-50 dark:bg-slate-950 p-4 text-center">
                <h2 className="text-2xl font-bold text-slate-900 dark:text-white mb-2">{t.error}</h2>
                <p className="text-red-500 mb-6">{error || t.userNotFound}</p>
                <Link to="/" className="text-blue-500 hover:underline">{t.backToSearch}</Link>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-slate-50 dark:bg-slate-950 pt-24 px-4 pb-10 transition-colors duration-300">
            <div className="max-w-6xl mx-auto animate-in slide-in-from-bottom-4 duration-500">
                <Link
                    to="/"
                    className="mb-6 flex items-center gap-2 text-slate-500 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white transition-colors group w-fit"
                >
                    <div className="bg-white dark:bg-slate-800 p-2 rounded-full border border-slate-200 dark:border-slate-700 group-hover:bg-slate-100 dark:group-hover:bg-slate-700 transition-colors shadow-sm">
                        <ArrowLeft size={20} />
                    </div>
                    <span className="font-medium">{t.backToSearch}</span>
                </Link>

                {/* Profile Header */}
                <div id="profile-header" className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-6 md:p-8 mb-8 flex flex-col md:flex-row items-center md:items-start gap-6 shadow-xl relative overflow-hidden transition-colors duration-300">
                    <div className="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-blue-500 via-purple-500 to-emerald-500"></div>

                    <div className="relative">
                        <img
                            src={user.avatar_url}
                            alt={user.login}
                            className="w-32 h-32 md:w-40 md:h-40 rounded-full border-4 border-white dark:border-slate-800 shadow-2xl relative z-10"
                        />
                    </div>

                    <div className="flex-1 text-center md:text-left space-y-4">
                        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                            <div>
                                <h1 className="text-3xl font-bold text-slate-900 dark:text-white">{user.name || user.login}</h1>
                                <p className="text-blue-500 dark:text-blue-400 font-medium">@{user.login}</p>
                            </div>

                            <div className="flex gap-2 justify-center md:justify-start">
                                <button
                                    id="save-button"
                                    onClick={toggleFavorite}
                                    className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition-colors border ${isFav
                                        ? 'bg-red-50 dark:bg-red-900/20 text-red-600 dark:text-red-400 border-red-200 dark:border-red-800'
                                        : 'bg-white dark:bg-slate-800 text-slate-600 dark:text-slate-300 border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-700'
                                        }`}
                                >
                                    <Heart size={20} className={isFav ? 'fill-current' : ''} />
                                    {isFav ? t.saved : t.save}
                                </button>
                                <a
                                    href={user.html_url}
                                    target="_blank"
                                    rel="noreferrer"
                                    className="bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700 text-slate-900 dark:text-white px-4 py-2 rounded-lg font-medium transition-colors text-sm border border-slate-200 dark:border-slate-700 flex items-center"
                                >
                                    {t.viewOnGithub} <ExternalLink size={16} className="ml-2" />
                                </a>
                            </div>
                        </div>

                        {user.bio && (
                            <p className="text-slate-600 dark:text-slate-300 max-w-2xl">{user.bio}</p>
                        )}

                        <div className="flex flex-wrap justify-center md:justify-start gap-4 md:gap-6 text-sm text-slate-500 dark:text-slate-400 pt-2">
                            {user.location && (
                                <div className="flex items-center gap-1.5">
                                    <MapPin size={16} />
                                    <span>{user.location}</span>
                                </div>
                            )}
                            <div className="flex items-center gap-1.5">
                                <Users size={16} />
                                <span><strong className="text-slate-900 dark:text-white">{user.followers}</strong> {t.followers}</span>
                            </div>
                            <div className="flex items-center gap-1.5">
                                <Users size={16} />
                                <span><strong className="text-slate-900 dark:text-white">{user.following}</strong> {t.following}</span>
                            </div>
                            <div className="flex items-center gap-1.5">
                                <Book size={16} />
                                <span><strong className="text-slate-900 dark:text-white">{user.public_repos}</strong> {t.repos}</span>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Stats Chart */}
                {repos.length > 0 && <StatsChart repos={repos} />}

                {/* Repos Grid */}
                <div>
                    <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6">
                        <h2 className="text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
                            <Book className="text-blue-500" />
                            {t.repositories}
                        </h2>
                        <div className="text-sm text-slate-500">
                            {t.showing} {processedRepos.length} {t.of} {repos.length}
                        </div>
                    </div>

                    <RepoConfig
                        sortOption={sortOption}
                        onSortChange={setSortOption}
                        languageFilter={languageFilter}
                        onFilterChange={setLanguageFilter}
                        availableLanguages={availableLanguages}
                    />

                    {processedRepos.length === 0 ? (
                        <div className="text-center py-12 text-slate-500 bg-slate-100 dark:bg-slate-900/50 rounded-xl border border-slate-200 dark:border-slate-800 border-dashed">
                            {t.noReposFound}
                        </div>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                            {processedRepos.map((repo) => (
                                <div
                                    key={repo.id}
                                    onClick={() => setSelectedRepo(repo)}
                                    className="bg-white dark:bg-slate-900/50 border border-slate-200 dark:border-slate-800 p-6 rounded-xl cursor-pointer 
                    transition-all duration-300 ease-out group flex flex-col h-full 
                    hover:border-blue-500/40 hover:bg-slate-50 dark:hover:bg-slate-900 hover:scale-[1.02] hover:shadow-2xl hover:shadow-blue-500/10 dark:hover:shadow-blue-900/10"
                                >
                                    <div className="flex items-center justify-between mb-4">
                                        <h3 className="font-bold text-lg text-slate-800 dark:text-slate-100 group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors truncate pr-3 flex-1">
                                            {repo.name}
                                        </h3>
                                        <div
                                            className={`w-2.5 h-2.5 rounded-full flex-shrink-0 ${repo.private ? 'bg-red-500 shadow-[0_0_8px_rgba(239,68,68,0.5)]' : 'bg-emerald-500 shadow-[0_0_8px_rgba(16,185,129,0.5)]'}`}
                                            title={repo.private ? 'Private' : 'Public'}
                                        />
                                    </div>

                                    <p className="text-slate-600 dark:text-slate-400 text-sm leading-relaxed mb-6 line-clamp-3 flex-grow">
                                        {repo.description || <span className="text-slate-400 dark:text-slate-600 italic">No description provided.</span>}
                                    </p>

                                    <div className="flex items-center justify-between text-xs text-slate-500 mt-auto pt-4 border-t border-slate-100 dark:border-slate-800/50">
                                        <div className="flex items-center gap-3">
                                            {repo.language && (
                                                <span className="flex items-center gap-1.5 font-medium text-slate-600 dark:text-slate-400 bg-slate-100 dark:bg-slate-800/50 px-2 py-1 rounded-full border border-slate-200 dark:border-slate-700/50">
                                                    <span className="w-1.5 h-1.5 rounded-full bg-blue-500"></span>
                                                    {repo.language}
                                                </span>
                                            )}
                                            <div className="flex items-center gap-4">
                                                <span className="flex items-center gap-1 text-slate-500 dark:text-slate-400" title={t.stars}>
                                                    <Star size={14} />
                                                    {repo.stargazers_count}
                                                </span>
                                                <span className="flex items-center gap-1 text-slate-500 dark:text-slate-400" title={t.forks}>
                                                    <GitFork size={14} />
                                                    {repo.forks_count}
                                                </span>
                                            </div>
                                        </div>
                                        <span className="font-medium text-slate-500 dark:text-slate-600">
                                            {new Date(repo.updated_at).toLocaleDateString(undefined, { month: 'short', day: 'numeric' })}
                                        </span>
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                {selectedRepo && (
                    <RepoModal
                        repo={selectedRepo}
                        onClose={() => setSelectedRepo(null)}
                    />
                )}
            </div>
        </div>
    );
}
