package com.casaculturaqxd.sgec.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import io.github.cdimascio.dotenv.Dotenv;

public class S3Service implements Service {
   private AmazonS3 client;
   private Dotenv dotenv = Dotenv.load();

   protected S3Service(String envAcessKey, String envSecretKey) {
      var credenciais = new BasicAWSCredentials(dotenv.get(envAcessKey), dotenv.get(envSecretKey));

      client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credenciais))
            .withRegion(Regions.US_EAST_2).build();
   }

   @Override
   public void criarBucket(String nomeBucket) throws IllegalArgumentException {
      if (!client.doesBucketExistV2(nomeBucket)) {
         client.createBucket(nomeBucket);
      } else {
         throw new IllegalArgumentException();
      }
   }

   @Override
   public void deletarBucket(String nomeBucket) throws IllegalArgumentException {
      if (client.doesBucketExistV2(nomeBucket)) {
         client.deleteBucket(nomeBucket);
      } else {
         throw new IllegalArgumentException();
      }
   }

   @Override
   public void enviarArquivo(ServiceFile serviceFile) throws IllegalArgumentException, IOException {
      if (!client.doesObjectExist(serviceFile.getBucket(), serviceFile.getFileKey())) {
         client.putObject(serviceFile.getBucket(), serviceFile.getFileKey(),
               serviceFile.getContent());
      } else {
         throw new IllegalArgumentException();
      }
   }

   /**
    * captura o conteudo do objeto em uma InputStream depois cria um arquivo temporário com esse
    * conteúdo, por fim retorna um service file desse conteudo
    */
   public ServiceFile getArquivo(String nomeBucket, String chaveArquivo) {
      var object = client.getObject(nomeBucket, chaveArquivo);
      InputStream content = object.getObjectContent();
      Date ultimaModificacao =
            new java.sql.Date(object.getObjectMetadata().getLastModified().getTime());
      File newFile;

      try {
         newFile = File.createTempFile(chaveArquivo, findFileSuffix(chaveArquivo));
         newFile.deleteOnExit();
         OutputStream output = new FileOutputStream(newFile, false);
         content.transferTo(output);
         return new ServiceFile(chaveArquivo, nomeBucket, ultimaModificacao, newFile);
      } catch (IOException e) {
         e.printStackTrace();
      }

      return null;
   }

   /**
    * Baseado no codigo disponivel neste <a href="https://github.com/feltex/aws-s3">link</a>
    */
   @Override
   public List<String> listarArquivos(String nomeBucket) throws IllegalArgumentException {
      var listaObjetos = client.listObjects(nomeBucket);
      return listaObjetos.getObjectSummaries().stream().map(sumario -> sumario.getKey())
            .collect(Collectors.toList());
   }

   @Override
   public void deletarArquivo(String nomeBucket, String chaveArquivo)
         throws IllegalArgumentException {
      if (client.doesObjectExist(nomeBucket, chaveArquivo)) {
         client.deleteObject(nomeBucket, chaveArquivo);
      } else {
         throw new IllegalArgumentException();
      }
   }

   private String findFileSuffix(String fileName) {
      Pattern pattern = Pattern.compile("\\..*");
      Matcher matcher = pattern.matcher(fileName);
      if (matcher.find()) {
         String suffix = matcher.group(0);
         return suffix;
      }
      return null;
   }
}
