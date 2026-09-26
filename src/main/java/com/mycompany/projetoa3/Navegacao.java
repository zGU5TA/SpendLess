package com.mycompany.projetoa3;

// Importações existentes
import com.mycompany.projetoa3.telas.adm.TelaGerenciarCategorias;
import com.mycompany.projetoa3.telas.adm.TelaGerenciarUsuarios;
import com.mycompany.projetoa3.telas.adm.TelaAdmin;
import com.mycompany.projetoa3.telas.renda.TelaRendas;
import com.mycompany.projetoa3.telas.*; // Para TelaPerfil e a NOVA TelaResumoDetalhado
import com.mycompany.projetoa3.telas.gasto.TelaGastos;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Navegacao extends JFrame {

    private final String cpfUsuario;
    private final String tipoUsuario;
    private final JPanel painelPrincipal;
    private final CardLayout cardLayout;
    private final Map<String, JButton> botoesNavegacao = new HashMap<>();
    private JLabel labelSaudacao; // Tornar acessível para atualização

    public Navegacao(String telaInicial, String cpfUsuario, String tipoUsuario) {
        this.cpfUsuario = cpfUsuario;
        this.tipoUsuario = tipoUsuario;

        setTitle("Sistema Financeiro Pessoal"); // Título mais descritivo
        setSize(800, 600); // Tamanho inicial um pouco maior
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Inicia maximizado
        setLayout(new BorderLayout());

        // Ícone da aplicação
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/pig_logo.png"));
            if (icon.getImage() != null) {
                setIconImage(icon.getImage());
            } else {
                System.err.println("Arquivo de ícone não encontrado ou inválido: /images/pig_logo.png");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o ícone da aplicação: " + e.getMessage());
        }

        // Painel principal com CardLayout
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);
        painelPrincipal.setBackground(Color.DARK_GRAY.darker()); // Cor de fundo para o painel principal

        // Telas comuns
        // ***** ALTERAÇÃO PRINCIPAL AQUI *****        
        if (telaInicial == "Resumo admin"){
            painelPrincipal.add(new TelaResumoAdmin(), "Resumo"); // Usa a nova tela
            painelPrincipal.add(new TelaPerfil(cpfUsuario), "Perfil");
        } else {
            painelPrincipal.add(new TelaResumoDetalhado(cpfUsuario), "Resumo"); // Usa a nova tela
            painelPrincipal.add(new TelaGastos(cpfUsuario), "Gastos");
            painelPrincipal.add(new TelaRendas(cpfUsuario), "Renda");
            painelPrincipal.add(new TelaPerfil(cpfUsuario), "Perfil");
        }    
        
        // Telas de admin
        if (telaInicial == "Resumo admin") {
            painelPrincipal.add(new JTextField(11));

            painelPrincipal.add(new TelaAdmin(cpfUsuario, e -> trocarTela(e.getActionCommand())), "Admin");
            // As telas de gerenciamento são tipicamente acessadas a partir da TelaAdmin,
            // mas se precisar adicioná-las diretamente ao cardLayout:
            painelPrincipal.add(new TelaGerenciarUsuarios(), "GerenciarUsuarios");
            painelPrincipal.add(new TelaGerenciarCategorias(), "GerenciarCategorias");
        }


        // Adiciona os componentes à janela
        add(criarBarraNavegacao(telaInicial), BorderLayout.NORTH);
        add(painelPrincipal, BorderLayout.CENTER);

        // Exibe a tela inicial (fallback para Resumo)
        setVisible(true);
        trocarTela((telaInicial == null || telaInicial.isBlank()) ? "Resumo" : telaInicial);

        // Atualiza a saudação periodicamente e ao focar na janela
        // (A atualização da saudação ao focar pode ser útil se o nome do usuário mudar em outra tela)
         addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent windowEvent) {
                atualizarSaudacao();
            }
        });
    }
    
    public void atualizarSaudacaoPublic() {
    atualizarSaudacao(); // Chama o método privado existente
}

    private void atualizarSaudacao() {
        if (labelSaudacao != null) {
            String nomeUsuario = SessaoUsuario.getNomeUsuario();
            if (nomeUsuario == null || nomeUsuario.trim().isEmpty()){
                // Tenta buscar novamente se estiver nulo, pode ser que a sessão demorou para carregar
                // Esta é uma contingência, idealmente a sessão já estaria carregada.
                // Se ConexaoDB tiver um método para buscar nome por CPF:
                // Usuario usr = ConexaoDB.buscarUsuarioPorCpf(cpfUsuario);
                // if(usr != null) nomeUsuario = usr.getNome();
                // else nomeUsuario = "Usuário"; // Fallback
                nomeUsuario = "Usuário"; // Fallback simples
            }
            labelSaudacao.setText(gerarSaudacao() + ", " + nomeUsuario + "!");
        }
    }


    private String gerarSaudacao() {
        LocalTime agora = LocalTime.now();
        if (agora.isBefore(LocalTime.NOON)) {
            return "Bom dia";
        } else if (agora.isBefore(LocalTime.of(18, 0))) {
            return "Boa tarde";
        } else {
            return "Boa noite";
        }
    }

    private JPanel criarBarraNavegacao(String telaInicial) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(60, 63, 65)); // Cor de fundo mais escura para a barra
        barra.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Pequena margem

        // Painel para a saudação à esquerda
        JPanel painelSaudacao = new JPanel(new GridBagLayout());
        painelSaudacao.setOpaque(false);
        // Removido setPreferredSize para permitir que o layout gerencie melhor

        labelSaudacao = new JLabel(); // Inicializa o label
        atualizarSaudacao(); // Define o texto inicial da saudação

        labelSaudacao.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Fonte um pouco maior e moderna
        labelSaudacao.setForeground(new Color(220, 220, 220)); // Cor do texto mais clara
        painelSaudacao.add(labelSaudacao);
        barra.add(painelSaudacao, BorderLayout.WEST);

        // Atualiza a saudação em tempo real a cada 30 segundos
        new javax.swing.Timer(10000, e -> atualizarSaudacao()).start();

        // Painel para os botões alinhados à direita
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10)); // Aumenta espaçamento
        painelBotoes.setOpaque(false);

        // Botões padrão
        // Botões usuraios
            String[] btsTela;
       if (!telaInicial.equals("Resumo admin")) {
           btsTela = new String[]{"Resumo", "Gastos", "Renda", "Perfil"};
       } else {
           btsTela = new String[]{"Resumo", "Perfil", "Admin"};
       }
            
        for (String nome : btsTela) {
            JButton botao = criarBotao(nome);
            botoesNavegacao.put(nome, botao);
            painelBotoes.add(botao);
        }        

        barra.add(painelBotoes, BorderLayout.EAST);
        return barra;
    }

    private JButton criarBotao(String nome) {
        JButton botao = new JButton(nome);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        botao.setBackground(new Color(75, 78, 80)); // Cor de fundo do botão
        botao.setForeground(Color.WHITE); // Cor do texto do botão
        botao.setPreferredSize(new Dimension(110, 40)); // Tamanho do botão
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90,93,95), 1), // Borda sutil
            BorderFactory.createEmptyBorder(5, 15, 5, 15) // Padding interno
        ));

        // Efeito Hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!botao.getBackground().equals(Color.RED)) { // Não altera se for o botão ativo
                    botao.setBackground(new Color(90, 93, 95));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                 if (!botao.getBackground().equals(Color.RED)) {
                    botao.setBackground(new Color(75, 78, 80));
                }
            }
        });

        botao.addActionListener(e -> trocarTela(nome));
        return botao;
    }

    private void trocarTela(String nomeTela) {
        if (nomeTela == null || nomeTela.isEmpty()) return;
        cardLayout.show(painelPrincipal, nomeTela);
        destacarBotaoAtual(nomeTela);
        // Garante que a saudação seja atualizada ao trocar de tela,
        // especialmente se o nome do usuário puder ser alterado em "Perfil"
        atualizarSaudacao();
    }

    private void destacarBotaoAtual(String telaAtual) {
        for (Map.Entry<String, JButton> entry : botoesNavegacao.entrySet()) {
            JButton botao = entry.getValue();
            boolean selecionado = entry.getKey().equalsIgnoreCase(telaAtual);

            botao.setBackground(selecionado ? Color.RED : new Color(75, 78, 80));
            botao.setForeground(selecionado ? Color.WHITE : Color.WHITE); // Texto sempre branco
            botao.setFont(botao.getFont().deriveFont(selecionado ? Font.BOLD : Font.PLAIN));
        }
    }
}