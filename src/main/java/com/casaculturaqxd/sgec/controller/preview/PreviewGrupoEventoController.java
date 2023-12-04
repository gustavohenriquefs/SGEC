package com.casaculturaqxd.sgec.controller.preview;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import com.casaculturaqxd.sgec.App;
import com.casaculturaqxd.sgec.controller.VisualizarGrupoEventosController;
import com.casaculturaqxd.sgec.models.GrupoEventos;
import com.casaculturaqxd.sgec.models.Meta;
import com.casaculturaqxd.sgec.service.DateFormattingService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class PreviewGrupoEventoController {
    
    private final int DT_LIMITE_NUM_DIAS_UTEIS = 5;
    private final Image IMAGEM_DEFAULT = new Image(App.class.getResourceAsStream("imagens/default_image.png"));

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
    private Text metasAtendidas;

    private GrupoEventos grupoEventos;

    @FXML
    void initialize() {
        verDetalhes.setDisable(false);
    }

    public void setGrupoEventos(GrupoEventos grupoEventos) {
        this.grupoEventos = grupoEventos;

        loadContent();
    }

    private void loadContent() {
        if(this.grupoEventos.getNome() != null) {
            nomeGrupoEventos.setText(grupoEventos.getNome());
        }
        
        setDataRealizada();
        setMetasAtendidas();
        setDataLimite();
        setImagemCapa();
    }

    private void setDataRealizada() {
        String dataRealizada = "";

        if(this.grupoEventos.getDataInicial() != null && this.grupoEventos.getDataFinal() != null) {
            Date dataInicial = this.grupoEventos.getDataInicial();
            Date dataFinal = this.grupoEventos.getDataFinal();

            dataRealizada += formatToBrazilian(dataInicial) + " até " + formatToBrazilian(dataFinal);
        }
        
        dataRealizacao.setText(dataRealizada);
    }

    private void setImagemCapa() {
        if(this.grupoEventos.getImagemCapa() == null) {
            imagem.setImage(IMAGEM_DEFAULT);
            return;
        };
    
        try {
            File imageFile = this.grupoEventos.getImagemCapa().getContent();
    
            if(imageFile != null && imageFile.exists()) {
                try (FileInputStream fileAsStream = new FileInputStream(imageFile)) {
                    this.imagem.setImage(new Image(fileAsStream));
                } catch (Exception e) {
                    imagem.setImage(IMAGEM_DEFAULT);
                    e.printStackTrace();
                }
            } else {
                imagem.setImage(IMAGEM_DEFAULT);
            }
        } catch (IOException e) {
            imagem.setImage(IMAGEM_DEFAULT);
            e.printStackTrace();
        }
    }

    private void setDataLimite() {
        if(this.grupoEventos.getDataInicial() == null) return;
    
        Calendar initialDateCalendar = Calendar.getInstance();
        initialDateCalendar.setTime(this.grupoEventos.getDataInicial());
    
        int month = initialDateCalendar.get(Calendar.MONTH);
        int year = initialDateCalendar.get(Calendar.YEAR);
    
        Calendar limitDateCalendar = getLimitDateCalendar(year, month);
    
        String limitDateText = calcDiaDataLimite(limitDateCalendar);
    
        this.dataLimite.setText(limitDateText);
    }
    
    private Calendar getLimitDateCalendar(int year, int month) {
        final int NEXT_MONTH = 1;
        final int FIRST_DAY_OF_MONTH = 1;
    
        Calendar limitDateCalendar = Calendar.getInstance();
        limitDateCalendar.set(year, month + NEXT_MONTH, FIRST_DAY_OF_MONTH);
    
        return limitDateCalendar;
    }

    private String calcDiaDataLimite(Calendar dataProxMes) {
        final int WEEKEND_SATURDAY = Calendar.SATURDAY;
        final int WEEKEND_SUNDAY = Calendar.SUNDAY;
        final int ONE_DAY = 1;
        
        int qtdDiasUteisProxMes = 0;
    
        while(qtdDiasUteisProxMes < DT_LIMITE_NUM_DIAS_UTEIS) {
            int dayOfWeek = dataProxMes.get(Calendar.DAY_OF_WEEK);
            
            if(dayOfWeek == WEEKEND_SATURDAY || dayOfWeek == WEEKEND_SUNDAY) {
                dataProxMes.add(Calendar.DAY_OF_MONTH, ONE_DAY);
            } else {
                qtdDiasUteisProxMes++;
                dataProxMes.add(Calendar.DAY_OF_MONTH, ONE_DAY);
            }
        }
    
        Date date = new Date(dataProxMes.getTimeInMillis());
        return formatToBrazilian(date);
    }

    private String formatToBrazilian(Date date) {
        return new DateFormattingService().formatToBrazilian(date);
    }

    private void setMetasAtendidas() {
        String metasAtendidasText = "";

        if(grupoEventos.getMetas() == null || grupoEventos.getMetas().size() == 0) {
            metasAtendidas.setText("Nenhuma meta atendida");
            return;
        }

        for(Meta metas: grupoEventos.getMetas()) {
            metasAtendidasText += metas.getNomeMeta() + ", ";
        }

        metasAtendidasText = metasAtendidasText.substring(0, metasAtendidasText.length() - 2);

        metasAtendidas.setText(metasAtendidasText);
    }

    @FXML
    public void verDetalhes(ActionEvent event) throws IOException, SQLException {
        try {
            URL url = App.class.getResource("view/grupoEventoExistente.fxml");
            FXMLLoader loaderVisualizacao = new FXMLLoader(url);
            Parent objVisualizacao = loaderVisualizacao.load();

            VisualizarGrupoEventosController controller = loaderVisualizacao.getController();

            controller.setGrupoEventos(dao.getGrupoEventos(grupoEventos).get());
            App.setRoot(objVisualizacao);

        } catch (NoSuchElementException e) {
            Alert erroLoading = new Alert(AlertType.WARNING, "Falha ao carregar o evento");
            erroLoading.show();
        }
    }
}
