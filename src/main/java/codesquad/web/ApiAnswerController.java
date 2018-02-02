package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import codesquad.validate.ValidationErrorsResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {
    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping("")
    public ResponseEntity create(@LoginUser User loginUser, @PathVariable long questionId, @Valid @RequestBody AnswerDto answerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            ValidationErrorsResponse validationErrorsResponse = new ValidationErrorsResponse();
            validationErrorsResponse.addAllValidationError(fieldErrors);
            return new ResponseEntity<>(validationErrorsResponse, HttpStatus.BAD_REQUEST);
        }

        Answer answer = qnaService.addAnswer(loginUser, questionId, answerDto.toAnswer());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api" + answer.generateUrl()));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/{answerId}")
    public ResponseEntity<Answer> read(@PathVariable long answerId) {
        Answer answer = qnaService.findAnswerById(answerId);
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @PutMapping("/{answerId}")
    public ResponseEntity update(@LoginUser User loginUser, @PathVariable long answerId, @Valid @RequestBody AnswerDto answerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            ValidationErrorsResponse validationErrorsResponse = new ValidationErrorsResponse();
            validationErrorsResponse.addAllValidationError(fieldErrors);
            return new ResponseEntity<>(validationErrorsResponse, HttpStatus.BAD_REQUEST);
        }

        Answer updatedAnswer = qnaService.updateAnswer(loginUser, answerId, answerDto);
        return new ResponseEntity<>(updatedAnswer, HttpStatus.OK);
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity delete(@LoginUser User loginUser, @PathVariable long answerId) {
        qnaService.deleteAnswer(loginUser, answerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
