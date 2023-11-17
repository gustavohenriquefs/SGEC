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

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

import io.github.cdimascio.dotenv.Dotenv;

public class S3Service implements Service {
   private AmazonS3 client;
   private Dotenv dotenv = Dotenv.load();

   protected S3Service(String envAcessKey, String envSecretKey) {
      var credenciais = new BasicAWSCredentials(dotenv.get(envAcessKey), dotenv.get(envSecretKey));

      client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credenciais))
            .withRegion(Regions.US_EAST_2).build();
   }

   @Override
   public void criarBucket(String nomeBucket) throws ServiceOperationException {
      try {
         requestCriarBucket(nomeBucket);
      } catch (IllegalArgumentException e) {
         throw new ServiceOperationException("bucket ja existe", e);
      } catch (SdkClientException e) {
         throw new ServiceOperationException("erro cliente S3", e);
      }
   }

   private void requestCriarBucket(String nomeBucket) {
      if (client.doesBucketExistV2(nomeBucket)) {
         throw new IllegalArgumentException();
      }
      CreateBucketRequest request = new CreateBucketRequest(nomeBucket);
      client.createBucket(request);
   }

   @Override
   public void deletarBucket(String nomeBucket) throws ServiceOperationException {

      try {
         requestDeletarBucket(nomeBucket);
      } catch (IllegalArgumentException e) {
         throw new ServiceOperationException("bucket nao encontrado", e);
      } catch (SdkClientException e) {
         throw new ServiceOperationException("erro cliente S3", e);
      }
   }

   private void requestDeletarBucket(String nomeBucket) throws SdkClientException {
      if (!client.doesBucketExistV2(nomeBucket)) {
         throw new IllegalArgumentException();
      }
      DeleteBucketRequest request = new DeleteBucketRequest(nomeBucket);
      client.deleteBucket(request);
   }

   @Override
   public void enviarArquivo(ServiceFile serviceFile) throws ServiceOperationException {
      try {
         requestEnviarArquivo(serviceFile.getBucket(), serviceFile.getFileKey(), serviceFile.getContent());
      } catch (IllegalArgumentException e) {
         throw new ServiceOperationException("arquivo ja existe: " + serviceFile.getFileKey(), e);
      } catch (SdkClientException e) {
         throw new ServiceOperationException("erro cliente S3", e);
      } catch (IOException e) {
         throw new ServiceOperationException("erro carregando conteudo do arquivo: " + serviceFile.getFileKey(), e);
      }
   }

   private void requestEnviarArquivo(String nomeBucket, String chaveArquivo, File content) {
      if (client.doesObjectExist(nomeBucket, chaveArquivo)) {
         throw new IllegalArgumentException();
      }
      PutObjectRequest request = new PutObjectRequest(nomeBucket, chaveArquivo, content);
      client.putObject(request);
   }

   @Override
   public ServiceFile getMetadata(String nomeBucket, String chaveArquivo) throws ServiceOperationException {
      try {
         return requestGetMetadata(nomeBucket, chaveArquivo);
      } catch (IllegalArgumentException e) {
         throw new ServiceOperationException("arquivo nao encontrado: " + chaveArquivo, e);
      } catch (SdkClientException e) {
         throw new ServiceOperationException("erro cliente S3", e);
      }
   }

   private ServiceFile requestGetMetadata(String nomeBucket, String chaveArquivo) {
      if (!client.doesObjectExist(nomeBucket, chaveArquivo)) {
         throw new IllegalArgumentException();
      }

      GetObjectRequest request = new GetObjectRequest(nomeBucket, chaveArquivo);
      S3Object object = client.getObject(request);
      var metadata = object.getObjectMetadata();
      Date ultimaModificacao = new java.sql.Date(metadata.getLastModified().getTime());
      long fileSize = metadata.getContentLength();
      String suffix = findFileSuffix(chaveArquivo);
      return new ServiceFile(suffix, nomeBucket, ultimaModificacao, null);
   }

   /**
    * captura o conteudo do objeto em uma InputStream depois cria um arquivo
    * temporário com esse conteúdo, por fim retorna um service file desse conteudo
    * 
    * @throws ServiceOperationException
    */
   @Override
   public File getArquivo(String nomeBucket, String chaveArquivo) throws ServiceOperationException {
      try {
         return requestGetArquivo(nomeBucket, chaveArquivo);
      } catch (IllegalArgumentException e) {
         throw new ServiceOperationException("arquivo nao encontrado: " + chaveArquivo, e);
      } catch (SdkClientException e) {
         throw new ServiceOperationException("erro cliente S3", e);
      } catch (IOException e) {
         throw new ServiceOperationException("erro carregando conteudo do arquivo: " + chaveArquivo, e);
      }
   }

   private File requestGetArquivo(String nomeBucket, String chaveArquivo) throws IOException {
      if (!client.doesObjectExist(nomeBucket, chaveArquivo)) {
         throw new IllegalArgumentException();
      }

      GetObjectRequest request = new GetObjectRequest(nomeBucket, chaveArquivo);
      S3Object object = client.getObject(request);
      InputStream content = object.getObjectContent();
      File newFile;
      newFile = File.createTempFile(chaveArquivo, findFileSuffix(chaveArquivo));
      newFile.deleteOnExit();
      OutputStream output = new FileOutputStream(newFile, false);
      content.transferTo(output);
      return newFile;
   }

   /**
    * Baseado no codigo disponivel neste
    * <a href="https://github.com/feltex/aws-s3">link</a>
    * 
    * @throws ServiceOperationException
    */
   @Override
   public List<String> listarArquivos(String nomeBucket) throws ServiceOperationException {
      try {
         return requestListaArquivos(nomeBucket);
      } catch (IllegalArgumentException e) {
         throw new ServiceOperationException("bucket nao encontrado", e);
      } catch (SdkClientException e) {
         throw new ServiceOperationException("erro cliente S3:" + e.getMessage());
      }
   }

   private List<String> requestListaArquivos(String nomeBucket) {
      if (!client.doesBucketExistV2(nomeBucket)) {
         throw new IllegalArgumentException();
      }
      ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(nomeBucket);
      return client.listObjectsV2(request).getObjectSummaries().stream().map(sumario -> sumario.getKey())
            .collect(Collectors.toList());
   }

   @Override
   public void deletarArquivo(String nomeBucket, String chaveArquivo) throws ServiceOperationException {
      try {
         requestDeleteArquivo(nomeBucket, chaveArquivo);
      } catch (IllegalArgumentException e) {
         throw new ServiceOperationException("arquivo nao encontrado: " + chaveArquivo, e);
      } catch (SdkClientException e) {
         throw new ServiceOperationException("erro cliente S3:" + e.getMessage());
      }
   }

   private void requestDeleteArquivo(String nomeBucket, String chaveArquivo) {
      if (!client.doesObjectExist(nomeBucket, chaveArquivo)) {
         throw new IllegalArgumentException("arquivo nao encontrado: " + chaveArquivo);
      }
      DeleteObjectRequest request = new DeleteObjectRequest(nomeBucket, chaveArquivo);
      client.deleteObject(request);
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

   @Override
   public String toString() {
      return "s3";
   }
}
