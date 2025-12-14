import { useState } from 'react'
import movies from './movies.json'
import './App.css'

function App() {
    const [selectedMovies, setSelectedMovies] = useState({})

    const handleCheckboxChange = (movie, isChecked) => {
        setSelectedMovies(prev => ({
            ...prev,
            [movie.id]: isChecked
        }))

        if (isChecked) {
            alert(`Você selecionou: ${movie.nome}`)
        }
    }

    return (
        <div className="container">
            <h1>Seleção de Filmes</h1>
            <ul className="movie-list">
                {movies.map(movie => (
                    <li key={movie.id} className={`movie-item ${selectedMovies[movie.id] ? 'selected' : ''}`}>
                        <label className="movie-label">
                            <input
                                type="checkbox"
                                checked={!!selectedMovies[movie.id]}
                                onChange={(e) => handleCheckboxChange(movie, e.target.checked)}
                            />
                            <span className="checkbox-custom"></span>
                            <span className="movie-info">
                                <span className="movie-name">{movie.nome}</span>
                                <span className="movie-genre">{movie.genero}</span>
                            </span>
                        </label>
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default App
