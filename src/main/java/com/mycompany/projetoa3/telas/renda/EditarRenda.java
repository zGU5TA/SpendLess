package com.mycompany.projetoa3.telas.renda;

import com.mycompany.projetoa3.Categoria;
import com.mycompany.projetoa3.CategoriaDAO;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FlowLayout;

public class EditarRenda extends JDialog {

    private Renda renda; // A renda a ser editada
    private Runnable onSuccess; // Callback para atualizar a tela principal após salvar
    private JTextField tfDescricao;
    private JTextField tfValor;
    private JComboBox<String> cbCategoria;
    private JCheckBox cbRecorrente; // JCheckBox para indicar se a renda é recorrente

    public EditarRenda(Frame parent, Renda renda, Runnable onSuccess) {
        super(parent, "Editar Renda", true); // true para modal
        this.renda = renda;
        this.onSuccess = onSuccess;
        initComponents();
        preencherCampos(); // Preenche os campos com os dados da renda existente
        setSize(400, 300); // Tamanho ajustado
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
        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.addActionListener(e -> salvarEdicao());

        // Painel para o botão, centralizado
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void carregarCategorias() {
        cbCategoria.removeAllItems(); // Limpa itens antigos
        // Assumindo que tipo 2 = Renda (conforme seu CategoriaDAO)
        List<Categoria> categorias = CategoriaDAO.listarCategoriasPorTipo(2);
        if (categorias.isEmpty()) {
            cbCategoria.addItem("Nenhuma categoria de renda");
        } else {
            for (Categoria c : categorias) {
                cbCategoria.addItem(c.getNome());
            }
        }
    }

    private void preencherCampos() {
        if (renda != null) {
            tfDescricao.setText(renda.getDescricao());
            // Formata o valor para exibição com vírgula, se for o padrão local
            tfValor.setText(String.valueOf(renda.getValor()).replace(".", ","));
            
            // Selecionar a categoria atual
            if (renda.getNomeCategoria() != null) {
                cbCategoria.setSelectedItem(renda.getNomeCategoria());
            } else {
                // Fallback se nomeCategoria não estiver populado (deve estar com o DAO atualizado)
                // Este fallback busca a categoria pelo ID para garantir a seleção correta.
                Categoria catAtual = null;
                List<Categoria> todasCategorias = CategoriaDAO.listarCategoriasPorTipo(2); // Busca apenas categorias de renda
                for(Categoria c : todasCategorias) {
                    if(c.getIdCategoria() == renda.getIdCategoria()) {
                        catAtual = c;
                        break;
                    }
                }
                if(catAtual != null) {
                    cbCategoria.setSelectedItem(catAtual.getNome());
                } else if (cbCategoria.getItemCount() > 0) {
                    // Se não encontrar a categoria específica, seleciona a primeira da lista (ou nenhuma)
                     cbCategoria.setSelectedIndex(0); // Ou -1 para nenhuma seleção se "Nenhuma categoria..." não for o primeiro
                }
            }
            // Define o estado do checkbox com base no valor do objeto Renda
            cbRecorrente.setSelected(renda.isEhRecorrente());
        }
    }

    private void salvarEdicao() {
        String descricao = tfDescricao.getText().trim();
        // Substitui vírgula por ponto para garantir compatibilidade com Double.parseDouble
        String valorStr = tfValor.getText().trim().replace(",", ".");
        String nomeCategoria = (String) cbCategoria.getSelectedItem();
        boolean ehRecorrente = cbRecorrente.isSelected(); // Pega o estado atual do checkbox

        // Validação dos campos
        if (descricao.isEmpty() || valorStr.isEmpty() || nomeCategoria == null || nomeCategoria.equals("Nenhuma categoria de renda")) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos e selecione uma categoria válida.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
            if (valor <= 0) { // Rendas devem ser positivas
                JOptionPane.showMessageDialog(this, "O valor da renda deve ser positivo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Use apenas números (ex: 50.75).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Busca o objeto Categoria completo para obter o ID
        Categoria categoriaSelecionada = null;
        List<Categoria> categoriasDisponiveis = CategoriaDAO.listarCategoriasPorTipo(2);
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

        // Atualizar os dados do objeto 'renda' existente
        renda.setDescricao(descricao);
        renda.setValor(valor);
        renda.setIdCategoria(categoriaSelecionada.getIdCategoria());
        // A data da renda original (renda.setDataRenda()) geralmente não é alterada na edição,
        // a menos que seja um requisito específico. Se precisar, adicione um JDateChooser.
        renda.setEhRecorrente(ehRecorrente); // Atualiza o status de recorrência
        // O cpfUsuario já deve estar no objeto 'renda' e não é tipicamente editado aqui.

        // Tenta atualizar no banco de dados
        boolean sucesso = RendaDAO.atualizarRenda(renda);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Renda atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            if (onSuccess != null) {
                onSuccess.run(); // Chama o callback para atualizar a tela de listagem (TelaRendas)
            }
            dispose(); // Fecha o diálogo de edição
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar renda. Verifique o console para mais detalhes.", "Erro no Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }
}
