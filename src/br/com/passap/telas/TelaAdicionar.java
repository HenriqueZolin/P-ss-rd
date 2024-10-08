/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package br.com.passap.telas;


import java.sql.*;
import br.com.passapp.dao.ModuloConexao;
import java.awt.Color;
import java.awt.Toolkit;
import java.util.Random;
import javax.swing.JOptionPane;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import java.io.File;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author henri
 */
public class TelaAdicionar extends javax.swing.JFrame {

    /**
     * Creates new form TelaAdicionar
     */
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int estado = 0;
    String keysetFilename = "src/keyset.json";
    Aead aead;
    
    public TelaAdicionar() throws Exception{
        initComponents();
        conexao = ModuloConexao.conector();
        
        AeadConfig.register();
        File keysetFile = new File(keysetFilename);
        KeysetHandle keysetHandle;
        
        if (keysetFile.exists()) {
            // Carregar a chave do arquivo existente
            keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(keysetFile));
        } else {
            // Gerar uma nova chave se o arquivo não existir
            keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES256_GCM);
            // Salvar a nova chave no arquivo
            CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(keysetFile));
        }
        
        aead = keysetHandle.getPrimitive(Aead.class);
        
        getContentPane().setBackground(Color.getHSBColor(0, 0, (float) 0.3));
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/passapp/icones/Icone-logo.png")));
    }
    
    public String criptografar(String texto) throws Exception {
        byte[] cipherText = aead.encrypt(texto.getBytes(), null);
        //FUNCIONA System.out.println(cipherText + " chipher text");
        return Base64.getEncoder().encodeToString(cipherText);
    }
    
    private void adicionar(){
        String sql = "insert into tbcontas(titulo,usuario,senha,email,descricao,idcli) values(?,?,?,?,?,?)";
        
        try {
            
            //ADICIONAR CRIPTOGRAFIA DE LOGIN E SENHA E DESCRIPTOGRAFAR DPS
            String usuarioCriptografado = criptografar(txtUsuario.getText());
            String senhaCriptografada = criptografar(txtSenha.getText());
            
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNome.getText());
            pst.setString(2, usuarioCriptografado);
            pst.setString(3, senhaCriptografada);
            pst.setString(4, txtEmail.getText());
            pst.setString(5, txtDescricao.getText());
            pst.setString(6, lblId.getText());
            
            
            if(txtNome.getText().isEmpty() ||
                    txtUsuario.getText().isEmpty() ||
                    txtSenha.getText().isEmpty() ||
                    txtDescricao.getText().length() < 20){
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");
            }else{ //Adicionou todos os campos
                
                //estrutura abaxio usada para cofnrimar a inserção dos dados na tebela
                int adicionado = pst.executeUpdate();
                //A linha abaixo serve de apoio ao entendimento da logica
                //System.out.println(adicionado);
                if(adicionado>0){
                    limparDados();
                    JOptionPane.showMessageDialog(null, "Usuario adicionado com sucesso!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void recuperarId(){
        String sql = "select * from tbusuarios where login=?";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblNomeUser.getText());
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
    
    private void limparDados(){
        txtNome.setText(null);
        txtUsuario.setText(null);
        txtSenha.setText(null);
        txtEmail.setText(null);
        txtDescricao.setText(null);
    }
    
    private void gerarSenha(){
        String cc = "abcdefghijklmnopqrstuvwxyz";
        String n = "0123456789";
        String cE = "|<>.:-+*!@#$%&?/";
        
        Random random = new Random();
        
        String chave = "";
        
        for(int i=0; i<3; i++){
            int indexC = random.nextInt(cc.length());
            int indexN = random.nextInt(n.length());
            int indexCE = random.nextInt(cE.length());
            
            chave += cc.substring(indexC, indexC + 1) + n.substring(indexN, indexN + 1)+ cc.substring(indexC, indexC + 1).toUpperCase() + cE.substring(indexCE, indexCE + 1);
        }
        
        txtSenha.setText(chave);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtNome = new javax.swing.JTextField();
        txtDescricao = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        txtMostraSenha = new javax.swing.JButton();
        txtUsuario = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblNomeUser = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Adicionar Conta");
        setResizable(false);

        txtNome.setToolTipText("Título desta conta");

        txtMostraSenha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/passapp/icones/visible.png"))); // NOI18N
        txtMostraSenha.setToolTipText("Mostrar Senha");
        txtMostraSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMostraSenhaActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Titulo da conta");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Usuario");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Senha");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("<html>email atribuido <br>a conta </html>");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Descrição");

        jButton1.setText("Adicionar conta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Criar senha forte aleatória");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        lblNomeUser.setForeground(new java.awt.Color(255, 255, 255));
        lblNomeUser.setToolTipText("");
        lblNomeUser.setAlignmentX(1.0F);
        lblNomeUser.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMostraSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblId)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(lblNomeUser, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(31, 31, 31))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1))
                .addGap(91, 91, 91))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblId)
                                    .addComponent(lblNomeUser, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(txtMostraSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(65, 65, 65)
                .addComponent(jButton1)
                .addGap(23, 23, 23))
        );

        txtNome.getAccessibleContext().setAccessibleName("Título");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        recuperarId();
        adicionar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        gerarSenha();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtMostraSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMostraSenhaActionPerformed
        // TODO add your handling code here:
        if(estado == 0){
            txtSenha.setEchoChar((char)0);
            txtMostraSenha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/passapp/icones/hide.png")));
            estado = 1;
        }else{
            txtSenha.setEchoChar('*');
            txtMostraSenha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/passapp/icones/visible.png")));
            estado = 0;
        }
        
    }//GEN-LAST:event_txtMostraSenhaActionPerformed

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
            java.util.logging.Logger.getLogger(TelaAdicionar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaAdicionar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaAdicionar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaAdicionar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TelaAdicionar().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(TelaAdicionar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblId;
    protected javax.swing.JLabel lblNomeUser;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JButton txtMostraSenha;
    private javax.swing.JTextField txtNome;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
