package com.casaculturaqxd.sgec.models.arquivo;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import com.casaculturaqxd.sgec.enums.ServiceType;
import com.casaculturaqxd.sgec.service.Service;
import com.casaculturaqxd.sgec.service.ServiceFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class ServiceFile {
    private Integer serviceFileId;
    private String fileKey;
    private String bucket;
    private Service service;
    private String suffix;
    private Long fileSize;
    private Date ultimaModificacao;
    private File preview;
    private File content;

    public ServiceFile(int serviceFileId) {
        this.serviceFileId = serviceFileId;
    }

    public ServiceFile(File content) {
        this.content = content;
        this.fileKey = content.getName();
        this.fileSize = content.length();
        this.suffix = findFileSuffix(content.getName());
        Dotenv dotenv = Dotenv.load();
        this.bucket = dotenv.get("BUCKET");
        this.service = ServiceFactory.getService(ServiceType.S3, "ACCESS_KEY", "SECRET_KEY");
    }

    public ServiceFile(File content, String bucket) {
        this.content = content;
        this.bucket = bucket;
        this.fileKey = content.getName();
        this.service = ServiceFactory.getService(ServiceType.S3, "ACCESS_KEY", "SECRET_KEY");
    }

    public ServiceFile(Integer serviceFileId, String bucket) {
        this.serviceFileId = serviceFileId;
        this.bucket = bucket;
        this.service = ServiceFactory.getService(ServiceType.S3, "ACCESS_KEY", "SECRET_KEY");
    }

    public ServiceFile(String fileKey, String bucket, String suffix, Date ultimaModificacao, long fileSize) {
        this.fileKey = fileKey;
        this.bucket = bucket;
        this.suffix = suffix;
        this.ultimaModificacao = ultimaModificacao;
        this.fileSize = fileSize;
        this.service = ServiceFactory.getService(ServiceType.S3, "ACCESS_KEY", "SECRET_KEY");
    }

    /**
     * copia os metadados de um outro objeto com mesmo nome e bucket
     * 
     * @param this
     * @param another
     * @return o arquivo atualizado com os metadados de another
     * @throws IllegalArgumentException caso o nome do arquivo ou do bucket seja
     *                                  diferente
     */
    public ServiceFile copyMetadata(ServiceFile another) {
        // se os dois objetos nao forem referencias a um mesmo arquivo
        if (!(this.getFileKey() == another.getFileKey() && this.getBucket() == another.getBucket())) {
            throw new IllegalArgumentException(
                    "arquivo " + this.getFileKey() + "nao possui os metadados de " + another.getFileKey());
        }
        if (this.getFileKey() == another.getFileKey() && this.getBucket() == another.getBucket()) {
            this.setUltimaModificacao(another.getUltimaModificacao());
            this.setFileSize(another.getFileSize());
            this.setSuffix(another.getSuffix());
        }
        return this;
    }

    public Integer getServiceFileId() {
        return serviceFileId;
    }

    public void setServiceFileId(Integer serviceFileId) {
        this.serviceFileId = serviceFileId;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Service getService() {
        return this.service;
    }

    public void setService(String serviceType) {
        this.service = ServiceFactory.getService(ServiceType.valueOf(serviceType.toUpperCase()), "ACCESS_KEY",
                "SECRET_KEY");
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public File getContent() throws IOException {
        return content;
    }

    public void setContent(File content) {
        this.content = content;
    }

    public File getPreview() {
        return preview;
    }

    public void setPreview(File preview) {
        this.preview = preview;
    }

    public Date getUltimaModificacao() {
        return ultimaModificacao;
    }

    public void setUltimaModificacao(Date ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    private String findFileSuffix(String fileName) {
        int previewStart = fileName.lastIndexOf(".");
        return fileName.substring(previewStart);
    }

    @Override
    public String toString() {
        return "ServiceFile [serviceFileId=" + serviceFileId + ", fileKey=" + fileKey + ", service=" + service
                + ", bucket=" + bucket + ", ultimaModificacao=" + ultimaModificacao + ", preview=" + preview
                + ", content=" + content + "]";
    }

}
