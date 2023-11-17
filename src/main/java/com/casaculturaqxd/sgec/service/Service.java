package com.casaculturaqxd.sgec.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public interface Service {
      /**
       * Cria um novo bucket com o nome especificado
       * 
       * @param nomeBucket
       * @throws IllegalArgumentException se o bucket ja existir
       */
      public void criarBucket(String nomeBucket) throws IllegalArgumentException;

      /**
       * Deleta o bucket com o nome passado
       * 
       * @param nomeBucket
       * @throws IllegalArgumentException se nao existir um bucket com esse nome
       */
      public void deletarBucket(String nomeBucket) throws IllegalArgumentException;

      /**
       * Captura os metadados do arquivo, como data de modificacao e tamanho
       * 
       * @param nomeBucket
       * @param chaveArquivo
       * @return wrapper do arquivo com os metadados encontrados
       * @throws IllegalArgumentException se o arquivo nao for encontrado
       */
      public ServiceFile getMetadata(String nomeBucket, String chaveArquivo) throws IllegalArgumentException;

      /**
       * Retorna um arquivo individual convertido para uma java.io.File
       * 
       * @param nomeBucket
       * @param chaveArquivo
       * @throws IOException              se a transferencia do conteudo falhar
       * @throws IllegalArgumentException se o arquivo nao for encontrado
       */
      public File getArquivo(String nomeBucket, String chaveArquivo) throws IllegalArgumentException, IOException;

      /**
       * realiza upload do arquivo para o bucket especificado, salvando-o com a chave
       * de destino escolhida retorna uma exceção caso o bucket ou o arquivo sejam
       * inválidos
       * 
       * @param serviceFile wrapper de conteudo de arquivo
       * @throws IllegalArgumentException - se ja existir uma chave com o mesmo nome
       *                                  do arquivo no bucket
       * @throws IOException              - se a operacao com o conteudo falhar
       */
      public void enviarArquivo(ServiceFile serviceFile) throws IllegalArgumentException, IOException;

      /**
       * Retorna uma lista com o nome de todos os arquivos no bucket especificado
       * 
       * @param nomeBucket
       * @throws IllegalArgumentException se o bucket nao existir
       */
      public List<String> listarArquivos(String nomeBucket) throws IllegalArgumentException;

      /**
       * Remove o arquivo com a chave especificada do bucket lança uma exceção caso a
       * chave ou o nome do bucket sejam inválidos
       * 
       * @param nomeBucket
       * @param chaveArquivo
       * @throws IllegalArgumentException se o arquivo nao existir
       */
      public void deletarArquivo(String nomeBucket, String chaveArquivo) throws IllegalArgumentException;
}
