package codesquad.web;

import codesquad.HtmlFormDataBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LoginAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(LoginAcceptanceTest.class);

    @Test
    public void 로그인_폼() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/login", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void 로그인() throws Exception {
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm();
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder
                .addParameter("userId", "javajigi")
                .addParameter("password", "test")
                .build();
        ResponseEntity<String> response = template().postForEntity("/login", request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertTrue(response.getHeaders().getLocation().getPath().startsWith("/users"));
    }

    @Test
    public void 로그인_실패_패스워드_불일치() throws Exception {
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm();
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder
                .addParameter("userId", "javajigi")
                .addParameter("password", "test2")
                .build();
        ResponseEntity<String> response = template().postForEntity("/login", request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void 로그인_실패_조회된_사용자없음() throws Exception {
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm();
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder
                .addParameter("userId", "ggulmool")
                .addParameter("password", "test2")
                .build();
        ResponseEntity<String> response = template().postForEntity("/login", request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void 로그아웃() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/logout", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        log.debug("body : {}", response.getBody());
    }
}
