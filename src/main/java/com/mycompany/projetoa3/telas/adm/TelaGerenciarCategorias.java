package com.mycompany.projetoa3.telas.adm;

import com.mycompany.projetoa3.Categoria;
import com.mycompany.projetoa3.ConexaoDB; // Usado para interagir com o banco
import com.mycompany.projetoa3.CategoriaDAO; // Se você moveu os métodos para CategoriaDAO

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TelaGerenciarCategorias extends JPanel {

    private JTable tabelaCategorias;
    private DefaultTableModel modeloTabela;
    private JTextField campoNome;
    private JComboBox<String> comboTipo;
    private JButton btnAdicionar, btnEditar, btnExcluir, btnLimpar; // Adicionado btnLimpar
    private Categoria categoriaSelecionada = null; // Para rastrear a categoria em edição

    public TelaGerenciarCategorias() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(45, 45, 45));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Gerenciar Categorias", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        // Painel Superior: Formulário e Botões de Ação do Formulário
        JPanel painelSuperior = new JPanel(new BorderLayout(10,10));
        painelSuperior.setOpaque(false);

        // Formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setOpaque(false);
        painelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Dados da Categoria",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14), Color.LIGHT_GRAY
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNome = new JLabel("Nome da Categoria:");
        personalizarLabel(lblNome);
        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(lblNome, gbc);

        campoNome = new JTextField(20);
        personalizarTextField(campoNome);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        painelForm.add(campoNome, gbc);

        JLabel lblTipo = new JLabel("Tipo:");
        personalizarLabel(lblTipo);
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        painelForm.add(lblTipo, gbc);

        comboTipo = new JComboBox<>(new String[]{"Gasto", "Renda"});
        personalizarComboBox(comboTipo);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        painelForm.add(comboTipo, gbc);
        
        painelSuperior.add(painelForm, BorderLayout.CENTER);

        // Botões de Ação do Formulário (Adicionar, Editar, Limpar)
        JPanel painelBotoesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoesForm.setOpaque(false);

        btnAdicionar = new JButton("Adicionar Nova");
        personalizarBotaoAcao(btnAdicionar, new Color(34, 139, 34)); // Verde
        btnAdicionar.addActionListener(e -> adicionarCategoria());

        btnEditar = new JButton("Salvar Edição");
        personalizarBotaoAcao(btnEditar, new Color(255, 165, 0)); // Laranja
        btnEditar.setEnabled(false); // Habilitado ao selecionar uma categoria
        btnEditar.addActionListener(e -> editarCategoria());
        
        btnLimpar = new JButton("Limpar Campos");
        personalizarBotaoAcao(btnLimpar, new Color(108, 117, 125)); // Cinza
        btnLimpar.addActionListener(e -> limparCamposEFormulario());

        painelBotoesForm.add(btnAdicionar);
        painelBotoesForm.add(btnEditar);
        painelBotoesForm.add(btnLimpar);
        painelSuperior.add(painelBotoesForm, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH); // Adiciona o painelForm ao NORTE do layout principal

        // Tabela de Categorias
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Tipo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Não editável diretamente
            }
        };
        tabelaCategorias = new JTable(modeloTabela);
        configurarTabela();
        
        JScrollPane scrollPane = new JScrollPane(tabelaCategorias);
        scrollPane.getViewport().setBackground(new Color(55, 55, 55));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // Botão Excluir (separado, abaixo da tabela ou ao lado)
        JPanel painelExcluir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelExcluir.setOpaque(false);
        btnExcluir = new JButton("Excluir Selecionada");
        personalizarBotaoAcao(btnExcluir, new Color(220, 53, 69)); // Vermelho
        btnExcluir.setEnabled(false); // Habilitado ao selecionar uma categoria
        btnExcluir.addActionListener(e -> excluirCategoria());
        painelExcluir.add(btnExcluir);
        add(painelExcluir, BorderLayout.SOUTH);


        // Eventos
        carregarCategorias();

        tabelaCategorias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaCategorias.getSelectedRow();
                if (linhaSelecionada != -1) {
                    int idCategoria = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
                    // Usar CategoriaDAO se os métodos estiverem lá, senão ConexaoDB
                    // categoriaSelecionada = ConexaoDB.buscarCategoriaPorId(idCategoria); // Exemplo
                    // Para este exemplo, vamos buscar da lista carregada para evitar nova consulta DB apenas para popular form
                    // Isso assume que o ID na tabela é confiável.
                    // Se CategoriaDAO não existir, os métodos de ConexaoDB devem ser usados.
                    // Para simplificar, vou recriar o objeto Categoria com base nos dados da tabela.
                    String nome = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
                    String tipoStr = (String) modeloTabela.getValueAt(linhaSelecionada, 2);
                    int tipoInt = tipoStr.equals("Gasto") ? 1 : 2;
                    
                    categoriaSelecionada = new Categoria(idCategoria, nome, tipoInt);


                    if (categoriaSelecionada != null) {
                        campoNome.setText(categoriaSelecionada.getNome());
                        comboTipo.setSelectedItem(categoriaSelecionada.getTipo() == 1 ? "Gasto" : "Renda");
                        btnEditar.setEnabled(true);
                        btnExcluir.setEnabled(true);
                        btnAdicionar.setEnabled(false); // Desabilita Adicionar ao selecionar para editar
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

    private void personalizarComboBox(JComboBox comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(new Color(60, 63, 65));
        comboBox.setForeground(Color.WHITE);
        // Para o popup do ComboBox, pode ser necessário um renderer customizado para o tema escuro
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
        tabelaCategorias.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaCategorias.setRowHeight(25);
        tabelaCategorias.setGridColor(Color.GRAY);
        tabelaCategorias.setBackground(new Color(55, 55, 55));
        tabelaCategorias.setForeground(Color.WHITE);
        tabelaCategorias.setSelectionBackground(new Color(70, 130, 180));
        tabelaCategorias.setSelectionForeground(Color.WHITE);

        JTableHeader header = tabelaCategorias.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(60, 63, 65));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Centralizar texto nas células
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelaCategorias.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        tabelaCategorias.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Tipo

        // Esconder coluna ID se preferir, mas é útil para debug e seleção
        // tabelaCategorias.getColumnModel().getColumn(0).setMaxWidth(50);
    }


    private void carregarCategorias() {
        modeloTabela.setRowCount(0); // Limpa tabela
        // Usar CategoriaDAO se os métodos estiverem lá, senão ConexaoDB
        List<Categoria> categorias = CategoriaDAO.listarCategorias(); // Ou ConexaoDB.listarCategorias();
        if (categorias != null) {
            for (Categoria cat : categorias) {
                String tipoStr = cat.getTipo() == 1 ? "Gasto" : "Renda";
                modeloTabela.addRow(new Object[]{cat.getIdCategoria(), cat.getNome(), tipoStr});
            }
        }
    }
    
    private void limparCamposEFormulario() {
        campoNome.setText("");
        comboTipo.setSelectedIndex(0);
        tabelaCategorias.clearSelection();
        categoriaSelecionada = null;
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        btnAdicionar.setEnabled(true);
        campoNome.requestFocus();
    }

    private void adicionarCategoria() {
        String nome = campoNome.getText().trim();
        int tipo = comboTipo.getSelectedItem().equals("Gasto") ? 1 : 2;

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome da categoria.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Categoria novaCategoria = new Categoria(nome, tipo); // Construtor sem ID para nova categoria
        // Usar CategoriaDAO se os métodos estiverem lá, senão ConexaoDB
        boolean sucesso = ConexaoDB.adicionarCategoria(novaCategoria); // Ou ConexaoDB.adicionarCategoria(novaCategoria);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Categoria adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarCategorias();
            limparCamposEFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar categoria.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void editarCategoria() {
        if (categoriaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma categoria selecionada para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = campoNome.getText().trim();
        int tipo = comboTipo.getSelectedItem().equals("Gasto") ? 1 : 2;

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome da categoria.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Atualiza o objeto categoriaSelecionada
        categoriaSelecionada.setNome(nome);
        categoriaSelecionada.setTipo(tipo);

        // Usar CategoriaDAO se os métodos estiverem lá, senão ConexaoDB
        boolean sucesso = ConexaoDB.editarCategoria(categoriaSelecionada); // Ou ConexaoDB.editarCategoria(categoriaSelecionada);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Categoria editada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarCategorias();
            limparCamposEFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao editar categoria.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCategoria() {
         if (categoriaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma categoria selecionada para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir a categoria '" + categoriaSelecionada.getNome() + "'?\nIsso pode afetar gastos e rendas existentes.", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            // Usar CategoriaDAO se os métodos estiverem lá, senão ConexaoDB
            boolean sucesso = CategoriaDAO.excluirCategoria(categoriaSelecionada.getIdCategoria()); // Ou ConexaoDB.excluirCategoria(categoriaSelecionada.getIdCategoria());
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarCategorias();
                limparCamposEFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir categoria.\nPode estar em uso ou não existir.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
