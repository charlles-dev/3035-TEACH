import React from 'react';
import { Filter, ArrowUpDown } from 'lucide-react';
import { useLanguage } from './contexts/LanguageContext';

export type SortOption = 'updated' | 'stars' | 'forks';

interface RepoConfigProps {
    sortOption: SortOption;
    onSortChange: (option: SortOption) => void;
    languageFilter: string;
    onFilterChange: (lang: string) => void;
    availableLanguages: string[];
}

const RepoConfig: React.FC<RepoConfigProps> = ({
    sortOption,
    onSortChange,
    languageFilter,
    onFilterChange,
    availableLanguages
}) => {
    const { t } = useLanguage();

    return (
        <div id="repo-config" className="flex flex-col md:flex-row gap-4 mb-6 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 p-4 rounded-xl">
            {/* Sort Control */}
            <div className="flex-1 flex items-center gap-2">
                <ArrowUpDown size={18} className="text-slate-500" />
                <span className="text-sm font-medium text-slate-700 dark:text-slate-300 mr-2">{t.sortBy}:</span>
                <select
                    value={sortOption}
                    onChange={(e) => onSortChange(e.target.value as SortOption)}
                    className="bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg px-3 py-2 text-sm text-slate-700 dark:text-slate-200 focus:ring-2 focus:ring-blue-500 outline-none"
                >
                    <option value="updated">{t.lastUpdated}</option>
                    <option value="stars">{t.stars}</option>
                    <option value="forks">{t.forks}</option>
                </select>
            </div>

            {/* Filter Control */}
            <div className="flex-1 flex items-center gap-2">
                <Filter size={18} className="text-slate-500" />
                <span className="text-sm font-medium text-slate-700 dark:text-slate-300 mr-2">{t.filterByLanguage}:</span>
                <select
                    value={languageFilter}
                    onChange={(e) => onFilterChange(e.target.value)}
                    className="bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg px-3 py-2 text-sm text-slate-700 dark:text-slate-200 focus:ring-2 focus:ring-blue-500 outline-none w-full md:w-auto"
                >
                    <option value="ALL">{t.allLanguages}</option>
                    {availableLanguages.map(lang => (
                        <option key={lang} value={lang}>{lang}</option>
                    ))}
                </select>
            </div>
        </div>
    );
};

export default RepoConfig;
