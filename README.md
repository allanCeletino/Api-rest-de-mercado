# 🛒 Mercado API

Uma API REST completa para gerenciamento de um mini-mercado, desenvolvida com **Java** e **Spring Boot**. O sistema gerencia o cadastro de usuários, o estoque de produtos e o fluxo completo de processamento de pedidos com múltiplos itens, calculando automaticamente o valor total e tratando de forma segura os relacionamentos entre as tabelas.

## 🚀 Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.x**
* **Spring Data JPA** (Persistência de dados e Derived Queries)
* **Spring Validation** (Validação de dados com `@Valid`, `@NotBlank`, etc.)
* **Jackson** (Serialização JSON e controle de referências bidirecionais)
* **Banco de Dados:** PostgreSQL / MySQL / H2 *(Ajuste de acordo com o que usou)*

---

## 🛠️ Funcionalidades Principais

* **Gestão de Usuários:** Cadastro, atualização de perfis e buscas customizadas (por ID e por E-mail).
* **Catálogo de Produtos Pagina-do:** Listagem inteligente de produtos utilizando paginação do Spring Data para otimizar a performance do front-end.
* **Busca por Intervalo:** Endpoint avançado de consulta de produtos filtrando por um intervalo customizado de IDs (`idIniciar` e `idFinal`).
* **Fluxo de Pedidos Completo:**
  * Cadastro de pedidos com múltiplos itens vinculando dinamicamente chaves estrangeiras.
  * Cálculo automatizado do valor total do pedido.
  * Tratamento de recursão infinita (Infinite Loop) em relacionamentos `@OneToMany` e `@ManyToOne` usando as anotações `@JsonManagedReference` e `@JsonBackReference` do Jackson.
* **Filtro por Datas:** Consulta paginada de pedidos realizados dentro de um intervalo de tempo específico (`LocalDateTime` no padrão ISO 8601).

---

## 🛣️ Endpoints da API

### 👤 Usuários (`/usuarios` ou `/Usuario`)
* `POST /usuarios` - Cadastra um novo usuário (Valida campos obrigatórios como senha).
* `PUT /usuarios` - Atualiza os dados de um usuário existente.
* `GET /usuarios/id/{id}` - Busca um usuário pelo seu ID único.
* `GET /usuarios/email/{email}` - Busca um usuário pelo endereço de e-mail.

### 📦 Produtos (`/produtos` ou `/produto`)
* `GET /produtos` - Retorna a lista de produtos cadastrados de forma paginada.
* `GET /produtos/intervaloId?idIniciar=1&idFinal=10` - Busca produtos dentro de um intervalo de IDs.

### 📝 Pedidos (`/pedidos`)
* `POST /pedidos` - Registra um novo pedido calculando o total a partir dos itens enviados.
* `PUT /pedidos` - Atualiza informações e status do pedido de forma segura.
* `GET /pedidos/BuscarPorDatas?inicio=2026-07-04T22:00:00&fim=2026-07-04T23:59:59` - Filtra pedidos por um período de tempo.

---

## 🔧 Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone [https://github.com/seu-usuario/mercado-api.git](https://github.com/seu-usuario/mercado-api.git)
