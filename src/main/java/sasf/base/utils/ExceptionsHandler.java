package sasf.base.utils;

import org.hibernate.exception.DataException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.TransactionRequiredException;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleException(Exception exception) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = "Error del servidor";
        String details = exception.getMessage();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), status, message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(status));
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleIOExceptions(IOException exception) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = "Error del servidor";
        String details = exception.getMessage();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), status, message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(status));
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleNullPointerExceptions(NullPointerException exception) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = "Error del servidor";
        String details = exception.getMessage();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), status, message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(status));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleRuntimeException(RuntimeException exception) {
        int status = HttpStatus.BAD_REQUEST.value();
        String message = "Petición no válida";
        String details = exception.getMessage();

        if (exception instanceof DataException || exception instanceof NoResultException
                || exception instanceof IllegalArgumentException || exception instanceof EntityExistsException) {
            status = HttpStatus.BAD_REQUEST.value();
        } else if (exception instanceof TransactionRequiredException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        } else if (exception.getCause() != null) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception.getCause())
                        .getConstraintViolations();
                Set<String> messages = new HashSet<>(constraintViolations.size());
                messages.addAll(constraintViolations.stream()
                        .map(constraintViolation -> String.format("%s", constraintViolation.getMessage()))
                        .collect(Collectors.toList()));
                details = messages.toString();
            } else if (exception.getCause().getCause() instanceof ConstraintViolationException) {
                Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception.getCause()
                        .getCause()).getConstraintViolations();
                Set<String> messages = new HashSet<>(constraintViolations.size());
                messages.addAll(constraintViolations.stream()
                        .map(constraintViolation -> String.format("%s", constraintViolation.getMessage()))
                        .collect(Collectors.toList()));
                details = messages.toString();
            }
        }

        ErrorDetails errorDetails = new ErrorDetails(new Date(), status, message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(status));
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleJsonParseException(JsonParseException exception) {
        String message = "Petición no válida";
        String details = "Formato incorrecto en el cuerpo de la petición";
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleJsonParseException(HttpMessageNotReadableException exception) {
        String message = "Petición no válida";
        String details = "Data invalida en el cuerpo de la petición: " + exception.getCause();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleAuthenticationException(AuthenticationException exception) {
        String message = "No autorizado";
        String details = "Pruebe iniciar sesión.";
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED.value(), message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException exception) {
        String message = "Acceso denegado";
        String details = "No tiene los permisos necesarios para realizar esta acción.";
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.FORBIDDEN.value(), message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleValidationErrors(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        StringBuilder errorMessage = new StringBuilder();
        fieldErrors.forEach(f -> errorMessage.append(f.getDefaultMessage() + " "));

        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Campos no válidos.",
                errorMessage.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
