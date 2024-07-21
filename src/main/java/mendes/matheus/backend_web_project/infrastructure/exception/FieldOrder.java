package mendes.matheus.backend_web_project.infrastructure.exception;
import java.util.Arrays;
import java.util.List;

public class FieldOrder {
    public static final List<String> EXPECTED_USER_FIELDS_ORDER = Arrays.asList(
            "name",
            "username",
            "email",
            "password"
    );

}

