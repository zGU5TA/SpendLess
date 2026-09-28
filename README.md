# 💰 SpendLess - Sistema de Gestão Financeira Pessoal

Um aplicativo desktop desenvolvido em Java para auxiliar no controle de despesas e receitas. O projeto foca em usabilidade, arquitetura organizada e facilidade de execução, permitindo análises visuais através de gráficos dinâmicos.

## ⚠️ Pré-requisitos
Para executar este sistema no seu computador, é necessário ter o **Java (JRE/JDK) versão 21** ou superior instalado.
* [Baixe o Java 21 aqui (Adoptium/Eclipse Temurin)](https://adoptium.net/pt-BR/temurin/releases/?version=21)

## 🚀 Destaques e Funcionalidades

* **Zero Configuração (Seed Data):** Não exige instalação de servidores SQL. O banco de dados SQLite é gerado e populado automaticamente na primeira execução. Basta abrir e usar!

* **Painel Interativo:** Dashboard com saldo, resumos mensais e gráficos de pizza gerados em tempo real.

* **CRUD Completo:** Cadastro, edição, exclusão e listagem de receitas e despesas.

* **Categorização Inteligente:** Sistema pré-carregado com 20 categorias padrão para organizar transações instantaneamente.

* **Filtros Avançados:** Telas de pesquisa de gastos e rendas por período de datas, valores mínimos/máximos e tipo de categoria.

## 🛠️ Tecnologias e Arquitetura

* **Java 21 (Swing):** Interface gráfica nativa e lógica de negócios.

* **SQLite (JDBC):** Banco de dados embutido para total portabilidade.

* **Maven:** Gerenciamento de dependências e construção do pacote (Fat JAR).

* **JFreeChart:** Renderização de relatórios visuais e gráficos.

* **Arquitetura DAO (Data Access Object):** Separação clara entre a interface do usuário e a lógica de persistência de dados.

## ⚙️ Como Executar

### Opção 1: Via Executável (Recomendado)

1. Acesse a aba [**Releases**](../../releases) deste repositório.

2. Baixe o arquivo `SpendLess.exe`.

3. Dê um duplo clique no arquivo. O banco de dados `spendless.db` será criado automaticamente na mesma pasta e o sistema abrirá em seguida.

### Opção 2: Compilando o Código-Fonte

1. Clone este repositório:

   ```bash
   git clone https://github.com/zGU5TA/SpendLess.git
   ```

2. Acesse a pasta do projeto e faça o build com Maven:

   ```bash
   cd SpendLess
   mvn clean package
   ```

3. Execute o pacote gerado na pasta target:

   ```bash
   java -jar target/SpendLess-1.0.jar
   ```

## 👨‍💻 Autor

**Gustavo Saldanha**
*Projeto desenvolvido como parte do curso de Engenharia da Computação na Universidade São Judas.*

* [LinkedIn](https://linkedin.com/in/seu-perfil-aqui)
* [GitHub](https://github.com/zGU5TA)
