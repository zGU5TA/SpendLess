package com.mycompany.projetoa3;


public class SessaoUsuario {
    private static String cpfUsuario;
    private static String nomeUsuario;
    private static String telefoneUsuario;
    private static String emailUsuario;

    public static void setDadosUsuario(String cpf, String nome, String telefone, String email) {
        cpfUsuario = cpf;
        nomeUsuario = nome;
        telefoneUsuario = telefone;
        emailUsuario = email;
    }
    
        public static void setNomeUsuario(String nome) {
        nomeUsuario = nome;
    }
        
    public static String getCpfUsuario() {
        return cpfUsuario;
    }

    public static String getNomeUsuario() {
        return nomeUsuario;
    }

    public static String getTelefoneUsuario() {
        return telefoneUsuario;
    }

    public static String getEmailUsuario() {
        return emailUsuario;
    }

    public static void limparSessao() {
        cpfUsuario = null;
        nomeUsuario = null;
        telefoneUsuario = null;
        emailUsuario = null;
    }
}