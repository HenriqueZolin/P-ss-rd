/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.passapp.dao;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

/**
 *
 * @author henri
 */
public class ModuloConexao {
    public static Connection conector() {
        java.sql.Connection conexao = null;
        //chamar o driver importado para bibliotecas

        try {
            String driver = "com.mysql.cj.jdbc.Driver";

            //ADICIONAR DADOS EM UM .PROPERTIES
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword("hzm0101");

            Properties props = new EncryptableProperties(encryptor);
            props.load(new FileInputStream("src/br/com/passapp/dao/values.properties"));
            
            String url = props.getProperty("datasource.url");
            String user = props.getProperty("datasource.username");
            String password = props.getProperty("datasource.password");
            
            //ligar banco de dados co o java
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            //Apoio para esclarecer o erro de conexão
            //System.out.println(e);
            return null;
        }
    }
}
