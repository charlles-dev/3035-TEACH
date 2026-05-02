/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React, { useState } from 'react';
import { Database, Server, CheckSquare, LayoutList, AlertTriangle } from 'lucide-react';
import { ToastProvider } from './components/ui/ToastContext';
import { ProductManager } from './components/ProductManager';
import { TodoApp } from './components/TodoApp';

type Tab = 'EX1' | 'EX2' | 'TODO';

export default function App() {
  const [activeTab, setActiveTab] = useState<Tab>('EX1');

  return (
    <ToastProvider>
      <div className="h-screen flex flex-col w-full max-w-7xl mx-auto overflow-hidden border-x border-[#1a1a1a]">
        {/* CORS Info Banner */}
        <div className="bg-indigo-900/30 border-b border-indigo-500/20 px-4 py-2 flex items-center justify-center gap-3 text-xs text-indigo-200 shrink-0">
          <AlertTriangle className="w-4 h-4 text-amber-400 shrink-0" />
          <p>
            Ao testar, se encontrar o erro <strong>"Failed to fetch"</strong>, certifique-se que as APIs Spring Boot locais estão rodando e com o CORS liberado, adicionando <code className="bg-black/30 px-1 py-0.5 rounded text-amber-300">@CrossOrigin(origins = "*")</code> nos seus controllers.
          </p>
        </div>

        {/* Navigation Bar */}
        <header className="h-20 border-b border-[#1a1a1a] flex items-center justify-center px-4 sm:px-8 bg-[#050505] shrink-0">
          <nav className="flex space-x-6 sm:space-x-10 h-full items-center">
            <button
              onClick={() => setActiveTab('EX1')}
              className={`text-[10px] sm:text-sm uppercase tracking-widest font-semibold pt-[18px] ${
                activeTab === 'EX1' 
                  ? 'tab-active' 
                  : 'text-gray-500 hover:text-gray-300'
              }`}
            >
              Exercício 01
            </button>
            <button
              onClick={() => setActiveTab('EX2')}
               className={`text-[10px] sm:text-sm uppercase tracking-widest font-semibold pt-[18px] ${
                activeTab === 'EX2' 
                  ? 'tab-active' 
                  : 'text-gray-500 hover:text-gray-300'
              }`}
            >
              Exercício 02
            </button>
            <button
              onClick={() => setActiveTab('TODO')}
               className={`text-[10px] sm:text-sm uppercase tracking-widest font-semibold pt-[18px] ${
                activeTab === 'TODO' 
                  ? 'tab-active' 
                  : 'text-gray-500 hover:text-gray-300'
              }`}
            >
              Desafio ToDo
            </button>
          </nav>
        </header>

        {/* Main Content Area */}
        <main className="flex-1 flex overflow-hidden">
          {activeTab === 'EX1' && (
            <ProductManager 
              title="Produtos In-Memory" 
              description="Gerencie o inventário volátil do servidor local." 
              baseUrl="http://localhost:8081" 
            />
          )}
          {activeTab === 'EX2' && (
            <ProductManager 
              title="Produtos PostgreSQL" 
              description="CRUD de produtos persistindo dados em um banco de dados relacional (PostgreSQL)." 
              baseUrl="http://localhost:8082" 
            />
          )}
          {activeTab === 'TODO' && (
            <TodoApp />
          )}
        </main>
      </div>
    </ToastProvider>
  );
}
