# 💰 SpendLess - Sistema de Gestão Financeira Pessoal

Um aplicativo desktop desenvolvido em Java para auxiliar no controlo de despesas e receitas. O projeto foca-se na usabilidade, arquitetura organizada e facilidade de execução, permitindo análises visuais através de gráficos dinâmicos.

## 🚀 Destaques e Funcionalidades
* **Zero Configuração (Seed Data):** Não exige instalação de servidores SQL. A base de dados SQLite é gerada e populada automaticamente na primeira execução. Basta abrir e usar!
* **Painel Interativo:** Dashboard com saldo, resumos mensais e gráficos de pizza gerados em tempo real.
* **CRUD Completo:** Cadastro, edição, exclusão e listagem de receitas e despesas.
* **Categorização Inteligente:** Sistema pré-carregado com 20 categorias padrão para organizar transações instantaneamente.
* **Filtros Avançados:** Pesquisa de gastos e rendas por período de datas, valores mínimos/máximos e tipo de categoria.

## 🛠️ Tecnologias e Arquitetura
* **Java (Swing):** Interface gráfica nativa e lógica de negócio.
* **SQLite (JDBC):** Base de dados embutida para total portabilidade.
* **Maven:** Gestão de dependências e construção do pacote (Fat JAR).
* **JFreeChart:** Renderização de relatórios visuais e gráficos.
* **Arquitetura DAO (Data Access Object):** Separação clara entre a interface de utilizador e a lógica de persistência de dados.

## ⚙️ Como Executar

### Opção 1: Via Executável (Recomendado)
1. Aceda à aba **[Releases](../../releases)** deste repositório.
2. Descarregue o ficheiro `SpendLess.exe`.
3. Dê um duplo clique no ficheiro. A base de dados `spendless.db` será criada automaticamente na mesma pasta e o sistema abrirá de imediato.

### Opção 2: Compilando o Código-Fonte
1. Clone este repositório:
   ```bash
   git clone https://github.com/zGU5TA/SpendLess.git
   ```
2. Aceda à pasta do projeto e faça o build com Maven:
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

* [LinkedIn](https://www.linkedin.com/in/gustasal/)
* [GitHub](https://github.com/zGU5TA)
