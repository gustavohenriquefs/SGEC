package com.casaculturaqxd.sgec.models;


public class User {
    private String nomeUsuario;
    private String email;
    private String senha;

    
    public User(String email, String senha) {
        this.email = email;
        this.senha = senha;
        this.nomeUsuario = null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome_usuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}
