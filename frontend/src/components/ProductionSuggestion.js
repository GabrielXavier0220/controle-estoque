import React, { useState } from 'react';
import api from '../services/api';
import './Common.css';

function ProductionSuggestion() {
  const [suggestion, setSuggestion] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchSuggestion = async () => {
    try {
      setLoading(true);
      const response = await api.get('/production/suggestions');
      setSuggestion(response.data);
    } catch (error) {
      alert('Erro ao carregar sugestão de produção: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h2>📊 Sugestão de Produção</h2>

      <div className="form-card">
        <p>
          Clique no botão abaixo para calcular quais produtos podem ser produzidos
          com as matérias-primas disponíveis em estoque.
        </p>
        <button
          className="btn btn-primary btn-large"
          onClick={fetchSuggestion}
          disabled={loading}
        >
          {loading ? 'Calculando...' : 'Calcular Produção Possível'}
        </button>
      </div>

      {suggestion && (
        <>
          <div className="info-card">
            <h3>💡 {suggestion.message}</h3>
            <div className="total-value">
              <span>Valor Total Estimado:</span>
              <strong>R$ {suggestion.totalValue.toFixed(2)}</strong>
            </div>
          </div>

          {suggestion.suggestedProducts.length === 0 ? (
            <div className="alert alert-warning">
              <p>⚠️ Nenhum produto pode ser produzido com o estoque atual.</p>
              <p>Verifique se há matérias-primas suficientes e se os produtos estão associados corretamente.</p>
            </div>
          ) : (
            <div className="table-card">
              <h3>Produtos que Podem ser Produzidos</h3>
              <div className="table-responsive">
                <table>
                  <thead>
                    <tr>
                      <th>Posição</th>
                      <th>Produto</th>
                      <th>SKU</th>
                      <th>Qtd. Máxima</th>
                      <th>Preço Unitário</th>
                      <th>Valor Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    {suggestion.suggestedProducts.map((item, index) => (
                      <tr key={item.productId} className={index === 0 ? 'highlight-row' : ''}>
                        <td>
                          {index === 0 && '🥇 '}
                          {index === 1 && '🥈 '}
                          {index === 2 && '🥉 '}
                          {index + 1}°
                        </td>
                        <td>{item.productName}</td>
                        <td>{item.productSku}</td>
                        <td>{item.maxQuantity} un.</td>
                        <td>R$ {item.unitPrice.toFixed(2)}</td>
                        <td><strong>R$ {item.totalValue.toFixed(2)}</strong></td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              <div className="production-tip">
                <p>
                  💡 <strong>Dica:</strong> Os produtos estão ordenados por maior valor total.
                  Priorize a produção dos primeiros itens para maximizar o lucro!
                </p>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default ProductionSuggestion;
