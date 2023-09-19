package com.casaculturaqxd.sgec.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.enums.ClassificacaoEtaria;
import com.casaculturaqxd.sgec.models.arquivo.Arquivo;

public class Evento {
    private String nome;
    private String descricao;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private LocalTime horario;
    private LocalTime cargaHoraria;
    private ClassificacaoEtaria classificacaoEtaria;
    private boolean certificavel;
    private boolean acessivelEmLibras;
    SortedSet<Localizacao> locais;
    SortedSet<Artista> listaArtistas;  
    SortedSet<Organizador> listaOrganizadores;
    SortedSet<Colaborador> listaColaborador;
    SortedSet<Arquivo> listaArquivos;
    /* TODO: indicadores pertencentes a um evento */

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public LocalDate getDataInicial() {
        return dataInicial;
    }
    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }
    public LocalDate getDataFinal() {
        return dataFinal;
    }
    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }
    public LocalTime getHorario() {
        return horario;
    }
    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }
    public LocalTime getCargaHoraria() {
        return cargaHoraria;
    }
    public void setCargaHoraria(LocalTime cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
    public boolean isCertificavel() {
        return certificavel;
    }
    public void setCertificavel(boolean certificavel) {
        this.certificavel = certificavel;
    }
    public boolean isAcessivelEmLibras() {
        return acessivelEmLibras;
    }
    public void setAcessivelEmLibras(boolean acessivelEmLibras) {
        this.acessivelEmLibras = acessivelEmLibras;
    }
   
}



