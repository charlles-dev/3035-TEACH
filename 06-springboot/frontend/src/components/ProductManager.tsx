import React, { useState, useEffect } from 'react';
import { Product, ProductPayload, ApiResponse } from '../types';
import { useToast } from './ui/ToastContext';
import { Trash2, Edit2, Plus, RefreshCw, Package, Tag, DollarSign, ArchiveRestore } from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

interface ProductManagerProps {
  title: string;
  description: string;
  baseUrl: string;
}

export const ProductManager: React.FC<ProductManagerProps> = ({ title, description, baseUrl }) => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  
  // Form State
  const [nome, setNome] = useState('');
  const [descricao, setDescricao] = useState('');
  const [preco, setPreco] = useState('');

  // List State
  const [searchQuery, setSearchQuery] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [sortBy, setSortBy] = useState('recent'); // recent, oldest, price_desc, price_asc, name_asc, name_desc

  const { addToast } = useToast();

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const res = await fetch(`${baseUrl}/produtos`);
      if (!res.ok) throw new Error('Falha ao buscar produtos');
      const json: ApiResponse<Product[]> = await res.json();
      if (json.success) {
        setProducts(json.data);
      } else {
        throw new Error(json.message || 'Erro na API');
      }
    } catch (error: any) {
      console.error(error);
      if (error.message === 'Failed to fetch') {
        addToast(`O servidor local (${baseUrl}) parece offline ou bloqueando CORS (Failed to fetch).`, 'error');
      } else {
        addToast('Não foi possível carregar os produtos.', 'error');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, [baseUrl]);

  const resetForm = () => {
    setEditingProduct(null);
    setNome('');
    setDescricao('');
    setPreco('');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!nome || !preco) {
      addToast('Nome e preço são obrigatórios', 'error');
      return;
    }

    const payload: ProductPayload = {
      nome,
      descricao,
      preco: parseFloat(preco)
    };

    try {
      const isEditing = !!editingProduct;
      const url = isEditing 
        ? `${baseUrl}/produtos/${editingProduct.id}`
        : `${baseUrl}/produtos`;
      
      const method = isEditing ? 'PATCH' : 'POST';

      const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      
      if (!res.ok) throw new Error('Falha ao salvar produto');
      
      const json: ApiResponse<Product> = await res.json();
      if (json.success) {
        addToast(isEditing ? 'Produto atualizado com sucesso!' : 'Produto criado com sucesso!', 'success');
        resetForm();
        fetchProducts();
      } else {
        throw new Error(json.message);
      }
    } catch (error: any) {
      if (error.message === 'Failed to fetch') {
        addToast(`Erro de conexão com ${baseUrl}. O servidor está online e permite CORS?`, 'error');
      } else {
        addToast(error.message || 'Erro ao salvar o produto.', 'error');
      }
    }
  };

  const handleEdit = (product: Product) => {
    setEditingProduct(product);
    setNome(product.nome);
    setDescricao(product.descricao || '');
    setPreco(product.preco.toString());
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Tem certeza que deseja excluir?')) return;
    try {
      const res = await fetch(`${baseUrl}/produtos/${id}`, { method: 'DELETE' });
      if (!res.ok) throw new Error('Falha ao excluir produto');
      const json: ApiResponse<null> = await res.json();
      if (json.success) {
        addToast('Produto excluído com sucesso.', 'success');
        fetchProducts();
      } else {
        throw new Error(json.message);
      }
    } catch (error: any) {
      if (error.message === 'Failed to fetch') {
        addToast('Failed to fetch. O servidor local está inacessível.', 'error');
      } else {
        addToast(error.message || 'Erro ao excluir o produto.', 'error');
      }
    }
  };

  const filteredProducts = products
    .filter(p => 
      p.nome.toLowerCase().includes(searchQuery.toLowerCase()) || 
      (p.descricao && p.descricao.toLowerCase().includes(searchQuery.toLowerCase()))
    )
    .filter(p => {
      const min = parseFloat(minPrice);
      const max = parseFloat(maxPrice);
      if (!isNaN(min) && p.preco < min) return false;
      if (!isNaN(max) && p.preco > max) return false;
      return true;
    })
    .sort((a, b) => {
      switch (sortBy) {
        case 'oldest': return new Date(a.dataCadastro).getTime() - new Date(b.dataCadastro).getTime();
        case 'recent': return new Date(b.dataCadastro).getTime() - new Date(a.dataCadastro).getTime();
        case 'price_desc': return b.preco - a.preco;
        case 'price_asc': return a.preco - b.preco;
        case 'name_asc': return a.nome.localeCompare(b.nome);
        case 'name_desc': return b.nome.localeCompare(a.nome);
        default: return 0;
      }
    });

  return (
    <div className="flex-1 flex flex-col md:flex-row h-full overflow-hidden animate-in fade-in duration-500 w-full">
      {/* Product List Section (Left) */}
      <section className="w-full md:w-2/3 flex flex-col border-r border-[#1a1a1a]">
        <div className="p-4 md:p-8 flex flex-col sm:flex-row sm:justify-between sm:items-end shrink-0 gap-4">
          <header>
            <div>
              <h2 className="text-2xl md:text-3xl serif mb-1">{title}</h2>
              <p className="text-sm text-gray-500">{description}</p>
            </div>
          </header>
          <div className="flex flex-wrap items-center gap-2 mt-4 sm:mt-0">
            <input 
              type="text" 
              placeholder="Buscar..." 
              value={searchQuery}
              onChange={e => setSearchQuery(e.target.value)}
              className="bg-[#111] border border-[#222] rounded px-3 py-1.5 text-xs w-full sm:w-48 focus:outline-none focus:border-indigo-500 text-gray-300" 
            />
            <div className="flex items-center gap-1 shrink-0">
              <input
                type="number"
                placeholder="Min R$"
                value={minPrice}
                onChange={e => setMinPrice(e.target.value)}
                className="bg-[#111] border border-[#222] rounded px-2 py-1.5 text-xs w-[72px] focus:outline-none focus:border-indigo-500 text-gray-300 text-center"
              />
              <span className="text-gray-600 text-xs">-</span>
              <input
                type="number"
                placeholder="Max R$"
                value={maxPrice}
                onChange={e => setMaxPrice(e.target.value)}
                className="bg-[#111] border border-[#222] rounded px-2 py-1.5 text-xs w-[72px] focus:outline-none focus:border-indigo-500 text-gray-300 text-center"
              />
            </div>
            <select
              value={sortBy}
              onChange={e => setSortBy(e.target.value)}
              className="bg-[#111] border border-[#222] rounded px-2 py-1.5 text-xs focus:outline-none focus:border-indigo-500 text-gray-300 shrink-0"
            >
              <option value="recent">Mais Recentes</option>
              <option value="oldest">Mais Antigos</option>
              <option value="price_desc">Maior Preço</option>
              <option value="price_asc">Menor Preço</option>
              <option value="name_asc">A-Z</option>
              <option value="name_desc">Z-A</option>
            </select>
            <button 
              onClick={fetchProducts}
              disabled={loading}
              className="bg-indigo-600 hover:bg-indigo-500 text-white text-xs px-4 py-1.5 rounded transition-colors whitespace-nowrap shrink-0 flex items-center gap-2"
            >
              <RefreshCw className={`w-3 h-3 ${loading ? 'animate-spin' : ''}`} />
              Recarregar
            </button>
          </div>
        </div>

        <div className="px-4 md:px-8 flex-1 overflow-hidden">
          <div className="h-full flex flex-col">
            <div className="hidden sm:grid grid-cols-5 text-[11px] uppercase tracking-widest text-gray-600 border-b border-[#1a1a1a] pb-3 mb-4 font-bold shrink-0">
              <div className="col-span-2">Produto</div>
              <div>Preço</div>
              <div>Data Cadastro</div>
              <div className="text-right">Ações</div>
            </div>
            
            <div className="flex-1 space-y-4 overflow-y-auto pr-2 pb-8">
              {filteredProducts.length === 0 && !loading ? (
                <div className="border border-[#222] border-dashed rounded p-12 text-center flex flex-col items-center text-gray-500 bg-[#0a0a0a]">
                  <ArchiveRestore className="w-8 h-8 mb-4 text-[#333]" />
                  <p className="text-sm font-medium">Nenhum produto encontrado.</p>
                </div>
              ) : (
                <AnimatePresence mode="popLayout">
                  {filteredProducts.map((product) => (
                    <motion.div 
                      layout
                      initial={{ opacity: 0, scale: 0.95 }}
                      animate={{ opacity: 1, scale: 1 }}
                      exit={{ opacity: 0, scale: 0.95, transition: { duration: 0.2 } }}
                      key={product.id}
                      className="card p-4 flex items-center group"
                    >
                      <div className="flex-1 grid grid-cols-1 sm:grid-cols-5 items-center gap-4 sm:gap-0">
                        <div className="col-span-2">
                          <h4 className="text-sm font-semibold text-gray-100">{product.nome}</h4>
                          <p className="text-xs text-gray-500 line-clamp-1">{product.descricao || 'Sem descrição'}</p>
                        </div>
                        <div className="text-sm font-mono text-indigo-300">
                          {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(product.preco)}
                        </div>
                        <div className="text-xs text-gray-500 font-mono italic">
                          {new Date(product.dataCadastro).toISOString().split('T')[0]}
                        </div>
                        <div className="flex justify-start sm:justify-end space-x-3">
                          <button 
                            onClick={() => handleEdit(product)}
                            className="text-gray-500 hover:text-blue-400 text-xs uppercase font-bold tracking-tighter transition-colors"
                          >
                            Edit
                          </button>
                          <button 
                            onClick={() => handleDelete(product.id)}
                            className="text-gray-500 hover:text-red-500 text-xs uppercase font-bold tracking-tighter transition-colors"
                          >
                            Del
                          </button>
                        </div>
                      </div>
                    </motion.div>
                  ))}
                </AnimatePresence>
              )}
            </div>
          </div>
        </div>
      </section>

      {/* Form Section (Right) */}
      <aside className="w-full md:w-1/3 bg-[#0a0a0a] p-4 md:p-8 flex flex-col justify-between overflow-y-auto">
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <h3 className="text-sm uppercase tracking-widest font-bold text-indigo-400 mb-6 flex items-center gap-2">
              <Package className="w-4 h-4" />
              {editingProduct ? 'Editar Produto' : 'Formulário de Produto'}
            </h3>
            
            <div className="space-y-5">
              <div className="space-y-2">
                <label className="text-[10px] uppercase tracking-wider text-gray-500 font-bold">Nome do Produto *</label>
                <input 
                  id="nome-input"
                  type="text" 
                  required
                  value={nome}
                  onChange={e => setNome(e.target.value)}
                  className="w-full bg-[#111] border border-[#333] rounded px-4 py-2 text-sm focus:border-indigo-500 outline-none text-gray-200 transition-colors"
                  placeholder="Ex: MacBook Pro M3"
                />
              </div>
              <div className="space-y-2">
                <label className="text-[10px] uppercase tracking-wider text-gray-500 font-bold">Preço (BRL) *</label>
                <input 
                  type="number" 
                  step="0.01"
                  required
                  min="0.01"
                  value={preco}
                  onChange={e => setPreco(e.target.value)}
                  className="w-full bg-[#111] border border-[#333] rounded px-4 py-2 text-sm focus:border-indigo-500 outline-none text-gray-200 transition-colors"
                  placeholder="0.00"
                />
              </div>
              <div className="space-y-2">
                <label className="text-[10px] uppercase tracking-wider text-gray-500 font-bold">Descrição</label>
                <textarea 
                  rows={4}
                  value={descricao}
                  onChange={e => setDescricao(e.target.value)}
                  className="w-full bg-[#111] border border-[#333] rounded px-4 py-2 text-sm focus:border-indigo-500 outline-none resize-none text-gray-200 transition-colors"
                  placeholder="Detalhes opcionais..."
                />
              </div>
            </div>
          </div>
          
          <div className="flex flex-col space-y-2 pt-4">
            <button 
              type="submit"
              className="w-full bg-indigo-600 py-3 rounded text-sm font-semibold hover:bg-indigo-500 transition-colors text-white"
            >
              {editingProduct ? 'Atualizar Produto' : 'Cadastrar Produto'}
            </button>
            <button 
              type="button"
              onClick={resetForm}
              className="w-full border border-[#333] py-3 rounded text-sm font-semibold text-gray-400 hover:bg-[#1a1a1a] transition-colors"
            >
              {editingProduct ? 'Cancelar Edição' : 'Limpar Campos'}
            </button>
          </div>
        </form>
      </aside>
    </div>
  );
};
