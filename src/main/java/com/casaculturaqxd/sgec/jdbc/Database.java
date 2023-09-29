package com.casaculturaqxd.sgec.jdbc;

import java.sql.Connection;

/**
 * <p>
 * Classe que define a forma generalizada
 * de gerenciar conexões com um banco de dados SQL
 * </p>
 * 
 * <p>
 * Todas as implementações de Database devem
 * utilizar o padrão Singleton e possuir métodos 
 * para conectar e desconectar do banco de dados,
 * utilizando um driver jdbc por exemplo.
 * </p>  
 */
public interface Database {
    Connection conectar();
    void desconectar(Connection connection);
}
