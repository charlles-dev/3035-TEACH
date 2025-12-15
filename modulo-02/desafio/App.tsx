import { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Navbar } from './components/Navbar';
import { Home } from './pages/Home';
import { Profile } from './pages/Profile';
import { Favorites } from './pages/Favorites';
import { Theme } from './types';
import { LanguageProvider, useLanguage } from './components/contexts/LanguageContext';

function AppContent() {
  const [theme, setTheme] = useState<Theme>('system');
  const { t } = useLanguage();

  // --- Theme Management ---
  useEffect(() => {
    const savedTheme = localStorage.getItem('devhub_theme') as Theme | null;
    if (savedTheme) {
      setTheme(savedTheme);
    }
  }, []);

  useEffect(() => {
    const root = window.document.documentElement;
    const applyTheme = (t: Theme) => {
      root.classList.remove('dark', 'light');

      if (t === 'system') {
        const systemDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
        root.classList.add(systemDark ? 'dark' : 'light');
      } else {
        root.classList.add(t);
      }
    };

    applyTheme(theme);
    localStorage.setItem('devhub_theme', theme);

    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
    const handleChange = () => {
      if (theme === 'system') {
        root.classList.toggle('dark', mediaQuery.matches);
        root.classList.toggle('light', !mediaQuery.matches);
      }
    };

    mediaQuery.addEventListener('change', handleChange);
    return () => mediaQuery.removeEventListener('change', handleChange);
  }, [theme]);

  // Clean, routed layout
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-slate-50 dark:bg-slate-950 font-sans text-slate-900 dark:text-slate-200 transition-colors duration-300">
        <Navbar theme={theme} onThemeChange={setTheme} />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/profile/:username" element={<Profile />} />
          <Route path="/favorites" element={<Favorites />} />
        </Routes>

        {/* Footer */}
        <footer className="fixed bottom-0 w-full text-center py-4 pointer-events-none z-50">
          <div className="inline-block bg-white/80 dark:bg-slate-900/80 backdrop-blur-sm px-4 py-2 rounded-full border border-slate-200/50 dark:border-slate-800/50 shadow-sm pointer-events-auto">
            <p className="text-xs text-slate-500 dark:text-slate-400 flex items-center gap-1 font-medium">
              {t.footer}
              <a
                href="https://github.com/charlles-dev"
                target="_blank"
                rel="noopener noreferrer"
                className="text-blue-500 hover:text-blue-600 hover:underline transition-colors"
              >
                charlles-dev
              </a>
            </p>
          </div>
        </footer>
      </div>
    </BrowserRouter>
  );
}

function App() {
  return (
    <LanguageProvider>
      <AppContent />
    </LanguageProvider>
  );
}

export default App;