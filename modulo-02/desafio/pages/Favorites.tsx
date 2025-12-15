import { Link } from 'react-router-dom';
import { useFavorites } from '../hooks/useFavorites';
import { ArrowLeft, Trash2, Heart } from 'lucide-react';
import { useLanguage } from '../components/contexts/LanguageContext';

export function Favorites() {
    const { favorites, removeFavorite } = useFavorites();
    const { t } = useLanguage();

    return (
        <div className="min-h-screen bg-slate-50 dark:bg-slate-950 pt-24 px-4 pb-10 transition-colors duration-300">
            <div className="max-w-6xl mx-auto">
                <div className="flex items-center justify-between mb-8">
                    <Link
                        to="/"
                        className="flex items-center gap-2 text-slate-500 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white transition-colors"
                    >
                        <ArrowLeft size={20} />
                        <span className="font-medium">{t.backToSearch}</span>
                    </Link>
                    <h1 className="text-2xl font-bold text-slate-900 dark:text-white flex items-center gap-2">
                        <Heart className="text-red-500 fill-red-500" /> {t.favorites}
                    </h1>
                </div>

                {favorites.length === 0 ? (
                    <div className="text-center py-20 bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800">
                        <div className="w-16 h-16 bg-slate-100 dark:bg-slate-800 rounded-full flex items-center justify-center mx-auto mb-4 text-slate-400">
                            <Heart size={32} />
                        </div>
                        <h3 className="text-xl font-semibold text-slate-900 dark:text-white mb-2">{t.noFavorites}</h3>
                        <p className="text-slate-500 dark:text-slate-400">{t.noFavoritesDesc}</p>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {favorites.map(user => (
                            <div key={user.id} className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-xl p-6 shadow-sm hover:shadow-lg transition-all relative group">
                                <div className="flex items-start gap-4 mb-4">
                                    <img
                                        src={user.avatar_url}
                                        alt={user.login}
                                        className="w-16 h-16 rounded-full border-2 border-slate-100 dark:border-slate-800"
                                    />
                                    <div className="flex-1 min-w-0">
                                        <h3 className="font-bold text-lg text-slate-900 dark:text-white truncate">{user.name || user.login}</h3>
                                        <p className="text-blue-500 text-sm">@{user.login}</p>
                                    </div>
                                    <button
                                        onClick={() => removeFavorite(user.id)}
                                        className="text-slate-400 hover:text-red-500 transition-colors p-2 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg"
                                        title="Remove from favorites"
                                    >
                                        <Trash2 size={18} />
                                    </button>
                                </div>

                                {user.bio && (
                                    <p className="text-slate-600 dark:text-slate-400 text-sm line-clamp-2 mb-4 h-10">
                                        {user.bio}
                                    </p>
                                )}

                                <Link
                                    to={`/profile/${user.login}`}
                                    className="block w-full py-2.5 text-center bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 hover:bg-blue-600 hover:text-white dark:hover:bg-blue-600 rounded-lg font-medium transition-colors"
                                >
                                    {t.viewProfile}
                                </Link>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}
