import { useState } from 'react'
import movies from './movies.json'
import './App.css'

function App() {
  const [search, setSearch] = useState('')

  const filteredMovies = movies.filter(movie =>
    movie.nome.toLowerCase().includes(search.toLowerCase())
  )

  return (
    <div className="container">
      <h1>Lista de Filmes</h1>
      <input
        type="text"
        placeholder="Pesquisar filme..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="search-input"
      />

      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Capa</th>
            <th>Nome</th>
            <th>Gênero</th>
          </tr>
        </thead>
        <tbody>
          {filteredMovies.map(movie => (
            <tr key={movie.id}>
              <td>{movie.id}</td>
              <td>
                <img src={movie.imagem} alt={movie.nome} className="movie-poster" />
              </td>
              <td>{movie.nome}</td>
              <td>{movie.genero}</td>
            </tr>
          ))}
          {filteredMovies.length === 0 && (
            <tr>
              <td colSpan="4" style={{ textAlign: 'center' }}>Nenhum filme encontrado</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  )
}

export default App
