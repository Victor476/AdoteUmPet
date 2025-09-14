"use client";

import React, { useState, useEffect, useCallback } from "react";
import { BreedData } from "../../utils/breedUtils";
import Pagination from "../../components/Pagination";
import { LoadingSpinner, ErrorMessage } from "../../components/UI";

// Componente para exibir um card de raça
function BreedCard({ breed }: { breed: BreedData }) {
  const [imageLoadError, setImageLoadError] = useState(false);

  return (
    <div className="bg-white rounded-xl shadow-md hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100 hover:scale-105 min-h-[420px] flex flex-col">
      <div className="aspect-square bg-gradient-to-br from-blue-50 to-purple-50 flex items-center justify-center relative overflow-hidden flex-shrink-0">
        {breed.image_url && !imageLoadError ? (
          <img
            src={breed.image_url}
            alt={`Imagem da raça ${breed.name}`}
            className="w-full h-full object-cover hover:scale-110 transition-transform duration-300"
            onError={() => setImageLoadError(true)}
          />
        ) : (
          <div className="text-6xl">🐾</div>
        )}
      </div>

      <div className="p-5 flex-1 flex flex-col min-h-[220px]">
        <h3 className="text-lg font-bold text-gray-900 mb-3 flex-shrink-0 leading-tight break-words max-w-full">{breed.name}</h3>

        <div className="space-y-3 text-sm flex-1 min-h-0">
          {breed.origin && (
            <div className="flex flex-col space-y-1">
              <span className="font-semibold text-gray-800 text-xs uppercase tracking-wide">Origem</span>
              <span className="text-gray-800 text-sm leading-relaxed break-words max-w-full">{breed.origin}</span>
            </div>
          )}

          {breed.temperament && (
            <div className="flex flex-col space-y-1">
              <span className="font-semibold text-gray-800 text-xs uppercase tracking-wide">Temperamento</span>
              <span className="text-gray-800 text-sm leading-relaxed break-words max-w-full">{breed.temperament}</span>
            </div>
          )}

          {breed.energy_level && (
            <div className="flex flex-col space-y-2">
              <span className="font-semibold text-gray-800 text-xs uppercase tracking-wide">Nível de Energia</span>
              <div className="flex items-center space-x-2">
                <div className="flex space-x-1">
                  {[1, 2, 3, 4, 5].map((level) => (
                    <div
                      key={level}
                      className={`h-2 w-6 rounded-full ${
                        level <= breed.energy_level!
                          ? "bg-blue-500"
                          : "bg-gray-200"
                      }`}
                    />
                  ))}
                </div>
                <span className="text-xs text-gray-800 font-medium">
                  {breed.energy_level}/5
                </span>
              </div>
            </div>
          )}
        </div>
        
        {/* Espaçador para garantir que não haja sobreposição */}
        <div className="mt-auto pt-4"></div>
      </div>
    </div>
  );
}

// Hook personalizado para debouncing
function useDebounce<T>(value: T, delay: number): T {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debouncedValue;
}

export default function BreedsPage() {
  const [species, setSpecies] = useState<"dog" | "cat">("dog");
  const [searchTerm, setSearchTerm] = useState("");
  const [allBreeds, setAllBreeds] = useState<BreedData[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 12; // 12 cards por página para um layout 4x3

  const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";
  
  // Debounce do termo de busca para evitar muitas requisições
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  // Filtrar raças baseado no termo de busca
  const filteredBreeds = allBreeds.filter(breed =>
    breed.name.toLowerCase().includes(debouncedSearchTerm.toLowerCase())
  );

  // Calcular paginação
  const totalPages = Math.ceil(filteredBreeds.length / itemsPerPage);
  const startIndex = currentPage * itemsPerPage;
  const paginatedBreeds = filteredBreeds.slice(startIndex, startIndex + itemsPerPage);

  const fetchBreeds = useCallback(async (selectedSpecies: string, nameFilter?: string) => {
    try {
      setLoading(true);
      setError(null);

      const searchParams = new URLSearchParams();
      if (nameFilter && nameFilter.trim()) {
        searchParams.append('name', nameFilter.trim());
      }

      const url = `${API_URL}/api/breeds/${selectedSpecies}${searchParams.toString() ? `?${searchParams.toString()}` : ''}`;
      
      const response = await fetch(url);
      
      if (!response.ok) {
        throw new Error(`Erro ${response.status}: ${response.statusText}`);
      }

      const data: BreedData[] = await response.json();
      setAllBreeds(data);
    } catch (err) {
      console.error("Erro ao buscar raças:", err);
      setError(err instanceof Error ? err.message : "Erro desconhecido");
      setAllBreeds([]);
    } finally {
      setLoading(false);
    }
  }, [API_URL]);

  // Resetar página quando espécie ou busca mudarem
  useEffect(() => {
    setCurrentPage(0);
  }, [species, debouncedSearchTerm]);

  // Buscar raças quando espécie mudar
  useEffect(() => {
    fetchBreeds(species, ""); // Buscar todas as raças da espécie
  }, [species, fetchBreeds]);

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Cabeçalho */}
      <div className="mb-8">
        <h1 className="text-4xl font-bold text-white-900 mb-4">
          Exploração de Raças
        </h1>
        <p className="text-white-800 text-lg font-medium">
          Descubra diferentes raças de cães e gatos, suas características e temperamentos
        </p>
      </div>

      {/* Filtros */}
      <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-8">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Seleção de Espécie */}
          <div>
            <label className="block text-sm font-bold text-gray-900 mb-3">
              Escolha a espécie:
            </label>
            <div className="flex space-x-4">
              <button
                onClick={() => setSpecies("dog")}
                className={`flex items-center px-4 py-2 rounded-lg font-bold transition-colors ${
                  species === "dog"
                    ? "bg-blue-600 text-white"
                    : "bg-gray-200 text-gray-900 hover:bg-gray-300 border border-gray-300"
                }`}
              >
                🐕 Cães
              </button>
              <button
                onClick={() => setSpecies("cat")}
                className={`flex items-center px-4 py-2 rounded-lg font-bold transition-colors ${
                  species === "cat"
                    ? "bg-blue-600 text-white"
                    : "bg-gray-200 text-gray-900 hover:bg-gray-300 border border-gray-300"
                }`}
              >
                🐱 Gatos
              </button>
            </div>
          </div>

          {/* Campo de Busca */}
          <div>
            <label htmlFor="search" className="block text-sm font-bold text-gray-900 mb-3">
              Buscar por nome da raça:
            </label>
            <input
              id="search"
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder={`Ex: ${species === "dog" ? "Golden Retriever" : "Persian"}`}
              className="w-full px-4 py-2 border text-gray-900 placeholder-gray-600 border-gray-400 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white font-medium"
            />
          </div>
        </div>
      </div>

      {/* Estados de Loading */}
      {loading && (
        <LoadingSpinner 
          size="lg" 
          text={`Buscando raças de ${species === "dog" ? "cães" : "gatos"}...`}
          className="py-12"
        />
      )}

      {/* Estado de Erro */}
      {error && !loading && (
        <ErrorMessage
          title="Erro ao carregar raças"
          message={error}
          onRetry={() => fetchBreeds(species, "")}
          className="py-12"
        />
      )}

      {/* Estado Vazio */}
      {!loading && !error && filteredBreeds.length === 0 && (
        <ErrorMessage
          title="Nenhuma raça encontrada"
          message={
            searchTerm 
              ? `Não encontramos raças de ${species === "dog" ? "cães" : "gatos"} que correspondam a "${searchTerm}".`
              : `Não há raças de ${species === "dog" ? "cães" : "gatos"} disponíveis no momento.`
          }
          icon="🔍"
          variant="info"
          className="py-12"
          actionButton={searchTerm ? {
            text: "Limpar busca",
            onClick: () => setSearchTerm("")
          } : undefined}
        />
      )}

      {/* Grid de Raças */}
      {!loading && !error && filteredBreeds.length > 0 && (
        <div>
          <div className="mb-6">
            <h2 className="text-2xl font-bold text-white-900">
              {searchTerm 
                ? `Resultados para "${searchTerm}"` 
                : `Raças de ${species === "dog" ? "Cães" : "Gatos"}`
              }
            </h2>
            <p className="text-gray-800 font-medium">
              {filteredBreeds.length} raça{filteredBreeds.length !== 1 ? "s" : ""} encontrada{filteredBreeds.length !== 1 ? "s" : ""}
              {totalPages > 1 && (
                <span className="ml-2">
                  (Página {currentPage + 1} de {totalPages})
                </span>
              )}
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8 auto-rows-fr">
            {paginatedBreeds.map((breed: BreedData, index: number) => (
              <BreedCard key={`${breed.name}-${index}`} breed={breed} />
            ))}
          </div>

          {/* Paginação */}
          {totalPages > 1 && (
            <Pagination
              totalPages={totalPages}
              currentPage={currentPage}
              onPageChange={setCurrentPage}
              className="justify-center"
            />
          )}
        </div>
      )}
    </div>
  );
}