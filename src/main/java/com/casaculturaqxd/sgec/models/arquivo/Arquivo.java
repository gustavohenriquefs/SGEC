package com.casaculturaqxd.sgec.models.arquivo;

import java.io.ByteArrayInputStream;

public abstract class Arquivo {
    private String nome;
    private ByteArrayInputStream imagemPreview;

    
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


}
