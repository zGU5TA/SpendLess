package com.mycompany.projetoa3.telas.adm;

import javax.swing.*;
import javax.swing.border.EmptyBorder; // Para adicionar margens
import java.awt.*;
import java.awt.event.ActionListener;

public class TelaAdmin extends JPanel {

    public TelaAdmin(String cpfUsuario, ActionListener listenerTrocaTela) {
        setLayout(new BorderLayout(20, 20)); // Adiciona espaçamento entre as regiões do BorderLayout
        setBackground(new Color(45, 45, 45)); // Um fundo escuro elegante
        setBorder(new EmptyBorder(30, 30, 30, 30)); // Margem geral para a tela

        // Título
        JLabel titulo = new JLabel("Área Administrativa", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Fonte moderna e maior
        titulo.setForeground(Color.WHITE); // Cor do texto do título
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0)); // Margem inferior para o título
        add(titulo, BorderLayout.NORTH);

        // Painel para os botões
        JPanel painelBotoes = new JPanel(new GridBagLayout()); // Usar GridBagLayout para mais controle
        painelBotoes.setOpaque(false); // Para herdar o fundo do painel principal
        // painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Removido, GridBagInsets fará o espaçamento

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Cada botão ocupa uma linha inteira
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz o botão preencher horizontalmente
        gbc.insets = new Insets(10, 0, 10, 0); // Espaçamento vertical entre botões

        JButton btnUsuarios = criarBotaoAdmin("Gerenciar Usuários", listenerTrocaTela, "GerenciarUsuarios");
        JButton btnCategorias = criarBotaoAdmin("Gerenciar Categorias", listenerTrocaTela, "GerenciarCategorias");

        painelBotoes.add(btnUsuarios, gbc);
        painelBotoes.add(btnCategorias, gbc);

        // Painel para centralizar o painelBotoes
        JPanel painelCentralizador = new JPanel(new GridBagLayout());
        painelCentralizador.setOpaque(false);
        painelCentralizador.add(painelBotoes); // Adiciona o painel de botões ao centro

        add(painelCentralizador, BorderLayout.CENTER);
    }

    private JButton criarBotaoAdmin(String texto, ActionListener listener, String actionCommand) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setPreferredSize(new Dimension(280, 60)); // Tamanho maior para os botões
        botao.setBackground(new Color(70, 130, 180)); // Um azul para os botões
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 110, 160), 1), // Borda sutil
            new EmptyBorder(10, 20, 10, 20) // Padding interno
        ));
        botao.setActionCommand(actionCommand);
        botao.addActionListener(listener);

        // Efeito Hover
        Color originalBackground = botao.getBackground();
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(originalBackground.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(originalBackground);
            }
        });
        return botao;
    }
}