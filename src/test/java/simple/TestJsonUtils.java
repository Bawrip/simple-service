package simple;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@UtilityClass
public final class TestJsonUtils {

    private static ConcurrentMap<String, String> cachedResources = new ConcurrentHashMap<>();

    public static <T> T loadFromJson(ObjectMapper objectMapper, Class<?> testClass, String jsonFileName, TypeReference<T> reference) {
        String key = testClass.getCanonicalName() + jsonFileName;
        String json = cachedResources.get(key);
        if (json != null) {
            return convert(objectMapper, testClass, jsonFileName, reference, json);
        }

        json = loadFromResource(testClass, jsonFileName);
        T result = convert(objectMapper, testClass, jsonFileName, reference, json);

        String cached = cachedResources.putIfAbsent(key, json);
        if (cached != null) {
            log.warn("Cache of resources already contains mapping for {}({}): {}", key, reference, cached);
        }
        return result;
    }

    public static String loadFromResource(Class<?> testClass, String jsonFileName) {
        try (InputStream resourceAsStream = testClass.getResourceAsStream(jsonFileName)) {
            return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            log.error(String.format("Failed to load contents from \"%s\".", jsonFileName), ex);
            throw new TestResourceLoadException(testClass, jsonFileName, String.class, ex);
        }
    }

    private static <T> T convert(ObjectMapper objectMapper, Class<?> testClass, String jsonFileName, TypeReference<T> reference, String json) {
        try {
            return objectMapper.readValue(json, reference);
        } catch (IOException ex) {
            log.error(String.format("Failed to convert a \"%s\" from \"%s\".", reference.getType().getTypeName(), jsonFileName), ex);
            throw new TestResourceLoadException(testClass, jsonFileName, reference.getType().getClass(), ex);
        }
    }

    @ToString
    public static class TestResourceLoadException extends RuntimeException {

        private static final long serialVersionUID = -4611801383285872251L;
        private final Class<?> testClass;
        private final String jsonFileName;
        private final Class<?> objectClass;

        TestResourceLoadException(Class<?> testClass, String jsonFileName, Class<?> objectClass, Exception ex) {
            super(ex);
            this.testClass = testClass;
            this.jsonFileName = jsonFileName;
            this.objectClass = objectClass;
        }
    }
}
