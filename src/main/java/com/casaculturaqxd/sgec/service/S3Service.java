package com.casaculturaqxd.sgec.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import io.github.cdimascio.dotenv.Dotenv;

public class S3Service implements Service{
   private AmazonS3 client;
   private Dotenv dotenv = Dotenv.load();

   public S3Service(String envAcessKey, String envSecretKey) {
      var credenciais = new BasicAWSCredentials(dotenv.get(envAcessKey),dotenv.get(envSecretKey));

      client = AmazonS3ClientBuilder.standard()
               .withCredentials(new AWSStaticCredentialsProvider(credenciais))
               .withRegion(Regions.US_EAST_2)
               .build();
   }

   @Override
   public void criarBucket(String nomeBucket) throws IllegalArgumentException {
      if(client.doesBucketExistV2(nomeBucket)){
         client.createBucket(nomeBucket);
      }
      else{
         throw new IllegalArgumentException();
      }
   }

   @Override
   public void deletarBucket(String nomeBucket) throws IllegalArgumentException {
      if(client.doesBucketExistV2(nomeBucket)){
         client.deleteBucket(nomeBucket);
      }
      else{
         throw new IllegalArgumentException();
      }
   }

   @Override
   public void enviarArquivo(String nomeBucket, String chaveDestino, File arquivo) throws IllegalArgumentException, IOException {
   }
   public ServiceFile getArquivo(String nomeBucket, String chaveArquivo){
      S3Object object = client.getObject(nomeBucket, chaveArquivo);
   }
   @Override
   public List<String> listarArquivos(String nomeBucket) throws IllegalArgumentException {
   }

   @Override
   public void deletarArquivo(String nomeBucket, String chaveArquivo) throws IllegalArgumentException {
      client.deleteObject(nomeBucket, chaveArquivo);
   }
}
