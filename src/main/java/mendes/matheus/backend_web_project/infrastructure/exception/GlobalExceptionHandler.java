package mendes.matheus.backend_web_project.infrastructure.exception;

import mendes.matheus.backend_web_project.infrastructure.dto.ErrorResponseDTO;
import mendes.matheus.backend_web_project.users.exceptions.UserAlreadyExistsException;
import mendes.matheus.backend_web_project.users.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method is responsible for handling exceptions of the UserNotFoundException type,
     * which are thrown when a user is not found in a search or access operation.
     * -
     * Esse método é responsável por tratar exceções do tipo UserNotFoundException,
     * que são lançadas quando um usuário não é encontrado em uma operação de pesquisa ou acesso.
     * @param exception
     * @return the complete HTTP response, with the status NOT_FOUND and the body containing the error message.
     * retorna a resposta HTTP completa, com o status NOT_FOUND e o corpo contendo a mensagem de erro.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(exception.getMessage()));
    }

    /**
     * This method is responsible for handling exceptions of the UserAlreadyExistsException type,
     * which are thrown when trying to create a user that already exists
     * -
     * Esse método é responsável por tratar as exceções do tipo UserAlreadyExistsException,
     * que são lançadas ao tentar criar um usuário que já existe
     * @param exception
     * @return HTTP response, with the status CONFLICT and the body containing the error message in JSON format.
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExists(UserAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDTO(exception.getMessage()));
    }

    /**
     * This method is responsible for handling exceptions of the MethodArgumentNotValidException type,
     * which are thrown when sending a blank field or one with an invalid attribute name
     * -
     * Esse método é responsável por tratar as exceções do tipo MethodArgumentNotValidException,
     * que são lançadas ao enviar um campo em branco ou com o nome do atributo inválido
     * @param exception
     * @return an HTTP response with status BAD_REQUEST (400) and the body of the response is the errors map
     * retorna uma resposta HTTP com o status BAD_REQUEST (400) e o corpo da resposta é o mapa de erros
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();

        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        // Ordena os FieldErrors de acordo com a lista de campos esperados
        List<FieldError> sortedFieldErrors = fieldErrors.stream()
                .sorted((fe1, fe2) -> Integer.compare(
                        FieldOrder.EXPECTED_USER_FIELDS_ORDER.indexOf(fe1.getField()),
                        FieldOrder.EXPECTED_USER_FIELDS_ORDER.indexOf(fe2.getField())
                ))
                .toList();

        // Preenche o LinkedHashMap com os erros na ordem correta
        sortedFieldErrors.forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
