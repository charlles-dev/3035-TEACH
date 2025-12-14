import { useState, useEffect } from 'react'
import styled from 'styled-components'

const Container = styled.div`
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  font-family: 'Poppins', sans-serif;
  text-align: center;
`

const Title = styled.h1`
  color: #333;
  font-size: 2.5rem;
  margin-bottom: 2rem;
  
  @media (prefers-color-scheme: dark) {
    color: #f0f2f5;
  }
`

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  padding: 10px;
`

const Card = styled.div`
  background: white;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(0,0,0,0.15);
  }

  @media (prefers-color-scheme: dark) {
    background: #2a2a2a;
    box-shadow: 0 4px 15px rgba(0,0,0,0.3);
  }
`

const PokemonImage = styled.img`
  width: 120px;
  height: 120px;
  margin-bottom: 10px;
`

const PokemonName = styled.h3`
  text-transform: capitalize;
  font-size: 1.2rem;
  color: #444;
  margin: 0;

  @media (prefers-color-scheme: dark) {
    color: #ddd;
  }
`

const Loading = styled.div`
  font-size: 1.5rem;
  color: #666;
  margin-top: 50px;
`

function App() {
    const [pokemons, setPokemons] = useState([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        const fetchPokemons = async () => {
            try {
                const response = await fetch('https://pokeapi.co/api/v2/pokemon?limit=20')
                const data = await response.json()

                // Fetch details for images
                const detailedPokemons = await Promise.all(
                    data.results.map(async (pokemon) => {
                        const res = await fetch(pokemon.url)
                        const details = await res.json()
                        return {
                            name: pokemon.name,
                            id: details.id,
                            image: details.sprites.other['official-artwork'].front_default
                        }
                    })
                )

                setPokemons(detailedPokemons)
            } catch (error) {
                console.error('Error fetching pokemons:', error)
            } finally {
                setLoading(false)
            }
        }

        fetchPokemons()
    }, [])

    return (
        <Container>
            <Title>Pokédex React</Title>
            {loading ? (
                <Loading>Carregando Pokémons...</Loading>
            ) : (
                <Grid>
                    {pokemons.map(pokemon => (
                        <Card key={pokemon.id}>
                            <PokemonImage src={pokemon.image} alt={pokemon.name} />
                            <PokemonName>{pokemon.name}</PokemonName>
                        </Card>
                    ))}
                </Grid>
            )}
        </Container>
    )
}

export default App
