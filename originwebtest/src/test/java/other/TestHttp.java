package other;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import java.net.InetAddress;

public class TestHttp {

    public static final String SQS_EU_WEST_1_AMAZONAWS_COM = "https://sqs.eu-west-1.amazonaws.com:443";

    @Test
    public void testAWSSQS() throws Exception{
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder().get().url(SQS_EU_WEST_1_AMAZONAWS_COM).build();
//        Response response = okHttpClient.newCall(request).execute();
//        System.out.println(response.body());
//
//        HttpResponse<String> stringHttpResponse = Unirest.get(SQS_EU_WEST_1_AMAZONAWS_COM).asString();
//        System.out.println(stringHttpResponse);
        InetAddress address = InetAddress.getByName("sqs.eu-west-1.amazonaws.com");
        boolean reachable = address.isReachable(1000);
        System.out.println(reachable);
    }
}
