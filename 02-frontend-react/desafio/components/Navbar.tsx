import { Github, Moon, Sun, Monitor, Languages } from 'lucide-react';
import { Theme, Language } from '../types';
import { useLanguage } from './contexts/LanguageContext';

interface NavbarProps {
    theme: Theme;
    onThemeChange: (t: Theme) => void;
}

export function Navbar({ theme, onThemeChange }: NavbarProps) {
    const { language, setLanguage } = useLanguage();

    const toggleLanguage = () => {
        if (language === 'pt') setLanguage('en');
        else if (language === 'en') setLanguage('es');
        else setLanguage('pt');
    };

    return (
        <nav className="fixed top-0 w-full z-40 bg-white/80 dark:bg-slate-950/80 backdrop-blur-md border-b border-slate-200 dark:border-slate-800 h-16 flex items-center justify-between px-6 transition-colors duration-300">
            <div className="flex items-center gap-3">
                <Github size={28} className="text-slate-900 dark:text-white" />
                <span className="font-bold text-xl text-slate-900 dark:text-white hidden md:inline">DevHub Explorer</span>
            </div>

            <div className="flex items-center gap-4">
                {/* Language Switcher */}
                <button
                    id="language-toggle"
                    onClick={toggleLanguage}
                    className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-slate-100 dark:bg-slate-800 hover:bg-slate-200 dark:hover:bg-slate-700 text-slate-700 dark:text-slate-300 transition-colors font-medium text-sm"
                    title="Switch Language"
                >
                    <Languages size={18} />
                    <span className="uppercase">{language}</span>
                </button>

                <div className="h-6 w-px bg-slate-200 dark:bg-slate-800 mx-2"></div>

                <div className="flex items-center gap-2">
                    <button
                        onClick={() => onThemeChange('system')}
                        className={`p-2 rounded-lg transition-colors ${theme === 'system' ? 'text-blue-500 bg-blue-500/10' : 'text-slate-500 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'}`}
                        title="System Theme"
                    >
                        <Monitor size={20} />
                    </button>
                    <button
                        onClick={() => onThemeChange('light')}
                        className={`p-2 rounded-lg transition-colors ${theme === 'light' ? 'text-amber-500 bg-amber-500/10' : 'text-slate-500 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'}`}
                        title="Light Mode"
                    >
                        <Sun size={20} />
                    </button>
                    <button
                        onClick={() => onThemeChange('dark')}
                        className={`p-2 rounded-lg transition-colors ${theme === 'dark' ? 'text-indigo-400 bg-indigo-500/10' : 'text-slate-500 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'}`}
                        title="Dark Mode"
                    >
                        <Moon size={20} />
                    </button>
                </div>
            </div>
        </nav>
    );
}
