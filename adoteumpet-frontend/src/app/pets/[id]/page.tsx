'use client';

import { useState, useEffect, use } from 'react';
import { useRouter } from 'next/navigation';
import dynamic from 'next/dynamic';

// Carregamento dinâmico do mapa para evitar problemas de SSR
const Map = dynamic(() => import('../../../components/Map/LeafletMap'), {
  ssr: false,
  loading: () => (
    <div className="w-full h-64 bg-gray-100 rounded-lg flex items-center justify-center">
      <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
    </div>
  ),
});

import LoadingSpinner from '../../../components/UI/LoadingSpinner';
import ErrorMessage from '../../../components/UI/ErrorMessage';
import Button from '../../../components/UI/Button';
import { Pet } from '../../../types/pet';

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

export default function PetDetails({ params }: { params: Promise<{ id: string }> }) {
  const resolvedParams = use(params);
  const [pet, setPet] = useState<Pet | null>(null);
  const [petImage, setPetImage] = useState<string | null>(null);
  const [imageLoading, setImageLoading] = useState(true);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  // Função para gerar emoji baseado na espécie
  const getSpeciesEmoji = (species: string) => {
    return species === 'DOG' ? '🐕' : '🐱';
  };

  // Função para buscar imagem específica da raça usando APIs especializadas
  const fetchBreedImage = async (pet: Pet) => {
    try {
      if (pet.species === 'DOG') {
        // Mapear raças para a Dog CEO API
        const breedMapping: { [key: string]: string } = {
          'Labrador Retriever': 'retriever/labrador',
          'Golden Retriever': 'retriever/golden',
          'German Shepherd': 'germanshepherd',
          'Pastor Alemão': 'germanshepherd',
          'Bulldog': 'bulldog/english',
          'French Bulldog': 'bulldog/french',
          'Poodle': 'poodle/standard',
          'Beagle': 'beagle',
          'Rottweiler': 'rottweiler',
          'Yorkshire Terrier': 'terrier/yorkshire',
          'Chihuahua': 'chihuahua',
          'Dachshund': 'dachshund',
          'Boxer': 'boxer',
          'Siberian Husky': 'husky',
          'Border Collie': 'collie/border',
          'Cocker Spaniel': 'spaniel/cocker',
          'Shih Tzu': 'shihtzu',
          'Maltese': 'maltese',
          'Pug': 'pug',
          'Akita': 'akita',
          'Doberman': 'doberman'
        };

        const apiBreed = breedMapping[pet.breed] || 'retriever/golden'; // fallback
        const response = await fetch(`https://dog.ceo/api/breed/${apiBreed}/images/random`);
        
        if (response.ok) {
          const data = await response.json();
          if (data.status === 'success' && data.message) {
            return data.message;
          }
        }
      }
      
      // Fallback para Unsplash com busca por raça
      const breedSearch = encodeURIComponent(`${pet.species.toLowerCase()} ${pet.breed}`);
      return `https://source.unsplash.com/400x300/?${breedSearch}`;
      
    } catch (error) {
      console.error('Erro ao buscar imagem da raça:', error);
      // Fallback para imagem genérica
      return `https://via.placeholder.com/400x300/f3f4f6/6b7280?text=${encodeURIComponent(getSpeciesEmoji(pet.species) + ' ' + pet.name)}`;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'AVAILABLE':
        return 'bg-green-100 text-green-800';
      case 'ADOPTED':
        return 'bg-gray-100 text-gray-800';
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatusText = (status: string) => {
    switch (status) {
      case 'AVAILABLE':
        return 'Disponível';
      case 'ADOPTED':
        return 'Adotado';
      case 'PENDING':
        return 'Pendente';
      default:
        return status;
    }
  };

  useEffect(() => {
    const fetchPet = async () => {
      try {
        setLoading(true);
        setError(null);
        
        const response = await fetch(`${API_URL}/api/pets/${resolvedParams.id}`);
        
        if (!response.ok) {
          if (response.status === 404) {
            setError('Pet não encontrado');
          } else {
            setError('Erro ao carregar dados do pet');
          }
          return;
        }
        
        const petData = await response.json();
        setPet(petData);
        
        // Buscar imagem específica da raça
        setImageLoading(true);
        try {
          const imageUrl = await fetchBreedImage(petData);
          setPetImage(imageUrl);
        } catch (error) {
          console.error('Erro ao carregar imagem:', error);
          // Usar fallback se a busca da imagem falhar
          setPetImage(`https://via.placeholder.com/400x300/f3f4f6/6b7280?text=${encodeURIComponent(getSpeciesEmoji(petData.species) + ' ' + petData.name)}`);
        } finally {
          setImageLoading(false);
        }
      } catch (error) {
        console.error('Erro ao buscar pet:', error);
        setError('Erro ao conectar com o servidor');
      } finally {
        setLoading(false);
      }
    };

    if (resolvedParams.id) {
      fetchPet();
    }
  }, [resolvedParams.id]);

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="container mx-auto px-4">
          <div className="bg-white rounded-lg shadow-md p-8 text-center">
            <LoadingSpinner />
            <p className="mt-4 text-gray-600">Carregando detalhes do pet...</p>
          </div>
        </div>
      </div>
    );
  }

  if (error || !pet) {
    return (
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="container mx-auto px-4">
          <div className="bg-white rounded-lg shadow-md p-8 text-center">
            <ErrorMessage message={error || 'Pet não encontrado'} />
            <Button 
              onClick={() => router.push('/pets')}
              className="mt-4"
            >
              ← Voltar para a lista de pets
            </Button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        {/* Header */}
        <div className="mb-6">
          <Button 
            onClick={() => router.push('/pets')}
            variant="outline"
            className="mb-4"
          >
            ← Voltar para a lista
          </Button>
          <div className="flex items-center gap-3">
            <span className="text-4xl">{getSpeciesEmoji(pet.species)}</span>
            <div>
              <h1 className="text-3xl font-bold text-gray-900">{pet.name}</h1>
              <span className={`inline-block px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(pet.status)}`}>
                {getStatusText(pet.status)}
              </span>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Informações do Pet */}
          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">Informações do Pet</h2>
            
            {/* Imagem do Pet */}
            <div className="mb-6">
              <div className="relative overflow-hidden rounded-lg bg-gray-100">
                {imageLoading ? (
                  <div className="w-full h-64 bg-gray-200 animate-pulse flex items-center justify-center">
                    <div className="text-center">
                      <div className="text-4xl mb-2">{getSpeciesEmoji(pet.species)}</div>
                      <div className="text-sm text-gray-500">Carregando imagem da raça...</div>
                    </div>
                  </div>
                ) : petImage ? (
                  <img 
                    src={petImage} 
                    alt={`${pet.breed} - ${pet.name}`}
                    className="w-full h-64 object-cover transition-transform hover:scale-105"
                    onError={(e) => {
                      // Fallback simples para placeholder
                      const target = e.target as HTMLImageElement;
                      if (!target.src.includes('via.placeholder.com')) {
                        target.src = `https://via.placeholder.com/400x300/e5e7eb/6b7280?text=${encodeURIComponent(getSpeciesEmoji(pet.species) + ' ' + pet.name)}`;
                      }
                    }}
                  />
                ) : (
                  <div className="w-full h-64 bg-gray-100 flex items-center justify-center">
                    <div className="text-center text-gray-500">
                      <div className="text-4xl mb-2">{getSpeciesEmoji(pet.species)}</div>
                      <div className="text-sm">Imagem não disponível</div>
                    </div>
                  </div>
                )}
                <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/50 to-transparent p-4">
                  <p className="text-white font-semibold text-lg">{pet.name}</p>
                  <p className="text-white/80 text-sm">{pet.breed}</p>
                </div>
              </div>
            </div>

            <div className="space-y-4">
              <div>
                <span className="font-semibold text-gray-700">Espécie:</span>
                <span className="ml-2 text-gray-900">{pet.species === 'DOG' ? 'Cachorro' : 'Gato'}</span>
              </div>
              
              <div>
                <span className="font-semibold text-gray-700">Raça:</span>
                <span className="ml-2 text-gray-900">{pet.breed}</span>
              </div>
              
              <div>
                <span className="font-semibold text-gray-700">Idade:</span>
                <span className="ml-2 text-gray-900">{pet.ageYears} {pet.ageYears === 1 ? 'ano' : 'anos'}</span>
              </div>
              
              <div>
                <span className="font-semibold text-gray-700">Status:</span>
                <span className={`ml-2 px-2 py-1 rounded-full text-sm ${getStatusColor(pet.status)}`}>
                  {getStatusText(pet.status)}
                </span>
              </div>

              <div>
                <span className="font-semibold text-gray-700">Cadastrado em:</span>
                <span className="ml-2 text-gray-900">
                  {new Date(pet.createdAt).toLocaleDateString('pt-BR')}
                </span>
              </div>
            </div>
          </div>

          {/* Informações do Abrigo e Mapa */}
          <div className="bg-white rounded-lg shadow-md p-6">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">Localização do Abrigo</h2>
            
            <div className="space-y-4 mb-6">
              <div>
                <span className="font-semibold text-gray-700">Cidade do Abrigo:</span>
                <span className="ml-2 text-gray-900">{pet.shelterCity}</span>
              </div>

              {pet.shelterLat && pet.shelterLng && (
                <div>
                  <span className="font-semibold text-gray-700">Coordenadas:</span>
                  <span className="ml-2 text-gray-900 text-sm font-mono">
                    {pet.shelterLat.toFixed(4)}, {pet.shelterLng.toFixed(4)}
                  </span>
                </div>
              )}
            </div>

            {/* Mapa */}
            {pet.shelterLat && pet.shelterLng ? (
              <div>
                <h3 className="text-lg font-medium text-gray-900 mb-3">Localização no Mapa</h3>
                <Map
                  latitude={pet.shelterLat}
                  longitude={pet.shelterLng}
                  shelterName={`Abrigo em ${pet.shelterCity}`}
                  shelterAddress={pet.shelterCity}
                  petName={pet.name}
                />
              </div>
            ) : (
              <div className="bg-gray-100 rounded-lg p-4 text-center text-gray-600">
                <p>Coordenadas do abrigo não disponíveis</p>
              </div>
            )}
          </div>
        </div>

        {/* Botão de Adoção */}
        {pet.status === 'AVAILABLE' && (
          <div className="mt-8 bg-white rounded-lg shadow-md p-6 text-center">
            <h3 className="text-xl font-semibold text-gray-900 mb-3">
              Interessado em adotar {pet.name}?
            </h3>
            <p className="text-gray-600 mb-4">
              Entre em contato com os abrigos da região de {pet.shelterCity} para saber mais sobre o processo de adoção.
            </p>
            <div className="flex flex-col sm:flex-row gap-3 justify-center">
              <Button className="bg-green-600 hover:bg-green-700 text-white">
                📍 Buscar Abrigos em {pet.shelterCity}
              </Button>
              <Button variant="outline">
                ❤️ Marcar como Favorito
              </Button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}