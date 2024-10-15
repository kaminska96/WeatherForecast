package ws;
import jakarta.jws.WebService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.net.URLEncoder;


@WebService
public class PullWeatherImpl implements PullWeather {

    private static final String API_KEY = "04c8e8e615aec140b646715ae8a02fcd";

    @Override
    public String pullWeather(String city, String country) {
        String result = "";
        try {
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            String encodedCountry = URLEncoder.encode(country, "UTF-8");

            String urlString = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s&units=metric",
                encodedCity, encodedCountry, API_KEY);
            System.out.println("URL: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            connection.disconnect();

            JSONObject weatherJson = new JSONObject(content.toString());
            System.out.println("Response from API: " + weatherJson.toString());

            JSONObject main = weatherJson.getJSONObject("main");
            double temperature = main.getDouble("temp");
            int humidity = main.getInt("humidity");

            JSONObject weather = weatherJson.getJSONArray("weather").getJSONObject(0);
            String description = weather.getString("description");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("city", city);
            jsonResponse.put("country", country);
            jsonResponse.put("temperature", temperature);
            jsonResponse.put("humidity", humidity);
            jsonResponse.put("description", description);

            result = jsonResponse.toString();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Не вдалося отримати дані про погоду.");
            result = errorResponse.toString();
        }

        return result;
    }
}
