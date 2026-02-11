import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-container">
        <h1 className="navbar-logo">📦 Controle de Estoque</h1>
        <ul className="navbar-menu">
          <li>
            <Link to="/" className="navbar-link">Produtos</Link>
          </li>
          <li>
            <Link to="/raw-materials" className="navbar-link">Matérias-Primas</Link>
          </li>
          <li>
            <Link to="/associations" className="navbar-link">Associações</Link>
          </li>
          <li>
            <Link to="/production" className="navbar-link">Sugestão de Produção</Link>
          </li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
