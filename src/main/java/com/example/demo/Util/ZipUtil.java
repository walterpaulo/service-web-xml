package com.example.demo.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

@Service
public class ZipUtil {
    public static void unzip(Path zipeFile) throws FileNotFoundException {
        byte[] buffer = new byte[1024];

        var zip = new ZipInputStream(new FileInputStream(zipeFile.toFile()));

        try (zip) {
            var zipEntry = zip.getNextEntry();
            while (zipEntry != null) {
                System.err.println(zipEntry.getName());
                var desFile = Path.of(zipEntry.getName());
                try (var fos = new FileOutputStream(desFile.toFile())) {
                    int len;
                    while ((len = zip.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    System.out.println("Error ao extrair arquivo: " + zipEntry.getName() + ": " +
                            e.getMessage());
                }
                zipEntry = zip.getNextEntry();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static byte[] createZipBytes(List<File> listaDeArquivos) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream)) {

            for (File arquivo : listaDeArquivos) {
                ZipEntry zipEntry = new ZipEntry(arquivo.getName());
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int len;
                try (FileInputStream fis = new FileInputStream(arquivo)) {
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }

                zos.closeEntry();
            }

            zos.close();

            return byteArrayOutputStream.toByteArray();
        }
    }

    public static void zipFiles(String outputDirectory, String zipFileName, List<File> listaDeArquivos)
            throws IOException {
        File outputFile = new File(outputDirectory, zipFileName);

        try (FileOutputStream fos = new FileOutputStream(outputFile);
                ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (File arquivo : listaDeArquivos) {
                ZipEntry zipEntry = new ZipEntry(arquivo.getName());
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = arquivo.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                zos.closeEntry();
            }
        }

        System.out.println("Arquivo ZIP criado em: " + outputFile.getAbsolutePath());
    }

    public static File createFile(String fileName, String fileContent) {
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(fileContent.getBytes());
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
