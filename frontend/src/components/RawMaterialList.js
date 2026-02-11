import React, { useState, useEffect } from 'react';
import api from '../services/api';
import './Common.css';

function RawMaterialList() {
  const [rawMaterials, setRawMaterials] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    code: '',
    stockQuantity: 0
  });
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchRawMaterials();
  }, []);

  const fetchRawMaterials = async () => {
    try {
      setLoading(true);
      const response = await api.get('/raw-materials');
      setRawMaterials(response.data);
    } catch (error) {
      alert('Erro ao carregar matérias-primas: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await api.put(`/raw-materials/${editingId}`, formData);
        alert('Matéria-prima atualizada com sucesso!');
      } else {
        await api.post('/raw-materials', formData);
        alert('Matéria-prima criada com sucesso!');
      }
      resetForm();
      fetchRawMaterials();
    } catch (error) {
      alert('Erro ao salvar matéria-prima: ' + error.message);
    }
  };

  const handleEdit = (rawMaterial) => {
    setFormData({
      name: rawMaterial.name,
      code: rawMaterial.code,
      stockQuantity: rawMaterial.stockQuantity
    });
    setEditingId(rawMaterial.id);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir esta matéria-prima?')) {
      try {
        await api.delete(`/raw-materials/${id}`);
        alert('Matéria-prima excluída com sucesso!');
        fetchRawMaterials();
      } catch (error) {
        alert('Erro ao excluir matéria-prima: ' + error.message);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      code: '',
      stockQuantity: 0
    });
    setEditingId(null);
  };

  return (
    <div className="container">
      <h2>🧪 Matérias-Primas</h2>

      <div className="form-card">
        <h3>{editingId ? 'Editar Matéria-Prima' : 'Nova Matéria-Prima'}</h3>
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
              <label>Código *</label>
              <input
                type="text"
                required
                value={formData.code}
                onChange={(e) => setFormData({ ...formData, code: e.target.value })}
              />
            </div>
          </div>

          <div className="form-group">
            <label>Quantidade em Estoque</label>
            <input
              type="number"
              value={formData.stockQuantity}
              onChange={(e) => setFormData({ ...formData, stockQuantity: parseInt(e.target.value) })}
            />
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
        <h3>Lista de Matérias-Primas</h3>
        {loading ? (
          <p>Carregando...</p>
        ) : rawMaterials.length === 0 ? (
          <p>Nenhuma matéria-prima cadastrada.</p>
        ) : (
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nome</th>
                  <th>Código</th>
                  <th>Estoque</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {rawMaterials.map((rm) => (
                  <tr key={rm.id}>
                    <td>{rm.id}</td>
                    <td>{rm.name}</td>
                    <td>{rm.code}</td>
                    <td>{rm.stockQuantity}</td>
                    <td>
                      <button className="btn-small btn-edit" onClick={() => handleEdit(rm)}>
                        Editar
                      </button>
                      <button className="btn-small btn-delete" onClick={() => handleDelete(rm.id)}>
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

export default RawMaterialList;
