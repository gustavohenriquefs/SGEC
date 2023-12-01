package com.casaculturaqxd.sgec.controller.preview;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.casaculturaqxd.sgec.DAO.GrupoEventosDAO;
import com.casaculturaqxd.sgec.jdbc.Database;
import com.casaculturaqxd.sgec.jdbc.DatabasePostgres;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Meta;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PreviewGrupoEventoController {
    
    private int DT_LIMITE_NUM_DIAS_UTEIS = 5;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label dataRealizacao;

    @FXML
    private Label nomeGrupoEventos;

    @FXML
    private Button verDetalhes;

    @FXML
    private Parent root;

    @FXML
    private ImageView imagem;

    @FXML
    private Label dataLimite;

    @FXML
    private Label metasAtendidas;

    
    private GrupoEventosDAO dao;
    private Database db = DatabasePostgres.getInstance("URL", "USER_NAME", "PASSWORD");
    private GrupoEventos grupoEventos;

    @FXML
    void initialize() {
        verDetalhes.setDisable(true);
    }

    public void setGrupoEventos(GrupoEventos grupoEventos) {
        this.grupoEventos = grupoEventos;

        loadContent();
    }

    private void loadContent() {
        String dataRealizada = grupoEventos.getDataInicial().toString() + " até " + grupoEventos.getDataFinal().toString();

        nomeGrupoEventos.setText(grupoEventos.getNome());
        dataRealizacao.setText(dataRealizada);
        
        setarMetasAtendidas();
        setarDataLimite();
    }

    private void setarDataLimite() {
        Calendar dataInicial = Calendar.getInstance();
        dataInicial.setTime(this.grupoEventos.getDataInicial());

        int mes = dataInicial.get(Calendar.MONTH);
        int ano = dataInicial.get(Calendar.YEAR);

        Calendar dataLimiteCalendar = Calendar.getInstance();
        dataLimiteCalendar.set(ano, mes + 1, 1);

        String dataLimiteText = calcDiaDataLimite(dataLimiteCalendar);

        this.dataLimite.setText(dataLimiteText);
    }

    private String calcDiaDataLimite(Calendar dataProxMes) {
        int qtdDiasUteisProxMes = 0;

        while(qtdDiasUteisProxMes < DT_LIMITE_NUM_DIAS_UTEIS) {
            if(dataProxMes.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || dataProxMes.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dataProxMes.add(Calendar.DAY_OF_MONTH, 1);
            } else {
                qtdDiasUteisProxMes ++;
                dataProxMes.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        return dataProxMes.toString();
    }  

    private void setarMetasAtendidas() {
        String metasAtendidasText = "";

        for(Meta metas: grupoEventos.getMetas()) {
            metasAtendidasText += metas.getNomeMeta() + ", ";
        }

        metasAtendidasText = metasAtendidasText.substring(0, metasAtendidasText.length() - 2);

        metasAtendidas.setText(metasAtendidasText);
    }
    
}
