"use client";

import PetGrid from "./components/PetGrid";

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header da aplicação */}
      <header className="bg-white shadow-sm border-b border-gray-200">
        <div className="container mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <span className="text-2xl">🐕</span>
              <h1 className="text-2xl font-bold text-gray-900">AdoteUmPet</h1>
            </div>
            <nav className="hidden md:flex space-x-6">
              <a href="/" className="text-blue-600 font-medium">Início</a>
              <a href="/pets" className="text-gray-600 hover:text-gray-900">Todos os Pets</a>
              <a href="/breeds" className="text-gray-600 hover:text-gray-900">Explorar Raças</a>
              <a href="/sobre" className="text-gray-600 hover:text-gray-900">Sobre</a>
            </nav>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="bg-gradient-to-r from-blue-600 to-purple-600 text-white py-16">
        <div className="container mx-auto px-4 text-center">
          <h2 className="text-5xl font-bold mb-6">Encontre seu novo melhor amigo</h2>
          <p className="text-xl mb-8 opacity-90 max-w-2xl mx-auto">
            Conectamos pets que precisam de um lar com famílias que querem amar. 
            Cada pet tem uma história única e está esperando por você!
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <a 
              href="#pets" 
              className="bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition-colors"
            >
              Ver Pets Disponíveis
            </a>
            <a 
              href="/breeds" 
              className="border-2 border-white text-white px-8 py-3 rounded-lg font-semibold hover:bg-white hover:text-blue-600 transition-colors"
            >
              Explorar Raças
            </a>
            <a 
              href="/pets" 
              className="border-2 border-white text-white px-8 py-3 rounded-lg font-semibold hover:bg-white hover:text-blue-600 transition-colors"
            >
              Busca Avançada
            </a>
          </div>
        </div>
      </section>

      {/* Seção principal com os cards */}
      <main id="pets" className="py-12">
        <div className="container mx-auto px-4">
          <PetGrid />
        </div>
      </main>

      {/* Footer */}
      <footer className="bg-gray-800 text-white py-8 mt-12">
        <div className="container mx-auto px-4 text-center">
          <div className="flex items-center justify-center space-x-2 mb-4">
            <span className="text-2xl">🐾</span>
            <span className="text-lg font-semibold">AdoteUmPet</span>
          </div>
          <p className="text-gray-300 mb-4">
            Facilitando adoções responsáveis e conectando corações
          </p>
          <div className="flex justify-center space-x-6 text-sm">
            <a href="/privacidade" className="hover:text-gray-300">Privacidade</a>
            <a href="/termos" className="hover:text-gray-300">Termos</a>
            <a href="/contato" className="hover:text-gray-300">Contato</a>
          </div>
          <p className="text-gray-400 text-sm mt-4">
            © 2025 AdoteUmPet. Todos os direitos reservados.
          </p>
        </div>
      </footer>
    </div>
  );
}
