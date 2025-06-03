/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package HW5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("generic")
public class GenericResource {
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response getSentiment(JsonObject input) {
        String sentence = input.getString("sentence");
        String prompt = "Please analyze the sentiment of the following sentence and respond with [positive|negative|neutral] only:\n" + sentence;

        JsonObject request = Json.createObjectBuilder()
                .add("model", "gemma3:12b-it-qat")
                .add("prompt", prompt)
                .add("stream", false)
                .build();

        try {
            URL url = new URL("http://163.13.202.232/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer 7f089f37a713212b92b73d872838dd76");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(request.toString().getBytes());
            }

            String result = "";
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) response.append(line);
                JsonObject jsonResponse = Json.createReader(new StringReader(response.toString())).readObject();
                result = jsonResponse.getString("response");
            }

            String sentiment = checkSentiment(result); 
            JsonObject output = Json.createObjectBuilder()
                .add("sentence", sentence)
                .add("feel", sentiment)
                .build();

            return Response.ok(output).build();

        } catch (Exception e) {
            return Response.status(500).entity("error:" + e.getMessage()).build();
        }
    }

    private String checkSentiment(String response) {
        response = response.toLowerCase();
        if (response.contains("positive")){
            return "positive";
        }
        if (response.contains("negative")){
            return "negative";
        }
        return "neutral";
    }
}
