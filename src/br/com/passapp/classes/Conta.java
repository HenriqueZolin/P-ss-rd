/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.passapp.classes;

/**
 *
 * @author henri
 */
public class Conta {
    private String id;
    private String nome;
    private String usuario;
    private String senha;
    private String email;
    private String descricao;

    public Conta(String id, String nome, String usuario, String senha, String email, String descricao) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.email = email;
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public String getDescricao() {
        return descricao;
    }
    
    
}
