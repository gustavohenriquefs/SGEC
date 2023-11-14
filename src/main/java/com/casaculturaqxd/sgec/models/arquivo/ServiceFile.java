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

    public ServiceFile(Integer serviceFileId) {
        this.serviceFileId = serviceFileId;
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

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
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

    /**
     * Criado com source action vscode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((serviceFileId == null) ? 0 : serviceFileId.hashCode());
        result = prime * result + ((fileKey == null) ? 0 : fileKey.hashCode());
        result = prime * result + ((suffix == null) ? 0 : suffix.hashCode());
        result = prime * result + (int) (fileSize ^ (fileSize >>> 32));
        result = prime * result + ((service == null) ? 0 : service.hashCode());
        result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
        result = prime * result + ((ultimaModificacao == null) ? 0 : ultimaModificacao.hashCode());
        result = prime * result + ((preview == null) ? 0 : preview.hashCode());
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        return result;
    }

    /**
     * Criado com source action vscode
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceFile other = (ServiceFile) obj;
        if (serviceFileId == null) {
            if (other.serviceFileId != null)
                return false;
        } else if (!serviceFileId.equals(other.serviceFileId))
            return false;
        if (fileKey == null) {
            if (other.fileKey != null)
                return false;
        } else if (!fileKey.equals(other.fileKey))
            return false;
        if (suffix == null) {
            if (other.suffix != null)
                return false;
        } else if (!suffix.equals(other.suffix))
            return false;
        if (fileSize != other.fileSize)
            return false;
        if (service == null) {
            if (other.service != null)
                return false;
        } else if (!service.equals(other.service))
            return false;
        if (bucket == null) {
            if (other.bucket != null)
                return false;
        } else if (!bucket.equals(other.bucket))
            return false;
        if (ultimaModificacao == null) {
            if (other.ultimaModificacao != null)
                return false;
        } else if (!ultimaModificacao.equals(other.ultimaModificacao))
            return false;
        if (preview == null) {
            if (other.preview != null)
                return false;
        } else if (!preview.equals(other.preview))
            return false;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        return true;
    }

}
