package simple;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.mts.trading.server.transport.response.ResponseDto;

import javax.annotation.Nullable;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.function.Consumer;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TariffApplication.class)
public class BaseApplicationTest {

    public static final ParameterizedTypeReference<ResponseDto<Void>> VOID = new ParameterizedTypeReference<ResponseDto<Void>>() {
    };

    @Getter
    @LocalServerPort
    protected int port;
    @Autowired
    protected TestRestTemplate testRestTemplate;
    @Autowired
    protected ObjectMapper objectMapper;

    private static <DTO> DTO assertResponse(
            ResponseEntity<DTO> response, HttpStatus httpStatus, boolean checkForNullContent
    ) {
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(httpStatus);
        DTO body = response.getBody();
        if (checkForNullContent) {
            Assertions.assertThat(body).isNotNull();
        }
        return body;
    }

    private static String generateUrl(int port, String path) {
        return String.format("http://localhost:%d/%s", port, path);
    }

    protected <RQ, RS> RS makeFullPathRequest(
            RQ requestDto,
            ParameterizedTypeReference<RS> responseType,
            HttpMethod method, String path,
            HttpStatus status,
            @Nullable Consumer<RequestEntity.BodyBuilder> requestEntityModifier
    ) {
        ResponseEntity<RS> response = makeExchange(requestDto, responseType, method, path, requestEntityModifier);
        return assertResponse(response, status, responseType != VOID);
    }

    protected <RQ, RS> ResponseEntity<RS> makeFullPathRequestWhichReturnResponseEntity(
            RQ requestDto,
            ParameterizedTypeReference<RS> responseType,
            HttpMethod method, String path,
            HttpStatus status,
            @Nullable Consumer<RequestEntity.BodyBuilder> requestEntityModifier
    ) {
        ResponseEntity<RS> response = makeExchange(requestDto, responseType, method, path, requestEntityModifier);
        assertResponse(response, status, responseType != VOID);
        return response;
    }

    private <RQ, RS> ResponseEntity<RS> makeExchange(
            RQ requestDto,
            ParameterizedTypeReference<RS> responseType,
            HttpMethod method, String path,
            @Nullable Consumer<RequestEntity.BodyBuilder> requestEntityModifier
    ) {
        RequestEntity.BodyBuilder builder = RequestEntity
                .method(method, URI.create(path))
                .contentType(MediaType.APPLICATION_JSON);

        if (requestEntityModifier != null) {
            requestEntityModifier.accept(builder);
        }

        return testRestTemplate.exchange(builder.body(requestDto), responseType);
    }

    public String generatePathUrl(String path) {
        return generateUrl(port, path);
    }

    public String generateV1Url(String pathTail) {
        return generatePathUrl("api/v1/" + pathTail);
    }

    protected <T> T loadFromJson(String jsonFileName, TypeReference<T> reference) {
        log.trace("loadFromJson: loading a \"{}\" from \"{}\"", reference.getType().getTypeName(), jsonFileName);
        final T result = TestJsonUtils.loadFromJson(objectMapper, getClass(), jsonFileName, reference);
        log.debug("loadFromJson: successfully loaded from \"{}\": {}", jsonFileName, result);
        return result;
    }

    protected <T> T loadFromJson(String jsonFileName, Class<T> objectClass) {
        return loadFromJson(jsonFileName, new TypeReference<T>() {
            @Override
            public Type getType() {
                return objectClass;
            }
        });
    }
}
