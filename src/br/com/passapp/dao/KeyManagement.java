/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.passapp.dao;

/**
 *
 * @author henri
 */
import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import java.io.File;

public class KeyManagement {
    
    
    public static Aead loadKey(String keysetFilename) throws Exception {
        // Carrega a chave do arquivo
        KeysetHandle keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(new File(keysetFilename)));

        // Obtém o primitivo de AEAD para criptografia/descriptografia
        return keysetHandle.getPrimitive(Aead.class);
    }
    
}
