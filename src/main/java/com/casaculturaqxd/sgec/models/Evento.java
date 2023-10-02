package com.casaculturaqxd.sgec.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.SortedSet;

import com.casaculturaqxd.sgec.enums.ClassificacaoEtaria;
import com.casaculturaqxd.sgec.models.arquivo.Arquivo;

public class Evento {
    private int idEvento;
    private String nome;
    private String descricao;
    private int publico_esperado;
    private int publico_alcancado;
    private int participantes_esperado;
    private int municipios_esperado;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
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

    public int getPublico_esperado() {
        return publico_esperado;
    }
    
    public void setPublico_esperado(int publico_esperado) {
        this.publico_esperado = publico_esperado;
    }

    public int getParticipantes_esperado() {
        return participantes_esperado;
    }   

    public void setParticipantes_esperado(int participantes_esperado) {
        this.participantes_esperado = participantes_esperado;
    }

    public int getMunicipios_esperado() {
        return municipios_esperado;
    }

    public void setMunicipios_esperado(int municipios_esperado) {
        this.municipios_esperado = municipios_esperado;
    }

    public int getPublico_alcancado() {
        return publico_alcancado;
    }
    public void setPublico_alcancado(int publico_alcancado) {
        this.publico_alcancado = publico_alcancado;
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
    public int getIdEvento() {
        return idEvento;
    }
    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }
    
}



