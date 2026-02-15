import React from 'react';
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip, Legend } from 'recharts';
import { GithubRepo } from '../types';
import { useLanguage } from './contexts/LanguageContext';

interface StatsChartProps {
    repos: GithubRepo[];
}

const COLORS = ['#3b82f6', '#8b5cf6', '#10b981', '#f59e0b', '#ef4444', '#64748b'];

const StatsChart: React.FC<StatsChartProps> = ({ repos }) => {
    const { t } = useLanguage();

    const data = React.useMemo(() => {
        const langMap: Record<string, number> = {};
        repos.forEach(repo => {
            if (repo.language) {
                langMap[repo.language] = (langMap[repo.language] || 0) + 1;
            }
        });

        return Object.entries(langMap)
            .map(([name, value]) => ({ name, value }))
            .sort((a, b) => b.value - a.value)
            .slice(0, 6); // Top 6 languages
    }, [repos]);

    if (data.length === 0) return null;

    return (
        <div id="stats-chart" className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-2xl p-6 shadow-sm mb-8">
            <h3 className="text-lg font-bold text-slate-900 dark:text-white mb-4">{t.topLanguages}</h3>
            <div className="h-[300px] w-full">
                <ResponsiveContainer width="100%" height="100%">
                    <PieChart>
                        <Pie
                            data={data}
                            cx="50%"
                            cy="50%"
                            innerRadius={60}
                            outerRadius={80}
                            fill="#8884d8"
                            paddingAngle={5}
                            dataKey="value"
                        >
                            {data.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                            ))}
                        </Pie>
                        <Tooltip
                            contentStyle={{ backgroundColor: '#1e293b', borderColor: '#334155', color: '#fff' }}
                            itemStyle={{ color: '#fff' }}
                        />
                        <Legend />
                    </PieChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default StatsChart;
