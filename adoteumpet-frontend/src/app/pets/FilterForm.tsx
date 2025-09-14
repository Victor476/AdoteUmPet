"use client";

import React, { useState, useEffect, useCallback } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import { Input, Select, Button } from "../../components/UI";
import type { SelectOption } from "../../components/UI";

export interface FilterFormProps {
  /** Função chamada quando os filtros são aplicados */
  onFiltersChange: (filters: Record<string, string>) => void;
  /** Se o formulário está em loading */
  loading?: boolean;
}

// Opções dos dropdowns
const speciesOptions: SelectOption[] = [
  { value: "", label: "Todas as espécies" },
  { value: "DOG", label: "Cão 🐕" },
  { value: "CAT", label: "Gato 🐱" }
];

const statusOptions: SelectOption[] = [
  { value: "", label: "Todos os status" },
  { value: "AVAILABLE", label: "Disponível" },
  { value: "ADOPTED", label: "Adotado" }
];

export default function FilterForm({ onFiltersChange, loading = false }: FilterFormProps) {
  const searchParams = useSearchParams();
  const router = useRouter();
  
  // Estados dos filtros
  const [name, setName] = useState("");
  const [species, setSpecies] = useState("");
  const [breed, setBreed] = useState("");
  const [shelterCity, setShelterCity] = useState("");
  const [status, setStatus] = useState("");

  // Debounce para o campo nome
  const [debouncedName, setDebouncedName] = useState("");

  // Inicializar filtros da URL
  useEffect(() => {
    const nameParam = searchParams.get("name") || "";
    const speciesParam = searchParams.get("species") || "";
    const breedParam = searchParams.get("breed") || "";
    const cityParam = searchParams.get("shelter_city") || "";
    const statusParam = searchParams.get("status") || "";

    setName(nameParam);
    setSpecies(speciesParam);
    setBreed(breedParam);
    setShelterCity(cityParam);
    setStatus(statusParam);
    setDebouncedName(nameParam);
  }, [searchParams]);

  // Debounce do campo nome
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedName(name);
    }, 500);

    return () => clearTimeout(timer);
  }, [name]);

  // Aplicar filtros quando houver mudança
  const applyFilters = useCallback(() => {
    const filters: Record<string, string> = {};
    
    if (debouncedName.trim()) filters.name = debouncedName.trim();
    if (species) filters.species = species;
    if (breed.trim()) filters.breed = breed.trim();
    if (shelterCity.trim()) filters.shelter_city = shelterCity.trim();
    if (status) filters.status = status;

    // Atualizar URL
    const params = new URLSearchParams();
    Object.entries(filters).forEach(([key, value]) => {
      if (value) params.set(key, value);
    });
    
    const newUrl = params.toString() ? `?${params.toString()}` : window.location.pathname;
    router.push(newUrl);

    // Notificar mudança
    onFiltersChange(filters);
  }, [debouncedName, species, breed, shelterCity, status, router, onFiltersChange]);

  // Aplicar filtros quando houver mudança (exceto nome que usa debounce)
  useEffect(() => {
    applyFilters();
  }, [debouncedName, species, breed, shelterCity, status, applyFilters]);

  // Limpar todos os filtros
  const clearFilters = () => {
    setName("");
    setSpecies("");
    setBreed("");
    setShelterCity("");
    setStatus("");
    router.push(window.location.pathname);
  };

  // Verificar se há filtros aplicados
  const hasFilters = name || species || breed || shelterCity || status;

  return (
    <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-lg font-bold text-gray-900">
          Filtrar Pets
        </h3>
        {hasFilters && (
          <Button
            variant="outline"
            size="sm"
            onClick={clearFilters}
            disabled={loading}
          >
            Limpar Filtros
          </Button>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-4">
        {/* Campo Nome */}
        <Input
          label="Nome do Pet"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Ex: Rex, Mila..."
          disabled={loading}
        />

        {/* Campo Espécie */}
        <Select
          label="Espécie"
          value={species}
          onChange={(e) => setSpecies(e.target.value)}
          options={speciesOptions}
          disabled={loading}
        />

        {/* Campo Raça */}
        <Input
          label="Raça"
          value={breed}
          onChange={(e) => setBreed(e.target.value)}
          placeholder="Ex: Labrador, Siamês..."
          disabled={loading}
        />

        {/* Campo Cidade */}
        <Input
          label="Cidade do Abrigo"
          value={shelterCity}
          onChange={(e) => setShelterCity(e.target.value)}
          placeholder="Ex: São Paulo, Rio..."
          disabled={loading}
        />

        {/* Campo Status */}
        <Select
          label="Status"
          value={status}
          onChange={(e) => setStatus(e.target.value)}
          options={statusOptions}
          disabled={loading}
        />
      </div>

      {/* Indicador de filtros ativos */}
      {hasFilters && (
        <div className="mt-4 pt-4 border-t border-gray-200">
          <div className="flex flex-wrap gap-2">
            <span className="text-sm text-gray-800 font-medium">Filtros ativos:</span>
            {name && (
              <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                Nome: {name}
              </span>
            )}
            {species && (
              <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                Espécie: {speciesOptions.find(opt => opt.value === species)?.label}
              </span>
            )}
            {breed && (
              <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                Raça: {breed}
              </span>
            )}
            {shelterCity && (
              <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                Cidade: {shelterCity}
              </span>
            )}
            {status && (
              <span className="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                Status: {statusOptions.find(opt => opt.value === status)?.label}
              </span>
            )}
          </div>
        </div>
      )}
    </div>
  );
}