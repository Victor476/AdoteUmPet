export interface Pet {
  id: string;
  name: string;
  species: 'DOG' | 'CAT';
  breed: string;
  ageYears: number;
  status: 'AVAILABLE' | 'ADOPTED';
  shelterCity: string;
  shelterLat?: number;
  shelterLng?: number;
  createdAt: string;
}

export interface PetFilters {
  name?: string;
  species?: string;
  breed?: string;
  shelterCity?: string;
  status?: string;
}

export interface PetListResponse {
  content: Pet[];
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  number: number;
  size: number;
}