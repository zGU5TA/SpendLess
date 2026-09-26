package com.mycompany.projetoa3.telas.gasto;

// import java.sql.Date; // Não é usado diretamente aqui, mas pode ser em Gasto.java
import com.mycompany.projetoa3.Categoria; // Usado para obter nome da categoria se não vier no Gasto
import com.mycompany.projetoa3.CategoriaDAO; // Usado para obter nome da categoria se não vier no Gasto
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
// import java.util.HashMap; // Removido se o sorter customizado complexo não for usado
import java.util.List;
// import java.util.Map; // Removido se o sorter customizado complexo não for usado
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.text.SimpleDateFormat; // Para formatar data

public class TelaGastos extends JPanel {

    private JTable tabelaGastos;
    private DefaultTableModel modeloTabela;
    private String cpfUsuario;

    public TelaGastos(String cpfUsuario) {
        this.setBackground(new Color(45, 45, 45));
        this.cpfUsuario = cpfUsuario;
        initComponents();
        carregarGastos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10,10)); // Adiciona espaçamento geral
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); // Margem externa

        // Modelo da tabela agora com a coluna "Recorrente"
        modeloTabela = new DefaultTableModel(      
            new Object[]{"ID", "Data", "Descrição", "Valor", "Categoria", "Recorrente"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Nenhuma célula é editável diretamente na tabela
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Define os tipos de dados das colunas para ordenação e renderização corretas
                switch (columnIndex) {
                    case 0: return Integer.class; // ID
                    case 1: return java.util.Date.class; // Data (para ordenação correta)
                    case 3: return Double.class;  // Valor
                    case 5: return String.class; // Coluna Recorrente exibirá "Sim" ou "Não"
                    default: return String.class; // Descrição, Categoria
                }
            }
        };

        tabelaGastos = new JTable(modeloTabela);
        tabelaGastos.setBackground(new Color(45, 45, 45));
        tabelaGastos.setRowHeight(25); // Altura da linha
        tabelaGastos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabelaGastos.setGridColor(new Color(220,220,220)); // Cor da grade mais suave
        tabelaGastos.setFillsViewportHeight(true); // Para que a tabela preencha a altura do scrollpane
        tabelaGastos.setAutoCreateRowSorter(true); // Habilita ordenação padrão ao clicar no header

        // Personalizar cabeçalho da tabela
        JTableHeader header = tabelaGastos.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180)); // Azul aço (cor de exemplo para gastos)
        header.setForeground(Color.BLACK);
        header.setReorderingAllowed(false); // Impede que o usuário reordene as colunas
        header.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); // Padding no header


        // Renderer customizado para zebrado, formatação de data/valor e cores
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(173, 216, 230)); // Azul claro para linha selecionada
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(row % 2 == 0 ? new Color(25, 25, 25) : new Color(45, 45, 45)); // Efeito zebrado
                    c.setForeground(Color.WHITE);
                }
                
                setHorizontalAlignment(SwingConstants.LEFT); // Alinhamento padrão à esquerda

                if (column == 3 && value instanceof Number) { // Coluna "Valor"
                    setText(String.format("R$ %.2f", ((Number) value).doubleValue()));
                    c.setForeground(Color.RED); // Gastos em vermelho
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else if (column == 1 && value instanceof java.util.Date) { // Coluna "Data"
                     setText(new SimpleDateFormat("dd/MM/yyyy").format(value)); // Formata a data
                     setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == 5) { // Coluna "Recorrente"
                    setHorizontalAlignment(SwingConstants.CENTER); // Centraliza "Sim" ou "Não"
                }
                return c;
            }
        };
        
        // Aplica o renderer customizado a todas as colunas
        for (int i = 0; i < tabelaGastos.getColumnCount(); i++) {
            tabelaGastos.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Esconder coluna ID (coluna 0)
        tabelaGastos.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaGastos.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaGastos.getColumnModel().getColumn(0).setWidth(0);

        // Definir larguras preferenciais para outras colunas para melhor visualização
        tabelaGastos.getColumnModel().getColumn(1).setPreferredWidth(100); // Data
        tabelaGastos.getColumnModel().getColumn(2).setPreferredWidth(250); // Descrição
        tabelaGastos.getColumnModel().getColumn(3).setPreferredWidth(120); // Valor
        tabelaGastos.getColumnModel().getColumn(4).setPreferredWidth(150); // Categoria
        tabelaGastos.getColumnModel().getColumn(5).setPreferredWidth(100); // Recorrente

        // Sorter (se precisar de ordenação customizada, pode ser adicionado aqui)
        // TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabela);
        // tabelaGastos.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabelaGastos);
        scroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Borda sutil no scrollpane

        // Botões de Ação
        JButton btnAdicionar = new JButton("Adicionar Gasto");
        personalizarBotao(btnAdicionar, new Color(70, 130, 180), new Color(60,120,170));
        btnAdicionar.addActionListener(e -> abrirAdicionarGasto());

        JButton btnEditar = new JButton("Editar Gasto");
        personalizarBotao(btnEditar, new Color(255, 165, 0), new Color(235,145,0)); // Laranja para editar
        btnEditar.addActionListener(e -> editarGastoSelecionado());

        JButton btnExcluir = new JButton("Excluir Gasto");
        personalizarBotao(btnExcluir, new Color(220, 20, 60), new Color(200,10,50)); // Vermelho para excluir
        btnExcluir.addActionListener(e -> excluirGastoSelecionado());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // FlowLayout para botões
        painelBotoes.setBackground(new Color(45, 45, 45));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(scroll, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    private void personalizarBotao(JButton botao, Color corFundo, Color corBorda) {
        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setPreferredSize(new Dimension(150, 35));
         botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corBorda,1), // Borda sutil
            BorderFactory.createEmptyBorder(5,15,5,15) // Padding interno
        ));
    }

    private void carregarGastos() {
        modeloTabela.setRowCount(0); // Limpa a tabela antes de carregar novos dados
        List<Gasto> lista = GastoDAO.listarGastosPorUsuario(cpfUsuario);
        for (Gasto g : lista) {
            // Adiciona os dados, incluindo o novo campo "ehRecorrente" convertido para String
            modeloTabela.addRow(new Object[]{
                g.getId(),
                g.getDataGasto(),
                g.getDescricao(),
                g.getValor(),
                g.getNomeCategoria(), // Assumindo que GastoDAO já popula este campo com o nome
                g.isEhRecorrente() ? "Sim" : "Não" // Converte boolean para String "Sim" ou "Não"
            });
        }
    }

    public void abrirAdicionarGasto() {
        JFrame framePai = (JFrame) SwingUtilities.getWindowAncestor(this);
        // Passa o método 'atualizarTabela' como callback para ser chamado após adição bem-sucedida
        AdicionarGasto telaAdd = new AdicionarGasto(framePai, cpfUsuario, this::atualizarTabela);
        telaAdd.setVisible(true);
    }

    public void atualizarTabela() {
        carregarGastos(); // Recarrega os dados da tabela
    }

    private void excluirGastoSelecionado() {
        int linhaSelecionadaView = tabelaGastos.getSelectedRow(); // Índice da linha na visão da tabela (pode ser diferente do modelo devido à ordenação)
        if (linhaSelecionadaView >= 0) {
            // Converte o índice da view para o índice do modelo, caso a tabela esteja ordenada
            int linhaSelecionadaModel = tabelaGastos.convertRowIndexToModel(linhaSelecionadaView);
            int idGasto = (int) modeloTabela.getValueAt(linhaSelecionadaModel, 0); // Coluna ID é a 0 no modelo

            int confirma = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente excluir o gasto selecionado?",
                    "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirma == JOptionPane.YES_OPTION) {
                if (GastoDAO.excluirGasto(idGasto)) {
                    JOptionPane.showMessageDialog(this, "Gasto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    atualizarTabela(); // Recarrega a tabela
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir gasto. Verifique o console.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um gasto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarGastoSelecionado() {
        int linhaSelecionadaView = tabelaGastos.getSelectedRow();
        if (linhaSelecionadaView == -1) { // Nenhuma linha selecionada
            JOptionPane.showMessageDialog(this, "Selecione um gasto para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int linhaSelecionadaModel = tabelaGastos.convertRowIndexToModel(linhaSelecionadaView);
        int idGasto = (int) modeloTabela.getValueAt(linhaSelecionadaModel, 0);

        Gasto gasto = GastoDAO.buscarGastoPorld(idGasto); // Usa o nome do método como no PDF
        if (gasto == null) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do gasto para edição. Verifique o console.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame framePai = (JFrame) SwingUtilities.getWindowAncestor(this);
        // Assume que EditarGasto.java existe e está configurado para receber o objeto Gasto
        // e o callback onSuccess. EditarGasto.java também precisará lidar com 'ehRecorrente'.
        EditarGasto editarDialog = new EditarGasto(framePai, gasto, this::atualizarTabela);
        editarDialog.setVisible(true);
    }
}
