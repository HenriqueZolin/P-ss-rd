/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.passapp.dao;

import java.sql.*;

/**
 *
 * @author henri
 */
public class ModuloConexao {
    public static Connection conector() {
        java.sql.Connection conexao = null;
        //chamar o driver importado para bibliotecas
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/dbpassapp";
        String user = "root";
        String password = "Dba@123456";

        try {
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
