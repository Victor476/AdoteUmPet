"use client";

import React, { useState, useEffect } from "react";
import PetListTable from "./PetListTable";
import PetPagination from "./PetPagination";
import PetSortSelect from "./PetSortSelect";
import FilterForm from "./FilterForm";
import { LoadingSpinner, ErrorMessage } from "../../components/UI";
import { AgeDistributionChart, PetStats } from "../../components/Charts";

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

export default function PetListPage() {
  const [pets, setPets] = useState([]);
  const [allPets, setAllPets] = useState([]); // Para o gráfico
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [filters, setFilters] = useState({});
  const [sort, setSort] = useState("createdAt,desc");
  const [loading, setLoading] = useState(false);
  const [chartLoading, setChartLoading] = useState(false);
  const [empty, setEmpty] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchPets = async () => {
    setLoading(true);
    setError(null);
    try {
      const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString(),
        sort,
        ...filters,
      });
      const response = await fetch(`${API_URL}/api/pets?${params}`);
      if (!response.ok) {
        throw new Error(`Erro ao carregar pets: ${response.statusText}`);
      }
      const data = await response.json();
      setPets(data.data || []);
      setTotal(data.total || 0);
      setEmpty((data.data || []).length === 0);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro desconhecido ao carregar pets');
      setPets([]);
      setTotal(0);
      setEmpty(true);
    } finally {
      setLoading(false);
    }
  };

  // Função para buscar todos os pets para o gráfico
  const fetchAllPetsForChart = async () => {
    setChartLoading(true);
    try {
      const params = new URLSearchParams({
        page: "0",
        size: "1000", // Buscar uma quantidade grande para ter todos os pets
        sort: "createdAt,desc",
        ...filters, // Usar os mesmos filtros
      });
      const response = await fetch(`${API_URL}/api/pets?${params}`);
      if (!response.ok) {
        throw new Error(`Erro ao carregar pets para gráfico: ${response.statusText}`);
      }
      const data = await response.json();
      setAllPets(data.data || []);
    } catch (err) {
      console.error("Erro ao buscar pets para gráfico:", err);
      // Em caso de erro, usar os pets da página atual
      setAllPets(pets);
    } finally {
      setChartLoading(false);
    }
  };

  // Reset page when filters change
  useEffect(() => {
    setPage(0);
  }, [filters]);

  useEffect(() => {
    fetchPets();
  }, [page, size, filters, sort]);

  // Buscar todos os pets para o gráfico quando filtros mudarem
  useEffect(() => {
    fetchAllPetsForChart();
  }, [filters]);

  return (
    <div className="container mx-auto p-4">
      {/* Componente de Filtros */}
      <FilterForm 
        onFiltersChange={setFilters} 
        loading={loading} 
      />

      {/* Seção de controles - Ordenação */}
      <div className="bg-white rounded-lg p-6 my-6 shadow-sm border border-gray-200">
        <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-4">
          <PetSortSelect sort={sort} setSort={setSort} />
          <div className="text-sm text-gray-800 font-medium">
            Total: {total} pet{total !== 1 ? 's' : ''} encontrado{total !== 1 ? 's' : ''}
          </div>
        </div>
      </div>

      {/* Estatísticas dos Pets */}
      <PetStats pets={allPets} loading={chartLoading} />

      {/* Gráfico de Distribuição de Idade */}
      <div className="mb-8">
        <AgeDistributionChart pets={allPets} loading={chartLoading} />
      </div>

      {/* Conteúdo principal */}
      {loading ? (
        <LoadingSpinner 
          size="lg" 
          text="Carregando pets..." 
          className="my-8" 
        />
      ) : error ? (
        <ErrorMessage
          title="Erro ao carregar pets"
          message={error}
          onRetry={fetchPets}
          className="my-8"
        />
      ) : empty ? (
        <ErrorMessage
          title="Nenhum pet encontrado"
          message="Tente ajustar os filtros para encontrar outros pets disponíveis."
          icon="🐕"
          variant="info"
          className="my-8"
        />
      ) : (
        <PetListTable pets={pets} />
      )}
      
      <PetPagination page={page} size={size} total={total} setPage={setPage} />
    </div>
  );
}
