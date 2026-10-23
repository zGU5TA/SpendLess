package com.mycompany.projetoa3;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Hash de senhas com PBKDF2 (HMAC-SHA256), usando apenas o que já vem no Java.
 *
 * Formato guardado no banco:  pbkdf2$<iteracoes>$<salt em Base64>$<hash em Base64>
 *
 * - Cada senha tem um salt aleatório próprio, então duas senhas iguais geram hashes diferentes.
 * - O número de iterações fica gravado no próprio texto, então dá para aumentá-lo no futuro
 *   sem quebrar as contas já existentes.
 * - Senhas antigas (texto puro, de antes desta mudança) ainda são aceitas uma última vez
 *   e devem ser regravadas como hash (veja precisaMigrar).
 */
public final class SenhaUtil {

    private static final String PREFIXO = "pbkdf2";
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACOES = 210_000;
    private static final int TAMANHO_SALT = 16;   // bytes
    private static final int TAMANHO_HASH = 32;   // bytes
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private SenhaUtil() {
    }

    /** Gera o texto que deve ser salvo na coluna "senha". */
    public static String gerarHash(String senha) {
        byte[] salt = new byte[TAMANHO_SALT];
        ALEATORIO.nextBytes(salt);
        byte[] hash = derivar(senha, salt, ITERACOES, TAMANHO_HASH);
        return PREFIXO + "$" + ITERACOES + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    /** Confere a senha digitada com o que está salvo no banco (hash novo ou texto puro antigo). */
    public static boolean verificar(String senhaDigitada, String armazenada) {
        if (senhaDigitada == null || armazenada == null) {
            return false;
        }
        if (!armazenada.startsWith(PREFIXO + "$")) {
            // Conta antiga: a senha estava salva em texto puro.
            return MessageDigest.isEqual(
                    senhaDigitada.getBytes(StandardCharsets.UTF_8),
                    armazenada.getBytes(StandardCharsets.UTF_8));
        }
        String[] partes = armazenada.split("\\$");
        if (partes.length != 4) {
            return false;
        }
        try {
            int iteracoes = Integer.parseInt(partes[1]);
            byte[] salt = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);
            byte[] calculado = derivar(senhaDigitada, salt, iteracoes, esperado.length);
            // Comparação em tempo constante (não revela quantos bytes batem).
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException e) {
            return false; // formato corrompido
        }
    }

    /** true se o valor salvo ainda é texto puro (ou usa menos iterações que o atual) e deve ser regravado. */
    public static boolean precisaMigrar(String armazenada) {
        if (armazenada == null || !armazenada.startsWith(PREFIXO + "$")) {
            return true;
        }
        String[] partes = armazenada.split("\\$");
        if (partes.length != 4) {
            return true;
        }
        try {
            return Integer.parseInt(partes[1]) < ITERACOES;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private static byte[] derivar(String senha, byte[] salt, int iteracoes, int tamanhoBytes) {
        PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, iteracoes, tamanhoBytes * 8);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 indisponível neste Java", e);
        } finally {
            spec.clearPassword();
        }
    }
}
