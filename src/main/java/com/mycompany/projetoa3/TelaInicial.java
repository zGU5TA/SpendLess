package com.mycompany.projetoa3; // Certifique-se que o pacote está correto

import javax.swing.*;
import javax.swing.border.EmptyBorder; // Para adicionar margens
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL; // Para carregar o ícone da janela
import java.sql.Connection; // Para o teste de conexão existente

public class TelaInicial extends JFrame {

    public TelaInicial() {
        setTitle("Bem Vindo ao Sistema Financeiro"); // Título mais informativo
        setSize(400, 300); // Tamanho um pouco maior para melhor visualização
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setResizable(false); // Impede redimensionamento

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

        // Painel de fundo com GridBagLayout para centralizar o conteúdo
        JPanel painelFundo = new JPanel(new GridBagLayout());
        painelFundo.setBackground(new Color(45, 45, 45)); // Fundo escuro
        setContentPane(painelFundo);

        // Painel de conteúdo principal (onde os botões e label ficarão)
        JPanel painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS)); // Mantém BoxLayout
        painelConteudo.setOpaque(false); // Para o fundo do painelFundo ser visível
        painelConteudo.setBorder(new EmptyBorder(20, 20, 20, 20)); // Margem interna

        // Título "BEM VINDO"
        JLabel lblTitulo = new JLabel("SPEND LESS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Fonte maior e moderna
        lblTitulo.setForeground(Color.WHITE); // Cor do texto
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelConteudo.add(lblTitulo);

        painelConteudo.add(Box.createRigidArea(new Dimension(0, 30))); // Espaço maior abaixo do título

        // Botão Entrar
        JButton btnEntrar = new JButton("ENTRAR");
        personalizarBotao(btnEntrar, new Color(70, 130, 180), Color.WHITE); // Azul
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelConteudo.add(btnEntrar);

        painelConteudo.add(Box.createRigidArea(new Dimension(0, 15))); // Espaço entre botões

        // Botão Criar Conta
        JButton btnCriarConta = new JButton("CRIAR CONTA");
        personalizarBotao(btnCriarConta, new Color(34, 139, 34), Color.WHITE); // Verde
        btnCriarConta.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelConteudo.add(btnCriarConta);

        // Adiciona o painel de conteúdo ao painel de fundo (que usa GridBagLayout para centralizar)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Centraliza o painelConteudo
        painelFundo.add(painelConteudo, gbc);


        // Ação dos botões (lógica original mantida)
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaEntrar().setVisible(true);
                dispose(); // fecha esta janela
            }
        });

        btnCriarConta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaCadastrar().setVisible(true);
                dispose(); // fecha esta janela
            }
        });

        // Testa conexão ao abrir a tela inicial (lógica original mantida)
        Connection conexao = ConexaoDB.conectar();
        if (conexao != null) {
            System.out.println("Conexão com o banco de dados realizada com sucesso na Tela Inicial!");
            // try { conexao.close(); } catch (Exception ex) { /* Ignorar */ } // Opcional: fechar conexão de teste
        } else {
            System.err.println("Falha na conexão com o banco de dados na Tela Inicial!");
            // Considerar exibir um JOptionPane.showMessageDialog para o usuário em caso de falha crítica.
            // JOptionPane.showMessageDialog(this, 
            //                               "Não foi possível conectar ao banco de dados.\nAlgumas funcionalidades podem estar indisponíveis.",
            //                               "Erro de Conexão", 
            //                               JOptionPane.ERROR_MESSAGE);
        }
    }

    private void personalizarBotao(JButton botao, Color background, Color foreground) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Fonte maior para os botões
        botao.setBackground(background);
        botao.setForeground(foreground);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mão
        // Definir tamanho preferencial e máximo para os botões terem o mesmo tamanho
        Dimension btnSize = new Dimension(220, 50); // Tamanho maior e consistente
        botao.setPreferredSize(btnSize);
        botao.setMaximumSize(btnSize);
        botao.setMinimumSize(btnSize);

        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(background.darker(), 1), // Borda sutil
            new EmptyBorder(10, 20, 10, 20) // Padding interno
        ));
        
        // Efeito Hover simples
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
                 if (botao.contains(evt.getPoint())) { // Garante que o mouse ainda está sobre o botão
                    botao.setBackground(hoverBackground);
                } else {
                    botao.setBackground(originalBackground);
                }
            }
        });
    }

    public static void main(String[] args) {
        // É uma boa prática definir o Look and Feel no início do método main
        // para uma aparência mais consistente em diferentes OS, se desejado.
        try {
            // Tenta usar o Nimbus Look and Feel para uma aparência mais moderna
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Se Nimbus não estiver disponível, usa o Look and Feel padrão do sistema
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            new TelaInicial().setVisible(true);
        });
    }
}