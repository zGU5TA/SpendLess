package com.mycompany.projetoa3; // Certifique-se que o pacote está correto

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL; // Para carregar o ícone da janela

public class TelaCadastrar extends JFrame {

    private JTextField nomeField, cpfField, telefoneField, emailField;
    private JPasswordField senhaField, confirmarSenhaField;

    public TelaCadastrar() {
        setTitle("Criar Nova Conta");
        // Aumentar um pouco a altura para acomodar melhor o espaçamento
        setSize(500, 580); 
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
        JPanel painelFundo = new JPanel(new BorderLayout(0, 20)); // Espaçamento vertical entre título, form e botões
        painelFundo.setBackground(new Color(45, 45, 45));
        painelFundo.setBorder(new EmptyBorder(30, 40, 30, 40)); // Margens externas
        setContentPane(painelFundo);

        // Título
        JLabel lblTitulo = new JLabel("CRIAR CONTA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);
        painelFundo.add(lblTitulo, BorderLayout.NORTH);

        // Painel do Formulário com GridBagLayout
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setOpaque(false); // Para herdar o fundo do painelFundo
        // Adicionar uma borda com título para o formulário
        painelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), // Sem borda externa visível
            "Dados Pessoais",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.LIGHT_GRAY
        ));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Espaçamento entre componentes
        gbc.anchor = GridBagConstraints.WEST; // Alinhamento padrão dos labels

        // Linha 0: Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2; // Peso para o label
        painelFormulario.add(criarRotulo("Nome Completo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8; // Peso para o campo
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nomeField = new JTextField(20);
        personalizarCampoTexto(nomeField);
        painelFormulario.add(nomeField, gbc);

        // Linha 1: CPF
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(criarRotulo("CPF (somente números):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.8; gbc.fill = GridBagConstraints.HORIZONTAL;
        cpfField = new JTextField(11);
        personalizarCampoTexto(cpfField);
        painelFormulario.add(cpfField, gbc);

        // Linha 2: Telefone
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(criarRotulo("Telefone (com DDD):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.8; gbc.fill = GridBagConstraints.HORIZONTAL;
        telefoneField = new JTextField(15);
        personalizarCampoTexto(telefoneField);
        painelFormulario.add(telefoneField, gbc);

        // Linha 3: Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.2; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(criarRotulo("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.8; gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(20);
        personalizarCampoTexto(emailField);
        painelFormulario.add(emailField, gbc);

        // Linha 4: Senha
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.2; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(criarRotulo("Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.8; gbc.fill = GridBagConstraints.HORIZONTAL;
        senhaField = new JPasswordField(20);
        personalizarCampoTexto(senhaField);
        painelFormulario.add(senhaField, gbc);

        // Linha 5: Confirmar Senha
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.2; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(criarRotulo("Confirmar Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 0.8; gbc.fill = GridBagConstraints.HORIZONTAL;
        confirmarSenhaField = new JPasswordField(20);
        personalizarCampoTexto(confirmarSenhaField);
        painelFormulario.add(confirmarSenhaField, gbc);

        painelFundo.add(painelFormulario, BorderLayout.CENTER);

        // Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        painelBotoes.setOpaque(false);

        JButton btnCadastrar = new JButton("CADASTRAR");
        personalizarBotao(btnCadastrar, new Color(34, 139, 34), Color.WHITE);
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica de cadastro (mantida da versão anterior)
                String nome = nomeField.getText().trim();
                String cpf = cpfField.getText().trim();
                String telefone = telefoneField.getText().trim();
                String email = emailField.getText().trim();
                String senha = new String(senhaField.getPassword());
                String confirmarSenha = new String(confirmarSenhaField.getPassword());

                if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                    JOptionPane.showMessageDialog(TelaCadastrar.this, "Todos os campos são obrigatórios.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!senha.equals(confirmarSenha)) {
                    JOptionPane.showMessageDialog(TelaCadastrar.this, "As senhas não coincidem.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (cpf.length() != 11 || !cpf.matches("\\d+")) {
                    JOptionPane.showMessageDialog(TelaCadastrar.this, "CPF inválido. Deve conter 11 números.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (ConexaoDB.consultarEmailCPF(email, cpf)) {
                     JOptionPane.showMessageDialog(TelaCadastrar.this, "CPF ou Email já cadastrado.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean inserido = ConexaoDB.inserirUsuario(cpf, nome, telefone, email, senha);
                if (inserido) {
                    JOptionPane.showMessageDialog(TelaCadastrar.this, "Conta criada com sucesso!", "Cadastro Concluído", JOptionPane.INFORMATION_MESSAGE);
                    new TelaEntrar().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(TelaCadastrar.this, "Erro ao criar conta. Tente novamente.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnVoltar = new JButton("VOLTAR");
        personalizarBotao(btnVoltar, new Color(108, 117, 125), Color.WHITE);
        btnVoltar.addActionListener(e -> {
            new TelaInicial().setVisible(true);
            dispose();
        });

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnVoltar);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
    }

    private JLabel criarRotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
            new EmptyBorder(8, 10, 8, 10)
        ));
    }

    private void personalizarBotao(JButton botao, Color background, Color foreground) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setBackground(background);
        botao.setForeground(foreground);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Dimension btnSize = new Dimension(180, 40); // Tamanho consistente para os botões
        botao.setPreferredSize(btnSize);
        
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
