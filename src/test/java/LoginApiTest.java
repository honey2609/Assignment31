import okhttp3.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import util.HttpUtil;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginApiTest {
    @Test
    public void testLogin() throws IOException {
        // Given
        String url = "https://api.automate.io/auth/login";
        JSONObject jsonObject = new JSONObject();
        String testUserEmail = "prashant@example.com";

        jsonObject.put("email", testUserEmail);
        jsonObject.put("password", "prashant1234");

        // When
        Response response = HttpUtil.post(url, jsonObject);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.code()).isNotEqualTo(HttpURLConnection.HTTP_NOT_FOUND);
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK);
        JSONObject responseBody = new JSONObject(response.body().string());
        assertThat(responseBody.get("status")).isEqualTo("success");
        assertThat(responseBody.getJSONObject("data").get("name")).isEqualTo("Prashant Thakur");
    }

    @Test
    public void testUnauthorized() throws IOException {
        // Given
        String url = "https://api.automate.io/auth/login";
        JSONObject jsonObject = new JSONObject();
        String testUserEmail = "prashant@example.com";

        jsonObject.put("email", testUserEmail);
        jsonObject.put("password", "wrongpassword");

        // When
        Response response = HttpUtil.post(url, jsonObject);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_UNAUTHORIZED);
        JSONObject responseObject = new JSONObject(response.body().string());
        assertThat(responseObject.get("status")).isEqualTo("error");
        assertThat(responseObject.getJSONObject("error").get("message")).isEqualTo("Login email or password incorrect.");
    }
}
