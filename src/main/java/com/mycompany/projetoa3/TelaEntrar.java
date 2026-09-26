package com.mycompany.projetoa3; // Certifique-se que o pacote está correto

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import javax.swing.border.EmptyBorder; 

import java.net.URL; 

public class TelaEntrar extends JFrame {

    private JTextField cpfField;
    private JPasswordField senhaField;

    public TelaEntrar() {
        setTitle("Entrar no Sistema"); 
        setSize(450, 380); // Altura pode precisar de pequeno ajuste
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false); 

        // Ícone da aplicação
        try {
            URL iconURL = getClass().getResource("/images/pig_logo.png");
            if (iconURL != null) {
                setIconImage(new ImageIcon(iconURL).getImage());
            } else {
                System.err.println("Ícone da aplicação não encontrado: /images/pig_logo.png");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o ícone da aplicação: " + e.getMessage());
        }

        // Painel principal com fundo escuro
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(new Color(45, 45, 45)); 
        painelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40)); 
        setContentPane(painelPrincipal);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título "ENTRAR"
        JLabel lblTitulo = new JLabel("ENTRAR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30)); 
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.insets = new Insets(0, 0, 25, 0); 
        painelPrincipal.add(lblTitulo, gbc);
        gbc.insets = new Insets(8, 5, 8, 5); 
        gbc.gridwidth = 1; 

        // Campo CPF
        JLabel lblCpf = criarLabel("CPF:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.weightx = 0.1; 
        painelPrincipal.add(lblCpf, gbc);

        cpfField = new JTextField(15);
        personalizarCampoTexto(cpfField);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.9; 
        painelPrincipal.add(cpfField, gbc);

        // Campo Senha
        JLabel lblSenha = criarLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.1;
        painelPrincipal.add(lblSenha, gbc);

        senhaField = new JPasswordField(15);
        personalizarCampoTexto(senhaField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.9;
        painelPrincipal.add(senhaField, gbc);

        // ***** PAINEL PARA OS BOTÕES LADO A LADO *****
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); // Espaçamento horizontal de 15 entre botões
        painelBotoes.setOpaque(false); // Para herdar o fundo do painelPrincipal

        JButton btnConfirmar = new JButton("CONFIRMAR");
        // Usar um tamanho um pouco menor para caberem lado a lado confortavelmente
        personalizarBotao(btnConfirmar, new Color(70, 130, 180), Color.WHITE, new Dimension(160, 40));
        painelBotoes.add(btnConfirmar);

        JButton btnVoltar = new JButton("Voltar"); // Texto mais curto para o botão voltar
        personalizarBotao(btnVoltar, new Color(108, 117, 125), Color.WHITE, new Dimension(160, 40));
        painelBotoes.add(btnVoltar);
        
        gbc.gridx = 0;
        gbc.gridy = 3; // Linha abaixo dos campos de texto
        gbc.gridwidth = 2; // Ocupa a largura das duas colunas
        gbc.fill = GridBagConstraints.HORIZONTAL; // Faz o painel de botões preencher horizontalmente
        gbc.anchor = GridBagConstraints.CENTER; 
        gbc.insets = new Insets(25, 0, 0, 0); // Margem superior para o painel de botões
        painelPrincipal.add(painelBotoes, gbc);
        // ***** FIM DO PAINEL PARA OS BOTÕES *****

        btnVoltar.addActionListener(e -> {
            new TelaInicial().setVisible(true); 
            dispose(); 
        });

        btnConfirmar.addActionListener(e -> {
            String cpf = cpfField.getText().trim();
            String senha = new String(senhaField.getPassword());

            if (cpf.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha CPF e senha.", "Campos Vazios", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] dadosUsuario = ConexaoDB.verificarLoginERetornarDados(cpf, senha);

            if (dadosUsuario != null && dadosUsuario.length == 2) {
                String nomeUsuario = dadosUsuario[0];
                String tipoUsuario = dadosUsuario[1];

                SessaoUsuario.setNomeUsuario(nomeUsuario);
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                if ("admin".equals(tipoUsuario)){
                    new Navegacao("Resumo admin", cpf, tipoUsuario).setVisible(true);
                } else {
                    System.out.println("tela usuario");
                    new Navegacao("Resumo", cpf, tipoUsuario).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "CPF ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15)); 
        label.setForeground(Color.LIGHT_GRAY);
        return label;
    }

    private void personalizarCampoTexto(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBackground(new Color(60, 63, 65));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(85, 85, 85), 1), 
            new EmptyBorder(10, 12, 10, 12) 
        ));
    }

    private void personalizarBotao(JButton botao, Color background, Color foreground, Dimension preferredSize) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        botao.setBackground(background);
        botao.setForeground(foreground);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        
        if (preferredSize != null) {
            botao.setPreferredSize(preferredSize);
        }
        
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(background.darker(), 1),
            new EmptyBorder(10, 20, 10, 20) 
        ));
        
        Color originalBackground = botao.getBackground();
        Color hoverBackground = originalBackground.brighter();
        Color pressedBackground = originalBackground.darker();

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(hoverBackground);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(originalBackground);
            }
             public void mousePressed(java.awt.event.MouseEvent evt) {
                botao.setBackground(pressedBackground);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (botao.contains(evt.getPoint())) { 
                    botao.setBackground(hoverBackground);
                } else {
                    botao.setBackground(originalBackground);
                }
            }
        });
    }
}