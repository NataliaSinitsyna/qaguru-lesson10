import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;

public class ParsToJson {

    ClassLoader cl = ParsToJson.class.getClassLoader();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void parsToJson() throws IOException {
        try (
                InputStream resource = cl.getResourceAsStream("test.json");
                InputStreamReader reader = new InputStreamReader(resource)
        ) {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Employee employee = objectMapper.readValue(reader, Employee.class);

            assertThat(employee.firstName).isEqualTo("Jeanette");
            assertThat(employee.lastName).isEqualTo("Penddreth");
            assertThat(employee.email).isEqualTo("jpenddreth0@census.gov");
            assertThat(employee.gender).isEqualTo("Female");
        }
    }
}