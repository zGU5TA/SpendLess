package com.mycompany.projetoa3.telas.gasto; // Certifique-se que o pacote está correto

import java.util.Date;

public class Gasto {

    private int id;
    private String descricao;
    private double valor;
    private Date dataGasto;
    private int idCategoria;
    private String cpfUsuario;
    private String nomeCategoria; // Para armazenar o nome da categoria após JOIN
    private boolean ehRecorrente; // Novo campo para indicar se o gasto é recorrente

    public Gasto() {}

    // Construtor pode ser atualizado para incluir ehRecorrente, se necessário
    public Gasto(int id, String descricao, double valor, Date dataGasto, int idCategoria, boolean ehRecorrente) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataGasto = dataGasto;
        this.idCategoria = idCategoria;
        this.ehRecorrente = ehRecorrente;
        // cpfUsuario e nomeCategoria podem ser definidos separadamente ou via outro construtor
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getDataGasto() {
        return dataGasto;
    }

    public void setDataGasto(Date dataGasto) {
        this.dataGasto = dataGasto;
    }

    public int getIdCategoria() { // Convenção Java padrão
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) { // Convenção Java padrão
        this.idCategoria = idCategoria;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public boolean isEhRecorrente() {
        return ehRecorrente;
    }

    public void setEhRecorrente(boolean ehRecorrente) {
        this.ehRecorrente = ehRecorrente;
    }
}
