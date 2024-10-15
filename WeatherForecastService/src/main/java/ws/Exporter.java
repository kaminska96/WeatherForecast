package ws;
import jakarta.xml.ws.Endpoint;

public class Exporter {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:1235/WeatherService/pullWeather", new PullWeatherImpl());
        System.out.println("Has started");
    }

}
