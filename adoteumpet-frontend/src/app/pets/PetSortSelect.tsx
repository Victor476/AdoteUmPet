"use client";

import React from "react";
import { Select } from "../../components/UI";
import type { SelectOption } from "../../components/UI";

const SORT_OPTIONS: SelectOption[] = [
  { value: "name,asc", label: "Nome A-Z" },
  { value: "name,desc", label: "Nome Z-A" },
  { value: "ageYears,asc", label: "Idade crescente" },
  { value: "ageYears,desc", label: "Idade decrescente" },
  { value: "createdAt,asc", label: "Mais antigos" },
  { value: "createdAt,desc", label: "Mais recentes" },
  { value: "species,asc", label: "Espécie A-Z" },
  { value: "shelterCity,asc", label: "Cidade A-Z" },
];

interface PetSortSelectProps {
  sort: string;
  setSort: any;
}

export default function PetSortSelect({ sort, setSort }: PetSortSelectProps) {
  return (
    <Select
      label="Ordenar por"
      value={sort}
      onChange={(e) => setSort(e.target.value)}
      options={SORT_OPTIONS}
      size="sm"
    />
  );
}
