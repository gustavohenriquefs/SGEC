package com.casaculturaqxd.sgec.service;

import java.io.File;
import java.util.List;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public interface Service {
      /**
       * Cria um novo bucket com o nome especificado
       * 
       * @param nomeBucket
       */
      public void criarBucket(String nomeBucket) throws ServiceOperationException;

      /**
       * Deleta o bucket com o nome passado
       * 
       * @param nomeBucket
       */
      public void deletarBucket(String nomeBucket) throws ServiceOperationException;

      /**
       * Captura os metadados do arquivo, como data de modificacao e tamanho
       * 
       * @param nomeBucket
       * @param chaveArquivo
       * @return wrapper do arquivo com os metadados encontrados
       */
      public ServiceFile getMetadata(String nomeBucket, String chaveArquivo) throws ServiceOperationException;

      /**
       * Retorna um arquivo individual convertido para uma java.io.File
       * 
       * @param nomeBucket
       * @param chaveArquivo
       * @throws ServiceOperationException
       */
      public File getArquivo(String nomeBucket, String chaveArquivo) throws ServiceOperationException;

      /**
       * realiza upload do arquivo para o bucket especificado, salvando-o com a chave
       * de destino escolhida retorna uma exceção caso o bucket ou o arquivo sejam
       * inválidos
       * 
       * @param serviceFile wrapper de conteudo de arquivo
       */
      public void enviarArquivo(ServiceFile serviceFile) throws ServiceOperationException;

      /**
       * Retorna uma lista com o nome de todos os arquivos no bucket especificado
       * 
       * @param nomeBucket
       */
      public List<String> listarArquivos(String nomeBucket) throws ServiceOperationException;

      /**
       * Remove o arquivo com a chave especificada do bucket lança uma exceção caso a
       * chave ou o nome do bucket sejam inválidos
       * 
       * @param nomeBucket
       * @param chaveArquivo
       */
      public void deletarArquivo(String nomeBucket, String chaveArquivo) throws ServiceOperationException;
}
