package com.casaculturaqxd.sgec.models.arquivo;

import java.io.File;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.casaculturaqxd.sgec.enums.ServiceType;
import com.casaculturaqxd.sgec.service.Service;
import com.casaculturaqxd.sgec.service.ServiceFactory;
import io.github.cdimascio.dotenv.Dotenv;

public class ServiceFile {
    private Integer serviceFileId;
    private String fileKey;
    private String suffix;
    private long fileSize;
    private Service service;
    private String bucket;
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

    public ServiceFile(String fileKey, String bucket, Date ultimaModificacao, File content) {
        this.fileKey = fileKey;
        this.bucket = bucket;
        this.ultimaModificacao = ultimaModificacao;
        this.content = content;
        this.service = ServiceFactory.getService(ServiceType.S3, "ACCESS_KEY", "SECRET_KEY");
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

    public File getContent() {
        if (this.content == null) {
            this.content = service.getArquivo(bucket, fileKey);
        }
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
        return "ServiceFile [serviceFileId=" + serviceFileId + ", fileKey=" + fileKey + ", service="
                + service + ", bucket=" + bucket + ", ultimaModificacao=" + ultimaModificacao
                + ", preview=" + preview + ", content=" + content + "]";
    }

}
