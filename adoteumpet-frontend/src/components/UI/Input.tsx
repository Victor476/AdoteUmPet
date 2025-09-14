"use client";

import React, { forwardRef } from "react";

export interface InputProps extends Omit<React.InputHTMLAttributes<HTMLInputElement>, 'size'> {
  /** Rótulo do campo */
  label?: string;
  /** Mensagem de erro a ser exibida */
  error?: string;
  /** Texto de ajuda ou descrição */
  helperText?: string;
  /** Variante visual do input */
  variant?: 'default' | 'outline' | 'filled';
  /** Tamanho do input */
  size?: 'sm' | 'md' | 'lg';
  /** Ícone à esquerda do input */
  leftIcon?: React.ReactNode;
  /** Ícone à direita do input */
  rightIcon?: React.ReactNode;
  /** Classe CSS adicional para o container */
  containerClassName?: string;
}

const Input = forwardRef<HTMLInputElement, InputProps>(({
  label,
  error,
  helperText,
  variant = 'default',
  size = 'md',
  leftIcon,
  rightIcon,
  containerClassName = '',
  className = '',
  disabled,
  ...props
}, ref) => {
  const baseClasses = "w-full border rounded-lg transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent";
  
  const variantClasses = {
    default: "border-gray-300 bg-white",
    outline: "border-gray-400 bg-transparent",
    filled: "border-gray-200 bg-gray-50"
  };

  const sizeClasses = {
    sm: "px-3 py-2 text-sm",
    md: "px-4 py-3 text-base",
    lg: "px-5 py-4 text-lg"
  };

  const disabledClasses = disabled 
    ? "opacity-50 cursor-not-allowed bg-gray-100" 
    : "hover:border-gray-400";

  const errorClasses = error 
    ? "border-red-500 focus:ring-red-500" 
    : "";

  const inputClasses = `
    ${baseClasses}
    ${variantClasses[variant]}
    ${sizeClasses[size]}
    ${disabledClasses}
    ${errorClasses}
    ${leftIcon ? 'pl-10' : ''}
    ${rightIcon ? 'pr-10' : ''}
    ${className}
  `.trim().replace(/\s+/g, ' ');

  return (
    <div className={`${containerClassName}`}>
      {label && (
        <label className="block text-sm font-medium text-gray-900 mb-2">
          {label}
          {props.required && <span className="text-red-600 ml-1">*</span>}
        </label>
      )}
      
      <div className="relative">
        {leftIcon && (
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <div className="text-gray-400">
              {leftIcon}
            </div>
          </div>
        )}
        
        <input
          ref={ref}
          className={inputClasses}
          disabled={disabled}
          {...props}
        />
        
        {rightIcon && (
          <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
            <div className="text-gray-400">
              {rightIcon}
            </div>
          </div>
        )}
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

Input.displayName = 'Input';

export default Input;