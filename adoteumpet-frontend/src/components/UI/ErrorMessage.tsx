"use client";

import React from "react";

export interface ErrorMessageProps {
  /** Mensagem de erro a ser exibida */
  message: string;
  /** Título do erro (opcional) */
  title?: string;
  /** Função chamada quando o botão de tentar novamente é clicado */
  onRetry?: () => void;
  /** Se deve exibir um ícone de erro */
  showIcon?: boolean;
  /** Ícone personalizado (emoji ou texto) */
  icon?: string;
  /** Variante visual do componente */
  variant?: 'error' | 'warning' | 'info';
  /** Classe CSS adicional */
  className?: string;
  /** Botão de ação personalizado */
  actionButton?: {
    text: string;
    onClick: () => void;
  };
}

export default function ErrorMessage({
  message,
  title = "Ops! Algo deu errado",
  onRetry,
  showIcon = true,
  icon,
  variant = 'error',
  className = "",
  actionButton
}: ErrorMessageProps) {
  const getDefaultIcon = () => {
    switch (variant) {
      case 'error': return '😿';
      case 'warning': return '⚠️';
      case 'info': return '🔍';
      default: return '😿';
    }
  };

  const getTextColor = () => {
    switch (variant) {
      case 'error': return 'text-red-800';
      case 'warning': return 'text-yellow-800';
      case 'info': return 'text-blue-800';
      default: return 'text-red-800';
    }
  };

  return (
    <div className={`flex flex-col items-center justify-center py-8 px-4 ${className}`}>
      {showIcon && (
        <div className="text-6xl mb-4">{icon || getDefaultIcon()}</div>
      )}
      
      <h3 className={`text-xl font-semibold mb-2 text-center ${getTextColor()}`}>
        {title}
      </h3>
      
      <p className="text-gray-800 text-center max-w-md mb-4">
        {message}
      </p>
      
      {onRetry && (
        <button
          onClick={onRetry}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
        >
          Tentar Novamente
        </button>
      )}

      {actionButton && (
        <button
          onClick={actionButton.onClick}
          className="mt-4 text-blue-600 hover:text-blue-700 font-medium focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 rounded"
        >
          {actionButton.text}
        </button>
      )}
    </div>
  );
}