"use client";

import React, { useState, useEffect } from "react";
import PetCard from "./PetCard";
import Pagination from "../../components/Pagination";
import { LoadingSpinner } from "../../components/UI";

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

interface PaginationInfo {
  page: number;
  size: number;
  total: number;
  totalPages: number;
}

// Dados mock simples para fallback
const mockPets: Pet[] = [
  {
    id: "1",
    name: "Buddy",
    species: "DOG",
    breed: "Golden Retriever",
    ageYears: 3,
    shelterCity: "São Paulo",
    status: "AVAILABLE"
  },
  {
    id: "2",
    name: "Luna",
    species: "CAT",
    breed: "Persian",
    ageYears: 2,
    shelterCity: "Rio de Janeiro",
    status: "AVAILABLE"
  },
  {
    id: "3",
    name: "Max",
    species: "DOG",
    breed: "Labrador",
    ageYears: 4,
    shelterCity: "Belo Horizonte",
    status: "AVAILABLE"
  }
];

const createMockPagination = (page: number = 0, size: number = 10): PaginationInfo => ({
  page,
  size,
  total: mockPets.length,
  totalPages: Math.ceil(mockPets.length / size)
});

export default function PetGrid() {
  const [pets, setPets] = useState<Pet[]>([]);
  const [pagination, setPagination] = useState<PaginationInfo>({
    page: 0,
    size: 10,
    total: 0,
    totalPages: 0
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

  const fetchPets = async (page: number = 0, size: number = 10) => {
    try {
      setLoading(true);
      setError(null);
      
      // Tentar buscar pets da API
      const response = await fetch(`${API_URL}/api/pets?page=${page}&size=${size}`);
      
      if (!response.ok) {
        throw new Error(`API retornou status ${response.status}`);
      }
      
      const petData = await response.json();
      
      // Processar diferentes estruturas de resposta
      let petList: Pet[] = [];
      let paginationData: PaginationInfo = {
        page,
        size,
        total: 0,
        totalPages: 0
      };

      if (Array.isArray(petData)) {
        // Resposta como array simples
        petList = petData;
        paginationData = {
          page: 0,
          size: petData.length,
          total: petData.length,
          totalPages: 1
        };
      } else if (petData.content && Array.isArray(petData.content)) {
        // Resposta Spring Boot paginada
        petList = petData.content;
        paginationData = {
          page: petData.number || 0,
          size: petData.size || 10,
          total: petData.totalElements || petList.length,
          totalPages: petData.totalPages || 1
        };
      } else if (petData.data && Array.isArray(petData.data)) {
        // Resposta customizada
        petList = petData.data;
        paginationData = {
          page: petData.page || 0,
          size: petData.size || 10,
          total: petData.total || petList.length,
          totalPages: petData.totalPages || 1
        };
      }
      
      setPets(petList);
      setPagination(paginationData);
    } catch (err) {
      console.warn("API indisponível, usando dados mock:", err);
      setPets(mockPets);
      setPagination(createMockPagination(page, size));
      setError("Usando dados de exemplo - API indisponível");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPets(pagination.page, pagination.size);
  }, [API_URL]);

  const handlePageChange = (newPage: number) => {
    fetchPets(newPage, pagination.size);
  };

  if (loading) {
    return (
      <div className="py-12">
        <LoadingSpinner 
          size="lg" 
          text="Carregando pets disponíveis..." 
          className="py-8"
        />
      </div>
    );
  }

  if (pets.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-12">
        <div className="text-6xl mb-4">🐾</div>
        <h3 className="text-xl font-semibold text-gray-900 mb-2">Nenhum pet disponível</h3>
        <p className="text-gray-600 text-center max-w-md">
          No momento não temos pets disponíveis para adoção.
        </p>
      </div>
    );
  }

  return (
    <div className="w-full">
      <div className="flex items-center justify-between mb-8">
        <div>
          <h2 className="text-3xl font-bold text-gray-900 mb-2">
            Pets Disponíveis para Adoção
          </h2>
          <p className="text-gray-600">
            {pagination.total > 0 ? (
              <>
                Mostrando {pets.length} de {pagination.total} pet{pagination.total !== 1 ? 's' : ''} 
                {pagination.totalPages > 1 && (
                  <span className="ml-2">
                    (Página {pagination.page + 1} de {pagination.totalPages})
                  </span>
                )}
              </>
            ) : (
              `${pets.length} pet${pets.length !== 1 ? 's' : ''} esperando por uma família`
            )}
            {error && <span className="text-orange-600 ml-2">({error})</span>}
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8">
        {pets.map((pet) => (
          <PetCard key={pet.id} pet={pet} />
        ))}
      </div>

      {/* Componente de Paginação */}
      {pagination.totalPages > 1 && (
        <div className="mt-8">
          <Pagination
            totalPages={pagination.totalPages}
            currentPage={pagination.page}
            onPageChange={handlePageChange}
            className="justify-center"
          />
        </div>
      )}
    </div>
  );
}