package com.casaculturaqxd.sgec.controller;


import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.DAO.EventoDAO;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.Evento;
import com.casaculturaqxd.sgec.models.Indicador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;

public class VisualizarEventoController {
    private Evento evento;
    private DatabasePostgres db = DatabasePostgres.getInstance("URL","USER_NAME","PASSWORD");
    private EventoDAO eventoDAO = new EventoDAO();; 
    @FXML
    AnchorPane page,headerField, secaoArquivos;
    @FXML 
    TextArea descricao;
    @FXML 
    Label titulo;
    @FXML 
    DatePicker dataInicial, dataFinal;
    @FXML
    TextField cargaHoraria, horario;
    @FXML 
    CheckBox certificavel, libras;
    @FXML
    ChoiceBox<String> classificacaoEtaria;
    @FXML
    private String[] classificacoes = {"Livre","10 anos","12 anos","14 anos","16 anos","18 anos"};
    //Tabela com todos os campos de input
    ObservableList<Control> camposInput = FXCollections.observableArrayList();  
    //Tabelas de indicadores
    @FXML
    TableView<Indicador> tabelaIndicadoresGerais,tabelaIndicadoresMeta1,tabelaIndicadoresMeta2;
    ObservableList<TableView<Indicador>> tabelas = FXCollections.observableArrayList();
    
    @FXML
    Button novoParticipante,novoOrganizador,novoColaborador,salvarAlteracoes,adicionarArquivo,visualizarTodos;

    public void initialize() throws IOException{
        addControls(page, camposInput);
        addPropriedadeAlterar(camposInput);

        FXMLLoader carregarMenu = new FXMLLoader(App.class.getResource("view/componentes/menu.fxml"));
        headerField.getChildren().add(carregarMenu.load());

        eventoDAO.setConnection(db.getConnection());
        Evento eventoMock = new Evento();
        //capturando evento de mock do banco
        eventoMock.setIdEvento(1);
        setEvento(eventoDAO.buscarEvento(eventoMock).get());
        loadContent();

        tabelas.addAll(tabelaIndicadoresGerais,tabelaIndicadoresMeta1,tabelaIndicadoresMeta2);
        for(TableView<Indicador> tabela : tabelas){
            loadTable(tabela);
        }
        Indicador numeroPublico = new Indicador("Quantidade de público", evento.getPublicoEsperado(), evento.getPublicoAlcancado());
        Indicador numeroMestres = new Indicador("Número de mestres da cultura", evento.getParticipantesEsperado(), evento.getListaParticipantes().size());
        Indicador numeroMunicipios = new Indicador("Número de municípios", evento.getMunicipiosEsperado(), eventoDAO.getNumeroMunicipiosDiferentes(evento.getIdEvento()));

        addIndicador(tabelaIndicadoresGerais, numeroPublico);
        addIndicador(tabelaIndicadoresMeta1, numeroMestres);
        addIndicador(tabelaIndicadoresMeta2, numeroMunicipios);

        /* TODO: adicionar funcionalidade de arquivos e
         *  reativar o botao
         */
        temporaryHideUnimplementedFields();
    }
    private void loadContent(){
        classificacaoEtaria.getItems().addAll(classificacoes);
        titulo.setText(evento.getNome());
        descricao.setText(evento.getDescricao());
        classificacaoEtaria.getSelectionModel().select(evento.getClassificacaoEtaria());
        if(evento.getHorario() != null){
            horario.setText(evento.getHorario().toString());
        }
        if(evento.getDataInicial() != null){
        dataInicial.setValue(evento.getDataInicial().toLocalDate());
        }
        if(evento.getDataFinal() != null){
        dataFinal.setValue(evento.getDataFinal().toLocalDate());
        }

        if(evento.getCargaHoraria() != null){
        cargaHoraria.setText(String.valueOf(evento.getCargaHoraria().toString()));
        }
        certificavel.setSelected(evento.isCertificavel());
        libras.setSelected(evento.isAcessivelEmLibras());
    }

    private void setEvento(Evento evento){
        this.evento = evento;
    }
    public boolean salvarAlteracoes(){
        alterarEvento();
        return eventoDAO.alterarEvento(evento);
    }
    public void alterarEvento(){
        try{
            evento.setDescricao(descricao.getText());
            evento.setDataInicial(Date.valueOf(dataInicial.getValue()));
            evento.setDataFinal(Date.valueOf(dataFinal.getValue()));
            evento.setClassificacaoEtaria(classificacaoEtaria.getSelectionModel().getSelectedItem());
            evento.setAcessivelEmLibras(libras.isSelected());
            evento.setCertificavel(certificavel.isSelected());
            evento.setHorario(Time.valueOf(horario.getText()));
            evento.setCargaHoraria(Time.valueOf(cargaHoraria.getText()));

            eventoDAO.alterarEvento(evento);

            Alert sucessoAtualizacao = new Alert(AlertType.INFORMATION);
            sucessoAtualizacao.setContentText("Alterações salvas");
            sucessoAtualizacao.show();
        }
        catch(Exception e){
            Alert erroAtualizacao = new Alert(AlertType.ERROR);
            erroAtualizacao.setContentText("Erro ao alterar evento");
            erroAtualizacao.show();
        }
    }
    /**
     * <p>
     * adiciona a propriedade de tornar um campo editável após o clique
     * <\p>
     */
    private void addPropriedadeAlterar(ObservableList<Control> listaInputs){
        for(Control input : listaInputs){
            input.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                input.setDisable(false);
                salvarAlteracoes.setDisable(false);
            }
        });

        }
    }
    private void loadTable(TableView<Indicador> tabela){
        TableColumn<Indicador,String> nomeIndicador = new TableColumn<>("Nome do indicador");
        nomeIndicador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        TableColumn<Indicador,Integer> valorEsperado = new TableColumn<>("Valor esperado");
        valorEsperado.setCellValueFactory(new PropertyValueFactory<>("valorEsperado"));
        valorEsperado.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        TableColumn<Indicador,Integer> valorAlcancado = new TableColumn<>("Valor alcançado");
        valorAlcancado.setCellValueFactory(new PropertyValueFactory<>("valorAlcancado"));
        valorAlcancado.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        tabela.getColumns().add(nomeIndicador);
        tabela.getColumns().add(valorEsperado);
        tabela.getColumns().add(valorAlcancado);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }
    private void addIndicador(TableView<Indicador> tabela, Indicador indicador){
        tabela.setItems(FXCollections.observableArrayList(indicador));
    }

    /**
     * <p>
     * Retorna todos os elementos que suportam interacao do usuario
     * presentes na pagina,
     * exceto botoes, labels e tableviews <p>
    */
    public void addControls(Parent parent, ObservableList<Control> list) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof Control && !(node instanceof Button || node instanceof Label)) {
                list.add((Control) node);
            } else if (node instanceof Parent) {
                addControls((Parent) node, list);
            }
        }
    }
    
    /** 
     * oculta e desabilita todas as funcionalidades nao implementadas 
     * TODO: remover o metodo apos arquivos serem implementados
     */
    private void temporaryHideUnimplementedFields(){
        for(Node node : secaoArquivos.getChildren()){
            node.setVisible(false);
        } 
    }
}
