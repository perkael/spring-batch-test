package it.perkael.springboot.batch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ByteUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ByteUtils.class);

    public static byte[] getFileAndUnzip(String uri) {

        RestTemplate template = new RestTemplate();

        String filename = "temp.zip";
        MultiValueMap<String, Object> multipartMap = new LinkedMultiValueMap<String, Object>();
        multipartMap.add("filename", filename);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

        template.getMessageConverters()
                .add(new ByteArrayHttpMessageConverter());
        HttpEntity<Object> request = new HttpEntity<Object>(multipartMap,
                headers);

        ResponseEntity<byte[]> response = template.exchange(uri,
                HttpMethod.GET, request, byte[].class, "1");

        byte[] input = response.getBody();

        InputStream inputZip = new ByteArrayInputStream(input);
        ByteArrayOutputStream fos = null;
        try {
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(inputZip);
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {

                fos = new ByteArrayOutputStream();
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (Exception e) {

        }

        return fos.toByteArray();
    }
}
