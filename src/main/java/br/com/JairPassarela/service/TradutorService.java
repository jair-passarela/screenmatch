package br.com.JairPassarela.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TradutorService {

    public static String obterTraducao(String texto) {
        try {
            if (texto == null || texto.isEmpty()) {
                return "";
            }

            String textoCodificado = URLEncoder.encode(texto, "UTF-8");

            String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=pt&dt=t&q=" + textoCodificado;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            if (status != 200) {
                return "Erro na tradução (HTTP " + status + ")";
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8")
            );

            StringBuilder response = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                response.append(linha);
            }

            reader.close();

            String resultado = response.toString();

            if (resultado.contains("\"")) {
                return resultado.split("\"")[1];
            }

            return resultado;

        } catch (Exception e) {
            return "Erro ao traduzir";
        }
    }
}