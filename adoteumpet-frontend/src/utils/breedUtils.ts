/**
 * Utilitários para trabalhar com raças de pets
 */

export interface BreedData {
  name: string;
  origin?: string;
  temperament?: string;
  energy_level?: number;
  image_url?: string;
}

/**
 * Busca informações de uma raça específica via API
 */
export async function fetchBreedData(
  apiUrl: string, 
  species: string, 
  breedName: string
): Promise<BreedData | null> {
  try {
    const response = await fetch(`${apiUrl}/api/breeds/${species.toLowerCase()}?name=${encodeURIComponent(breedName)}`);
    
    if (!response.ok) {
      return null;
    }
    
    const breeds: BreedData[] = await response.json();
    
    // Procura por correspondência exata primeiro, depois por similaridade
    const exactMatch = breeds.find(breed => 
      breed.name.toLowerCase() === breedName.toLowerCase()
    );
    
    if (exactMatch) {
      return exactMatch;
    }
    
    // Se não encontrar correspondência exata, procura por similaridade
    const similarMatch = breeds.find(breed => 
      breed.name.toLowerCase().includes(breedName.toLowerCase()) ||
      breedName.toLowerCase().includes(breed.name.toLowerCase())
    );
    
    return similarMatch || breeds[0] || null;
  } catch (error) {
    console.warn(`Erro ao buscar dados da raça ${breedName}:`, error);
    return null;
  }
}

/**
 * Mapeia espécies para formato amigável
 */
export function getSpeciesDisplayName(species: string): string {
  switch (species.toUpperCase()) {
    case 'DOG':
      return 'Cachorro';
    case 'CAT':
      return 'Gato';
    default:
      return species;
  }
}

/**
 * Mapeia status para formato amigável
 */
export function getStatusDisplayName(status: string): string {
  switch (status.toUpperCase()) {
    case 'AVAILABLE':
      return 'Disponível';
    case 'ADOPTED':
      return 'Adotado';
    default:
      return status;
  }
}

/**
 * Retorna emoji apropriado para a espécie
 */
export function getSpeciesEmoji(species: string): string {
  switch (species.toUpperCase()) {
    case 'DOG':
      return '🐕';
    case 'CAT':
      return '🐱';
    default:
      return '🐾';
  }
}

/**
 * Retorna cor apropriada para o status
 */
export function getStatusColor(status: string): string {
  switch (status.toUpperCase()) {
    case 'AVAILABLE':
      return 'bg-green-100 text-green-800';
    case 'ADOPTED':
      return 'bg-gray-100 text-gray-800';
    default:
      return 'bg-blue-100 text-blue-800';
  }
}