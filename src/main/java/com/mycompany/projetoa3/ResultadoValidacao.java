package com.mycompany.projetoa3;

public class ResultadoValidacao {
    public Usuario usuario;
    public String mensagemErro;

    public ResultadoValidacao(Usuario usuario, String mensagemErro) {
        this.usuario = usuario;
        this.mensagemErro = mensagemErro;
    }
}
