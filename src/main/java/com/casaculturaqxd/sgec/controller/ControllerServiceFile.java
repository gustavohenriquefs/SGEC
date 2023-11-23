package com.casaculturaqxd.sgec.controller;

import java.io.IOException;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

/**
 * Interface para todo controller que realize alguma operação com arquivos
 */

public interface ControllerServiceFile {
    /** adicionar um novo arquivo e carregar o preview dele */
    public void adicionarArquivo(ServiceFile serviceFile) throws IOException;

    /**
     * chamado a partir do preview de arquivo, remove o preview do arquivo
     * correspondente
     */
    public void removerArquivo(ServiceFile serviceFile);
}
