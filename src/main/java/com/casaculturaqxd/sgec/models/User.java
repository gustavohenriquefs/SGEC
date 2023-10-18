package com.casaculturaqxd.sgec.models;


public class User {
    private int idUsuario;
    private String nomeUsuario;
    private String email;
    private String senha;
    private boolean editor;
    
    public User(){

    }
    
    public User(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public User(String nomeUsuario, String email, String senha, boolean editor) {
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.editor = editor;
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return this.senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeUsuario() {
        return this.nomeUsuario;
    }
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }


    public boolean isEditor() {
        return editor;
    }

    public void setEditor(boolean editor) {
        this.editor = editor;
    }
    
}
