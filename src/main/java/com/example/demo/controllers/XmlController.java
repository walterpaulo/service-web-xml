package com.example.demo.controllers;

import java.io.File;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.XmlService;

@RestController
@RequestMapping("/")
public class XmlController {

    @Autowired
    private XmlService service;

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE, path = "/")
    public String index() {
        
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

}
