package isep.ipp.pt.Smart_cities.Events;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AllEventsTest {
    public static void main(String[] args) {
        try {
            String url = "http://localhost:9091/api/events";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("Resposta GET: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Resposta do Servidor: " + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}