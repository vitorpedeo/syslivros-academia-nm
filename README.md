<h1 align="center">
  Syslivros
</h1>

Sistema criado utilizando Java durante o curso realizado pela Academia de TI da Novo Mundo.

## 🔨 Como executar o projeto

Para executar o projeto, são necessárias as seguintes configurações:

### ⚙️ Docker

- Execute o arquivo `docker-compose.yml` utilizando o seguinte comando:
```bash
docker-compose up -d
```
- Conecte-se ao banco de dados utilizando as seguintes credenciais:
```bash
USER: syslivros
```
```bash
PASSWORD: syslivros
```
```bash
DATABASE: syslivros
```
- Rode o seguinte script SQL:
```sql
create table author(
	id 					serial,
	name 				varchar(100),
	nationality varchar(100),
	birth_year 	integer
);

alter table author add constraint pk_author primary key (id);

create table book(
	id 					serial,
	title 			varchar(50),
	isbn 				varchar(50),
	edition 		integer,
	description varchar(1000)
);

alter table book add constraint pk_book primary key (id);

create table author_book(
	id 					serial,
	author_id 	integer,
	book_id 		integer
);

alter table author_book add constraint pk_author_book primary key (id);
alter table author_book add constraint fk_author foreign key (author_id) references author (id);
alter table author_book add constraint fk_book foreign key (book_id) references book (id);
```

### ⚙️ VSCode

- No arquivo `launch.json` localizado na pasta `.vscode`, coloque o caminho
do `JavaFX` da sua máquina na propriedade `vmArgs`;
- No arquivo `settings.json` localizado na pasta `.vscode`, coloque os caminhos
do `JavaFX` e do `JDBC` da sua máquina.