'use client';

import React from 'react';
import { Pet } from '../../types/pet';

interface PetStatsProps {
  pets: Pet[];
  loading?: boolean;
}

export default function PetStats({ pets, loading = false }: PetStatsProps) {
  if (loading) {
    return (
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        {[...Array(4)].map((_, index) => (
          <div key={index} className="bg-white rounded-lg shadow-md p-4">
            <div className="animate-pulse">
              <div className="h-8 bg-gray-200 rounded mb-2"></div>
              <div className="h-4 bg-gray-200 rounded"></div>
            </div>
          </div>
        ))}
      </div>
    );
  }

  const totalPets = pets.length;
  const availablePets = pets.filter(pet => pet.status === 'AVAILABLE').length;
  const adoptedPets = pets.filter(pet => pet.status === 'ADOPTED').length;
  const averageAge = totalPets > 0 
    ? (pets.reduce((sum, pet) => {
        const age = pet.ageYears;
        return sum + (typeof age === 'number' ? age : 0);
      }, 0) / totalPets).toFixed(1)
    : '0';

  const stats = [
    {
      title: 'Total de Pets',
      value: totalPets,
      icon: '🐾',
      color: 'text-blue-600',
      bgColor: 'bg-blue-50',
    },
    {
      title: 'Disponíveis',
      value: availablePets,
      icon: '❤️',
      color: 'text-green-600',
      bgColor: 'bg-green-50',
    },
    {
      title: 'Adotados',
      value: adoptedPets,
      icon: '🏠',
      color: 'text-purple-600',
      bgColor: 'bg-purple-50',
    },
    {
      title: 'Idade Média',
      value: `${averageAge} anos`,
      icon: '📊',
      color: 'text-orange-600',
      bgColor: 'bg-orange-50',
    },
  ];

  return (
    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
      {stats.map((stat, index) => (
        <div key={index} className={`${stat.bgColor} rounded-lg shadow-md p-4 transition-transform hover:scale-105`}>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 font-medium">{stat.title}</p>
              <p className={`text-2xl font-bold ${stat.color}`}>
                {stat.value}
              </p>
            </div>
            <div className="text-2xl opacity-80">
              {stat.icon}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}