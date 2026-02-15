import React, { useEffect, useRef } from 'react';
import { GithubRepo } from '../types';
import { X, ExternalLink, Lock, Globe, Code, Eye } from 'lucide-react';
import { useLanguage } from './contexts/LanguageContext';

interface RepoModalProps {
  repo: GithubRepo | null;
  onClose: () => void;
}

const RepoModal: React.FC<RepoModalProps> = ({ repo, onClose }) => {
  const modalRef = useRef<HTMLDivElement>(null);
  const { t } = useLanguage();

  useEffect(() => {
    const handleEscape = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };

    if (repo) {
      document.addEventListener('keydown', handleEscape);
      // Prevent body scrolling when modal is open
      document.body.style.overflow = 'hidden';
    }

    return () => {
      document.removeEventListener('keydown', handleEscape);
      document.body.style.overflow = 'unset';
    };
  }, [repo, onClose]);

  if (!repo) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm transition-opacity duration-300">
      <div
        ref={modalRef}
        className="bg-slate-900 border border-slate-700 rounded-xl w-full max-w-lg shadow-2xl transform transition-all scale-100 overflow-hidden"
        role="dialog"
        aria-modal="true"
      >
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-slate-800 bg-slate-950/50">
          <h3 className="text-xl font-bold text-white truncate pr-4">{repo.name}</h3>
          <button
            onClick={onClose}
            className="text-slate-400 hover:text-white transition-colors p-1 hover:bg-slate-800 rounded-full"
          >
            <X size={24} />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-6">
          <p className="text-slate-300 leading-relaxed">
            {repo.description || <span className="italic text-slate-500">{t.noDescription}</span>}
          </p>

          <div className="grid grid-cols-2 gap-4">
            <div className="bg-slate-800/50 p-3 rounded-lg border border-slate-700">
              <span className="text-xs text-slate-500 uppercase font-semibold flex items-center mb-1">
                <Code size={12} className="mr-1" /> {t.filterByLanguage}
              </span>
              <span className="text-sm font-medium text-blue-400">
                {repo.language || 'N/A'}
              </span>
            </div>

            <div className="bg-slate-800/50 p-3 rounded-lg border border-slate-700">
              <span className="text-xs text-slate-500 uppercase font-semibold flex items-center mb-1">
                <Eye size={12} className="mr-1" /> {t.visibility}
              </span>
              <span className="text-sm font-medium text-emerald-400 flex items-center gap-2">
                {repo.private ? <Lock size={14} /> : <Globe size={14} />}
                {repo.visibility}
              </span>
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="p-6 pt-2 pb-6">
          <a
            href={repo.html_url}
            target="_blank"
            rel="noopener noreferrer"
            className="w-full flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-500 text-white py-3 px-4 rounded-lg font-semibold transition-all active:scale-[0.98] shadow-lg shadow-blue-900/20"
          >
            {t.viewOnGithub}
            <ExternalLink size={18} />
          </a>
        </div>
      </div>
    </div>
  );
};

export default RepoModal;