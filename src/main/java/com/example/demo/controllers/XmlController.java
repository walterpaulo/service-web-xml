package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.example.demo.Util.ZipUtil;
import com.example.demo.service.XmlService;

@RestController
@RequestMapping("/")
public class XmlController {

    @Autowired
    private XmlService service;

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE, path = "/")
    public String index() throws ParserConfigurationException, TransformerException {
        return service.inicialXML();
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE, path = "/produtos")
    public String produtos() throws ParserConfigurationException, TransformerException {
        return service.visualizarXML();
    }

    @GetMapping("/download-xml")
    public ResponseEntity<ByteArrayResource> downloadXML() {
        byte[] xmlBytes = service.visualizarXML().getBytes();

        ByteArrayResource resource = new ByteArrayResource(xmlBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exemplo.xml");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(xmlBytes.length)
                .contentType(MediaType.APPLICATION_XML)
                .body(resource);
    }

    @GetMapping("/download-xml-zip")
    public ResponseEntity<ByteArrayResource> downloadXMLZip() throws IOException {

        List<File> listaDeArquivos = new ArrayList<>();

        listaDeArquivos.add(ZipUtil.createFile("arquivo1.txt", "Conteúdo do arquivo 1"));
        listaDeArquivos.add(ZipUtil.createFile("arquivo2.txt", "Conteúdo do arquivo 2"));
        listaDeArquivos.add(ZipUtil.createFile(" produtos.xml", service.visualizarXML()));

        byte[] zipBytes = ZipUtil.createZipBytes(listaDeArquivos);

        ByteArrayResource resource = new ByteArrayResource(zipBytes);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exemplo.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(zipBytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
