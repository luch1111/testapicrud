package com.randomname.integration;

import com.randomname.integration.utils.ConfigurationReader;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.BeforeClass;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class AbstractTest {

    static String ID = "ID";
    static String FIRSTNAME = "FIRSTNAME";
    static String LASTNAME = "LASTNAME";

    static String url;
    static String regexp;
    static String nonexistUser;

    @BeforeClass
    public static void setup() {
        ConfigurationReader configurationReader = new ConfigurationReader("application");
        String serviceHost = configurationReader.getConfig("service_host");
        String mainUrlPath = configurationReader.getConfig("mainUrlPath");
        url = serviceHost + mainUrlPath;

        nonexistUser = configurationReader.getConfig("nonexistUser");
        regexp = configurationReader.getConfig("regexp");
    }

    Map<Object, Object> parseResponseDataIntoMap(String str) {
        String[][] data = Stream.of(str.split(","))
                .map(elem -> elem.trim().split("="))
                .toArray(String[][]::new);

        return ArrayUtils.toMap(data);
    }

    HashMap<String, String> buildHeaders(String fn, String ln) {
        HashMap<String, String> headers = new HashMap<>();
        if (fn != null) headers.put("firstName", fn);
        if (ln != null) headers.put("lastName", ln);
        return headers;
    }
}
