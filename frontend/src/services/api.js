import axios from 'axios';

// Configuração base da API
const api = axios.create({
  baseURL: 'http://localhost:8081', // URL do backend Spring Boot
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
