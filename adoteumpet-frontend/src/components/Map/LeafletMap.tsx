'use client';

import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// Fix for default markers in Next.js
const icon = L.icon({
  iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});

interface LeafletMapProps {
  latitude: number;
  longitude: number;
  shelterName: string;
  shelterAddress?: string;
  petName: string;
}

export default function LeafletMap({
  latitude,
  longitude,
  shelterName,
  shelterAddress,
  petName
}: LeafletMapProps) {
  // Validação das coordenadas
  if (!latitude || !longitude || isNaN(latitude) || isNaN(longitude)) {
    return (
      <div className="w-full h-64 bg-gray-100 rounded-lg flex items-center justify-center text-gray-600">
        <p>Localização não disponível</p>
      </div>
    );
  }

  // Verificar se as coordenadas estão dentro de limites válidos
  if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
    return (
      <div className="w-full h-64 bg-gray-100 rounded-lg flex items-center justify-center text-gray-600">
        <p>Coordenadas inválidas</p>
      </div>
    );
  }

  return (
    <div className="w-full h-64 rounded-lg overflow-hidden shadow-lg">
      <MapContainer
        center={[latitude, longitude]}
        zoom={13}
        style={{ height: '100%', width: '100%' }}
        className="rounded-lg"
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <Marker position={[latitude, longitude]} icon={icon}>
          <Popup>
            <div className="text-center">
              <h3 className="font-semibold text-gray-800 mb-1">{shelterName}</h3>
              {shelterAddress && (
                <p className="text-sm text-gray-600 mb-2">{shelterAddress}</p>
              )}
              <p className="text-sm text-blue-600">
                Localização do abrigo onde {petName} está disponível
              </p>
            </div>
          </Popup>
        </Marker>
      </MapContainer>
    </div>
  );
}