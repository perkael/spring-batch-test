package it.perkael.springboot.batch.controller;

import com.google.common.io.ByteStreams;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
public class GetFileController {

    @RequestMapping(value = "/getFile", produces = {"application/octet-stream"}, method = RequestMethod.GET)
    public void getFile(HttpServletResponse response) throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("users.csv");
        String fileName = classPathResource.getFilename();

        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("charset", "utf-8");
        response.setContentType("application/csv");
        byte[] out = ByteStreams.toByteArray(classPathResource.getInputStream());

        ByteArrayOutputStream baos = new ByteArrayOutputStream(out.length);
        baos.write(out, 0, out.length);

        response.setContentLength(baos.size());

        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();

        response.flushBuffer();
    }

    @RequestMapping(value = "/getFileZip", produces = {"application/octet-stream"}, method = RequestMethod.GET)
    public void getFileZip(HttpServletResponse response) throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("users.zip");
        String fileName = classPathResource.getFilename();

        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setHeader("charset", "utf-8");
        response.setContentType("application/csv");
        byte[] out = ByteStreams.toByteArray(classPathResource.getInputStream());

        ByteArrayOutputStream baos = new ByteArrayOutputStream(out.length);
        baos.write(out, 0, out.length);

        response.setContentLength(baos.size());

        OutputStream os = response.getOutputStream();
        baos.writeTo(os);
        os.flush();
        os.close();

        response.flushBuffer();
    }
}
