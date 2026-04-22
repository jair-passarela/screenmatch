//package br.com.JairPassarela.service;
//
//import com.theokanning.openai.completion.CompletionRequest;
//import com.theokanning.openai.service.OpenAiService;
//
//public class ConsultaChatGPT {
//    public static String obterTraducao(String texto) {
//        OpenAiService service = new OpenAiService("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");
//
//
//        CompletionRequest requisicao = CompletionRequest.builder()
//                .model("text-davinci-003")
//                .prompt("traduza para o português o texto: " + texto)
//                .maxTokens(1000)
//                .temperature(0.7)
//                .build();
//
//
//        var resposta = service.createCompletion(requisicao);
//        return resposta.getChoices().get(0).getText();
//    }
//}
package br.com.JairPassarela.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ConsultaChatGPT {

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