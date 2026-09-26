package com.mycompany.projetoa3;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ConexaoDB {

    // Cria o arquivo do banco na raiz do projeto
    private static final String URL = "jdbc:sqlite:spendless.db";

    // Conecta ao banco de dados e garante que as tabelas existem
    public static Connection conectar() {
        Connection conexao = null;
        try {
            conexao = DriverManager.getConnection(URL);
            criarTabelas(conexao); // Chama a criação automática
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco SQLite: " + e.getMessage());
        }
        return conexao;
    }

    // Método que cria as tabelas essenciais e popula as categorias
    private static void criarTabelas(Connection conn) {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS tb_usuarios ("
                + "cpf TEXT PRIMARY KEY,"
                + "nome TEXT NOT NULL,"
                + "telefone TEXT,"
                + "email TEXT,"
                + "senha TEXT NOT NULL,"
                + "tipo_usuario TEXT"
                + ");";

        String sqlCategorias = "CREATE TABLE IF NOT EXISTS tb_categorias ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "tipo INTEGER"
                + ");";

        // Corrigido: categoria_id e eh_recorrente adicionados
        String sqlGastos = "CREATE TABLE IF NOT EXISTS tb_gastos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "descricao TEXT,"
                + "valor REAL,"
                + "data TEXT,"
                + "cpf_usuario TEXT,"
                + "categoria_id INTEGER,"
                + "eh_recorrente BOOLEAN"
                + ");";

        // Corrigido: categoria_id e eh_recorrente adicionados
        String sqlRendas = "CREATE TABLE IF NOT EXISTS tb_rendas ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "descricao TEXT,"
                + "valor REAL,"
                + "data TEXT,"
                + "cpf_usuario TEXT,"
                + "categoria_id INTEGER,"
                + "eh_recorrente BOOLEAN"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            // 1. Cria as tabelas
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlCategorias);
            stmt.execute(sqlGastos);
            stmt.execute(sqlRendas);
            
            // 2. Verifica se a tabela de categorias está vazia
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM tb_categorias");
            if (rs.next() && rs.getInt("total") == 0) {
                System.out.println("Banco vazio detectado. Inserindo 20 categorias padrão...");
                
                String insereCategorias = "INSERT INTO tb_categorias (nome, tipo) VALUES "
                        // GASTOS (Tipo 1)
                        + "('Alimentação e Mercado', 1), "
                        + "('Moradia e Contas', 1), "
                        + "('Educação e Faculdade', 1), "
                        + "('Combustível e Veículo', 1), "
                        + "('Academia e Saúde', 1), "
                        + "('Lazer e Jogos', 1), "
                        + "('Assinaturas Digitais', 1), "
                        + "('Vestuário e Cuidados', 1), "
                        + "('Cartão de Crédito', 1), "
                        + "('Reserva de Emergência', 1), "
                        // RENDAS (Tipo 2)
                        + "('Salário', 2), "
                        + "('Bolsa Auxílio / Estágio', 2), "
                        + "('Freelance e Projetos', 2), "
                        + "('Rendimento de Investimentos', 2), "
                        + "('Venda de Produtos', 2), "
                        + "('Cashback', 2), "
                        + "('Mesada ou Ajuda', 2), "
                        + "('Restituição e Impostos', 2), "
                        + "('Bônus e Premiações', 2), "
                        + "('Resgate de Investimentos', 2);";
                        
                stmt.execute(insereCategorias);
                System.out.println("Categorias padrão inseridas com sucesso.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao criar/popular tabelas: " + e.getMessage());
        }
    }

    // =========================================================================
    // MÉTODOS DE USUÁRIOS
    // =========================================================================

    public static boolean inserirUsuario(String cpf, String nome, String telefone, String email, String senha) {
        String sql = "INSERT INTO tb_usuarios (cpf, nome, telefone, email, senha, tipo_usuario) VALUES (?, ?, ?, ?, ?, 'padrao')";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, nome);
            stmt.setString(3, telefone);
            stmt.setString(4, email);
            stmt.setString(5, senha);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean inserirUsuario2(Usuario usuario, String senha) {
        String sql = "INSERT INTO tb_usuarios (cpf, nome, telefone, email, senha, tipo_usuario) VALUES (?, ?, ?, ?, ?, 'padrao')";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getCpf());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, senha);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean consultarEmailCPF(String email, String cpf) {
        boolean achouEmail = false;
        boolean achouCpf = false;

        String sqlEmail = "SELECT * FROM tb_usuarios WHERE email = ?";
        String sqlCpf = "SELECT * FROM tb_usuarios WHERE cpf = ?";
        
        try (Connection conn = conectar(); PreparedStatement stmtEmail = conn.prepareStatement(sqlEmail)) {
            stmtEmail.setString(1, email);
            ResultSet rsEmail = stmtEmail.executeQuery();
            if (rsEmail.next()) {
                achouEmail = true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        try (Connection conn = conectar(); PreparedStatement stmtCpf = conn.prepareStatement(sqlCpf)) {
            stmtCpf.setString(1, cpf);
            ResultSet rsCpf = stmtCpf.executeQuery();
            if (rsCpf.next()) {
                achouCpf = true;
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return achouEmail || achouCpf;
    }
    
    public static boolean consultarEmail(String email) {
        String sql = "SELECT * FROM tb_usuarios WHERE email = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao verificar login: " + e.getMessage());
            return false;
        }
    }

    public static boolean verificarLogin(String cpf, String senha) {
        String sql = "SELECT * FROM tb_usuarios WHERE cpf = ? AND senha = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Erro ao verificar login: " + e.getMessage());
            return false;
        }
    }

    public static String verificarLoginERetornarNome(String cpf, String senha) {
        String sql = "SELECT nome FROM tb_usuarios WHERE cpf = ? AND senha = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nome");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar nome do usuário: " + e.getMessage());
        }
        return null;
    }

    public static String[] verificarLoginERetornarDados(String cpf, String senha) {
        String sql = "SELECT nome, tipo_usuario FROM tb_usuarios WHERE cpf = ? AND senha = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String tipo = rs.getString("tipo_usuario");
                return new String[] { nome, tipo };
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar dados do usuário: " + e.getMessage());
        }
        return null;
    }

    public static Usuario buscarUsuarioPorCpf(String cpf) {
        String sql = "SELECT cpf, nome, telefone, email, tipo_usuario FROM tb_usuarios WHERE cpf = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("tipo_usuario")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
        }
        return null;
    }

    public static boolean atualizarUsuario(String cpf, String nome, String telefone, String email) {
        String sql = "UPDATE tb_usuarios SET nome = ?, telefone = ?, email = ? WHERE cpf = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, email);
            stmt.setString(4, cpf);
            int linhasAtualizadas = stmt.executeUpdate();
            return linhasAtualizadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

    public static boolean excluirUsuarioPorCpf(String cpf) {
        String sql = "DELETE FROM tb_usuarios WHERE cpf = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
            return false;
        }
    }

    public static List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT cpf, nome, telefone, email, tipo_usuario FROM tb_usuarios";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("tipo_usuario")
                );
                usuarios.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar usuários: " + e.getMessage());
        }
        return usuarios;
    }
    
    public static boolean editarUsuario(Usuario usuario) {
        String sql = "UPDATE tb_usuarios SET nome = ?, email = ?, tipo_usuario = ? WHERE cpf = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTipo());
            stmt.setString(4, usuario.getCpf());
            int linhasAtualizadas = stmt.executeUpdate();
            return linhasAtualizadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao editar usuário: " + e.getMessage());
            return false;
        }
    }

    public static boolean excluirUsuario(String cpf) {
        return excluirUsuarioPorCpf(cpf);
    }

    // =========================================================================
    // MÉTODOS DE CATEGORIAS
    // =========================================================================

    public static List<Categoria> listarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nome, tipo FROM tb_categorias";

        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int tipo = rs.getInt("tipo");
                categorias.add(new Categoria(id, nome, tipo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categorias;
    }

    public static boolean adicionarCategoria(Categoria categoria) {
        String sql = "INSERT INTO tb_categorias (nome, tipo) VALUES (?, ?)";

        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getTipo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean editarCategoria(Categoria categoria) {
        String sql = "UPDATE tb_categorias SET nome = ?, tipo = ? WHERE id = ?";

        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getTipo());
            stmt.setInt(3, categoria.getIdCategoria());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean excluirCategoria(int id) {
        String sql = "DELETE FROM tb_categorias WHERE id = ?";

        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}