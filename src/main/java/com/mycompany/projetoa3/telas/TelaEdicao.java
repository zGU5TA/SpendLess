package com.mycompany.projetoa3.telas;

import com.mycompany.projetoa3.ConexaoDB;
import com.mycompany.projetoa3.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaEdicao extends JFrame {

    private String cpfUsuario;
    private JTextField nomeField;
    private JTextField telefoneField;
    private JTextField emailField;

    public TelaEdicao(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;

        setTitle("Editar Dados");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);

        nomeField = new JTextField(20);
        gbc.gridx = 1;
        add(nomeField, gbc);

        // Telefone
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Telefone:"), gbc);

        telefoneField = new JTextField(20);
        gbc.gridx = 1;
        add(telefoneField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(btnSalvar, gbc);

        // Carregar dados atuais do usuário do banco e preencher campos
        carregarDadosUsuario();

        // Ação do botão Salvar
        btnSalvar.addActionListener((ActionEvent e) -> {
            salvarDados();
        });
    }

    private void carregarDadosUsuario() {
        // Buscar os dados atuais do usuário no banco pelo CPF
        Usuario usuario = ConexaoDB.buscarUsuarioPorCpf(cpfUsuario);
        if (usuario != null) {
            nomeField.setText(usuario.getNome());
            telefoneField.setText(usuario.getTelefone());
            emailField.setText(usuario.getEmail());
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do usuário.");
            dispose();
        }
    }

    private void salvarDados() {
        String nome = nomeField.getText().trim();
        String telefone = telefoneField.getText().trim();
        String email = emailField.getText().trim();

        if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        boolean atualizado = ConexaoDB.atualizarUsuario(cpfUsuario, nome, telefone, email);
        if (atualizado) {
            JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar dados.");
        }
    }
}