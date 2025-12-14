import { useState, useEffect } from 'react'
import styled, { createGlobalStyle, keyframes } from 'styled-components'

const GlobalStyle = createGlobalStyle`
  @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;500;700&display=swap');
  
  body {
    font-family: 'Outfit', sans-serif;
    margin: 0;
    padding: 0;
    min-height: 100vh;
    background: linear-gradient(135deg, #fdfbfb 0%, #ebedee 100%);
    display: flex;
    justify-content: center;
    
    @media (prefers-color-scheme: dark) {
      background: linear-gradient(135deg, #232526 0%, #414345 100%);
    }
  }

  #root {
    width: 100%;
    display: flex;
    justify-content: center;
  }
`

const Container = styled.div`
  max-width: 1200px;
  width: 90%;
  padding: 4rem 1rem;
`

const Title = styled.h1`
  text-align: center;
  font-size: 3rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 3rem;
  text-transform: uppercase;
  letter-spacing: 2px;
  
  span {
    color: #ff5252;
  }

  @media (prefers-color-scheme: dark) {
    color: #fff;
  }
`

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 30px;
`

const fadeInUp = keyframes`
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
`

const Card = styled.div`
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 2rem;
  text-align: center;
  box-shadow: 0 10px 30px rgba(0,0,0,0.05);
  transition: all 0.3s ease;
  border: 1px solid rgba(255,255,255,0.5);
  animation: ${fadeInUp} 0.5s ease backwards;
  animation-delay: ${props => props.index * 0.05}s;
  cursor: pointer;

  &:hover {
    transform: translateY(-10px) scale(1.02);
    box-shadow: 0 20px 40px rgba(0,0,0,0.12);
  }

  @media (prefers-color-scheme: dark) {
    background: rgba(30, 30, 30, 0.6);
    border-color: rgba(255,255,255,0.1);
    box-shadow: 0 10px 30px rgba(0,0,0,0.3);
  }
`

const PokemonImage = styled.img`
  width: 140px;
  height: 140px;
  object-fit: contain;
  margin-bottom: 1.5rem;
  filter: drop-shadow(0 10px 10px rgba(0,0,0,0.2));
`

const PokemonName = styled.h3`
  font-size: 1.3rem;
  text-transform: capitalize;
  color: #2d3436;
  margin: 0;
  font-weight: 700;

  @media (prefers-color-scheme: dark) {
    color: #dfe6e9;
  }
`

const TypeBadge = styled.span`
  background: #dfe6e9;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  color: #636e72;
  margin-top: 10px;
  display: inline-block;
  font-weight: 500;
`

function App() {
  const [pokemons, setPokemons] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchPokemons = async () => {
      try {
        const response = await fetch('https://pokeapi.co/api/v2/pokemon?limit=24')
        const data = await response.json()
        
        const detailedPokemons = await Promise.all(
          data.results.map(async (pokemon) => {
            const res = await fetch(pokemon.url)
            const details = await res.json()
            return {
              name: pokemon.name,
              id: details.id,
              image: details.sprites.other['official-artwork'].front_default,
              // Intentionally skipping types to keep logic simple as allowed in prompt,
              // or could add if we really wanted to go extra mile. I'll stick to name/image as verified.
            }
          })
        )
        
        setPokemons(detailedPokemons)
      } catch (error) {
        console.error('Error:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchPokemons()
  }, [])

  return (
    <>
      <GlobalStyle />
      <Container>
        <Title>Pokédex <span>React</span></Title>
        <Grid>
          {pokemons.map((pokemon, index) => (
            <Card key={pokemon.id} index={index}>
              <PokemonImage src={pokemon.image} alt={pokemon.name} />
              <PokemonName>{pokemon.name}</PokemonName>
              {/* <TypeBadge>Type</TypeBadge> */}
            </Card>
          ))}
        </Grid>
      </Container>
    </>
  )
}

export default App
