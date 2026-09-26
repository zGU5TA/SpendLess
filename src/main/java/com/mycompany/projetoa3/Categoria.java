package com.mycompany.projetoa3;

public class Categoria {
    private int idCategoria;
    private String nome;
    private int tipo; // agora é inteiro

    public Categoria(int idCategoria, String nome, int tipo) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.tipo = tipo;
    }

    public Categoria(String nome, int tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return nome + " (" + (tipo == 1 ? "G" : "R") + ")";
    }
}
