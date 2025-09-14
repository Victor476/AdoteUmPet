"use client";

import React from "react";
import Link from "next/link";

export default function PetListTable({ pets }: any) {
  return (
    <div className="bg-white shadow-sm rounded-lg overflow-hidden mb-6">
      {/* Versão desktop - tabela */}
      <div className="hidden md:block">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nome</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Espécie</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Raça</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Idade</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cidade</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {pets.map((pet: any) => (
              <tr key={pet.id} className="hover:bg-gray-50 transition-colors">
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  <Link 
                    href={`/pets/${pet.id}`}
                    className="text-blue-600 hover:text-blue-800 hover:underline"
                  >
                    {pet.name}
                  </Link>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {pet.species === "DOG" ? "🐕 Cachorro" : "🐱 Gato"}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{pet.breed || "N/A"}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {pet.ageYears ? `${pet.ageYears} ano${pet.ageYears !== 1 ? 's' : ''}` : "N/A"}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{pet.shelterCity}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                    pet.status === "AVAILABLE" 
                      ? "bg-green-100 text-green-800" 
                      : "bg-gray-100 text-gray-800"
                  }`}>
                    {pet.status === "AVAILABLE" ? "Disponível" : "Adotado"}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Versão mobile - cards */}
      <div className="md:hidden space-y-4 p-4">
        {pets.map((pet: any) => (
          <Link key={pet.id} href={`/pets/${pet.id}`}>
            <div className="bg-white border border-gray-200 rounded-lg p-4 shadow-sm hover:shadow-md transition-shadow cursor-pointer">
              <div className="flex justify-between items-start mb-2">
                <h3 className="text-lg font-semibold text-blue-600 hover:text-blue-800">{pet.name}</h3>
                <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                  pet.status === "AVAILABLE" 
                    ? "bg-green-100 text-green-800" 
                    : "bg-gray-100 text-gray-800"
                }`}>
                  {pet.status === "AVAILABLE" ? "Disponível" : "Adotado"}
                </span>
              </div>
              <div className="space-y-1 text-sm text-gray-600">
                <p><span className="font-medium">Espécie:</span> {pet.species === "DOG" ? "🐕 Cachorro" : "🐱 Gato"}</p>
                <p><span className="font-medium">Raça:</span> {pet.breed || "N/A"}</p>
                <p><span className="font-medium">Idade:</span> {pet.ageYears ? `${pet.ageYears} ano${pet.ageYears !== 1 ? 's' : ''}` : "N/A"}</p>
                <p><span className="font-medium">Cidade:</span> {pet.shelterCity}</p>
              </div>
              <div className="mt-3 text-sm text-blue-600 font-medium">
                Ver detalhes →
              </div>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}
