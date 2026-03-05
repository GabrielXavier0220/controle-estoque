# 📦 Sistema de Controle de Estoque

Sistema web completo para gerenciamento de estoque, controle de matérias-primas e cálculo de capacidade produtiva.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Como Executar](#como-executar)
- [Testes Unitários](#testes-unitários)
- [Documentação da API](#documentação-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Autor](#autor)

---

## 🎯 Sobre o Projeto

Este sistema foi desenvolvido como teste técnico e mantido como parte do meu portfólio. Permite que indústrias gerenciem seu estoque de matérias-primas e produtos acabados, calculem a capacidade de produção com base no estoque disponível e priorizem a produção por valor.

### Requisitos de Negócio

O sistema permite:
- Gerenciar produtos (operações CRUD)
- Gerenciar matérias-primas (operações CRUD)
- Associar matérias-primas aos produtos (com quantidades necessárias)
- Calcular quais produtos podem ser fabricados com o estoque atual
- Priorizar sugestões de produção por maior valor

---

## ✨ Funcionalidades

### Backend (API)
- ✅ **RF001** - CRUD completo de Produtos
- ✅ **RF002** - CRUD completo de Matérias-Primas
- ✅ **RF003** - CRUD de Associações Produto-Matéria-Prima
- ✅ **RF004** - Algoritmo de sugestão de produção com priorização por valor

### Frontend (Interface Web)
- ✅ **RF005** - Interface de gerenciamento de Produtos
- ✅ **RF006** - Interface de gerenciamento de Matérias-Primas
- ✅ **RF007** - Interface de gerenciamento de Associações
- ✅ **RF008** - Dashboard de Sugestão de Produção

### Funcionalidades Adicionais
- 🔄 Cálculo de estoque em tempo real
- 📊 Análise de capacidade produtiva
- 💰 Estimativa de valor total de produção
- 🎨 Design responsivo (compatível com celular)
- ⚡ Arquitetura API RESTful

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17**
- **Spring Boot 3.4.0**
- **Spring Data JPA** (ORM)
- **PostgreSQL 16** (Banco de Dados)
- **Flyway** (Migrações de banco)
- **Maven** (Gerenciamento de dependências)
- **Docker** (Containerização)
- **JUnit 5 + Mockito** (Testes unitários)

### Frontend
- **React 18**
- **React Router DOM** (Navegação)
- **Axios** (Cliente HTTP)
- **CSS3** (Estilização responsiva)

### DevOps & Ferramentas
- **Docker Compose** (Orquestração do banco)
- **Git** (Controle de versão)

---

## 🏗️ Arquitetura

```
┌─────────────────┐      HTTP/REST      ┌─────────────────┐
│                 │ ◄─────────────────► │                 │
│  React Frontend │     Porta 3000      │ Spring Boot API │
│   (Porta 3000)  │                     │   (Porta 8081)  │
│                 │                     │                 │
└─────────────────┘                     └────────┬────────┘
                                                 │
                                                 │ JPA/Hibernate
                                                 │
                                        ┌────────▼────────┐
                                        │   PostgreSQL    │
                                        │   (Porta 5432)  │
                                        │ Container Docker│
                                        └─────────────────┘
```

### Modelo de Dados

**Tabelas Principais:**
- `products` - Armazena produtos acabados
- `raw_materials` - Armazena matérias-primas/insumos
- `product_raw_materials` - Tabela de associação (muitos-para-muitos com quantidades)

---

## 📦 Pré-requisitos

Antes de executar este projeto, certifique-se de ter instalado:

- **Java JDK 17+** 
- **Node.js 16+** e **npm** 
- **Docker Desktop** 
- **Maven 3.8+** (ou use o Maven Wrapper incluído)
- **Git** 

### Verificar instalações:
```bash
java -version    # Deve mostrar Java 17+
node -version    # Deve mostrar Node 16+
npm -version     # Deve mostrar npm 8+
docker -version  # Deve mostrar Docker 20+
```

---

## 🚀 Instalação e Configuração

### 1️⃣ Clonar o Repositório

```bash
git clone https://github.com/GabrielXavier0220/controle-estoque.git
cd controle-estoque
```

### 2️⃣ Configurar o Banco de Dados (PostgreSQL com Docker)

```bash
cd backend
docker-compose up -d
```

Isso vai:
- Baixar a imagem do PostgreSQL 16
- Criar um banco chamado `inventory`
- Iniciar o banco na porta `5432`
- Criar usuário `inventory_user` com senha `inventory_pass`

**Verificar se o banco está rodando:**
```bash
docker ps
```
Você deve ver um container chamado `controle-estoque-db-1` em execução.

### 3️⃣ Configurar o Backend

```bash
cd backend
mvn clean install
```

Isso vai:
- Baixar todas as dependências
- Executar as migrações do banco (Flyway)
- Criar as tabelas necessárias
- Compilar a aplicação

### 4️⃣ Configurar o Frontend

```bash
cd ../frontend
npm install
```

Isso vai instalar todas as dependências do React.

---

## ▶️ Como Executar

### Iniciar na ordem correta:

#### 1️⃣ Iniciar o Banco de Dados (se não estiver rodando)
```bash
cd backend
docker-compose up -d
```

#### 2️⃣ Iniciar a API Backend
```bash
cd backend
mvn spring-boot:run
```

Aguarde até ver:
```
Started BackendApplication in X seconds
```

**Backend estará disponível em:** `http://localhost:8081`

#### 3️⃣ Iniciar o Frontend
Abra um **novo terminal** e execute:
```bash
cd frontend
npm start
```

**Frontend abrirá automaticamente em:** `http://localhost:3000`

---

## 🧪 Testes Unitários

O projeto inclui **16 testes unitários** cobrindo as principais funcionalidades do backend.

### 📊 Cobertura de Testes

| Classe de Teste | Testes | Descrição |
|----------------|--------|-----------|
| **ProductServiceTest** | 5 | Testa CRUD de produtos, validação de SKU duplicado, e deleção em cascata |
| **RawMaterialServiceTest** | 5 | Testa CRUD de matérias-primas, atualização de estoque, e deleção em cascata |
| **ProductionServiceTest** | 6 | Testa algoritmo de sugestão de produção e priorização por valor |
| **TOTAL** | **16** | **100% de aprovação** |

### Executar os Testes

**No IntelliJ IDEA:**
1. Clique com botão direito na pasta `test`
2. Selecione **Run 'All Tests'**

**Ou via linha de comando:**
```bash
cd backend
mvn test
```

### Resultado Esperado

```
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### O Que os Testes Validam

✅ **ProductServiceTest:**
- Criação de produto com sucesso
- Validação de SKU duplicado (deve lançar exceção)
- Busca de produto por ID
- Tratamento de erro quando produto não existe
- Deleção de produto e suas associações (cascade)

✅ **RawMaterialServiceTest:**
- Criação de matéria-prima
- Listagem de todas as matérias-primas
- Atualização de quantidade em estoque
- Deleção de matéria-prima e associações (cascade)
- Tratamento de erro quando matéria-prima não existe

✅ **ProductionServiceTest:**
- Cálculo correto da produção possível
- **Priorização por maior valor** 
- Retorno zero quando não há matérias-primas
- Limitação pela matéria-prima que acaba primeiro
- Mensagem apropriada quando nenhum produto pode ser produzido
- Validação com todas as matérias-primas (farinha, açúcar, chocolate, ovos, leite)

---

## 📚 Documentação da API

### URL Base
```
http://localhost:8081
```

### Endpoints

#### Produtos
```http
GET    /products              # Listar todos os produtos
GET    /products/{id}         # Buscar produto por ID
POST   /products              # Criar novo produto
PUT    /products/{id}         # Atualizar produto
DELETE /products/{id}         # Deletar produto
```

#### Matérias-Primas
```http
GET    /raw-materials         # Listar todas as matérias-primas
GET    /raw-materials/{id}    # Buscar matéria-prima por ID
POST   /raw-materials         # Criar nova matéria-prima
PUT    /raw-materials/{id}    # Atualizar matéria-prima
DELETE /raw-materials/{id}    # Deletar matéria-prima
```

#### Associações
```http
GET    /products/{productId}/raw-materials                    # Listar associações de um produto
POST   /products/{productId}/raw-materials                    # Criar associação
PUT    /product-raw-materials/{associationId}                 # Atualizar quantidade necessária
DELETE /product-raw-materials/{id}                            # Deletar associação por ID
```

#### Sugestão de Produção
```http
GET    /production/suggestions    # Calcular capacidade de produção e valor
```

### Exemplos de Requisições

#### Criar Produto
```bash
curl -X POST http://localhost:8081/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bolo de Chocolate",
    "sku": "BOLO-CHOC-001",
    "description": "Bolo de chocolate 1kg",
    "quantity": 0,
    "minQuantity": 5,
    "costPrice": 15.00,
    "salePrice": 35.00
  }'
```

#### Obter Sugestão de Produção
```bash
curl http://localhost:8081/production/suggestions
```

**Resposta:**
```json
{
  "suggestedProducts": [
    {
      "productId": 1,
      "productName": "Bolo de Chocolate",
      "productSku": "BOLO-CHOC-001",
      "maxQuantity": 2,
      "unitPrice": 35.00,
      "totalValue": 70.00
    }
  ],
  "totalValue": 70.00,
  "message": "Sugestão de produção baseada no estoque disponível (ordenada por maior valor)."
}
```

---

## 📁 Estrutura do Projeto

```
controle-estoque/
│
├── backend/                          # API Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com.controleestoque.backend/
│   │   │   │       ├── config/       # Configuração CORS
│   │   │   │       ├── controller/   # Controllers REST
│   │   │   │       ├── dto/          # Data Transfer Objects
│   │   │   │       ├── entity/       # Entidades JPA
│   │   │   │       ├── repository/   # Repositórios Spring Data
│   │   │   │       └── service/      # Lógica de Negócio
│   │   │   └── resources/
│   │   │       ├── application.yaml  # Configurações
│   │   │       └── db.migration/     # Migrações Flyway
│   │   └── test/                     # Testes unitários
│   │       └── java/
│   │           └── com.controleestoque.backend.service/
│   │               ├── ProductServiceTest.java
│   │               ├── RawMaterialServiceTest.java
│   │               └── ProductionServiceTest.java
│   ├── http/
│   │   └── requests.http             # Requisições HTTP de teste
│   ├── docker-compose.yml            # Container PostgreSQL
│   └── pom.xml                       # Dependências Maven
│
└── frontend/                         # Aplicação React
    ├── public/
    ├── src/
    │   ├── components/               # Componentes React
    │   │   ├── Navbar.js             # Menu de navegação
    │   │   ├── Navbar.css
    │   │   ├── ProductList.js        # CRUD de Produtos
    │   │   ├── RawMaterialList.js    # CRUD de Matérias-Primas
    │   │   ├── Associations.js       # Gerenciamento de Associações
    │   │   ├── ProductionSuggestion.js # Dashboard de Produção
    │   │   └── Common.css            # Estilos compartilhados
    │   ├── services/
    │   │   └── api.js                # Configuração Axios
    │   ├── App.js                    # App principal com rotas
    │   ├── App.css                   # Estilos globais
    │   └── index.js                  # Ponto de entrada
    ├── package.json
    └── package-lock.json
```

---

## 🎯 Lógica de Negócio Principal

### Algoritmo de Cálculo de Produção

O sistema calcula a capacidade de produção usando esta lógica:

1. Para cada produto, verifica todas as matérias-primas necessárias
2. Calcula quantas unidades podem ser feitas com cada matéria-prima
3. O **fator limitante** (menor quantidade) determina a produção máxima
4. Ordena produtos por **valor total** (quantidade × preço) - maior primeiro
5. Exibe sugestões de produção com receita estimada

**Exemplo:**
```
Produto: Bolo de Chocolate (vende por R$ 35,00)
Requer:
- 500g Farinha (estoque: 1000g) → pode fazer 2 bolos
- 200g Açúcar (estoque: 500g)  → pode fazer 2 bolos
- 150g Chocolate (estoque: 300g) → pode fazer 2 bolos

Resultado: Pode produzir 2 bolos = R$ 70,00 de valor total
```

---

## 🔒 Segurança e CORS

O CORS está configurado para permitir requisições de `http://localhost:3000` (frontend).

Veja: `backend/src/main/java/com/controleestoque/backend/config/CorsConfig.java`

---

## 🚧 Melhorias Futuras

- [ ] Adicionar autenticação de usuários (Spring Security + JWT)
- [ ] Implementar testes de integração (Cypress para frontend)
- [ ] Criar histórico de produção
- [ ] Adicionar exportação de dados (CSV, relatórios PDF)
- [ ] Implementar alertas de estoque baixo
- [ ] Adicionar suporte multi-idioma (i18n)
- [ ] Dashboard com gráficos e estatísticas

---

## 👤 Autor

**Gabriel Xavier**

- GitHub: [@GabrielXavier0220](https://github.com/GabrielXavier0220)
- LinkedIn: [Gabriel Xavier](https://www.linkedin.com/in/gabriel-xavier-a5255a173/)
- Email: gabrielkazeka@hotmail.com

---

## 📄 Licença

Este projeto foi desenvolvido como teste técnico e está disponível para fins de portfólio.

---

## 📞 Suporte

Em caso de dúvidas sobre como executar este projeto:
1. Verifique a seção de troubleshooting abaixo
2. Revise os passos de instalação
3. Entre em contato via email

---

## 🔧 Troubleshooting (Resolução de Problemas)

### Erros de conexão com o banco de dados:
```bash
# Certifique-se de que o Docker está rodando
docker ps

# Reinicie o banco de dados
docker-compose down
docker-compose up -d
```

### Porta já em uso:
```bash
# Backend (8081)
# Verifique o que está usando a porta
netstat -ano | findstr :8081

# Frontend (3000)
# Escolha uma porta diferente quando solicitado
```

### Erros de CORS:
- Certifique-se de que o backend está rodando na porta 8081
- Verifique se `frontend/src/services/api.js` tem a baseURL correta
- Reinicie backend e frontend

### Erros nos testes:
```bash
# Limpe e recompile o projeto
cd backend
mvn clean install

# Execute os testes novamente
mvn test
```

---
