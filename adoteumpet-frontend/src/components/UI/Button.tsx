"use client";

import React from "react";

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  /** Variante visual do botão */
  variant?: "primary" | "secondary" | "danger" | "outline";
  /** Tamanho do botão */
  size?: "sm" | "md" | "lg";
  /** Se o botão está em estado de loading */
  loading?: boolean;
  /** Ícone a ser exibido antes do texto */
  icon?: React.ReactNode;
  /** Se o botão deve ocupar toda a largura disponível */
  fullWidth?: boolean;
}

export default function Button({
  children,
  variant = "primary",
  size = "md",
  loading = false,
  icon,
  fullWidth = false,
  disabled,
  className = "",
  ...props
}: ButtonProps) {
  const baseClasses = `
    inline-flex items-center justify-center font-medium rounded-lg 
    transition-colors duration-200 focus:outline-none focus:ring-2 
    focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed
  `;

  const variantClasses = {
    primary: `
      bg-blue-600 text-white hover:bg-blue-700 
      focus:ring-blue-500 active:bg-blue-800
    `,
    secondary: `
      bg-gray-200 text-gray-900 hover:bg-gray-300 
      focus:ring-gray-500 active:bg-gray-400
    `,
    danger: `
      bg-red-600 text-white hover:bg-red-700 
      focus:ring-red-500 active:bg-red-800
    `,
    outline: `
      bg-transparent text-blue-600 border border-blue-600 
      hover:bg-blue-50 focus:ring-blue-500 active:bg-blue-100
    `
  };

  const sizeClasses = {
    sm: "px-3 py-1.5 text-sm",
    md: "px-4 py-2 text-sm",
    lg: "px-6 py-3 text-base"
  };

  const widthClass = fullWidth ? "w-full" : "";

  return (
    <button
      className={`
        ${baseClasses}
        ${variantClasses[variant]}
        ${sizeClasses[size]}
        ${widthClass}
        ${className}
      `}
      disabled={disabled || loading}
      {...props}
    >
      {loading ? (
        <>
          <svg
            className="animate-spin -ml-1 mr-2 h-4 w-4"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle
              className="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              strokeWidth="4"
            />
            <path
              className="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
            />
          </svg>
          Carregando...
        </>
      ) : (
        <>
          {icon && <span className="mr-2">{icon}</span>}
          {children}
        </>
      )}
    </button>
  );
}