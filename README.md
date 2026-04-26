# ScreenMatch — Back-end

API REST que desenvolvi em Java com Spring Boot para gerenciar séries e episódios. Os dados são buscados automaticamente da API do OMDB e salvos no PostgreSQL.

## O que tem no projeto

- API REST com Spring Boot
- Busca de séries e episódios pela API do OMDB
- Banco de dados PostgreSQL
- Cadastro de séries com todos os episódios de uma vez
- Filtros por título, gênero, avaliação e temporada
- Top 5 séries por avaliação
- Tradução automática da sinopse para português

## Tecnologias que usei

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- API OMDB

## Rotas disponíveis

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | /series | Lista todas as séries |
| GET | /series/{id} | Busca uma série pelo id |
| GET | /series/{id}/episodios | Lista episódios de uma série |
| GET | /series/top5 | Top 5 séries por avaliação |
| GET | /series/busca?titulo= | Busca série por título |
| GET | /series/genero?genero= | Filtra séries por gênero |
| POST | /series/cadastrar?nome= | Cadastra série com episódios |

## Como rodar

Você vai precisar ter o Java 17+, Maven e PostgreSQL instalados.

Crie um banco de dados PostgreSQL e configure as variáveis de ambiente:

```
DB_HOST=localhost
DB_PORT=5432
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

Depois rode o projeto pela sua IDE ou pelo terminal:

```bash
./mvnw spring-boot:run
```

A API vai rodar em `http://localhost:8080`.

## Front-end

O front-end desse projeto está aqui:
https://github.com/jair-passarela/screenmatch-FrontEnd