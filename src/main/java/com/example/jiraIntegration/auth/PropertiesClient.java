package com.example.jiraIntegration.auth;


import com.google.api.client.util.Value;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class PropertiesClient {
    public static final String CONSUMER_KEY = "consumer_key";
    public static final String PRIVATE_KEY = "private_key";
    public static final String REQUEST_TOKEN = "request_token";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String SECRET = "secret";
    public static final String JIRA_HOME = "jira_home";

    /*@Value("{jira.oauth.consumerKey}")
    private String consumerKey;

    @Value("{jira.oauth.consumerKey}")*/

    private final static Map<String, String> DEFAULT_PROPERTY_VALUES = ImmutableMap.<String, String>builder()
            .put(JIRA_HOME, "http://localhost:9090/jira")
            .put(CONSUMER_KEY, "OauthKey")
            .put(PRIVATE_KEY, "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMUmZCoiJHpxPG1i9DXe33IvKnCWAs1wXN0HH+9C+KS5RQnQwATdHncgmRzRYTytsMJFA6wPJYBuXJ2J9OXhbvEjNcTIUVb+VBiSFZ40xWpDtgxjIBWfcr1/T21maAhKGOAe4bi1I8mRuvK52ZIvYCKsb0mx7i/C8/qzTpy/WlqtAgMBAAECgYArncG9PI57dD/dJ25F2MfweBTYkaePEdPJpoDfKx7dOovWjxNcQBs0WcbmBECR7w9S+3fDghpW3pOo2tOOZCBp3vpgMuQz4654MPagJ2G5TfIAmQDEKYA0hlzskvzZA4S1LXiuBP6MoUxGvPt2z5IlSTMCv6bXGq0vV6rL3nGaFQJBAPbiq9Vz+XYaaHIeM7wGdRNkFDUp8kHGVJNbdQNZL3vusVTBa3fIBWQQpFrNuLD4Tiyi0mm1mPThw0bu8N4eErMCQQDMbarNrFrnQ2s9WZVXXB9yQuqnlwUvuZ3K5G8KGP/RygyRfKvZLwga7viglU7ofGb8+i3j9SnkJ3l1+fGHsQ0fAkEA8QI8bsala0bCWUzkghPnrINROsj1BoK/Q47PfvJMaXaNREkBSVBI+vNIDqCvQItVfTuBntI5PC08PX26Q4smAwJBAMjrUGBs8bsyTAg0L894v39xvWeRyosph6iN84SdJXFvgALN4ajHzNOL49kQZkmhWydwTAIsR0vycDI/wd93aEkCQHc864O4pb+Rqcro7QZte8UfVwdTmlAzjk8lqny8OWR2VIYDECgvgz9BSKs72ES2wlGfuM+KeMD3uFlE/bC6/gM\\=")
            .build();

    private final String fileUrl;
    private final String propFileName = "config.properties";

    public PropertiesClient() throws Exception {
        fileUrl = "./" + propFileName;
    }

    public Map<String, String> getPropertiesOrDefaults() {
        try {
            Map<String, String> map = toMap(tryGetProperties());
            map.putAll(Maps.difference(map, DEFAULT_PROPERTY_VALUES).entriesOnlyOnRight());
            return map;
        } catch (FileNotFoundException e) {
            tryCreateDefaultFile();
            return new HashMap<>(DEFAULT_PROPERTY_VALUES);
        } catch (IOException e) {
            return new HashMap<>(DEFAULT_PROPERTY_VALUES);
        }
    }

    private Map<String, String> toMap(Properties properties) {
        return properties.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(o -> o.getKey().toString(), t -> t.getValue().toString()));
    }

    private Properties toProperties(Map<String, String> propertiesMap) {
        Properties properties = new Properties();
        propertiesMap.entrySet()
                .stream()
                .forEach(entry -> properties.put(entry.getKey(), entry.getValue()));
        return properties;
    }

    private Properties tryGetProperties() throws IOException {
        InputStream inputStream = new FileInputStream(new File(fileUrl));
        Properties prop = new Properties();
        prop.load(inputStream);
        return prop;
    }

    public void savePropertiesToFile(Map<String, String> properties) {
        OutputStream outputStream = null;
        File file = new File(fileUrl);

        try {
            outputStream = new FileOutputStream(file);
            Properties p = toProperties(properties);
            p.store(outputStream, null);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            closeQuietly(outputStream);
        }
    }

    public void tryCreateDefaultFile() {
        System.out.println("Creating default properties file: " + propFileName);
        tryCreateFile().ifPresent(file -> savePropertiesToFile(DEFAULT_PROPERTY_VALUES));
    }

    private Optional<File> tryCreateFile() {
        try {
            File file = new File(fileUrl);
            file.createNewFile();
            return Optional.of(file);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            // ignored
        }
    }
}
