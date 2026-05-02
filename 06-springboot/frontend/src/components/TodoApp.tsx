import React, { useState, useEffect } from 'react';
import { Task, TaskPayload, TaskStatus } from '../types';
import { useToast } from './ui/ToastContext';
import { 
  LogOut, 
  Plus, 
  ShieldCheck, 
  KeyRound, 
  User,
  MoreVertical,
  Trash2,
  GripVertical
} from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';
import {
  DndContext,
  DragOverlay,
  rectIntersection,
  KeyboardSensor,
  PointerSensor,
  useSensor,
  useSensors,
  DragStartEvent,
  DragEndEvent,
  DragOverEvent,
  useDroppable,
} from '@dnd-kit/core';
import {
  SortableContext,
  sortableKeyboardCoordinates,
  verticalListSortingStrategy,
  useSortable,
} from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';

const TODO_API = 'http://localhost:8083/api/v1';

interface Column {
  id: TaskStatus;
  title: string;
  color: string;
}

const COLUMNS: Column[] = [
  { id: 'PENDENTE', title: 'Pendente', color: '#64748b' },
  { id: 'EM_ANDAMENTO', title: 'Em Progresso', color: '#f59e0b' },
  { id: 'CONCLUIDA', title: 'Concluído', color: '#10b981' },
];

function SortableTask({ task, onDelete }: { task: Task; onDelete: (id: number) => void }) {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({ id: task.id.toString() });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  };

  return (
    <div
      ref={setNodeRef}
      style={style}
      {...attributes}
      {...listeners}
      className={`card p-3 mb-2 cursor-grab active:cursor-grabbing group ${
        isDragging ? 'opacity-50 ring-2 ring-indigo-500' : ''
      }`}
    >
      <div className="flex items-start gap-2">
        <div className="flex-1 min-w-0">
          <h4 className={`text-sm font-medium truncate ${task.status === 'CONCLUIDA' ? 'text-gray-500 line-through' : 'text-gray-100'}`}>
            {task.titulo}
          </h4>
          {task.descricao && (
            <p className={`text-xs mt-1 line-clamp-2 ${task.status === 'CONCLUIDA' ? 'text-gray-600' : 'text-gray-400'}`}>
              {task.descricao}
            </p>
          )}
        </div>
        <button
          onClick={(e) => { e.stopPropagation(); onDelete(task.id); }}
          className="opacity-0 group-hover:opacity-100 text-gray-600 hover:text-red-500 transition-all"
        >
          <Trash2 className="w-4 h-4" />
        </button>
      </div>
    </div>
  );
}

function DroppableColumn({ 
  column, 
  tasks, 
  onDelete 
}: { 
  column: Column; 
  tasks: Task[];
  onDelete: (id: number) => void;
}) {
  const { setNodeRef, isOver } = useDroppable({ id: column.id });

  return (
    <div
      ref={setNodeRef}
      className={`flex-1 min-w-[280px] flex flex-col rounded-lg transition-colors ${
        isOver ? 'bg-gray-800/50 ring-2 ring-indigo-500/50' : ''
      }`}
    >
      <div className="flex items-center gap-2 mb-4 px-2">
        <div className="w-3 h-3 rounded-full" style={{ backgroundColor: column.color }} />
        <h3 className="text-sm font-semibold text-gray-300 uppercase tracking-wider">
          {column.title}
        </h3>
        <span className="text-xs text-gray-600 bg-gray-900 px-2 py-0.5 rounded-full">
          {tasks.length}
        </span>
      </div>

      <div className="flex-1 bg-gray-900/30 rounded-lg p-2 min-h-[200px]">
        <SortableContext items={tasks.map(t => t.id.toString())} strategy={verticalListSortingStrategy}>
          <AnimatePresence>
            {tasks.map((task) => (
              <motion.div
                key={task.id}
                layout
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -10 }}
              >
                <SortableTask task={task} onDelete={onDelete} />
              </motion.div>
            ))}
          </AnimatePresence>
        </SortableContext>

        {tasks.length === 0 && (
          <div className="text-center py-8 text-gray-600 text-sm">
            Arraste tarefas aqui
          </div>
        )}
      </div>
    </div>
  );
}

export const TodoApp: React.FC = () => {
  const [token, setToken] = useState<string | null>(localStorage.getItem('@app_token'));
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [authLoading, setAuthLoading] = useState(false);

  const [tasks, setTasks] = useState<Task[]>([]);
  const [tasksLoading, setTasksLoading] = useState(false);
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');

  const [activeId, setActiveId] = useState<string | null>(null);

  const { addToast } = useToast();

  const sensors = useSensors(
    useSensor(PointerSensor, { activationConstraint: { distance: 8 } }),
    useSensor(KeyboardSensor, { coordinateGetter: sortableKeyboardCoordinates })
  );

  useEffect(() => {
    if (token) fetchTasks();
  }, [token]);

  const handleAuth = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username || !password) {
      addToast('Preencha os campos', 'error');
      return;
    }
    setAuthLoading(true);

    try {
      const endpoint = isLogin ? '/auth/login' : '/auth/register';
      const formData = new URLSearchParams();
      formData.append('username', username);
      formData.append('password', password);
      
      const res = await fetch(`${TODO_API}${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
      });

      const json = await res.json();
      const tokenValue = json.data?.accessToken || json.token;

      if (res.ok && tokenValue) {
        localStorage.setItem('@app_token', tokenValue);
        setToken(tokenValue);
        addToast(isLogin ? 'Login realizado com sucesso!' : 'Conta criada!', 'success');
        setUsername('');
        setPassword('');
      } else {
        throw new Error(json.message || json.error || 'Erro de autenticação');
      }
    } catch (error: any) {
      addToast(error.message, 'error');
    } finally {
      setAuthLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('@app_token');
    setToken(null);
    setTasks([]);
  };

  const fetchTasks = async () => {
    setTasksLoading(true);
    try {
      console.log('Fetching tasks with token:', token?.substring(0, 20));
      const res = await fetch(`${TODO_API}/tasks`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      console.log('Tasks response status:', res.status);
      if (res.status === 401 || res.status === 403) {
        handleLogout();
        addToast('Sessão expirada', 'error');
        return;
      }
      const json = await res.json();
console.log('Tasks response raw:', json);
      const tasksData = json.data || json;
      console.log('Tasks data:', tasksData);
      console.log('Task statuses:', tasksData.map((t: Task) => t.status));
      setTasks(Array.isArray(tasksData) ? tasksData : []);
    } catch (error: any) {
      console.error('Fetch tasks error:', error);
      addToast('Erro ao carregar tarefas', 'error');
    } finally {
      setTasksLoading(false);
    }
  };

  const handleCreateTask = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!titulo) return;

    try {
      const formData = new URLSearchParams();
      formData.append('titulo', titulo);
      if (descricao) formData.append('descricao', descricao);
      
      const res = await fetch(`${TODO_API}/tasks`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'Authorization': `Bearer ${token}`
        },
        body: formData.toString()
      });
      if (!res.ok) throw new Error('Falha ao criar tarefa');
      
      addToast('Tarefa criada!', 'success');
      setTitulo('');
      setDescricao('');
      fetchTasks();
    } catch (error: any) {
      addToast('Erro ao criar tarefa', 'error');
    }
  };

  const handleDeleteTask = async (id: number) => {
    try {
      const res = await fetch(`${TODO_API}/tasks/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (!res.ok) throw new Error('Erro ao deletar');
      fetchTasks();
      addToast('Tarefa deletada', 'success');
    } catch (error: any) {
      addToast('Erro ao deletar', 'error');
    }
  };

  const handleDragStart = (event: DragStartEvent) => {
    setActiveId(event.active.id as string);
  };

  const handleDragOver = (event: DragOverEvent) => {
    const { active, over } = event;
    if (!over) return;

    const activeId = active.id as string;
    const overId = over.id as string;

    const activeTask = tasks.find(t => t.id.toString() === activeId);
    if (!activeTask) return;

    const overColumn = COLUMNS.find(c => c.id === overId);
    const overTask = tasks.find(t => t.id.toString() === overId);

    if (overColumn && activeTask.status !== overColumn.id) {
      const newStatus = overColumn.id;
      if (activeTask.status !== newStatus) {
        setTasks(prev => prev.map(t => 
          t.id.toString() === activeId ? { ...t, status: newStatus } : t
        ));
      }
    } else if (overTask && activeTask.status !== overTask.status) {
      setTasks(prev => prev.map(t => 
        t.id.toString() === activeId ? { ...t, status: overTask.status } : t
      ));
    }
  };

  const handleDragEnd = async (event: DragEndEvent) => {
    const { active, over } = event;
    setActiveId(null);

    if (!over) return;

    const activeTask = tasks.find(t => t.id.toString() === active.id);
    if (!activeTask) return;

    // Check if dropped on a column
    const overColumn = COLUMNS.find(c => c.id === over.id);
    if (overColumn) {
      try {
        const formData = new URLSearchParams();
        formData.append('status', overColumn.id);
        
        await fetch(`${TODO_API}/tasks/${activeTask.id}`, {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': `Bearer ${token}`
          },
          body: formData.toString()
        });
        fetchTasks();
      } catch (error) {
        fetchTasks();
      }
    }
  };

  const getTasksByColumn = (status: TaskStatus) => {
    const filtered = tasks.filter(t => t.status === status);
    console.log(`Column ${status}:`, filtered.length, filtered.map(t => t.titulo));
    return filtered;
  };

  const activeTask = tasks.find(t => t.id.toString() === activeId);

  if (!token) {
    return (
      <div className="flex-1 flex items-center justify-center h-full w-full bg-[#050505]">
        <div className="card p-8 relative overflow-hidden w-full max-w-sm">
          <div className="absolute top-0 right-0 -tr-3xl">
            <div className="bg-indigo-900/20 w-32 h-32 rounded-bl-[100px] absolute top-0 right-0 -z-10" />
          </div>
          
          <div className="flex justify-center mb-8">
            <div className="w-12 h-12 bg-indigo-600 rounded flex items-center justify-center font-bold text-white shadow-lg shadow-indigo-600/30">
              <ShieldCheck className="w-6 h-6" />
            </div>
          </div>
          
          <h2 className="text-xl font-bold text-center text-gray-100 mb-2">
            {isLogin ? 'Autenticação' : 'Criar Conta'}
          </h2>
          <p className="text-center text-gray-500 mb-8 text-xs uppercase tracking-widest font-semibold">
            {isLogin ? 'Secured JWT Access' : 'Register New User'}
          </p>

          <form onSubmit={handleAuth} className="space-y-4">
            <div className="space-y-2">
              <label className="text-[10px] uppercase tracking-wider text-gray-500 font-bold">Usuário</label>
              <div className="relative">
                <input 
                  type="text" 
                  value={username}
                  onChange={e => setUsername(e.target.value)}
                  className="w-full pl-10 pr-4 py-2.5 bg-[#111] border border-[#333] rounded text-sm focus:outline-none focus:border-indigo-500 transition-colors text-gray-200"
                  placeholder="admin"
                />
                <User className="w-4 h-4 text-gray-500 absolute left-3.5 top-3" />
              </div>
            </div>
            
            <div className="space-y-2">
              <label className="text-[10px] uppercase tracking-wider text-gray-500 font-bold">Senha</label>
              <div className="relative">
                <input 
                  type="password" 
                  value={password}
                  onChange={e => setPassword(e.target.value)}
                  className="w-full pl-10 pr-4 py-2.5 bg-[#111] border border-[#333] rounded text-sm focus:outline-none focus:border-indigo-500 transition-colors text-gray-200"
                  placeholder="••••••••"
                />
                <KeyRound className="w-4 h-4 text-gray-500 absolute left-3.5 top-3" />
              </div>
            </div>

            <button 
              type="submit"
              disabled={authLoading}
              className="w-full bg-indigo-600 hover:bg-indigo-500 text-white text-sm font-semibold py-3 rounded transition-colors disabled:opacity-70 mt-6"
            >
              {authLoading ? 'Processando...' : (isLogin ? 'Autenticar' : 'Registrar')}
            </button>
          </form>

          <div className="mt-8 text-center pt-6 border-t border-[#1a1a1a]">
            <button 
              onClick={() => setIsLogin(!isLogin)}
              className="text-[10px] uppercase tracking-widest font-bold text-gray-500 hover:text-indigo-400 transition-colors"
            >
              {isLogin ? 'Criar nova conta' : 'Já possui conta? Faça login'}
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="flex-1 flex flex-col h-full overflow-hidden w-full">
      <header className="flex items-center justify-between px-6 py-4 border-b border-[#1a1a1a] shrink-0">
        <div>
          <h2 className="text-xl font-bold text-gray-100">Kanban JWT</h2>
          <p className="text-xs text-gray-500">
            Sessão: <span className="font-mono text-emerald-400">{token?.substring(0, 12)}...</span>
          </p>
        </div>
        <div className="flex items-center gap-4">
          <button 
            onClick={handleLogout}
            className="bg-[#111] hover:bg-[#1a1a1a] border border-[#222] text-rose-500 text-xs px-4 py-2 rounded transition-colors flex items-center gap-2"
          >
            <LogOut className="w-3 h-3" />
            Sair
          </button>
        </div>
      </header>

      <div className="flex-1 flex flex-col lg:flex-row overflow-hidden">
        <div className="flex-1 p-4 lg:p-6 overflow-x-auto">
          <DndContext
            sensors={sensors}
            collisionDetection={rectIntersection}
            onDragStart={handleDragStart}
            onDragOver={handleDragOver}
            onDragEnd={handleDragEnd}
          >
            <div className="flex gap-4 min-h-full">
              {COLUMNS.map((column) => (
                <DroppableColumn
                  key={column.id}
                  column={column}
                  tasks={getTasksByColumn(column.id)}
                  onDelete={handleDeleteTask}
                />
              ))}
            </div>
            <DragOverlay>
              {activeTask && (
                <div className="card p-3 w-[250px] opacity-90 ring-2 ring-indigo-500">
                  <h4 className="text-sm font-medium text-gray-100">{activeTask.titulo}</h4>
                </div>
              )}
            </DragOverlay>
          </DndContext>
        </div>

        <aside className="w-full lg:w-80 bg-[#0a0a0a] p-4 lg:p-6 border-l border-[#1a1a1a] overflow-y-auto shrink-0">
          <form onSubmit={handleCreateTask} className="space-y-6">
            <div>
              <h3 className="text-sm uppercase tracking-widest font-bold text-indigo-400 mb-4 flex items-center gap-2">
                <Plus className="w-4 h-4" />
                Nova Tarefa
              </h3>
              
              <div className="space-y-4">
                <div className="space-y-2">
                  <label className="text-[10px] uppercase tracking-wider text-gray-500 font-bold">Título *</label>
                  <input 
                    type="text" 
                    value={titulo}
                    onChange={e => setTitulo(e.target.value)}
                    placeholder="Título da tarefa..."
                    required
                    className="w-full bg-[#111] border border-[#333] rounded px-4 py-2 text-sm focus:border-indigo-500 outline-none text-gray-200 transition-colors"
                  />
                </div>
                <div className="space-y-2">
                  <label className="text-[10px] uppercase tracking-wider text-gray-500 font-bold">Descrição</label>
                  <textarea 
                    value={descricao}
                    onChange={e => setDescricao(e.target.value)}
                    placeholder="Detalhes (opcional)..."
                    rows={3}
                    className="w-full bg-[#111] border border-[#333] rounded px-4 py-2 text-sm focus:border-indigo-500 outline-none resize-none text-gray-200 transition-colors"
                  />
                </div>
              </div>
            </div>
            
            <button 
              type="submit"
              className="w-full bg-indigo-600 hover:bg-indigo-500 py-3 rounded text-sm text-white font-semibold transition-colors"
            >
              Criar Tarefa
            </button>
          </form>
        </aside>
      </div>
    </div>
  );
};