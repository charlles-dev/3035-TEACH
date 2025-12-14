import { useTheme, ThemeProvider } from './ThemeContext'
import './App.css'

function ThemeContent() {
    const { theme, toggleTheme } = useTheme()

    return (
        <div className={`container ${theme}-mode`}>
            <h1>Alternador de Tema</h1>
            <div className="card">
                <p>O tema atual é: <strong>{theme === 'light' ? 'Claro ☀️' : 'Escuro 🌙'}</strong></p>
                <button onClick={toggleTheme} className="toggle-btn">
                    Alternar para {theme === 'light' ? 'Escuro' : 'Claro'}
                </button>
            </div>
            <p className="description">
                Este exemplo utiliza a Context API para gerenciar o estado do tema globalmente.
                Mudanças no tema afetam toda a aplicação.
            </p>
        </div>
    )
}

function App() {
    return (
        <ThemeProvider>
            <ThemeContent />
        </ThemeProvider>
    )
}

export default App
