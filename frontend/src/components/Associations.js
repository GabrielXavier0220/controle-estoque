import React, { useState, useEffect } from 'react';
import api from '../services/api';
import './Common.css';

function Associations() {
  const [products, setProducts] = useState([]);
  const [rawMaterials, setRawMaterials] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState('');
  const [associations, setAssociations] = useState([]);
  const [formData, setFormData] = useState({
    rawMaterialId: '',
    requiredQuantity: 0
  });

  useEffect(() => {
    fetchProducts();
    fetchRawMaterials();
  }, []);

  useEffect(() => {
    if (selectedProduct) {
      fetchAssociations(selectedProduct);
    }
  }, [selectedProduct]);

  const fetchProducts = async () => {
    try {
      const response = await api.get('/products');
      setProducts(response.data);
    } catch (error) {
      alert('Erro ao carregar produtos: ' + error.message);
    }
  };

  const fetchRawMaterials = async () => {
    try {
      const response = await api.get('/raw-materials');
      setRawMaterials(response.data);
    } catch (error) {
      alert('Erro ao carregar matérias-primas: ' + error.message);
    }
  };

  const fetchAssociations = async (productId) => {
    try {
      const response = await api.get(`/products/${productId}/raw-materials`);
      setAssociations(response.data);
    } catch (error) {
      alert('Erro ao carregar associações: ' + error.message);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedProduct) {
      alert('Selecione um produto primeiro!');
      return;
    }
    try {
      await api.post(`/products/${selectedProduct}/raw-materials`, formData);
      alert('Associação criada com sucesso!');
      setFormData({ rawMaterialId: '', requiredQuantity: 0 });
      fetchAssociations(selectedProduct);
    } catch (error) {
      alert('Erro ao criar associação: ' + error.message);
    }
  };

  const handleDelete = async (associationId) => {
    if (window.confirm('Tem certeza que deseja remover esta associação?')) {
      try {
        await api.delete(`/product-raw-materials/${associationId}`);
        alert('Associação removida com sucesso!');
        fetchAssociations(selectedProduct);
      } catch (error) {
        alert('Erro ao remover associação: ' + error.message);
      }
    }
  };

  return (
    <div className="container">
      <h2>🔗 Associações Produto ↔ Matéria-Prima</h2>

      <div className="form-card">
        <h3>Selecione o Produto</h3>
        <div className="form-group">
          <label>Produto *</label>
          <select
            value={selectedProduct}
            onChange={(e) => setSelectedProduct(e.target.value)}
            className="select-large"
          >
            <option value="">-- Selecione um produto --</option>
            {products.map((p) => (
              <option key={p.id} value={p.id}>
                {p.name} ({p.sku})
              </option>
            ))}
          </select>
        </div>
      </div>

      {selectedProduct && (
        <>
          <div className="form-card">
            <h3>Adicionar Matéria-Prima</h3>
            <form onSubmit={handleSubmit}>
              <div className="form-row">
                <div className="form-group">
                  <label>Matéria-Prima *</label>
                  <select
                    required
                    value={formData.rawMaterialId}
                    onChange={(e) => setFormData({ ...formData, rawMaterialId: e.target.value })}
                  >
                    <option value="">-- Selecione --</option>
                    {rawMaterials.map((rm) => (
                      <option key={rm.id} value={rm.id}>
                        {rm.name} ({rm.code})
                      </option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label>Quantidade Necessária *</label>
                  <input
                    type="number"
                    required
                    value={formData.requiredQuantity}
                    onChange={(e) => setFormData({ ...formData, requiredQuantity: parseInt(e.target.value) })}
                  />
                </div>
              </div>
              <button type="submit" className="btn btn-primary">
                Adicionar
              </button>
            </form>
          </div>

          <div className="table-card">
            <h3>Matérias-Primas Associadas</h3>
            {associations.length === 0 ? (
              <p>Nenhuma matéria-prima associada a este produto.</p>
            ) : (
              <div className="table-responsive">
                <table>
                  <thead>
                    <tr>
                      <th>Matéria-Prima</th>
                      <th>Código</th>
                      <th>Qtd. Necessária</th>
                      <th>Estoque Disponível</th>
                      <th>Ações</th>
                    </tr>
                  </thead>
                  <tbody>
                    {associations.map((assoc) => (
                      <tr key={assoc.id}>
                        <td>{assoc.rawMaterial.name}</td>
                        <td>{assoc.rawMaterial.code}</td>
                        <td>{assoc.requiredQuantity}</td>
                        <td>{assoc.rawMaterial.stockQuantity}</td>
                        <td>
                          <button
                            className="btn-small btn-delete"
                            onClick={() => handleDelete(assoc.id)}
                          >
                            Remover
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}

export default Associations;
