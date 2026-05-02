import React, { createContext, useContext, useState, useCallback, ReactNode } from 'react';
import { CheckCircle, XCircle, X } from 'lucide-react';
import { motion, AnimatePresence, useReducedMotion } from 'motion/react';

type ToastType = 'success' | 'error';

interface Toast {
  id: string;
  message: string;
  type: ToastType;
}

interface ToastContextData {
  addToast: (message: string, type: ToastType) => void;
}

const ToastContext = createContext<ToastContextData>({} as ToastContextData);

export const ToastProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [messages, setMessages] = useState<Toast[]>([]);

  const addToast = useCallback((message: string, type: ToastType) => {
    const id = Math.random().toString(36).substring(2, 9);
    const toast = { id, message, type };

    setMessages((state) => [...state, toast]);

    setTimeout(() => {
      setMessages((state) => state.filter((message) => message.id !== id));
    }, 4000);
  }, []);

  const removeToast = useCallback((id: string) => {
    setMessages((state) => state.filter((message) => message.id !== id));
  }, []);

  return (
    <ToastContext.Provider value={{ addToast }}>
      {children}
      <div className="fixed bottom-4 right-4 z-50 flex flex-col gap-2">
        <AnimatePresence>
          {messages.map((message) => (
            <ToastMessage key={message.id} message={message} onRemove={removeToast} />
          ))}
        </AnimatePresence>
      </div>
    </ToastContext.Provider>
  );
};

const ToastMessage: React.FC<{ message: Toast; onRemove: (id: string) => void }> = ({ message, onRemove }) => {
  const prefersReducedMotion = useReducedMotion();
  return (
    <motion.div
      initial={prefersReducedMotion ? { opacity: 0 } : { opacity: 0, x: 50 }}
      animate={{ opacity: 1, x: 0 }}
      exit={prefersReducedMotion ? { opacity: 0 } : { opacity: 0, x: 50, scale: 0.9 }}
      className={`flex items-center gap-3 p-4 pr-12 rounded shadow-lg border relative min-w-[300px] overflow-hidden ${
        message.type === 'success' ? 'bg-[#0a0a0a] border-[#222] text-emerald-400' : 'bg-[#0a0a0a] border-[#222] text-rose-400'
      }`}
    >
      <div className={`absolute left-0 top-0 bottom-0 w-1 ${message.type === 'success' ? 'bg-emerald-500' : 'bg-rose-500'}`} />
      {message.type === 'success' ? <CheckCircle className="w-5 h-5 text-emerald-500" /> : <XCircle className="w-5 h-5 text-rose-500" />}
      <span className="text-sm font-medium text-gray-200">{message.message}</span>
      <button 
        onClick={() => onRemove(message.id)}
        className="absolute right-3 top-1/2 -translate-y-1/2 p-1 rounded-md opacity-50 hover:opacity-100 hover:bg-[#222] transition-colors"
      >
        <X className="w-4 h-4 text-gray-400" />
      </button>
    </motion.div>
  );
};

export const useToast = () => useContext(ToastContext);
