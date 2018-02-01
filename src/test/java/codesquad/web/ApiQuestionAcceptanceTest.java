package codesquad.web;

import codesquad.domain.Question;
import codesquad.dto.QuestionDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    @Test
    public void create() throws Exception {
        QuestionDto questionDto = new QuestionDto("new title", "new contents");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", questionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        Question respQuestion = template().getForObject(location, Question.class);
        assertThat(respQuestion.toQuestionDto(), is(questionDto));
    }

    @Test
    public void update() throws Exception {
        QuestionDto questionDto = new QuestionDto("new title", "new contents");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", questionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        QuestionDto updatedQuestionDto = new QuestionDto("updated title", "updated contents");
        basicAuthTemplate().put(location, updatedQuestionDto);

        Question respQuestion = template().getForObject(location, Question.class);
        assertThat(respQuestion.toQuestionDto(), is(updatedQuestionDto));
    }

    @Test
    public void delete() throws Exception {
        QuestionDto questionDto = new QuestionDto("new title", "new contents");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", questionDto, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        basicAuthTemplate().delete(location);
        Question respQuestion = template().getForObject(location, Question.class);
        assertTrue(respQuestion!=null);
    }
}
