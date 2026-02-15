import { useState } from 'react'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="container">
      <h1>Contador Interativo</h1>
      <div className="card">
        <p className="count-display">
          Você clicou <span>{count}</span> vezes
        </p>
        <button onClick={() => setCount(count + 1)}>
          Clique para contar
        </button>
      </div>
    </div>
  )
}

export default App
