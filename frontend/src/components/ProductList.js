import React, { useState, useEffect } from 'react';
import api from '../services/api';
import './Common.css';

function ProductList() {
  const [products, setProducts] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    sku: '',
    description: '',
    quantity: 0,
    minQuantity: 0,
    costPrice: 0,
    salePrice: 0
  });
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const response = await api.get('/products');
      setProducts(response.data);
    } catch (error) {
      alert('Erro ao carregar produtos: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await api.put(`/products/${editingId}`, formData);
        alert('Produto atualizado com sucesso!');
      } else {
        await api.post('/products', formData);
        alert('Produto criado com sucesso!');
      }
      resetForm();
      fetchProducts();
    } catch (error) {
      alert('Erro ao salvar produto: ' + error.message);
    }
  };

  const handleEdit = (product) => {
    setFormData({
      name: product.name,
      sku: product.sku,
      description: product.description || '',
      quantity: product.quantity,
      minQuantity: product.minQuantity,
      costPrice: product.costPrice,
      salePrice: product.salePrice
    });
    setEditingId(product.id);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este produto?')) {
      try {
        await api.delete(`/products/${id}`);
        alert('Produto excluído com sucesso!');
        fetchProducts();
      } catch (error) {
        alert('Erro ao excluir produto: ' + error.message);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      sku: '',
      description: '',
      quantity: 0,
      minQuantity: 0,
      costPrice: 0,
      salePrice: 0
    });
    setEditingId(null);
  };

  return (
    <div className="container">
      <h2>📦 Produtos</h2>

      <div className="form-card">
        <h3>{editingId ? 'Editar Produto' : 'Novo Produto'}</h3>
        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label>Nome *</label>
              <input
                type="text"
                required
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label>SKU *</label>
              <input
                type="text"
                required
                value={formData.sku}
                onChange={(e) => setFormData({ ...formData, sku: e.target.value })}
              />
            </div>
          </div>

          <div className="form-group">
            <label>Descrição</label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              rows="3"
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Quantidade</label>
              <input
                type="number"
                value={formData.quantity}
                onChange={(e) => setFormData({ ...formData, quantity: parseInt(e.target.value) })}
              />
            </div>
            <div className="form-group">
              <label>Qtd. Mínima</label>
              <input
                type="number"
                value={formData.minQuantity}
                onChange={(e) => setFormData({ ...formData, minQuantity: parseInt(e.target.value) })}
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Preço de Custo</label>
              <input
                type="number"
                step="0.01"
                value={formData.costPrice}
                onChange={(e) => setFormData({ ...formData, costPrice: parseFloat(e.target.value) })}
              />
            </div>
            <div className="form-group">
              <label>Preço de Venda</label>
              <input
                type="number"
                step="0.01"
                value={formData.salePrice}
                onChange={(e) => setFormData({ ...formData, salePrice: parseFloat(e.target.value) })}
              />
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" className="btn btn-primary">
              {editingId ? 'Atualizar' : 'Criar'}
            </button>
            {editingId && (
              <button type="button" className="btn btn-secondary" onClick={resetForm}>
                Cancelar
              </button>
            )}
          </div>
        </form>
      </div>

      <div className="table-card">
        <h3>Lista de Produtos</h3>
        {loading ? (
          <p>Carregando...</p>
        ) : products.length === 0 ? (
          <p>Nenhum produto cadastrado.</p>
        ) : (
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nome</th>
                  <th>SKU</th>
                  <th>Qtd.</th>
                  <th>Preço Venda</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {products.map((product) => (
                  <tr key={product.id}>
                    <td>{product.id}</td>
                    <td>{product.name}</td>
                    <td>{product.sku}</td>
                    <td>{product.quantity}</td>
                    <td>R$ {product.salePrice?.toFixed(2)}</td>
                    <td>
                      <button className="btn-small btn-edit" onClick={() => handleEdit(product)}>
                        Editar
                      </button>
                      <button className="btn-small btn-delete" onClick={() => handleDelete(product.id)}>
                        Excluir
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default ProductList;
