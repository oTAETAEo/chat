package hexa.chat.adapter.web;

import hexa.chat.domain.auth.InvalidCredentialsException;
import hexa.chat.domain.member.DuplicateEmailException;
import hexa.chat.domain.member.DuplicateNameException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("요청 값이 올바르지 않습니다.");

        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new FieldErrorResponse(
                error.getField(),
                error.getDefaultMessage()
            ))
            .toList();

        problem.setProperty("errors", fieldErrors);
        return problem;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail invalidCredentialsException(InvalidCredentialsException e){
        return getProblemDetail(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail noSuchElementException(NoSuchElementException e){
        return getProblemDetail(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail illegalArgumentException(IllegalArgumentException e){
        return getProblemDetail(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ProblemDetail duplicateEmailException(DuplicateEmailException e){
        return getProblemDetail(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(DuplicateNameException.class)
    public ProblemDetail duplicateNameException(DuplicateNameException e){
        return getProblemDetail(HttpStatus.BAD_REQUEST, e);
    }

    private @NonNull ProblemDetail getProblemDetail(HttpStatus status, Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());

        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("exception", exception.getClass().getSimpleName());

        return problemDetail;
    }
}
