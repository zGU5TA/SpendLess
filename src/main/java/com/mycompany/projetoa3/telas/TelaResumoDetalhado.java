package com.mycompany.projetoa3.telas;

import com.mycompany.projetoa3.telas.gasto.Gasto;
import com.mycompany.projetoa3.telas.gasto.GastoDAO;
import com.mycompany.projetoa3.telas.renda.Renda;
import com.mycompany.projetoa3.telas.renda.RendaDAO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent; // Import para ActionEvent
import java.awt.event.ActionListener; // Import para ActionListener
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date; 
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class TelaResumoDetalhado extends JPanel {

    private final String cpfUsuario;
    private final DecimalFormat df = new DecimalFormat("R$ #,##0.00");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    private final SimpleDateFormat sdfAnoMes = new SimpleDateFormat("yyyy-MM");


    private JLabel lblSaldoTotalValor;
    private JLabel lblTotalDespesasMesValor;
    private JLabel lblTotalRendaMesValor;
    private JLabel lblGastosFuturosValor;
    private JLabel lblRendaFuturaValor;
    private JLabel lblPrevisaoSaldoValor;
    private JLabel lblPrevisaoStatus;
    private JPanel painelBalancoMensalConteudo;
    private DefaultTableModel modeloUltimasTransacoes;
    private JPanel painelRendasChartContainer;
    private JPanel painelGastosChartContainer;
    private JPanel painelPrincipaisRendasLista;
    private JPanel painelPrincipaisGastosLista;
    private JLabel lblDataAtual; // Tornar acessível para atualizar a data também

    public TelaResumoDetalhado(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.DARK_GRAY.darker());

        initComponents();
        carregarDados(); // Carga inicial dos dados
    }

    private void initComponents() {
        // Painel Superior (Data e Botão Atualizar)
        JPanel painelSuperior = new JPanel(new BorderLayout(10,0)); // Espaçamento horizontal entre data e botão
        painelSuperior.setOpaque(false);
        
        lblDataAtual = new JLabel("DATA: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblDataAtual.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblDataAtual.setForeground(Color.WHITE);
        painelSuperior.add(lblDataAtual, BorderLayout.WEST);

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
        painelSuperior.add(btnAtualizar, BorderLayout.EAST);
        
        add(painelSuperior, BorderLayout.NORTH);

        // Painel Principal (Colunas)
        JPanel painelPrincipal = new JPanel(new GridLayout(1, 3, 10, 10));
        painelPrincipal.setOpaque(false);

        JPanel colunaEsquerda = criarColunaEsquerda();  // Resumo
        JPanel colunaCentro = criarColunaCentro();      // Lista
        JPanel colunaDireita = criarColunaDireita();    // Graficos

        painelPrincipal.add(colunaEsquerda);
        painelPrincipal.add(colunaCentro);
        painelPrincipal.add(colunaDireita);

        add(painelPrincipal, BorderLayout.CENTER);
        
    }

    private JPanel criarColunaEsquerda() {
        JPanel coluna = new JPanel();
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));
        coluna.setOpaque(false);
        coluna.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JPanel painelSaldoTotal = new JPanel(new GridLayout(0, 1, 0, 5));
        painelSaldoTotal.setOpaque(false);
        painelSaldoTotal.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "SALDO TOTAL",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("SansSerif", Font.BOLD, 18), Color.WHITE)
        );

        lblSaldoTotalValor = new JLabel(df.format(0), SwingConstants.LEFT);
        lblSaldoTotalValor.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblSaldoTotalValor.setForeground(new Color(102, 178, 255));

        lblTotalDespesasMesValor = new JLabel("TOTAL DE DESPESAS (mês): " + df.format(0));
        lblTotalDespesasMesValor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTotalDespesasMesValor.setForeground(Color.WHITE);

        lblTotalRendaMesValor = new JLabel("TOTAL DE RENDA (mês): " + df.format(0));
        lblTotalRendaMesValor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTotalRendaMesValor.setForeground(Color.WHITE);

        painelSaldoTotal.add(lblSaldoTotalValor);
        painelSaldoTotal.add(lblTotalDespesasMesValor);
        painelSaldoTotal.add(lblTotalRendaMesValor);
        coluna.add(painelSaldoTotal);
        coluna.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel painelFuturos = new JPanel(new GridLayout(0, 1, 0, 5));
        painelFuturos.setOpaque(false);
        painelFuturos.setBorder(new EmptyBorder(5,0,5,0));

        lblGastosFuturosValor = new JLabel("GASTOS A SEREM DESCONTADOS: " + df.format(0));
        lblGastosFuturosValor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblGastosFuturosValor.setForeground(Color.WHITE);

        lblRendaFuturaValor = new JLabel("RENDA A SER RECEBIDA: " + df.format(0));
        lblRendaFuturaValor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRendaFuturaValor.setForeground(Color.WHITE);

        painelFuturos.add(lblGastosFuturosValor);
        painelFuturos.add(lblRendaFuturaValor);
        coluna.add(painelFuturos);
        coluna.add(Box.createRigidArea(new Dimension(0,15)));

        JPanel painelPrevisao = new JPanel(new GridLayout(0, 1, 0, 5));
        painelPrevisao.setOpaque(false);
        painelPrevisao.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "PREVISÃO DE SALDO",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("SansSerif", Font.BOLD, 16), Color.WHITE)
        );
        lblPrevisaoSaldoValor = new JLabel(df.format(0));
        lblPrevisaoSaldoValor.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblPrevisaoSaldoValor.setForeground(Color.WHITE);

        lblPrevisaoStatus = new JLabel("Fechar o mês ...");
        lblPrevisaoStatus.setFont(new Font("SansSerif", Font.ITALIC, 14));

        painelPrevisao.add(lblPrevisaoSaldoValor);
        painelPrevisao.add(lblPrevisaoStatus);
        coluna.add(painelPrevisao);
        coluna.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel painelBalancoMensal = new JPanel(new BorderLayout());
        painelBalancoMensal.setOpaque(false);
         painelBalancoMensal.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "BALANÇO MENSAL",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("SansSerif", Font.BOLD, 16), Color.WHITE)
        );

        painelBalancoMensalConteudo = new JPanel();
        painelBalancoMensalConteudo.setLayout(new BoxLayout(painelBalancoMensalConteudo, BoxLayout.Y_AXIS));
        painelBalancoMensalConteudo.setOpaque(false);
        JScrollPane scrollBalanco = new JScrollPane(painelBalancoMensalConteudo);
        scrollBalanco.setOpaque(false);
        scrollBalanco.getViewport().setOpaque(false);
        scrollBalanco.setBorder(null);

        painelBalancoMensal.add(scrollBalanco, BorderLayout.CENTER);
        coluna.add(painelBalancoMensal);
        coluna.add(Box.createVerticalGlue());

        return coluna;
    }

    private JPanel criarColunaCentro() {
        JPanel coluna = new JPanel(new BorderLayout(0, 10));
        coluna.setOpaque(false);
        coluna.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTituloTransacoes = new JLabel("ÚLTIMAS TRANSAÇÕES", SwingConstants.CENTER);
        lblTituloTransacoes.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTituloTransacoes.setForeground(Color.WHITE);
        coluna.add(lblTituloTransacoes, BorderLayout.NORTH);

        modeloUltimasTransacoes = new DefaultTableModel(new Object[]{"Data", "Descrição", "Valor"}, 0) {
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

        DefaultTableCellRenderer transparentRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) c).setOpaque(false); 
                if (value instanceof String) { 
                     setHorizontalAlignment(JLabel.LEFT);
                }
                return c;
            }
        };
        tabelaUltimasTransacoes.getColumnModel().getColumn(0).setCellRenderer(transparentRenderer);
        tabelaUltimasTransacoes.getColumnModel().getColumn(1).setCellRenderer(transparentRenderer);
        
        tabelaUltimasTransacoes.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Double) {
                    double val = (Double) value;
                    setText(df.format(val));
                    setForeground(val < 0 ? new Color(255, 100, 100) : new Color(100, 255, 100));
                }
                setHorizontalAlignment(JLabel.RIGHT);
                ((JComponent) c).setOpaque(false); 
                return c;
            }
        });

        JScrollPane scrollTransacoes = new JScrollPane(tabelaUltimasTransacoes);
        scrollTransacoes.setOpaque(false);
        scrollTransacoes.getViewport().setOpaque(false);
        scrollTransacoes.setBorder(BorderFactory.createEmptyBorder());

        coluna.add(scrollTransacoes, BorderLayout.CENTER);
        return coluna;
    }

    private JPanel criarColunaDireita() {
        JPanel coluna = new JPanel(new GridLayout(2, 1, 0, 10));
        coluna.setOpaque(false);

        JPanel painelRendas = new JPanel(new BorderLayout(0,5));
        painelRendas.setOpaque(false);
        painelRendas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel lblTituloRendas = new JLabel("PRINCIPAIS RENDAS DO MÊS", SwingConstants.CENTER);
        lblTituloRendas.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTituloRendas.setForeground(Color.WHITE);
        painelRendas.add(lblTituloRendas, BorderLayout.NORTH);

        painelRendasChartContainer = new JPanel(new BorderLayout());
        painelRendasChartContainer.setPreferredSize(new Dimension(200, 150));
        painelRendasChartContainer.setOpaque(false);
        painelRendas.add(painelRendasChartContainer, BorderLayout.CENTER);

        painelPrincipaisRendasLista = new JPanel();
        painelPrincipaisRendasLista.setLayout(new BoxLayout(painelPrincipaisRendasLista, BoxLayout.Y_AXIS));
        painelPrincipaisRendasLista.setOpaque(false);
        JScrollPane scrollRendasLista = new JScrollPane(painelPrincipaisRendasLista);
        scrollRendasLista.setPreferredSize(new Dimension(100, 80));
        scrollRendasLista.setOpaque(false);
        scrollRendasLista.getViewport().setOpaque(false);
        scrollRendasLista.setBorder(null);
        painelRendas.add(scrollRendasLista, BorderLayout.SOUTH);

        coluna.add(painelRendas);

        JPanel painelGastos = new JPanel(new BorderLayout(0,5));
        painelGastos.setOpaque(false);
        painelGastos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel lblTituloGastos = new JLabel("PRINCIPAIS GASTOS DO MÊS", SwingConstants.CENTER);
        lblTituloGastos.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTituloGastos.setForeground(Color.WHITE);
        painelGastos.add(lblTituloGastos, BorderLayout.NORTH);

        painelGastosChartContainer = new JPanel(new BorderLayout());
        painelGastosChartContainer.setPreferredSize(new Dimension(200, 150));
        painelGastosChartContainer.setOpaque(false);
        painelGastos.add(painelGastosChartContainer, BorderLayout.CENTER);

        painelPrincipaisGastosLista = new JPanel();
        painelPrincipaisGastosLista.setLayout(new BoxLayout(painelPrincipaisGastosLista, BoxLayout.Y_AXIS));
        painelPrincipaisGastosLista.setOpaque(false);
        JScrollPane scrollGastosLista = new JScrollPane(painelPrincipaisGastosLista);
        scrollGastosLista.setPreferredSize(new Dimension(100,80));
        scrollGastosLista.setOpaque(false);
        scrollGastosLista.getViewport().setOpaque(false);
        scrollGastosLista.setBorder(null);
        painelGastos.add(scrollGastosLista, BorderLayout.SOUTH);

        coluna.add(painelGastos);
        return coluna;
    }

    // Método público para permitir atualização externa, se necessário
    public void atualizarDados() {
        carregarDados();
    }

    private void carregarDados() {
        // Atualiza a data exibida para a data atual no momento do carregamento
        if (lblDataAtual != null) { // Verifica se o label já foi inicializado
            lblDataAtual.setText("DATA: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        LocalDate hoje = LocalDate.now();
        YearMonth mesAtualYrMo = YearMonth.from(hoje);
        String inicioMesAtualStr = mesAtualYrMo.atDay(1).format(DateTimeFormatter.ISO_DATE);
        String fimMesAtualStr = mesAtualYrMo.atEndOfMonth().format(DateTimeFormatter.ISO_DATE);
        String nomeMesAtual = hoje.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR")).toUpperCase();
        String anoMesAtualStr = sdfAnoMes.format(Date.from(hoje.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // --- SALDO TOTAL DINÂMICO ---
        List<Renda> todasRendasUsuario = RendaDAO.listarRendasPorUsuario(cpfUsuario);
        double somaTotalRendas = todasRendasUsuario.stream().mapToDouble(Renda::getValor).sum();

        List<Gasto> todosGastosUsuario = GastoDAO.listarGastosPorUsuario(cpfUsuario);
        double somaTotalGastos = todosGastosUsuario.stream().mapToDouble(Gasto::getValor).sum();
        
        double saldoTotalAtual = somaTotalRendas - somaTotalGastos;
        lblSaldoTotalValor.setText(df.format(saldoTotalAtual));
        
        // --- TOTAIS DO MÊS ATUAL ---
        List<Gasto> gastosMesAtual = todosGastosUsuario.stream()
            .filter(g -> {
                String anoMesGastoStr = sdfAnoMes.format(g.getDataGasto());
                return anoMesGastoStr.equals(anoMesAtualStr);
            })
            .collect(Collectors.toList());
        double totalDespesasMes = gastosMesAtual.stream().mapToDouble(Gasto::getValor).sum();
        lblTotalDespesasMesValor.setText("TOTAL DE DESPESAS (" + nomeMesAtual + "): " + df.format(totalDespesasMes));

        List<Renda> rendasMesAtual = todasRendasUsuario.stream()
            .filter(r -> {
                String anoMesRendaStr = sdfAnoMes.format(r.getDataRenda());
                return anoMesRendaStr.equals(anoMesAtualStr);
            })
            .collect(Collectors.toList());
        double totalRendaMes = rendasMesAtual.stream().mapToDouble(Renda::getValor).sum();
        lblTotalRendaMesValor.setText("TOTAL DE RENDA (" + nomeMesAtual + "): " + df.format(totalRendaMes));

        // --- GASTOS E RENDAS FUTURAS/RECORRENTES ---
        double gastosFuturosCalculado = 0;
        double rendasFuturasCalculado = 0;

        for (Gasto g : todosGastosUsuario) {
            String anoMesGastoStr = sdfAnoMes.format(g.getDataGasto());
            LocalDate dataGastoLocalDate = ((java.sql.Date) g.getDataGasto()).toLocalDate();

            if (g.isEhRecorrente() && anoMesGastoStr.compareTo(anoMesAtualStr) < 0) {
                gastosFuturosCalculado += g.getValor();
            }
            else if (!g.isEhRecorrente() && anoMesGastoStr.equals(anoMesAtualStr) && dataGastoLocalDate.isAfter(hoje)) {
                 gastosFuturosCalculado += g.getValor();
            }
        }

        for (Renda r : todasRendasUsuario) {
            String anoMesRendaStr = sdfAnoMes.format(r.getDataRenda());
            LocalDate dataRendaLocalDate = ((java.sql.Date) r.getDataRenda()).toLocalDate();

            if (r.isEhRecorrente() && anoMesRendaStr.compareTo(anoMesAtualStr) < 0) {
                rendasFuturasCalculado += r.getValor();
            }
             else if (!r.isEhRecorrente() && anoMesRendaStr.equals(anoMesAtualStr) && dataRendaLocalDate.isAfter(hoje)) {
                 rendasFuturasCalculado += r.getValor();
            }
        }

        lblGastosFuturosValor.setText("GASTOS A SEREM DESCONTADOS: " + df.format(gastosFuturosCalculado));
        lblRendaFuturaValor.setText("RENDA A SER RECEBIDA: " + df.format(rendasFuturasCalculado));
        
        // --- PREVISÃO DE SALDO ---
        double previsaoSaldo = saldoTotalAtual - gastosFuturosCalculado + rendasFuturasCalculado;
        lblPrevisaoSaldoValor.setText(df.format(previsaoSaldo));
        if (previsaoSaldo >= 0) {
            lblPrevisaoStatus.setText("Fechar o mês " + nomeMesAtual + " POSITIVO");
            lblPrevisaoStatus.setForeground(new Color(100, 255, 100));
        } else {
            lblPrevisaoStatus.setText("Fechar o mês " + nomeMesAtual + " NEGATIVO");
            lblPrevisaoStatus.setForeground(new Color(255, 100, 100));
        }

        // --- BALANÇO MENSAL ---
        painelBalancoMensalConteudo.removeAll();
        for (int i = 1; i <= 6; i++) {
            YearMonth mesAnteriorLoop = mesAtualYrMo.minusMonths(i);
            String nomeMesAnterior = mesAnteriorLoop.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR")).toUpperCase();
            String anoMesAnteriorStr = sdfAnoMes.format(Date.from(mesAnteriorLoop.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            double totalDespesasMesAnterior = todosGastosUsuario.stream()
                .filter(g -> sdfAnoMes.format(g.getDataGasto()).equals(anoMesAnteriorStr))
                .mapToDouble(Gasto::getValor).sum();

            double totalRendaMesAnterior = todasRendasUsuario.stream()
                .filter(r -> sdfAnoMes.format(r.getDataRenda()).equals(anoMesAnteriorStr))
                .mapToDouble(Renda::getValor).sum();

            double balancoDoMes = totalRendaMesAnterior - totalDespesasMesAnterior;
            JLabel lblBalancoItem = new JLabel(nomeMesAnterior + " - " + df.format(balancoDoMes));
            lblBalancoItem.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblBalancoItem.setForeground(balancoDoMes >= 0 ? new Color(180, 255, 180) : new Color(255, 180, 180));
            painelBalancoMensalConteudo.add(lblBalancoItem);
        }
        painelBalancoMensalConteudo.revalidate();
        painelBalancoMensalConteudo.repaint();

        // --- ÚLTIMAS TRANSAÇÕES ---
        modeloUltimasTransacoes.setRowCount(0);
        List<TransacaoResumo> ultimasTransacoes = new ArrayList<>();
        todosGastosUsuario.forEach(g -> ultimasTransacoes.add(new TransacaoResumo(g.getDataGasto(), g.getDescricao(), -g.getValor())));
        todasRendasUsuario.forEach(r -> ultimasTransacoes.add(new TransacaoResumo(r.getDataRenda(), r.getDescricao(), r.getValor())));
        
        Collections.sort(ultimasTransacoes, Comparator.comparing(TransacaoResumo::getData).reversed());
        int count = 0;
        for (TransacaoResumo tr : ultimasTransacoes) {
            if (count++ < 10) {
                modeloUltimasTransacoes.addRow(new Object[]{sdf.format(tr.getData()), tr.getDescricao(), tr.getValor()});
            } else {
                break;
            }
        }

        // --- GRÁFICOS DE PIZZA ---
        Map<String, Double> rendasPorCategoria = rendasMesAtual.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getNomeCategoria() != null ? r.getNomeCategoria() : "Cat. " + r.getIdCategoria(),
                        Collectors.summingDouble(Renda::getValor)
                ));
        atualizarGraficoPizza(painelRendasChartContainer, "Principais Rendas", rendasPorCategoria, painelPrincipaisRendasLista, totalRendaMes, Color.GREEN.darker());

        Map<String, Double> gastosPorCategoria = gastosMesAtual.stream()
                .collect(Collectors.groupingBy(
                        g -> g.getNomeCategoria() != null ? g.getNomeCategoria() : "Cat. " + g.getIdCategoria(),
                        Collectors.summingDouble(Gasto::getValor)
                ));
        atualizarGraficoPizza(painelGastosChartContainer, "Principais Gastos", gastosPorCategoria, painelPrincipaisGastosLista, totalDespesasMes, Color.RED.darker());
    
        // Força a repintura de todo o painel para garantir que as atualizações visuais sejam aplicadas
        this.revalidate();
        this.repaint();
    }

    private void atualizarGraficoPizza(JPanel container, String tituloExterno, Map<String, Double> dados, JPanel listaContainer, double totalValorMes, Color defaultColor) {
        container.removeAll();
        listaContainer.removeAll();

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (dados.isEmpty() || totalValorMes == 0) {
            dataset.setValue("Sem dados", 100);
        } else {
            List<Map.Entry<String, Double>> sortedEntries = dados.entrySet().stream()
                 .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                 .collect(Collectors.toList());

            double outrosValor = 0;
            int count = 0;
            for (Map.Entry<String, Double> entry : sortedEntries) {
                if (count < 4 || sortedEntries.size() <=5 ) {
                     if (entry.getValue() > 0) dataset.setValue(entry.getKey(), entry.getValue());
                } else {
                    outrosValor += entry.getValue();
                }
                count++;
            }
            if (outrosValor > 0) {
                dataset.setValue("Outros", outrosValor);
            }
             if (dataset.getItemCount() == 0 && totalValorMes > 0){
                dataset.setValue("Valores pequenos", totalValorMes);
            } else if (dataset.getItemCount() == 0 && totalValorMes == 0){
                 dataset.setValue("Sem dados", 100);
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
                null, 
                dataset,
                false, 
                true,  
                false  
        );
        chart.setBackgroundPaint(null); 

        TextTitle chartTitle = chart.getTitle();
        if (chartTitle != null) {
            chartTitle.setVisible(false); 
        }

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", new DecimalFormat("0.#"), new DecimalFormat("0.#%")));
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        plot.setLabelBackgroundPaint(new Color(220, 220, 220, 150));
        plot.setSimpleLabels(true);
        plot.setInteriorGap(0.05);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(false);
        container.add(chartPanel, BorderLayout.CENTER);

        final double thresholdPercent = 0.01;
        dados.entrySet().stream()
            .filter(entry -> totalValorMes > 0 && (entry.getValue() / totalValorMes) >= thresholdPercent)
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(entry -> {
                double percent = (entry.getValue() / totalValorMes) * 100;
                JLabel lblItem = new JLabel(String.format("%s - %.0f%%", entry.getKey(), percent));
                lblItem.setFont(new Font("SansSerif", Font.PLAIN, 12));
                lblItem.setForeground(Color.LIGHT_GRAY);
                listaContainer.add(lblItem);
            });

        // Revalida e repinta os containers específicos do gráfico e da lista
        container.revalidate();
        container.repaint();
        listaContainer.revalidate();
        listaContainer.repaint();
    }

    private static class TransacaoResumo {
        private java.util.Date data;
        private String descricao;
        private double valor;

        public TransacaoResumo(java.util.Date data, String descricao, double valor) {
            this.data = data;
            this.descricao = descricao;
            this.valor = valor;
        }

        public java.util.Date getData() { return data; }
        public String getDescricao() { return descricao; }
        public double getValor() { return valor; }
    }
}
