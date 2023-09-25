package com.casaculturaqxd.sgec.models.arquivo;

import java.io.ByteArrayInputStream;

public abstract class Arquivo {
    private int idArquivo;
    private String nome;
    private boolean privado;
    private ByteArrayInputStream imagemPreview;
    private int idEvento;
    
    
    public Arquivo(int idArquivo, String nome, boolean privado, ByteArrayInputStream imagemPreview, int idEvento) {
        this.idArquivo = idArquivo;
        this.nome = nome;
        this.privado = privado;
        this.imagemPreview = imagemPreview;
        this.idEvento = idEvento;
    }
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public ByteArrayInputStream getImagemPreview() {
        return imagemPreview;
    }
    public void setImagemPreview(ByteArrayInputStream imagemPreview) {
        this.imagemPreview = imagemPreview;
    }
    public int getIdArquivo() {
        return idArquivo;
    }
    public void setIdArquivo(int idArquivo) {
        this.idArquivo = idArquivo;
    }
    public boolean isPrivado() {
        return privado;
    }
    public void setPrivado(boolean privado) {
        this.privado = privado;
    }

    public int getIdEvento() {
        return idEvento;
    }
    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }
    
}
