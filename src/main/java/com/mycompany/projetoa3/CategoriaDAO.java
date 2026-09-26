package com.mycompany.projetoa3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    // Lista todas categorias (de gasto e renda)
    public static List<Categoria> listarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nome, tipo FROM tb_categorias";

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("tipo")
                );
                categorias.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
        }
        return categorias;
    }

    // Lista categorias filtradas por tipo (1 = gasto, 2 = renda)
    public static List<Categoria> listarCategoriasPorTipo(int tipo) {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nome, tipo FROM tb_categorias WHERE tipo = ?";

        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tipo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Categoria c = new Categoria(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("tipo")
                    );
                    categorias.add(c);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias por tipo: " + e.getMessage());
        }
        return categorias;
    }

    // Insere nova categoria
    public static boolean inserirCategoria(Categoria categoria) {
        String sql = "INSERT INTO tb_categorias (nome, tipo) VALUES (?, ?)";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getTipo());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir categoria: " + e.getMessage());
            return false;
        }
    }

    // Atualiza categoria existente
    public static boolean atualizarCategoria(Categoria categoria) {
        String sql = "UPDATE tb_categorias SET nome = ?, tipo = ? WHERE id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getTipo());
            stmt.setInt(3, categoria.getIdCategoria());

            int linhasAtualizadas = stmt.executeUpdate();
            return linhasAtualizadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar categoria: " + e.getMessage());
            return false;
        }
    }

    // Exclui categoria pelo id
    public static boolean excluirCategoria(int id) {
        String sql = "DELETE FROM tb_categorias WHERE id = ?";
        try (Connection conn = ConexaoDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasExcluidas = stmt.executeUpdate();
            return linhasExcluidas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir categoria: " + e.getMessage());
            return false;
        }
    }
}
