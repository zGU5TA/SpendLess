package com.mycompany.projetoa3.telas.gasto;

import com.mycompany.projetoa3.Categoria;
import com.mycompany.projetoa3.CategoriaDAO; // Certifique-se que esta importação existe e é usada
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox; // Importar JCheckBox
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FlowLayout; // Para o painel do checkbox

public class AdicionarGasto extends JDialog {

    private String cpfUsuario;
    private Runnable onSuccess; // Callback para atualizar a tela principal após salvar
    private JTextField tfDescricao;
    private JTextField tfValor;
    private JComboBox<String> cbCategoria;
    private JCheckBox cbRecorrente; // JCheckBox para indicar se o gasto é recorrente

    public AdicionarGasto(Frame parent, String cpfUsuario, Runnable onSuccess) {
        super(parent, "Adicionar Gasto", true); // true para modal
        this.cpfUsuario = cpfUsuario;
        this.onSuccess = onSuccess;
        initComponents();
        setSize(400, 300); // Tamanho ajustado para incluir o novo campo
        setLocationRelativeTo(parent); // Centraliza em relação à janela pai
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // Espaçamento entre componentes

        // Painel para os campos do formulário
        JPanel painelForm = new JPanel(new GridLayout(0, 2, 10, 10)); // 0 linhas = flexível, espaçamento 10

        painelForm.add(new JLabel("Descrição:"));
        tfDescricao = new JTextField();
        painelForm.add(tfDescricao);

        painelForm.add(new JLabel("Valor (R$):"));
        tfValor = new JTextField();
        painelForm.add(tfValor);

        painelForm.add(new JLabel("Categoria:"));
        cbCategoria = new JComboBox<>();
        carregarCategorias();
        painelForm.add(cbCategoria);

        painelForm.add(new JLabel("Recorrente?:")); // Label para o checkbox
        cbRecorrente = new JCheckBox();
        painelForm.add(cbRecorrente); // Adiciona o checkbox ao formulário

        add(painelForm, BorderLayout.CENTER);

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarGasto());
        
        // Painel para o botão, centralizado
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarCategorias() {
        cbCategoria.removeAllItems(); // Limpa itens antigos
        // Assumindo que tipo 1 = Gasto (conforme seu CategoriaDAO)
        List<Categoria> categorias = CategoriaDAO.listarCategoriasPorTipo(1);
        if (categorias.isEmpty()) {
            cbCategoria.addItem("Nenhuma categoria de gasto");
            // Considerar desabilitar o JComboBox ou o botão salvar se não houver categorias
        } else {
            for (Categoria c : categorias) {
                cbCategoria.addItem(c.getNome());
            }
        }
    }

    private void salvarGasto() {
        String descricao = tfDescricao.getText().trim();
        // Substitui vírgula por ponto para garantir compatibilidade com Double.parseDouble
        String valorStr = tfValor.getText().trim().replace(",", ".");
        String nomeCategoria = (String) cbCategoria.getSelectedItem();
        boolean ehRecorrente = cbRecorrente.isSelected(); // Pega o estado do checkbox

        // Validação dos campos
        if (descricao.isEmpty() || valorStr.isEmpty() || nomeCategoria == null || nomeCategoria.equals("Nenhuma categoria de gasto")) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos e selecione uma categoria válida.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) { // Gastos devem ser positivos
                JOptionPane.showMessageDialog(this, "O valor do gasto deve ser positivo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números (ex: 50.75).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Busca o objeto Categoria completo para obter o ID
        Categoria categoriaSelecionada = null;
        List<Categoria> categoriasDisponiveis = CategoriaDAO.listarCategoriasPorTipo(1);
        for (Categoria cat : categoriasDisponiveis) {
            if (cat.getNome().equals(nomeCategoria)) {
                categoriaSelecionada = cat;
                break;
            }
        }

        if (categoriaSelecionada == null) { // Segurança adicional
             JOptionPane.showMessageDialog(this, "Categoria selecionada não encontrada. Tente novamente.", "Erro Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cria o objeto Gasto
        Gasto novoGasto = new Gasto();
        novoGasto.setDescricao(descricao);
        novoGasto.setValor(valor);
        novoGasto.setIdCategoria(categoriaSelecionada.getIdCategoria()); // Usa o ID da categoria selecionada
        novoGasto.setCpfUsuario(cpfUsuario);
        novoGasto.setDataGasto(new java.util.Date()); // Data atual do sistema
        novoGasto.setEhRecorrente(ehRecorrente); // Define o status de recorrência

        // Tenta inserir no banco de dados
        boolean sucesso = GastoDAO.inserirGasto(novoGasto);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Gasto adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            if (onSuccess != null) {
                onSuccess.run(); // Chama o callback para atualizar a tela de listagem (TelaGastos)
            }
            dispose(); // Fecha o diálogo de adição
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar gasto. Verifique o console para mais detalhes.", "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }
}
