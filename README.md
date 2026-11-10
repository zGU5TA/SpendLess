# 💰 SpendLess — Gestão Financeira Pessoal

![Java 21](https://img.shields.io/badge/Java-21-orange)
![SQLite](https://img.shields.io/badge/SQLite-embutido-blue)
![Maven](https://img.shields.io/badge/Maven-build-red)

Aplicativo desktop em Java para controlar receitas e despesas. Cada usuário tem sua conta, registra gastos e rendas por categoria e acompanha o mês em um painel com gráficos. Tudo roda localmente: não precisa instalar servidor de banco de dados nem criar conta em nenhum serviço.

Projeto acadêmico em grupo, criado para aplicar os conceitos de Java vistos em aula e mantido aqui como portfólio.

![Painel de resumo do SpendLess](docs/img/resumo.png)

## 📑 Sumário

- [Funcionalidades](#-funcionalidades)
- [Telas](#-telas)
- [Como executar](#-como-executar)
- [Primeiro acesso e administrador](#-primeiro-acesso-e-administrador)
- [Tecnologias e arquitetura](#-tecnologias-e-arquitetura)
- [Segurança](#-segurança)
- [Do MySQL ao SQLite](#-do-mysql-ao-sqlite)
- [Limitações e próximos passos](#-limitações-e-próximos-passos)
- [Autor](#-autor)

## ✨ Funcionalidades

**Para o usuário**
- Cadastro e login por CPF e senha.
- Lançamento de **gastos** e **rendas** (adicionar, editar, excluir e listar), com marcação de lançamento recorrente.
- **20 categorias** já cadastradas (10 de gasto e 10 de renda) na primeira execução.
- **Painel de resumo do mês:** total de despesas, total de renda, valores a descontar e a receber, últimas transações e gráficos de pizza com as principais rendas e os principais gastos por categoria.
- Perfil com edição dos dados e exclusão da conta.

**Para o administrador**
- Resumo geral com todas as transações do sistema, com filtro por CPF.
- Gerenciar usuários (editar dados, alterar o tipo de conta, excluir).
- Gerenciar categorias (criar, renomear e excluir). Uma categoria que ainda tem gastos ou rendas não pode ser excluída, para que nenhum lançamento "suma" das listas.

## 🖼️ Telas

| Resumo do mês | Gastos |
|---|---|
| ![Resumo](docs/img/resumo.png) | ![Gastos](docs/img/gastos.png) |

| Rendas | Perfil |
|---|---|
| ![Rendas](docs/img/renda.png) | ![Perfil](docs/img/perfil.png) |

| Administração de usuários | Administração de categorias |
|---|---|
| ![Usuários](docs/img/admin-usuarios.png) | ![Categorias](docs/img/admin-categorias.png) |

## ⚙️ Como executar

### Opção 1: executável para Windows (mais fácil)

1. Instale o **Java 21 ou superior**: [Eclipse Temurin 21](https://adoptium.net/pt-BR/temurin/releases/?version=21).
2. Abra a página de [**Releases**](../../releases) e baixe o `SpendLess.exe`.
3. Coloque o arquivo em uma pasta de sua escolha e dê um duplo clique.

O banco de dados `spendless.db` é criado automaticamente **na mesma pasta do executável**. Para fazer backup dos seus dados, basta copiar esse arquivo.

> **Aviso do Windows:** como o programa não tem assinatura digital, o Windows pode mostrar "O Windows protegeu seu computador". Clique em **Mais informações** e depois em **Executar assim mesmo**.

### Opção 2: a partir do código-fonte

Requisitos: **JDK 21+** e **Maven 3.9+**.

```bash
git clone https://github.com/zGU5TA/SpendLess.git
cd SpendLess
mvn clean package
java -jar target/SpendLess-1.0.jar
```

O `mvn clean package` gera um *fat JAR* (com todas as dependências) em `target/`.

Para abrir no **VS Code**, **NetBeans** ou **IntelliJ**, importe a pasta como projeto Maven. A classe principal é `com.mycompany.projetoa3.TelaInicial`.

## 🔑 Primeiro acesso e administrador

Em um banco novo, **o primeiro usuário cadastrado se torna administrador** e vê a área "Admin". Os cadastros seguintes são usuários comuns. Um administrador pode promover ou rebaixar outras contas pela tela de gerenciamento de usuários.

Para recomeçar do zero, feche o programa e apague (ou renomeie) o arquivo `spendless.db`.

## 🛠️ Tecnologias e arquitetura

| Camada | Tecnologia |
|---|---|
| Interface | Java Swing |
| Persistência | SQLite via JDBC (`sqlite-jdbc`) |
| Gráficos | JFreeChart 1.5.3 |
| Build | Maven (Shade Plugin para o fat JAR) |
| Executável | Launch4j |

O código é organizado em pacotes por funcionalidade (`telas/gasto`, `telas/renda`, `telas/adm`), e o acesso ao banco de gastos, rendas e categorias passa por classes **DAO** (`GastoDAO`, `RendaDAO`, `CategoriaDAO`), separando a interface da lógica de persistência. Todas as consultas usam `PreparedStatement`, o que protege contra SQL injection.

```
src/main/java/com/mycompany/projetoa3/
├── ConexaoDB.java        # conexão, criação das tabelas e usuários
├── SenhaUtil.java        # hash e verificação de senhas
├── CategoriaDAO.java
├── telas/
│   ├── gasto/            # Gasto, GastoDAO e telas
│   ├── renda/            # Renda, RendaDAO e telas
│   └── adm/              # administração de usuários e categorias
└── ...
```

## 🔒 Segurança

- As senhas **não** são guardadas em texto puro: usam **PBKDF2 com HMAC-SHA256**, 210.000 iterações e um *salt* aleatório por usuário, tudo com a biblioteca padrão do Java.
- Contas criadas antes dessa mudança continuam funcionando: a senha antiga é convertida em hash automaticamente no primeiro login.
- Os dados ficam apenas no computador do usuário (`spendless.db`); nada é enviado pela rede.

## 🔄 Do MySQL ao SQLite

O SpendLess nasceu usando **MySQL**, que era o banco ensinado na faculdade. Quando o servidor que o hospedava deixou de existir, o projeto deixou de funcionar para quem o baixava. A solução foi migrar para **SQLite**: o banco virou um arquivo local, criado e populado sozinho na primeira execução, sem nenhuma configuração. Isso também resolveu a portabilidade, porque os dados de cada pessoa ficam na própria máquina.

Antes, o administrador era cadastrado manualmente no servidor MySQL. Sem servidor, essa etapa deixou de existir, e hoje o primeiro usuário cadastrado assume esse papel.

## 🚧 Limitações e próximos passos

Pontos conhecidos que ainda podem evoluir:

- [ ] Telas de filtro para gastos e rendas (por período, faixa de valor e categoria): a consulta já existe nos DAOs (`listarGastosFiltrados` e `listarRendasFiltradas`), falta a interface.
- [ ] Valores monetários ainda usam `double`; o ideal é `BigDecimal` (ou centavos inteiros) para evitar erros de arredondamento.
- [ ] Criar um `UsuarioDAO` e tirar de `ConexaoDB` a lógica de usuários e categorias.
- [ ] Impedir que o administrador exclua a si mesmo ou remova o próprio acesso (e que o último administrador seja rebaixado).
- [ ] Testes automatizados (JUnit) para os DAOs e para o `SenhaUtil`.
- [ ] Exportação de dados (CSV) e metas de gastos por categoria.
- [ ] Modo claro/escuro e versão para outros sistemas (hoje o `.exe` é só para Windows; o `.jar` roda em qualquer sistema com Java 21+).

## 👨‍💻 Autor

**Gustavo Saldanha** — coordenação do grupo, migração do banco para SQLite e manutenção deste repositório.
Projeto desenvolvido no curso de Engenharia da Computação da Universidade São Judas.

- [GitHub](https://github.com/zGU5TA)
