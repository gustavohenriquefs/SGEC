package com.casaculturaqxd.sgec.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public interface Service {
      /**
       * Cria um novo bucket com o nome especificado, se já existir um bucket com esse
       * nome lança
       * uma exceção
       * 
       * @param nomeBucket
       * @throws IllegalArgumentException
       */
      public void criarBucket(String nomeBucket) throws IllegalArgumentException;

      /**
       * Deleta o bucket com o nome passado, se não existir nenhum bucket com esse
       * nome lança uma
       * exceção
       * 
       * @param nomeBucket
       * @throws IllegalArgumentException
       */
      public void deletarBucket(String nomeBucket) throws IllegalArgumentException;

      /**
       * Captura os metadados do arquivo, como data de modificacao e tamanho
       * 
       * @param nomeBucket
       * @param chaveArquivo
       * @return serviceFile com os metadados encontrados
       * @throws IllegalArgumentException
       */
      public ServiceFile getMetadata(String nomeBucket, String chaveArquivo)
                  throws IllegalArgumentException;

      /**
       * Retorna um arquivo individual convertido para uma java.io.File, levanta uma
       * exceção caso o
       * nome do bucket ou do arquivo sejam inválidos
       * 
       * @param nomeBucket
       * @param chaveArquivo
       * @throws IllegalArgumentException
       */
      public File getArquivo(String nomeBucket, String chaveArquivo)
                  throws IllegalArgumentException;

      /**
       * realiza upload do arquivo para o bucket especificado, salvando-o com a chave
       * de destino
       * escolhida retorna uma exceção caso o bucket ou o arquivo sejam inválidos
       * 
       * @param nomeBucket
       * @param chaveDestino
       * @param arquivo
       * @throws IllegalArgumentException
       * @throws IOException
       */
      public void enviarArquivo(ServiceFile serviceFile)
                  throws IllegalArgumentException, IOException;

      /**
       * Retorna uma lista com o nome de todos os arquivos no bucket especificado
       * 
       * @param nomeBucket
       * @throws IllegalArgumentException
       */
      public List<String> listarArquivos(String nomeBucket) throws IllegalArgumentException;

      /**
       * Remove o arquivo com a chave especificada do bucket lança uma exceção caso a
       * chave ou o
       * nome do bucket sejam inválidos
       * 
       * @param nomeBucket
       * @param chaveArquivo
       * @throws IllegalArgumentException
       */
      public void deletarArquivo(String nomeBucket, String chaveArquivo)
                  throws IllegalArgumentException;
}
