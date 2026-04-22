
package br.com.JairPassarela.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TradutorService {

    public static String obterTraducao(String texto) {
        try {
            String textoCodificado = URLEncoder.encode(texto, "UTF-8");

            String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=pt&dt=t&q=" + textoCodificado;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                response.append(linha);
            }

            reader.close();

            // Extrai só a tradução
            String resultado = response.toString();
            return resultado.split("\"")[1];

        } catch (Exception e) {
            return "Erro ao traduzir: " + e.getMessage();
        }
    }
}