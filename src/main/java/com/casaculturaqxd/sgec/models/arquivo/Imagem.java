package com.casaculturaqxd.sgec.models.arquivo;

import java.io.ByteArrayInputStream;

public class Imagem extends Arquivo{
    public Imagem(int idArquivo, String nome, boolean privado, ByteArrayInputStream imagemPreview, int idEvento){
        super(idArquivo, nome, privado, imagemPreview, idEvento);
    }
}
