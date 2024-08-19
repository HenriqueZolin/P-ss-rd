/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.passap.telas;

import java.sql.*;
import javax.swing.*;
import br.com.passapp.classes.Conta;
import br.com.passapp.dao.ModuloConexao;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.*;
import java.io.File;
import java.util.Base64;

/**
 *
 * @author henri
 */
public class TelaPrincipal extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private Aead aead;
    
    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal(String nome) {
        initComponents();
        conexao = ModuloConexao.conector();
        lblNome.setText(nome);
        String keysetFilename = "src/keyset.json";
        
        try {
            // Initialize Tink
            AeadConfig.register();

            // Create or load the keyset
            KeysetHandle keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File(keysetFilename)));
            aead = keysetHandle.getPrimitive(Aead.class);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro inesperado!");
            this.dispose();
        }
        
        atualizarDisplay();
        getContentPane().setBackground(Color.getHSBColor(0, 0, (float) 0.2));
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/passapp/icones/Icone-logo.png")));
    }
    
    private String descriptografar(String cipherText) throws Exception {
        byte[] cipherTextBytes = Base64.getDecoder().decode(cipherText);
        byte[] plainText = aead.decrypt(cipherTextBytes, null);
        return new String(plainText);
    }
    
    public List<Conta> listarElementos() throws SQLException {
        List<Conta> elementos = new ArrayList<>();
        String sql = "select * from tbcontas where idcli=?";
        recuperarId();
        try{
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblId.getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                
                String nome = rs.getString(2);
                String usuarioCriptografado = rs.getString(3);
                String senhaCriptografada = rs.getString(4);
                String email = rs.getString(5);
                String descricao = rs.getString(6);
                String idcli = rs.getString(7);
                
                
                String usuario = descriptografar(usuarioCriptografado);
                String senha = descriptografar(senhaCriptografada);
                
                Conta conta = new Conta(idcli, nome, usuario, senha, email, descricao);
                elementos.add(conta);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
        return elementos;
    }
    
    private void atualizarDisplay() {
        try {
            List<Conta> elementos = listarElementos();
            displayPanel.removeAll();
            displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
            
            for (Conta elemento : elementos) {
                JButton elementoButton = new JButton(elemento.getNome());
                elementoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(verificarSenhaConta() == true){
                            TelaInfo infos = new TelaInfo();
                            infos.lblId.setText(elemento.getId());
                            infos.txtNome.setText(elemento.getNome());
                            infos.txtUsuario.setText(elemento.getUsuario());
                            infos.txtSenha.setText(elemento.getSenha());
                            infos.txtEmail.setText(elemento.getEmail());
                            infos.txtDescricao.setText(elemento.getDescricao());
                            infos.setVisible(true);
                        }else{
                            JOptionPane.showMessageDialog(null, "Senha Incorreta!");
                        }
                        
                    }
                });
                displayPanel.add(elementoButton);
            }
            displayPanel.revalidate();
            displayPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    private void recuperarId(){
        String sql = "select * from tbusuarios where login=?";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblNome.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                lblId.setVisible(false);
                lblId.setText(rs.getString(1));    
            }else{
                JOptionPane.showMessageDialog(null, "Erro de usuário");
            }
                          
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            
        }
    }
    
    private boolean verificarSenhaConta(){
        
        String sql = "select * from tbusuarios where iduser=?";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblId.getText());
            
            rs = pst.executeQuery();
            
            if(rs.next()){
                String senhaConta = rs.getString(5);
                JPasswordField passwordField = new JPasswordField();
                int option = JOptionPane.showConfirmDialog(
                    null, 
                    passwordField, 
                    "Digite a senha de aplicativo:", 
                    JOptionPane.OK_CANCEL_OPTION, 
                    JOptionPane.PLAIN_MESSAGE
                );
                
                if (option == JOptionPane.OK_OPTION) {
                    String senhaDigitada = new String(passwordField.getPassword());
                    if (senhaDigitada.equals(senhaConta)) {
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null, "Senha incorreta!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        displayPanel = new javax.swing.JPanel();
        lblId = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("P@ss**rd");
        setResizable(false);

        jButton1.setText("Adionar nova conta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        displayPanel.setBackground(new java.awt.Color(54, 54, 54));
        displayPanel.setToolTipText("");

        javax.swing.GroupLayout displayPanelLayout = new javax.swing.GroupLayout(displayPanel);
        displayPanel.setLayout(displayPanelLayout);
        displayPanelLayout.setHorizontalGroup(
            displayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 647, Short.MAX_VALUE)
        );
        displayPanelLayout.setVerticalGroup(
            displayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/passapp/icones/refresh-page-option.png"))); // NOI18N
        jButton2.setToolTipText("Atualizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/passapp/icones/Logo PassApp_resized.png"))); // NOI18N

        lblNome.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblNome.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(displayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblId)
                        .addGap(258, 258, 258))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(412, 412, 412)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(16, 16, 16))
                            .addComponent(lblNome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblNome, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(displayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(29, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addContainerGap())))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            TelaAdicionar adicionar = new TelaAdicionar();
            adicionar.setVisible(true);
            adicionar.lblNomeUser.setText(lblNome.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao abrir janela");
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        atualizarDisplay();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal(lblNome.getText()).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel displayPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblId;
    protected static javax.swing.JLabel lblNome;
    // End of variables declaration//GEN-END:variables
}
