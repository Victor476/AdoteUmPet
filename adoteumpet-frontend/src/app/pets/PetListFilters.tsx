"use client";

import React, { useState } from "react";

const SPECIES = ["dog", "cat"];
const STATUS = ["available", "adopted"];

export default function PetListFilters({ filters, setFilters }: any) {
  const [localFilters, setLocalFilters] = useState(filters);

  function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
    setLocalFilters({ ...localFilters, [e.target.name]: e.target.value });
  }

  function applyFilters(e: React.FormEvent) {
    e.preventDefault();
    setFilters(localFilters);
  }

  function clearFilters() {
    setLocalFilters({});
    setFilters({});
  }

  return (
    <form className="space-y-4" onSubmit={applyFilters}>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div>
          <label htmlFor="name" className="block text-sm font-medium mb-1">Nome</label>
          <input 
            id="name"
            name="name" 
            placeholder="Digite o nome do pet" 
            className="w-full border border-gray-300 p-2 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500" 
            value={localFilters.name || ""} 
            onChange={handleChange} 
          />
        </div>
        
        <div>
          <label htmlFor="species" className="block text-sm font-medium mb-1">Espécie</label>
          <select 
            id="species"
            name="species" 
            className="w-full border border-gray-300 p-2 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500" 
            value={localFilters.species || ""} 
            onChange={handleChange}
          >
            <option value="">Todas as espécies</option>
            {SPECIES.map((s) => (
              <option key={s} value={s}>{s === "dog" ? "Cachorro" : "Gato"}</option>
            ))}
          </select>
        </div>
        
        <div>
          <label htmlFor="breed" className="block text-sm font-medium mb-1">Raça</label>
          <input 
            id="breed"
            name="breed" 
            placeholder="Digite a raça" 
            className="w-full border border-gray-300 p-2 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500" 
            value={localFilters.breed || ""} 
            onChange={handleChange} 
          />
        </div>
        
        <div>
          <label htmlFor="shelter_city" className="block text-sm font-medium mb-1">Cidade do abrigo</label>
          <input 
            id="shelter_city"
            name="shelter_city" 
            placeholder="Digite a cidade" 
            className="w-full border border-gray-300 p-2 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500" 
            value={localFilters.shelter_city || ""} 
            onChange={handleChange} 
          />
        </div>
        
        <div>
          <label htmlFor="status" className="block text-sm font-medium mb-1">Status</label>
          <select 
            id="status"
            name="status" 
            className="w-full border border-gray-300 p-2 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500" 
            value={localFilters.status || ""} 
            onChange={handleChange}
          >
            <option value="">Todos os status</option>
            {STATUS.map((s) => (
              <option key={s} value={s}>{s === "available" ? "Disponível" : "Adotado"}</option>
            ))}
          </select>
        </div>
      </div>
      
      <div className="flex flex-col sm:flex-row gap-2">
        <button 
          type="submit" 
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-md transition-colors"
        >
          Aplicar Filtros
        </button>
        <button 
          type="button"
          onClick={clearFilters}
          className="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-md transition-colors"
        >
          Limpar Filtros
        </button>
      </div>
    </form>
  );
}
