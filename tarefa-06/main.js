        let tarefas = [];
        
        let proximoId = 1;
        
        let filtroAtual = 'todas';
        
        const taskForm = document.getElementById('taskForm');
        const taskInput = document.getElementById('taskInput');
        const taskList = document.getElementById('taskList');
        const taskCounter = document.getElementById('taskCounter');
        const filterButtons = document.querySelectorAll('.filter-btn');
        
        function adicionarTarefa(nome) {
            if (!nome || nome.trim() === '') {
                alert('Por favor, digite o nome da tarefa!');
                return;
            }
            
            const novaTarefa = {
                id: proximoId++,
                nome: nome.trim(),
                dataCriacao: new Date(),
                concluida: false
            };
            
            tarefas.push(novaTarefa);
            renderizarTarefas();
            atualizarContador();
            taskInput.value = '';
        }
        
        function marcarComoConcluida(id) {
            const tarefa = tarefas.find(t => t.id === id);
            if (tarefa) {
                tarefa.concluida = !tarefa.concluida;
                renderizarTarefas();
                atualizarContador();
            }
        }
        function removerTarefa(id) {
            const index = tarefas.findIndex(t => t.id === id);
            if (index !== -1) {
                tarefas.splice(index, 1);
                renderizarTarefas();
                atualizarContador();
            }
        }
        function listarTarefasPendentes() {
            return tarefas.filter(tarefa => !tarefa.concluida);
        }
        function formatarData(data) {
            return data.toLocaleString('pt-BR', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
        }

        function filtrarTarefas() {
            switch (filtroAtual) {
                case 'pendentes':
                    return tarefas.filter(tarefa => !tarefa.concluida);
                case 'concluidas':
                    return tarefas.filter(tarefa => tarefa.concluida);
                default:
                    return tarefas;
            }
        }

        function renderizarTarefas() {
            const tarefasFiltradas = filtrarTarefas();
            
            if (tarefasFiltradas.length === 0) {
                taskList.innerHTML = `
                    <div class="empty-message">
                        Nenhuma tarefa encontrada
                    </div>
                `;
                return;
            }
            
            taskList.innerHTML = tarefasFiltradas.map(tarefa => `
                <div class="task-item ${tarefa.concluida ? 'completed' : ''}">
                    <div 
                        class="task-checkbox ${tarefa.concluida ? 'checked' : ''}"
                        onclick="marcarComoConcluida(${tarefa.id})"
                        title="${tarefa.concluida ? 'Marcar como pendente' : 'Marcar como concluída'}"
                    ></div>
                    
                    <div class="task-content">
                        <div class="task-name">${tarefa.nome}</div>
                        <div class="task-date">
                            Criada em: ${formatarData(tarefa.dataCriacao)}
                        </div>
                    </div>
                    
                    <div class="task-actions">
                        <button 
                            class="delete-btn" 
                            onclick="removerTarefa(${tarefa.id})"
                            title="Excluir tarefa"
                        >
                            Excluir
                        </button>
                    </div>
                </div>
            `).join('');
        }
        function atualizarContador() {
            const tarefasPendentes = listarTarefasPendentes();
            const count = tarefasPendentes.length;
            taskCounter.textContent = `${count} ${count === 1 ? 'tarefa pendente' : 'tarefas pendentes'}`;
        }
        function definirFiltro(filtro) {
            filtroAtual = filtro;
            filterButtons.forEach(btn => {
                btn.classList.remove('active');
                if (btn.dataset.filter === filtro) {
                    btn.classList.add('active');
                }
            });
            
            renderizarTarefas();
        }        
        taskForm.addEventListener('submit', (e) => {
            e.preventDefault();
            adicionarTarefa(taskInput.value);
        });
        
        filterButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                definirFiltro(btn.dataset.filter);
            });
        });

        function inicializarApp() {
            // Tarefas de exemplo baseadas nos dados fornecidos
            const tarefasExemplo = [
                { nome: 'Estudar JavaScript', concluida: false },
                { nome: 'Fazer compras', concluida: false },
                { nome: 'Praticar programação', concluida: false }
            ];
            
            tarefasExemplo.forEach(exemplo => {
                adicionarTarefa(exemplo.nome);
                if (exemplo.concluida) {
                    const ultimaTarefa = tarefas[tarefas.length - 1];
                    ultimaTarefa.concluida = true;
                }
            });
            
            renderizarTarefas();
            atualizarContador();
        }
        document.addEventListener('DOMContentLoaded', inicializarApp);