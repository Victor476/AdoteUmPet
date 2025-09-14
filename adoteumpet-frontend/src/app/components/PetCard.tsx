"use client";

import React, { useState, useEffect } from "react";
import Link from "next/link";
import { LoadingSpinner } from "../../components/UI";
import { 
  fetchBreedData, 
  getSpeciesDisplayName, 
  getStatusDisplayName, 
  getSpeciesEmoji, 
  getStatusColor 
} from "../../utils/breedUtils";

// Cache simples para imagens de raças
const breedCache = new Map<string, string>();

interface Pet {
  id: string;
  name: string;
  species: string;
  breed?: string;
  ageYears?: number;
  shelterCity: string;
  status: string;
  imageUrl?: string;
}

interface PetCardProps {
  pet: Pet;
}

export default function PetCard({ pet }: PetCardProps) {
  const [breedImage, setBreedImage] = useState<string | null>(null);
  const [isClient, setIsClient] = useState(false);
  const [imageLoadError, setImageLoadError] = useState(false);
  const [isLoadingBreedImage, setIsLoadingBreedImage] = useState(false);
  
  const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

  useEffect(() => {
    setIsClient(true);
  }, []);

  // Buscar imagem da raça se não houver imagem própria
  useEffect(() => {
    if (!isClient || pet.imageUrl || !pet.breed || !pet.species) return;

    const fetchBreedImage = async () => {
      try {
        setIsLoadingBreedImage(true);
        const species = pet.species.toLowerCase();
        const cacheKey = `${species}_${pet.breed}`;
        
        // Verificar cache primeiro
        if (breedCache.has(cacheKey)) {
          setBreedImage(breedCache.get(cacheKey) || null);
          return;
        }

        // Buscar dados da raça via API
        if (pet.breed) {
          const breedData = await fetchBreedData(API_URL, species, pet.breed);
          
          if (breedData?.image_url) {
            // Salvar no cache
            breedCache.set(cacheKey, breedData.image_url);
            setBreedImage(breedData.image_url);
          }
        }
      } catch (error) {
        console.warn(`Erro ao buscar imagem da raça para ${pet.name}:`, error);
      } finally {
        setIsLoadingBreedImage(false);
      }
    };

    fetchBreedImage();
  }, [pet.species, pet.breed, pet.imageUrl, isClient, API_URL]);

  return (
    <Link href={`/pets/${pet.id}`} className="group">
      <div className="bg-white rounded-xl shadow-md hover:shadow-xl transition-all duration-300 cursor-pointer overflow-hidden border border-gray-100 group-hover:scale-105">
        <div className="aspect-square bg-gradient-to-br from-blue-50 to-purple-50 flex items-center justify-center relative overflow-hidden">
          {(pet.imageUrl || (isClient && breedImage)) && !imageLoadError ? (
            <>
              <img 
                src={pet.imageUrl || breedImage || ''} 
                alt={`Foto de ${pet.name} - ${pet.breed || pet.species}`}
                className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
                onError={() => setImageLoadError(true)}
                onLoad={() => setImageLoadError(false)}
              />
              <div className="absolute inset-0 bg-black opacity-0 group-hover:opacity-20 transition-opacity duration-300"></div>
            </>
          ) : isLoadingBreedImage ? (
            <>
              <LoadingSpinner size="sm" color="blue" />
              <div className="absolute inset-0 bg-black opacity-0 group-hover:opacity-10 transition-opacity duration-300"></div>
            </>
          ) : (
            <>
              <div className="text-6xl group-hover:scale-110 transition-transform duration-300">
                {getSpeciesEmoji(pet.species)}
              </div>
              <div className="absolute inset-0 bg-black opacity-0 group-hover:opacity-10 transition-opacity duration-300"></div>
            </>
          )}
        </div>

        <div className="p-4">
          <div className="flex items-start justify-between mb-3">
            <h3 className="text-lg font-bold text-gray-900 truncate flex-1 mr-2 group-hover:text-blue-600 transition-colors">
              {pet.name}
            </h3>
            <span className={`px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(pet.status)}`}>
              {getStatusDisplayName(pet.status)}
            </span>
          </div>

          <div className="space-y-2 text-sm text-gray-600">
            <div className="flex items-center">
              <span className="font-medium text-gray-700">Espécie:</span>
              <span className="ml-2">{getSpeciesDisplayName(pet.species)}</span>
            </div>
            
            {pet.breed && (
              <div className="flex items-center">
                <span className="font-medium text-gray-700">Raça:</span>
                <span className="ml-2">{pet.breed}</span>
              </div>
            )}

            {pet.ageYears && (
              <div className="flex items-center">
                <span className="font-medium text-gray-700">Idade:</span>
                <span className="ml-2">{pet.ageYears} ano{pet.ageYears !== 1 ? 's' : ''}</span>
              </div>
            )}

            <div className="flex items-center">
              <span className="font-medium text-gray-700">📍</span>
              <span className="ml-2">{pet.shelterCity}</span>
            </div>
          </div>

          <div className="mt-4 pt-3 border-t border-gray-100">
            <span className="text-blue-600 text-sm font-medium group-hover:text-blue-700">
              Clique para ver detalhes →
            </span>
          </div>
        </div>
      </div>
    </Link>
  );
}
