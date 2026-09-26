package com.mycompany.projetoa3.telas; // Certifique-se que o pacote está correto

import com.mycompany.projetoa3.ConexaoDB;
import com.mycompany.projetoa3.SessaoUsuario;
import com.mycompany.projetoa3.TelaInicial;
import com.mycompany.projetoa3.Usuario;
import com.mycompany.projetoa3.Navegacao; // Import para atualizar saudação

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.net.URL; // Não é mais necessário para ícones

public class TelaPerfil extends JPanel {
    private String cpfUsuario;
    private JLabel lblNomeValor;
    private JLabel lblTelefoneValor;
    private JLabel lblEmailValor;

    public TelaPerfil(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
        initComponents();
        carregarDadosUsuario(); // Carrega os dados ao iniciar a tela
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 50, 30, 50));
        setBackground(Color.DARK_GRAY.darker());

        // Painel do Título
        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTitulo.setOpaque(false);
        JLabel lblTitulo = new JLabel("Meu Perfil");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        painelTitulo.add(lblTitulo);
        add(painelTitulo, BorderLayout.NORTH);

        // Painel de Informações
        JPanel painelInfo = new JPanel(new GridBagLayout());
        painelInfo.setOpaque(false);
        painelInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY), "Informações Pessoais",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 16), Color.LIGHT_GRAY
                ),
                new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color labelColor = Color.LIGHT_GRAY;
        Color valueColor = Color.WHITE;

        // Nome
        JLabel lblNomeTitulo = new JLabel("Nome:");
        lblNomeTitulo.setFont(labelFont);
        lblNomeTitulo.setForeground(labelColor);
        painelInfo.add(lblNomeTitulo, gbc);
        
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        lblNomeValor = new JLabel("Carregando...");
        lblNomeValor.setFont(valueFont);
        lblNomeValor.setForeground(valueColor);
        painelInfo.add(lblNomeValor, gbc);

        // Telefone
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblTelefoneTitulo = new JLabel("Telefone:");
        lblTelefoneTitulo.setFont(labelFont);
        lblTelefoneTitulo.setForeground(labelColor);
        painelInfo.add(lblTelefoneTitulo, gbc);

        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        lblTelefoneValor = new JLabel("Carregando...");
        lblTelefoneValor.setFont(valueFont);
        lblTelefoneValor.setForeground(valueColor);
        painelInfo.add(lblTelefoneValor, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblEmailTitulo = new JLabel("Email:");
        lblEmailTitulo.setFont(labelFont);
        lblEmailTitulo.setForeground(labelColor);
        painelInfo.add(lblEmailTitulo, gbc);

        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        lblEmailValor = new JLabel("Carregando...");
        lblEmailValor.setFont(valueFont);
        lblEmailValor.setForeground(valueColor);
        painelInfo.add(lblEmailValor, gbc);
        
        // Painel de preenchimento para empurrar os campos para cima
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;  
        painelInfo.add(new JPanel(){{setOpaque(false);}}, gbc);

        add(painelInfo, BorderLayout.CENTER);

        // Painel de Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painelBotoes.setOpaque(false);

        JButton btnEditar = new JButton("Editar Dados");
        personalizarBotao(btnEditar, new Color(70, 130, 180), Color.WHITE);
        btnEditar.addActionListener(e -> {
            TelaEdicao telaEdicao = new TelaEdicao(cpfUsuario);
            telaEdicao.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    carregarDadosUsuario();
                    Window ancestor = SwingUtilities.getWindowAncestor(TelaPerfil.this);
                    if (ancestor instanceof Navegacao) {
                        ((Navegacao) ancestor).atualizarSaudacaoPublic(); 
                    }
                }
            });
            telaEdicao.setVisible(true);
        });

        JButton btnExcluir = new JButton("Excluir Conta");
        personalizarBotao(btnExcluir, new Color(220, 53, 69), Color.WHITE);

        btnExcluir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    TelaPerfil.this,
                    "Tem certeza que deseja excluir sua conta?\nEsta ação é irreversível.",
                    "Confirmar Exclusão de Conta",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean sucesso = ConexaoDB.excluirUsuarioPorCpf(cpfUsuario);
                if (sucesso) {
                    JOptionPane.showMessageDialog(TelaPerfil.this, "Conta excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    // Limpar dados da sessão após exclusão bem-sucedida
                    SessaoUsuario.limparSessao(); // Adicionado para limpar a sessão

                    Window ancestor = SwingUtilities.getWindowAncestor(TelaPerfil.this);
                    if (ancestor != null) {
                        ancestor.dispose();
                    }
                    new TelaInicial().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(TelaPerfil.this, "Erro ao excluir conta. Verifique o console.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // NOVO BOTÃO SAIR
        JButton btnSair = new JButton("SAIR");
        personalizarBotao(btnSair, new Color(108, 117, 125), Color.WHITE); // Cor cinza para o botão SAIR

        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        TelaPerfil.this,
                        "Tem certeza que quer sair?",
                        "Confirmar Saída",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Limpar os dados da sessão do usuário atual
                    // Se sua classe SessaoUsuario não tiver um método limparSessao(),
                    // você pode setar os campos relevantes para null, por exemplo:
                    // SessaoUsuario.setCPFLogado(null);
                    // SessaoUsuario.setNomeUsuario(null);
                    SessaoUsuario.limparSessao(); // Assumindo que este método existe e limpa os dados da sessão

                    // Fecha a janela atual (que provavelmente é a tela de Navegação)
                    Window ancestor = SwingUtilities.getWindowAncestor(TelaPerfil.this);
                    if (ancestor != null) {
                        ancestor.dispose();
                    }

                    // Abre a TelaInicial
                    new TelaInicial().setVisible(true);
                }
            }
        });

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnSair); // Adiciona o botão SAIR ao painel
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    private void personalizarBotao(JButton botao, Color background, Color foreground) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setBackground(background);
        botao.setForeground(foreground);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background.darker(), 1),
                new EmptyBorder(10, 25, 10, 25) 
        ));
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

    public void carregarDadosUsuario() {
        Usuario usuario = ConexaoDB.buscarUsuarioPorCpf(cpfUsuario);
        if (usuario != null) {
            lblNomeValor.setText(usuario.getNome() != null ? usuario.getNome() : "Não informado");
            lblTelefoneValor.setText(usuario.getTelefone() != null && !usuario.getTelefone().isEmpty() ? usuario.getTelefone() : "Não informado");
            lblEmailValor.setText(usuario.getEmail() != null && !usuario.getEmail().isEmpty() ? usuario.getEmail() : "Não informado");
            SessaoUsuario.setNomeUsuario(usuario.getNome());
        } else {
            lblNomeValor.setText("Erro ao carregar");
            lblTelefoneValor.setText("Erro ao carregar");
            lblEmailValor.setText("Erro ao carregar");
            JOptionPane.showMessageDialog(this, "Não foi possível carregar os dados do perfil.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}