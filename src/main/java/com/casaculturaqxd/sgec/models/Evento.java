package com.casaculturaqxd.sgec.models;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import com.casaculturaqxd.sgec.models.arquivo.ServiceFile;

public class Evento {
    private Integer idEvento;
    private String nome;
    private ServiceFile imagemCapa;
    private String descricao;
    private int publicoEsperado;
    private int publicoAlcancado;
    private int numParticipantesEsperado;
    private int numParticipantesAlcancado;
    private int numMunicipiosEsperado;
    private int numMunicipiosAlcancado;
    private int numColaboradoresEsperado;

    private int numColaboradoresAlcancado;
    private Date dataInicial;
    private Date dataFinal;
    private Time horario;
    private Time cargaHoraria;
    private String classificacaoEtaria;
    private boolean certificavel;
    private boolean acessivelEmLibras;
    private Date cadastradoEm;

    private GrupoEventos grupoEventos;
    private ArrayList<Integer> locais;
    private ArrayList<Integer> listaParticipantes;
    private ArrayList<ServiceFile> listaArquivos;
    private ArrayList<Instituicao> listaOrganizadores;
    private ArrayList<Instituicao> listaColaboradores;
    private ArrayList<Meta> listaMetas;

    public Evento() {

    }

    public Evento(int idEvento) {
        this.idEvento = idEvento;
    }

    public ArrayList<Meta> getListaMetas() {
        return listaMetas;
    }

    public void setListaMetas(ArrayList<Meta> listaMetas) {
        this.listaMetas = listaMetas;
    }

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

    public ServiceFile getImagemCapa() {
        return imagemCapa;
    }

    public void setImagemCapa(ServiceFile imagemCapa) {
        this.imagemCapa = imagemCapa;
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

    public Time getHorario() {
        return horario;
    }

    public void setHorario(Time horario) {
        this.horario = horario;
    }

    public Time getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Time cargaHoraria) {
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

    public int getNumParticipantesEsperado() {
        return numParticipantesEsperado;
    }

    public void setNumParticipantesEsperado(int participantesEsperado) {
        this.numParticipantesEsperado = participantesEsperado;
    }

    public int getNumMunicipiosEsperado() {
        return numMunicipiosEsperado;
    }

    public void setNumMunicipiosEsperado(int municipiosEsperado) {
        this.numMunicipiosEsperado = municipiosEsperado;
    }

    public int getPublicoAlcancado() {
        return publicoAlcancado;
    }

    public void setPublicoAlcancado(int publicoAlcancado) {
        this.publicoAlcancado = publicoAlcancado;
    }

    public int getNumParticipantesAlcancado() {
        return numParticipantesAlcancado;
    }

    public void setNumParticipantesAlcancado(int numParticipantesAlcancado) {
        this.numParticipantesAlcancado = numParticipantesAlcancado;
    }

    public int getNumMunicipiosAlcancado() {
        return numMunicipiosAlcancado;
    }

    public void setNumMunicipiosAlcancado(int numMunicipiosAlcancado) {
        this.numMunicipiosAlcancado = numMunicipiosAlcancado;
    }

    public int getNumColaboradoresEsperado() {
        return numColaboradoresEsperado;
    }

    public void setNumColaboradoresEsperado(int numColaboradoresEsperado) {
        this.numColaboradoresEsperado = numColaboradoresEsperado;
    }

    public int getNumColaboradoresAlcancado() {
        return numColaboradoresAlcancado;
    }

    public void setNumColaboradoresAlcancado(int numColaboradoresAlcancado) {
        this.numColaboradoresAlcancado = numColaboradoresAlcancado;
    }

    public GrupoEventos getGrupoEventos() {
        return grupoEventos;
    }

    public void setGrupoEventos(GrupoEventos grupoEventos) {
        this.grupoEventos = grupoEventos;
    }

    public String getClassificacaoEtaria() {
        return classificacaoEtaria;
    }

    public void setClassificacaoEtaria(String classificacaoEtaria) {

        this.classificacaoEtaria = classificacaoEtaria;
    }

    public List<Integer> getLocais() {
        return locais;
    }

    public void setLocais(ArrayList<Integer> locais) {
        this.locais = locais;
    }

    public void addLocal(Integer local) {
        this.locais.add(local);
    }

    public boolean removeLocal(Integer local) {
        return this.locais.remove(local);
    }

    public List<Integer> getListaParticipantes() {
        return listaParticipantes;
    }

    public void setListaParticipantes(ArrayList<Integer> listaParticipantes) {
        this.listaParticipantes = listaParticipantes;
    }

    public void addParticipante(Integer participante) {
        this.listaParticipantes.add(participante);
    }

    public boolean removeParticipante(Integer participante) {
        return this.listaParticipantes.remove(participante);
    }

    public ArrayList<Instituicao> getListaOrganizadores() {
        return listaOrganizadores;
    }

    public void setListaOrganizadores(ArrayList<Instituicao> listaOrganizadores) {
        this.listaOrganizadores = listaOrganizadores;
    }

    public void addOrganizador(Instituicao organizador) {
        this.listaOrganizadores.add(organizador);
    }

    public boolean removeOrganizador(Instituicao organizador) {
        return this.listaOrganizadores.remove(organizador);
    }

    public ArrayList<Instituicao> getListaColaboradores() {
        return listaColaboradores;
    }

    public void setListaColaboradores(ArrayList<Instituicao> listaColaboradores) {
        this.listaColaboradores = listaColaboradores;
    }

    public void addColaborador(Instituicao colaborador) {
        this.listaColaboradores.add(colaborador);
    }

    public boolean removeColaborador(Instituicao colaborador) {
        return this.listaColaboradores.remove(colaborador);
    }

    public ArrayList<ServiceFile> getListaArquivos() {
        return listaArquivos;
    }

    public void setListaArquivos(ArrayList<ServiceFile> listaArquivos) {
        this.listaArquivos = listaArquivos;
    }

    public void addArquivo(ServiceFile arquivo) {
        this.listaArquivos.add(arquivo);
    }

    public boolean removeArquivo(ServiceFile arquivo) {
        return this.listaArquivos.remove(arquivo);
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    @Override
    public String toString() {
        return "Evento [idEvento=" + idEvento + ", nome=" + nome + ", descricao=" + descricao + ", publicoEsperado="
                + publicoEsperado + ", publicoAlcancado=" + publicoAlcancado + ", participantesEsperado="
                + numParticipantesEsperado + ", municipiosEsperado=" + numMunicipiosEsperado + ", dataInicial="
                + dataInicial + ", dataFinal=" + dataFinal + ", horario=" + horario + ", cargaHoraria=" + cargaHoraria
                + ", classificacaoEtaria=" + classificacaoEtaria + ", certificavel=" + certificavel
                + ", acessivelEmLibras=" + acessivelEmLibras + ", locais=" + locais + ", listaParticipantes="
                + listaParticipantes + ", listaOrganizadores=" + listaOrganizadores + ", listaColaboradores="
                + listaColaboradores + ", listaArquivos=" + listaArquivos + "]";
    }

    public Date getCadastradoEm() {
        return cadastradoEm;
    }

    public void setCadastradoEm(Date cadastradoEm) {
        this.cadastradoEm = cadastradoEm;

    }

}
