import { useState } from 'react'
import { TodoProvider, useTodo } from './TodoContext'
import './App.css'

function TodoList() {
    const [input, setInput] = useState('')
    const { todos, addTodo, removeTodo, toggleTodo } = useTodo()

    const handleSubmit = (e) => {
        e.preventDefault()
        if (!input.trim()) return
        addTodo(input)
        setInput('')
    }

    return (
        <div className="container">
            <h1>Lista de Tarefas</h1>
            <form onSubmit={handleSubmit} className="todo-form">
                <input
                    value={input}
                    onChange={e => setInput(e.target.value)}
                    placeholder="Nova tarefa..."
                    className="todo-input"
                />
                <button type="submit" className="add-btn">Adicionar</button>
            </form>

            <ul className="todo-list">
                {todos.map(todo => (
                    <li key={todo.id} className={`todo-item ${todo.completed ? 'completed' : ''}`}>
                        <span onClick={() => toggleTodo(todo.id)} className="todo-text">
                            {todo.text}
                        </span>
                        <button onClick={() => removeTodo(todo.id)} className="delete-btn">
                            Excluir
                        </button>
                    </li>
                ))}
                {todos.length === 0 && <p className="empty-msg">Nenhuma tarefa por enquanto!</p>}
            </ul>
        </div>
    )
}

function App() {
    return (
        <TodoProvider>
            <TodoList />
        </TodoProvider>
    )
}

export default App
