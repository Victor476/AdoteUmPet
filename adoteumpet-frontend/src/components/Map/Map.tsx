'use client';

import dynamic from 'next/dynamic';
import LoadingSpinner from '../UI/LoadingSpinner';

interface MapProps {
  latitude: number;
  longitude: number;
  shelterName: string;
  shelterAddress?: string;
  petName: string;
}

// Carregamento dinâmico do componente de mapa para evitar problemas de SSR
const DynamicLeafletMap = dynamic(() => import('./LeafletMap'), {
  ssr: false,
  loading: () => (
    <div className="w-full h-64 bg-gray-100 rounded-lg flex items-center justify-center">
      <LoadingSpinner />
    </div>
  ),
});

export default function Map({ latitude, longitude, shelterName, shelterAddress, petName }: MapProps) {
  return (
    <DynamicLeafletMap
      latitude={latitude}
      longitude={longitude}
      shelterName={shelterName}
      shelterAddress={shelterAddress}
      petName={petName}
    />
  );
}