'use client';

import React, { useMemo } from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';
import { Bar } from 'react-chartjs-2';
import { Pet } from '../../types/pet';

// Registrar os componentes necessários do Chart.js
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

interface AgeDistributionChartProps {
  pets: Pet[];
  loading?: boolean;
}

export default function AgeDistributionChart({ pets, loading = false }: AgeDistributionChartProps) {
  // Processar os dados para criar a distribuição de idade
  const ageDistribution = useMemo(() => {
    if (!pets || pets.length === 0) {
      return {
        '0-1 ano': 0,
        '2-3 anos': 0,
        '4-6 anos': 0,
        '7+ anos': 0,
      };
    }

    const distribution = pets.reduce(
      (acc, pet) => {
        const age = pet.ageYears;
        if (typeof age !== 'number' || isNaN(age)) {
          acc['7+ anos']++;
          return acc;
        }
        if (age <= 1) {
          acc['0-1 ano']++;
        } else if (age >= 2 && age <= 3) {
          acc['2-3 anos']++;
        } else if (age >= 4 && age <= 6) {
          acc['4-6 anos']++;
        } else {
          acc['7+ anos']++;
        }
        return acc;
      },
      {
        '0-1 ano': 0,
        '2-3 anos': 0,
        '4-6 anos': 0,
        '7+ anos': 0,
      }
    );

    return distribution;
  }, [pets]);

  const chartData = {
    labels: Object.keys(ageDistribution),
    datasets: [
      {
        label: 'Número de Pets',
        data: Object.values(ageDistribution),
        backgroundColor: [
          'rgba(34, 197, 94, 0.7)',   // Verde para 0-1 ano
          'rgba(59, 130, 246, 0.7)',  // Azul para 2-3 anos
          'rgba(245, 158, 11, 0.7)',  // Amarelo para 4-6 anos
          'rgba(239, 68, 68, 0.7)',   // Vermelho para 7+ anos
        ],
        borderColor: [
          'rgba(34, 197, 94, 1)',
          'rgba(59, 130, 246, 1)',
          'rgba(245, 158, 11, 1)',
          'rgba(239, 68, 68, 1)',
        ],
        borderWidth: 2,
        borderRadius: 4,
        borderSkipped: false,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top' as const,
        display: false, // Ocultar legenda pois os labels já são descritivos
      },
      title: {
        display: true,
        text: 'Distribuição de Idade dos Pets',
        font: {
          size: 18,
          weight: 'bold' as const,
        },
        color: '#1f2937',
        padding: {
          bottom: 20,
        },
      },
      tooltip: {
        backgroundColor: 'rgba(0, 0, 0, 0.8)',
        titleColor: '#ffffff',
        bodyColor: '#ffffff',
        cornerRadius: 8,
        displayColors: false,
        callbacks: {
          label: function(context: any) {
            const value = context.parsed.y;
            const total = Object.values(ageDistribution).reduce((sum: number, val: any) => sum + val, 0);
            const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : '0';
            return `${value} pets (${percentage}%)`;
          },
        },
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          stepSize: 1,
          color: '#6b7280',
          font: {
            size: 12,
          },
        },
        grid: {
          color: 'rgba(229, 231, 235, 0.8)',
        },
        title: {
          display: true,
          text: 'Quantidade de Pets',
          color: '#374151',
          font: {
            size: 14,
            weight: 'bold' as const,
          },
        },
      },
      x: {
        ticks: {
          color: '#6b7280',
          font: {
            size: 12,
            weight: 'bold' as const,
          },
        },
        grid: {
          display: false,
        },
        title: {
          display: true,
          text: 'Faixa Etária',
          color: '#374151',
          font: {
            size: 14,
            weight: 'bold' as const,
          },
        },
      },
    },
    animation: {
      duration: 1000,
      easing: 'easeOutQuart' as const,
    },
  };

  if (loading) {
    return (
      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="h-80 flex items-center justify-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
          <span className="ml-3 text-gray-600">Carregando gráfico...</span>
        </div>
      </div>
    );
  }

  const totalPets = Object.values(ageDistribution).reduce((sum, val) => sum + val, 0);

  if (totalPets === 0) {
    return (
      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="h-80 flex flex-col items-center justify-center text-gray-500">
          <div className="text-6xl mb-4">📊</div>
          <h3 className="text-lg font-semibold mb-2">Nenhum dado encontrado</h3>
          <p className="text-sm text-center">
            Não há pets disponíveis para gerar o gráfico de distribuição de idade.
            <br />
            Tente ajustar os filtros de busca.
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <div className="mb-4 flex items-center justify-between">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">
            Distribuição por Idade
          </h3>
          <p className="text-sm text-gray-600">
            Total de {totalPets} {totalPets === 1 ? 'pet' : 'pets'} analisados
          </p>
        </div>
        <div className="text-right">
          <div className="text-sm text-gray-500">
            Atualizado automaticamente
          </div>
        </div>
      </div>
      
      <div className="h-80">
        <Bar data={chartData} options={options} />
      </div>

      {/* Resumo estatístico */}
      <div className="mt-6 grid grid-cols-2 md:grid-cols-4 gap-4">
        {Object.entries(ageDistribution).map(([ageRange, count]) => {
          const percentage = totalPets > 0 ? ((count / totalPets) * 100).toFixed(1) : '0';
          return (
            <div key={ageRange} className="text-center p-3 bg-gray-50 rounded-lg">
              <div className="text-2xl font-bold text-gray-900">{count}</div>
              <div className="text-sm text-gray-600">{ageRange}</div>
              <div className="text-xs text-gray-500">{percentage}%</div>
            </div>
          );
        })}
      </div>
    </div>
  );
}