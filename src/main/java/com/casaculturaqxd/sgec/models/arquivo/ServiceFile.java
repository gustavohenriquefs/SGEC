package com.casaculturaqxd.sgec.models.arquivo;

import java.io.File;
import java.sql.Date;

public class ServiceFile {
    private Integer serviceFileId;
    private String fileKey;
    private String service;
    private String bucket;
    private Date ultimaModificacao;
    private File preview;
    private File content;
    
    public ServiceFile(File content,String bucket) {
        this.content = content;
        this.bucket = bucket;
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
    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }
    public String getBucket() {
        return bucket;
    }
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
    public File getContent() {
        return content;
    }
    public void setContent(File content) {
        this.content = content;
    }

    public Date getUltimaModificacao() {
        return ultimaModificacao;
    }

    public void setUltimaModificacao(Date ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }

}
