# 🏅 Programa de Fidelidade com Padrões de Projeto (Spring Boot)

API REST em **Java + Spring Boot** que gerencia clientes e um **programa de fidelidade**: cada compra gera pontos, os pontos definem o nível do cliente (Bronze, Prata ou Ouro) e cada nível dá um desconto diferente.

O projeto nasceu do laboratório de **Padrões de Projeto (GoF) com Spring** da Digital Innovation One e foi evoluído com uma regra de negócio própria.

---

## 💡 A regra de negócio

| Nível  | Pontos necessários | Desconto nas compras |
|--------|--------------------|----------------------|
| Bronze | 0                  | 0%                   |
| Prata  | 100                | 5%                   |
| Ouro   | 300                | 10%                  |

- O desconto é calculado pelo **nível atual** do cliente.
- Cada **R$ 10,00 pagos** (já com desconto) rendem **1 ponto**.
- O nível **não é gravado no banco**: ele é derivado dos pontos, então nunca fica inconsistente.
- Todo cliente novo começa com **0 pontos** (Bronze), e os pontos não podem ser alterados via `PUT`.

---

## 🧩 Padrões de projeto aplicados

| Padrão        | Onde                                                                 | Para quê |
|---------------|----------------------------------------------------------------------|----------|
| **Singleton** | `ClienteServiceImpl` (`@Service` do Spring)                          | Uma única instância gerenciada pelo Spring |
| **Strategy**  | `ClienteService` e `DescontoStrategy` (+ `DescontoBronze/Prata/Ouro`) | Cada nível tem sua própria regra de desconto, atrás da mesma interface |
| **Factory Method** | `DescontoFactory`                                               | Escolhe a estratégia de desconto certa a partir do nível |
| **Facade**    | `ClienteRestController` e `ClienteServiceImpl`                       | Esconde a complexidade de banco de dados e ViaCEP atrás de uma API simples |

---

## 🛠️ Tecnologias

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database (banco em memória)
- OpenFeign (consumo da API do [ViaCEP](https://viacep.com.br))

---

## 🚀 Como executar

```bash
# clonar o repositório
git clone <URL-DO-SEU-REPOSITORIO>
cd <NOME-DA-PASTA>

# subir a aplicação
./mvnw spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

> Se o projeto usar Gradle, troque o último comando por `./gradlew bootRun`.

---

## 📡 Endpoints

| Método | Rota                              | Descrição |
|--------|-----------------------------------|-----------|
| GET    | `/clientes`                       | Lista todos os clientes |
| GET    | `/clientes/{id}`                  | Busca um cliente por id |
| POST   | `/clientes`                       | Cadastra um cliente (busca o endereço no ViaCEP pelo CEP) |
| PUT    | `/clientes/{id}`                  | Atualiza um cliente (os pontos são preservados) |
| DELETE | `/clientes/{id}`                  | Remove um cliente |
| POST   | `/clientes/{id}/compras?valor=X`  | Registra uma compra, aplica o desconto e soma pontos |

---

## 🧪 Exemplo de uso

**1. Cadastrar um cliente**

```http
POST /clientes
Content-Type: application/json

{
  "nome": "Maria",
  "endereco": { "cep": "01001000" }
}
```

**2. Fazer uma compra de R$ 1.000,00** (cliente Bronze, sem desconto)

```http
POST /clientes/1/compras?valor=1000
```

```json
{
  "valorOriginal": 1000,
  "valorFinal": 1000.00,
  "pontosGanhos": 100,
  "pontosTotais": 100,
  "nivel": "PRATA"
}
```

**3. Fazer a mesma compra de novo** (agora Prata, com 5% de desconto)

```json
{
  "valorOriginal": 1000,
  "valorFinal": 950.00,
  "pontosGanhos": 95,
  "pontosTotais": 195,
  "nivel": "PRATA"
}
```

---

## 🗂️ Estrutura do projeto

```
one.digitalinnovation.gof
├── Application.java
├── controller
│   └── ClienteRestController.java
├── model
│   ├── Cliente.java
│   ├── CompraResultado.java
│   ├── Endereco.java
│   ├── NivelFidelidade.java
│   ├── ClienteRepository.java
│   └── EnderecoRepository.java
└── service
    ├── ClienteService.java
    ├── ViaCepService.java
    ├── desconto
    │   ├── DescontoStrategy.java
    │   ├── DescontoBronze.java
    │   ├── DescontoPrata.java
    │   ├── DescontoOuro.java
    │   └── DescontoFactory.java
    └── impl
        └── ClienteServiceImpl.java
```

---

## 🔭 Próximos passos

- Retornar erro `404` quando o cliente não existir (hoje o `buscarPorId` usa `Optional.get()`).
- Adicionar o padrão **Observer** para avisar o cliente quando ele subir de nível.
- Criar testes unitários para as estratégias de desconto.

---

## 👤 Autor

Feito por ([Gustavo](https://github.com/gbasilio321>)), a partir do projeto base do instrutor [falvojr](https://github.com/falvojr) no bootcamp da Digital Innovation One.