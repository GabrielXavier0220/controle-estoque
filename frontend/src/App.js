import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import ProductList from './components/ProductList';
import RawMaterialList from './components/RawMaterialList';
import Associations from './components/Associations';
import ProductionSuggestion from './components/ProductionSuggestion';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <Routes>
          <Route path="/" element={<ProductList />} />
          <Route path="/raw-materials" element={<RawMaterialList />} />
          <Route path="/associations" element={<Associations />} />
          <Route path="/production" element={<ProductionSuggestion />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
