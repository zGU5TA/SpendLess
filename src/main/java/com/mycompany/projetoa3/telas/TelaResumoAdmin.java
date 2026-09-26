package com.mycompany.projetoa3.telas;

import com.mycompany.projetoa3.telas.gasto.Gasto;
import com.mycompany.projetoa3.telas.gasto.GastoDAO;
import com.mycompany.projetoa3.telas.renda.Renda;
import com.mycompany.projetoa3.telas.renda.RendaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TelaResumoAdmin extends JPanel{
    private final DecimalFormat df = new DecimalFormat("R$ #,##0.00");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    
    
    private DefaultTableModel modeloUltimasTransacoes;
    
    private JLabel lblDataAtual; // Tornar acessível para atualizar a data também
    private JTextField txtFiltroCpf; // Campo para CPF


    public TelaResumoAdmin() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.DARK_GRAY.darker());

        initComponents();
        System.out.println(RendaDAO.listarRenda());
        carregarDados(); 
    }
    
    private void initComponents() {
        // Painel Superior (Data e Botão Atualizar)
        JPanel painelSuperior = new JPanel(new BorderLayout(10,0)); // Espaçamento horizontal entre data e botão
        painelSuperior.setOpaque(false);
        
        lblDataAtual = new JLabel("DATA: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblDataAtual.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblDataAtual.setForeground(Color.WHITE);
        painelSuperior.add(lblDataAtual, BorderLayout.WEST);
        
        txtFiltroCpf = new JTextField();
        txtFiltroCpf.setToolTipText("Digite o CPF para filtrar");
        txtFiltroCpf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtFiltroCpf.setColumns(10);
        txtFiltroCpf.setMaximumSize(txtFiltroCpf.getPreferredSize()); // Evita esticar

        painelSuperior.add(txtFiltroCpf, BorderLayout.CENTER);


        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnAtualizar.setToolTipText("Recarregar informações financeiras");
        // Estilização simples para o botão de atualizar
        btnAtualizar.setBackground(new Color(90, 90, 90));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarDados(); // Chama o método para recarregar todos os dados
            }
        });
        
        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarDados();
            }
        });

        painelSuperior.add(btnAtualizar, BorderLayout.EAST);
        
        add(painelSuperior, BorderLayout.NORTH);

        // Painel Principal (Colunas)
            JPanel painelPrincipal = new JPanel(new GridLayout(1, 3, 10, 10));
            painelPrincipal.setOpaque(false);

            JPanel colunaLista = criarColunaLista();      // DESCRICAO
            
            painelPrincipal.add(colunaLista);

            add(painelPrincipal, BorderLayout.CENTER);
    }

    private JPanel criarColunaLista() {
        JPanel coluna = new JPanel(new BorderLayout(0, 10));
        coluna.setOpaque(false);
        coluna.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTituloTransacoes = new JLabel("TRANSAÇÕES", SwingConstants.CENTER);
        lblTituloTransacoes.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTituloTransacoes.setForeground(Color.WHITE);
        coluna.add(lblTituloTransacoes, BorderLayout.NORTH);

        modeloUltimasTransacoes = new DefaultTableModel(new Object[]{"Cpf","Data", "Categoria", "Valor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
    JTable tabelaUltimasTransacoes = new JTable(modeloUltimasTransacoes);
    tabelaUltimasTransacoes.setFont(new Font("SansSerif", Font.PLAIN, 12));
    tabelaUltimasTransacoes.setRowHeight(20);
    tabelaUltimasTransacoes.setOpaque(false); 
    tabelaUltimasTransacoes.setShowGrid(false);
    tabelaUltimasTransacoes.setForeground(Color.LIGHT_GRAY);
    
    tabelaUltimasTransacoes.setCellSelectionEnabled(true);
    tabelaUltimasTransacoes.setRowSelectionAllowed(true);

    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Seleção
            if (isSelected) {
                c.setBackground(new Color(144, 238, 144)); // Verde claro para seleção
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(row % 2 == 0 ? new Color(25, 25, 25) : new Color(45, 45, 45)); // Zebra
                c.setForeground(Color.WHITE);
            }

            // Alinhamento padrão
            setHorizontalAlignment(SwingConstants.LEFT);

            // Coluna 1: Data
            if (column == 1 && value instanceof java.util.Date) {
                setText(new SimpleDateFormat("dd/MM/yyyy").format(value));
                setHorizontalAlignment(SwingConstants.CENTER);
            }

            // Coluna 3: Valor
            else if (column == 3 && value instanceof Number) {
                double val = ((Number) value).doubleValue(); // Converte para double
                setText(String.format("R$ %.2f", val));      // Formata o valor
                // Define cor baseada no valor
                setForeground(val < 0 ? new Color(255, 100, 100) : new Color(100, 255, 100)); // Vermelho ou Verde

                setHorizontalAlignment(SwingConstants.RIGHT);
            }

            return c;
        }
    };
    
    for (int i = 0; i < tabelaUltimasTransacoes.getColumnCount(); i++) {
        tabelaUltimasTransacoes.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
    }
    
        JScrollPane scrollTransacoes = new JScrollPane(tabelaUltimasTransacoes);
        scrollTransacoes.setOpaque(false);
        scrollTransacoes.getViewport().setOpaque(false);
        scrollTransacoes.setBorder(BorderFactory.createEmptyBorder());

        coluna.add(scrollTransacoes, BorderLayout.CENTER);
        return coluna;
    }
     
    private void carregarDados() {
    if (lblDataAtual != null) {
        lblDataAtual.setText("DATA: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    modeloUltimasTransacoes.setRowCount(0);
    List<TransacaoLinha> ultimasTransacoes = new ArrayList<>();

    String filtroCpf = txtFiltroCpf.getText().trim(); // Lê o filtro digitado

    for (Renda r : RendaDAO.listarRenda()) {
        if (filtroCpf.isEmpty() || r.getCpfUsuario().equals(filtroCpf)) {
            ultimasTransacoes.add(new TransacaoLinha(r.getCpfUsuario(), r.getDataRenda(), r.getNomeCategoria(), r.getValor()));
        }
    }

    for (Gasto g : GastoDAO.listarGastos()) {
        if (filtroCpf.isEmpty() || g.getCpfUsuario().equals(filtroCpf)) {
            ultimasTransacoes.add(new TransacaoLinha(g.getCpfUsuario(), g.getDataGasto(), g.getNomeCategoria(), -g.getValor()));
        }
    }

    Collections.sort(ultimasTransacoes, Comparator.comparing(TransacaoLinha::getData).reversed());

    int count = 0;
    for (TransacaoLinha tr : ultimasTransacoes) {
        if (count++ < 100) {
            modeloUltimasTransacoes.addRow(new Object[]{
                tr.getCpf(),
                sdf.format(tr.getData()),
                tr.getDescricao(),
                tr.getValor()
            });
        } else {
            break;
        }
    }
}

          
    private static class TransacaoLinha {
        private String cpf;
        private java.util.Date data;
        private String descricao;
        private double valor;

        public TransacaoLinha(String cpf, java.util.Date data, String descricao, double valor) {
            this.cpf = cpf;
            this.data = data;
            this.descricao = descricao;
            this.valor = valor;
        }
        
        public String getCpf() { return cpf; }
        public java.util.Date getData() { return data; }
        public String getDescricao() { return descricao; }
        public double getValor() { return valor; }
    }

}
