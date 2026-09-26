package com.mycompany.projetoa3.telas.adm;

import com.mycompany.projetoa3.Usuario;
import com.mycompany.projetoa3.ConexaoDB; // Usado para interagir com o banco

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TelaGerenciarUsuarios extends JPanel {

    private JTable tabelaUsuarios;
    private DefaultTableModel modeloTabela;
    private JTextField campoNome, campoEmail, campoTipo; // campoTelefone removido do form, pois não é editável aqui
    private JButton btnEditar, btnExcluir, btnLimpar;
    private Usuario usuarioSelecionado = null; // Para rastrear o usuário em edição

    public TelaGerenciarUsuarios() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(45, 45, 45));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Gerenciar Usuários", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);
        
        // Painel Superior: Formulário e Botões de Ação do Formulário
        JPanel painelSuperior = new JPanel(new BorderLayout(10,10));
        painelSuperior.setOpaque(false);

        // Formulário de Edição
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setOpaque(false);
        painelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Dados do Usuário (Edição)",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14), Color.LIGHT_GRAY
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNome = new JLabel("Nome:");
        personalizarLabel(lblNome);
        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(lblNome, gbc);

        campoNome = new JTextField(20);
        personalizarTextField(campoNome);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        painelForm.add(campoNome, gbc);

        JLabel lblEmail = new JLabel("Email:");
        personalizarLabel(lblEmail);
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painelForm.add(lblEmail, gbc);

        campoEmail = new JTextField(20);
        personalizarTextField(campoEmail);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        painelForm.add(campoEmail, gbc);
        
        JLabel lblTipo = new JLabel("Tipo (admin/padrao):");
        personalizarLabel(lblTipo);
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painelForm.add(lblTipo, gbc);

        campoTipo = new JTextField(20); // Campo de texto para tipo, pois pode ser "admin" ou "padrao"
        personalizarTextField(campoTipo);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        painelForm.add(campoTipo, gbc);
        
        painelSuperior.add(painelForm, BorderLayout.CENTER);

        // Botões de Ação do Formulário (Editar, Limpar)
        JPanel painelBotoesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoesForm.setOpaque(false);

        btnEditar = new JButton("Salvar Edição");
        personalizarBotaoAcao(btnEditar, new Color(255, 165, 0)); // Laranja
        btnEditar.setEnabled(false); // Habilitado ao selecionar um usuário
        btnEditar.addActionListener(e -> editarUsuario());
        
        btnLimpar = new JButton("Limpar Campos");
        personalizarBotaoAcao(btnLimpar, new Color(108, 117, 125)); // Cinza
        btnLimpar.addActionListener(e -> limparCamposEFormulario());

        painelBotoesForm.add(btnEditar);
        painelBotoesForm.add(btnLimpar);
        painelSuperior.add(painelBotoesForm, BorderLayout.SOUTH);
        
        add(painelSuperior, BorderLayout.NORTH);


        // Tabela de Usuários
        modeloTabela = new DefaultTableModel(new Object[]{"CPF", "Nome", "Email", "Telefone", "Tipo"}, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Não editável diretamente
            }
        };
        tabelaUsuarios = new JTable(modeloTabela);
        configurarTabela();
        
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        scrollPane.getViewport().setBackground(new Color(55, 55, 55));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // Botão Excluir (separado)
        JPanel painelExcluir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelExcluir.setOpaque(false);
        btnExcluir = new JButton("Excluir Selecionado");
        personalizarBotaoAcao(btnExcluir, new Color(220, 53, 69)); // Vermelho
        btnExcluir.setEnabled(false); // Habilitado ao selecionar um usuário
        btnExcluir.addActionListener(e -> excluirUsuario());
        painelExcluir.add(btnExcluir);
        add(painelExcluir, BorderLayout.SOUTH);

        // Eventos
        carregarUsuarios();

        tabelaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaUsuarios.getSelectedRow();
                if (linhaSelecionada != -1) {
                    String cpf = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
                    usuarioSelecionado = ConexaoDB.buscarUsuarioPorCpf(cpf); // Busca o objeto completo

                    if (usuarioSelecionado != null) {
                        campoNome.setText(usuarioSelecionado.getNome());
                        campoEmail.setText(usuarioSelecionado.getEmail());
                        campoTipo.setText(usuarioSelecionado.getTipo());
                        // campoTelefone não é editável aqui, apenas exibido na tabela
                        btnEditar.setEnabled(true);
                        btnExcluir.setEnabled(true);
                    }
                }
            }
        });
    }
    
    private void personalizarLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Color.LIGHT_GRAY);
    }

    private void personalizarTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBackground(new Color(60, 63, 65));
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
    }
    
    private void personalizarBotaoAcao(JButton botao, Color corFundo) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setPreferredSize(new Dimension(150, 35));
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corFundo.darker(), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
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
    }

    private void configurarTabela() {
        tabelaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaUsuarios.setRowHeight(25);
        tabelaUsuarios.setGridColor(Color.GRAY);
        tabelaUsuarios.setBackground(new Color(55, 55, 55));
        tabelaUsuarios.setForeground(Color.WHITE);
        tabelaUsuarios.setSelectionBackground(new Color(70, 130, 180));
        tabelaUsuarios.setSelectionForeground(Color.WHITE);

        JTableHeader header = tabelaUsuarios.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(60, 63, 65));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Esconder coluna CPF se preferir, mas é o identificador principal
        // tabelaUsuarios.getColumnModel().getColumn(0).setMaxWidth(100); 
    }

    private void carregarUsuarios() {
        modeloTabela.setRowCount(0);
        List<Usuario> usuarios = ConexaoDB.listarUsuarios();
        if (usuarios != null) {
            for (Usuario u : usuarios) {
                modeloTabela.addRow(new Object[]{u.getCpf(), u.getNome(), u.getEmail(), u.getTelefone(), u.getTipo()});
            }
        }
    }
    
    private void limparCamposEFormulario() {
        campoNome.setText("");
        campoEmail.setText("");
        campoTipo.setText("");
        tabelaUsuarios.clearSelection();
        usuarioSelecionado = null;
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        campoNome.requestFocus();
    }

    private void editarUsuario() {
        if (usuarioSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Nenhum usuário selecionado para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String tipo = campoTipo.getText().trim().toLowerCase(); // Normaliza para minúsculas

        if (nome.isEmpty() || email.isEmpty() || tipo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos (Nome, Email, Tipo).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!tipo.equals("admin") && !tipo.equals("padrao")) {
            JOptionPane.showMessageDialog(this, "O tipo de usuário deve ser 'admin' ou 'padrao'.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Atualiza o objeto usuarioSelecionado
        // O telefone não é editável neste formulário, então usamos o valor original.
        // O CPF também não é editável.
        Usuario usuarioAtualizado = new Usuario(
            usuarioSelecionado.getCpf(), 
            nome, 
            usuarioSelecionado.getTelefone(), // Mantém telefone original
            email, 
            tipo
        );

        if (ConexaoDB.editarUsuario(usuarioAtualizado)) { // Assume que ConexaoDB.editarUsuario aceita o objeto Usuario
            JOptionPane.showMessageDialog(this, "Usuário editado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarUsuarios();
            limparCamposEFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao editar usuário.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirUsuario() {
        if (usuarioSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Nenhum usuário selecionado para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Adicionar verificação para não permitir excluir o próprio admin logado (se aplicável)
        // if (usuarioSelecionado.getCpf().equals(cpfUsuarioLogado) && usuarioSelecionado.getTipo().equals("admin")) {
        //     JOptionPane.showMessageDialog(this, "Não é possível excluir o administrador atualmente logado.", "Ação Não Permitida", JOptionPane.ERROR_MESSAGE);
        //     return;
        // }

        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir o usuário '" + usuarioSelecionado.getNome() + "'?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            if (ConexaoDB.excluirUsuario(usuarioSelecionado.getCpf())) { // Assume que ConexaoDB.excluirUsuario usa CPF
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarUsuarios();
                limparCamposEFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir usuário.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
