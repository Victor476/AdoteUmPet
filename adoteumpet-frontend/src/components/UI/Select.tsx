"use client";

import React, { forwardRef } from "react";

export interface SelectOption {
  /** Valor da opção */
  value: string;
  /** Texto exibido da opção */
  label: string;
  /** Se a opção está desabilitada */
  disabled?: boolean;
}

export interface SelectProps extends Omit<React.SelectHTMLAttributes<HTMLSelectElement>, 'size'> {
  /** Rótulo do campo */
  label?: string;
  /** Mensagem de erro a ser exibida */
  error?: string;
  /** Texto de ajuda ou descrição */
  helperText?: string;
  /** Variante visual do select */
  variant?: 'default' | 'outline' | 'filled';
  /** Tamanho do select */
  size?: 'sm' | 'md' | 'lg';
  /** Opções do select */
  options: SelectOption[];
  /** Texto placeholder quando nenhuma opção está selecionada */
  placeholder?: string;
  /** Classe CSS adicional para o container */
  containerClassName?: string;
}

const Select = forwardRef<HTMLSelectElement, SelectProps>(({
  label,
  error,
  helperText,
  variant = 'default',
  size = 'md',
  options,
  placeholder,
  containerClassName = '',
  className = '',
  disabled,
  ...props
}, ref) => {
  const baseClasses = "w-full border rounded-lg transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent appearance-none bg-white";
  
  const variantClasses = {
    default: "border-gray-300",
    outline: "border-gray-400",
    filled: "border-gray-200 bg-gray-50"
  };

  const sizeClasses = {
    sm: "px-3 py-2 text-sm pr-8",
    md: "px-4 py-3 text-base pr-10",
    lg: "px-5 py-4 text-lg pr-12"
  };

  const disabledClasses = disabled 
    ? "opacity-50 cursor-not-allowed bg-gray-100" 
    : "hover:border-gray-400";

  const errorClasses = error 
    ? "border-red-500 focus:ring-red-500" 
    : "";

  const selectClasses = `
    ${baseClasses}
    ${variantClasses[variant]}
    ${sizeClasses[size]}
    ${disabledClasses}
    ${errorClasses}
    ${className}
  `.trim().replace(/\s+/g, ' ');

  const chevronSize = {
    sm: 'w-4 h-4',
    md: 'w-5 h-5', 
    lg: 'w-6 h-6'
  };

  return (
    <div className={`${containerClassName}`}>
      {label && (
        <label className="block text-sm font-medium text-gray-900 mb-2">
          {label}
          {props.required && <span className="text-red-600 ml-1">*</span>}
        </label>
      )}
      
      <div className="relative">
        <select
          ref={ref}
          className={selectClasses}
          disabled={disabled}
          {...props}
        >
          {placeholder && (
            <option value="" disabled>
              {placeholder}
            </option>
          )}
          {options.map((option) => (
            <option
              key={option.value}
              value={option.value}
              disabled={option.disabled}
            >
              {option.label}
            </option>
          ))}
        </select>
        
        {/* Chevron down icon */}
        <div className="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none">
          <svg 
            className={`${chevronSize[size]} text-gray-400`}
            fill="none" 
            stroke="currentColor" 
            viewBox="0 0 24 24"
          >
            <path 
              strokeLinecap="round" 
              strokeLinejoin="round" 
              strokeWidth={2} 
              d="M19 9l-7 7-7-7" 
            />
          </svg>
        </div>
      </div>
      
      {(error || helperText) && (
        <div className="mt-2">
          {error && (
            <p className="text-sm text-red-700">{error}</p>
          )}
          {helperText && !error && (
            <p className="text-sm text-gray-700">{helperText}</p>
          )}
        </div>
      )}
    </div>
  );
});

Select.displayName = 'Select';

export default Select;