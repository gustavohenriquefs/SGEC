package com.casaculturaqxd.sgec.models;

import java.sql.Date;
import java.time.LocalTime;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.enums.ClassificacaoEtaria;
import com.casaculturaqxd.sgec.models.arquivo.Arquivo;

public class Evento {
    private Integer idEvento;
    private String nome;
    private String descricao;
    private int publicoEsperado;
    private int publicoAlcancado;
    private int participantesEsperado;
    private int municipiosEsperado;
    private Date dataInicial;
    private Date dataFinal;
    private LocalTime horario;
    private LocalTime cargaHoraria;
    private ClassificacaoEtaria classificacaoEtaria;
    private boolean certificavel;
    private boolean acessivelEmLibras;
    SortedSet<Integer> locais;
    SortedSet<Integer> listaParticipantes;  
    SortedSet<Integer> listaOrganizadores;
    SortedSet<Integer> listaColaboradores;
    SortedSet<Arquivo> listaArquivos;

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

    public Date getDataInicial() {
        return dataInicial;
    }
    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }
    public void setDataFinal(Date dataFinal) {
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

    public int getPublicoEsperado() {
        return publicoEsperado;
    }
    
    public void setPublicoEsperado(int publicoEsperado) {
        this.publicoEsperado = publicoEsperado;
    }

    public int getParticipantesEsperado() {
        return participantesEsperado;
    }   

    public void setParticipantesEsperado(int participantesEsperado) {
        this.participantesEsperado = participantesEsperado;
    }

    public int getMunicipiosEsperado() {
        return municipiosEsperado;
    }

    public void setMunicipiosEsperado(int municipiosEsperado) {
        this.municipiosEsperado = municipiosEsperado;
    }

    public int getPublicoAlcancado() {
        return publicoAlcancado;
    }
    public void setPublicoAlcancado(int publicoAlcancado) {
        this.publicoAlcancado = publicoAlcancado;
    }

    public ClassificacaoEtaria getClassificacaoEtaria() {
        return this.classificacaoEtaria;
    }

    public void setClassificacaoEtaria(ClassificacaoEtaria classificacaoEtaria) {
        this.classificacaoEtaria = classificacaoEtaria;
    }

    public SortedSet<Integer> getLocais() {
        return locais;
    }
    public void setLocais(SortedSet<Integer> locais) {
        this.locais = locais;
    }
    public void addLocal(Integer local){
        this.locais.add(local);
    }
    public boolean removeLocal(Integer local){
        return this.locais.remove(local);
    }

    public SortedSet<Integer> getListaParticipantes() {
        return listaParticipantes;
    }
    public void setListaParticipantes(SortedSet<Integer> listaParticipantes) {
        this.listaParticipantes = listaParticipantes;
    }
    public void addParticipante(Integer participante){
        this.listaParticipantes.add(participante);
    }
    public boolean removeParticipante(Integer participante){
        return this.listaParticipantes.remove(participante);
    }

    public SortedSet<Integer> getListaOrganizadores() {
        return listaOrganizadores;
    }
    public void setListaOrganizadores(SortedSet<Integer> listaOrganizadores) {
        this.listaOrganizadores = listaOrganizadores;
    }
    public void addOrganizador(Integer organizador){
        this.listaOrganizadores.add(organizador);
    }
    public boolean removeOrganizador(Instituicao organizador){
        return this.listaOrganizadores.remove(organizador);
    }

    public SortedSet<Integer> getListaColaboradores() {
        return listaColaboradores;
    }
    public void setListaColaboradores(SortedSet<Integer> listaColaboradores) {
        this.listaColaboradores = listaColaboradores;
    }
    public void addColaborador(Integer colaborador){
        this.listaColaboradores.add(colaborador);
    }
    public boolean removeColaborador(Integer colaborador){
        return this.listaColaboradores.remove(colaborador);
    }

    public SortedSet<Arquivo> getListaArquivos() {
        return listaArquivos;
    }
    public void setListaArquivos(SortedSet<Arquivo> listaArquivos) {
        this.listaArquivos = listaArquivos;
    }
    public void addArquivo(Arquivo arquivo){
        this.listaArquivos.add(arquivo);
    }
    public boolean removeArquivo(Arquivo arquivo){
        return this.listaArquivos.remove(arquivo);
    }
    public Integer getIdEvento() {
        return idEvento;
    }
    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }
    
}



