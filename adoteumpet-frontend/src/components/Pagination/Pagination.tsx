"use client";

import React from "react";

export interface PaginationProps {
  /** Número total de páginas */
  totalPages: number;
  /** Página atual (baseada em 0) */
  currentPage: number;
  /** Função chamada quando a página muda */
  onPageChange: (page: number) => void;
  /** Classe CSS adicional para customização */
  className?: string;
  /** Número máximo de páginas a serem exibidas */
  maxVisiblePages?: number;
}

export default function Pagination({
  totalPages,
  currentPage,
  onPageChange,
  className = "",
  maxVisiblePages = 5
}: PaginationProps) {
  // Se não há páginas suficientes para paginação, não renderiza nada
  if (totalPages <= 1) {
    return null;
  }

  // Calcular quais páginas devem ser exibidas
  const getVisiblePages = (): number[] => {
    const pages: number[] = [];
    const half = Math.floor(maxVisiblePages / 2);
    
    let start = Math.max(0, currentPage - half);
    let end = Math.min(totalPages - 1, currentPage + half);
    
    // Ajustar se estamos no início ou fim
    if (end - start + 1 < maxVisiblePages) {
      if (start === 0) {
        end = Math.min(totalPages - 1, start + maxVisiblePages - 1);
      } else {
        start = Math.max(0, end - maxVisiblePages + 1);
      }
    }
    
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    
    return pages;
  };

  const visiblePages = getVisiblePages();
  const isFirstPage = currentPage === 0;
  const isLastPage = currentPage === totalPages - 1;

  // Componente para botão de página
  const PageButton = ({ 
    page, 
    isActive = false, 
    disabled = false, 
    children, 
    onClick 
  }: {
    page?: number;
    isActive?: boolean;
    disabled?: boolean;
    children: React.ReactNode;
    onClick?: () => void;
  }) => (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`
        px-3 py-2 text-sm font-medium border transition-colors duration-200
        ${isActive 
          ? 'bg-blue-600 text-white border-blue-600 cursor-default' 
          : disabled
            ? 'bg-gray-100 text-gray-400 border-gray-300 cursor-not-allowed'
            : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-50 hover:border-gray-400'
        }
        ${page !== undefined ? 'min-w-[40px] justify-center' : 'px-4'}
        flex items-center
      `}
      aria-label={
        page !== undefined 
          ? `Ir para página ${page + 1}` 
          : typeof children === 'string' 
            ? children 
            : undefined
      }
      aria-current={isActive ? 'page' : undefined}
    >
      {children}
    </button>
  );

  return (
    <nav 
      className={`flex items-center justify-center space-x-1 ${className}`}
      aria-label="Paginação"
      role="navigation"
    >
      {/* Botão Anterior */}
      <PageButton
        disabled={isFirstPage}
        onClick={() => !isFirstPage && onPageChange(currentPage - 1)}
      >
        <span className="hidden sm:inline">Anterior</span>
        <span className="sm:hidden">‹</span>
      </PageButton>

      {/* Primeira página + reticências se necessário */}
      {visiblePages[0] > 0 && (
        <>
          <PageButton
            page={0}
            onClick={() => onPageChange(0)}
          >
            1
          </PageButton>
          {visiblePages[0] > 1 && (
            <span className="px-2 py-2 text-gray-500">...</span>
          )}
        </>
      )}

      {/* Páginas visíveis */}
      {visiblePages.map((page) => (
        <PageButton
          key={page}
          page={page}
          isActive={page === currentPage}
          onClick={() => page !== currentPage && onPageChange(page)}
        >
          {page + 1}
        </PageButton>
      ))}

      {/* Última página + reticências se necessário */}
      {visiblePages[visiblePages.length - 1] < totalPages - 1 && (
        <>
          {visiblePages[visiblePages.length - 1] < totalPages - 2 && (
            <span className="px-2 py-2 text-gray-500">...</span>
          )}
          <PageButton
            page={totalPages - 1}
            onClick={() => onPageChange(totalPages - 1)}
          >
            {totalPages}
          </PageButton>
        </>
      )}

      {/* Botão Próximo */}
      <PageButton
        disabled={isLastPage}
        onClick={() => !isLastPage && onPageChange(currentPage + 1)}
      >
        <span className="hidden sm:inline">Próximo</span>
        <span className="sm:hidden">›</span>
      </PageButton>

      {/* Informações da página atual (mobile) */}
      <div className="sm:hidden ml-4 text-sm text-gray-600">
        {currentPage + 1} de {totalPages}
      </div>
    </nav>
  );
}