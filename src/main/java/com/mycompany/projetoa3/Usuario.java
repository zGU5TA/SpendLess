package com.mycompany.projetoa3;

public class Usuario {
    private String cpf;
    private String nome;
    private String telefone;
    private String email;
    private String tipo;  // novo campo tipo

    public Usuario(String cpf, String nome, String telefone, String email, String tipo) {
        this.cpf = cpf;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.tipo = tipo;
    }
    
    public Usuario(){
    
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public String getTipo() { return tipo; }
    
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setNome(String nome) { this.nome = nome; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEmail(String email) { this.email = email; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
