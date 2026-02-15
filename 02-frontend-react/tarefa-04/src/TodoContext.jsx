import { createContext, useState, useContext } from 'react'

const TodoContext = createContext()

export function TodoProvider({ children }) {
    const [todos, setTodos] = useState([])

    const addTodo = (text) => {
        setTodos([...todos, { id: Date.now(), text, completed: false }])
    }

    const removeTodo = (id) => {
        setTodos(todos.filter(todo => todo.id !== id))
    }

    const toggleTodo = (id) => {
        setTodos(todos.map(todo =>
            todo.id === id ? { ...todo, completed: !todo.completed } : todo
        ))
    }

    return (
        <TodoContext.Provider value={{ todos, addTodo, removeTodo, toggleTodo }}>
            {children}
        </TodoContext.Provider>
    )
}

export function useTodo() {
    return useContext(TodoContext)
}
