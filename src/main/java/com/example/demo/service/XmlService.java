package com.example.demo.service;

import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.github.javafaker.Faker;

@Service
public class XmlService {

    public Document criarDocumento() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dc = dbf.newDocumentBuilder();
        return dc.newDocument();
    }

    private void criarAtrituto(Element element, Document document, String nome, String valor) {
        Attr attr = document.createAttribute(nome);
        attr.setValue(valor);
        element.setAttributeNode(attr);
    }

    private void criarElemento(Element element, Document document, String nome, String valor) {
        Element elementoNovo = document.createElement(nome);
        elementoNovo.appendChild(document.createTextNode(valor));
        element.appendChild(elementoNovo);
    }

    public String inicialXML() throws TransformerException, ParserConfigurationException {
        Document document = this.criarDocumento();

        // elemento raiz do XML
        Element raiz = document.createElement("projeto");
        this.criarAtrituto(raiz, document, "versao", "1.00");
        this.criarElemento(raiz, document, "nome", "SERVICE WEB XML");
        document.appendChild(raiz);
        DOMSource domSource = new DOMSource(document);

        return transformDOMSourceToString(domSource);
    }

    public String visualizarXML() {
        try {
            Faker faker = new Faker();

            Document document = this.criarDocumento();

            // elemento raiz do XML
            Element raiz = document.createElement("produtos");
            document.appendChild(raiz);

            this.criarAtrituto(raiz, document, "versao", "1.00");
            for (int i = 0; i < 10; i++) {
                Element produto = document.createElement("produto");
                raiz.appendChild(produto);
    
                this.criarAtrituto(produto, document, "id", "_" + UUID.randomUUID());
    
                this.criarElemento(produto, document, "nome", faker.commerce().productName());
                this.criarElemento(produto, document, "preco", faker.commerce().price(10, 50));
                this.criarElemento(produto, document, "codigoBarra", faker.idNumber().invalid());
                this.criarElemento(produto, document, "códigoPromocao", faker.commerce().promotionCode(10)); 
            }


            DOMSource domSource = new DOMSource(document);

            return transformDOMSourceToString(domSource);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    private String transformDOMSourceToString(DOMSource source) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StreamResult result = new StreamResult(new java.io.StringWriter());
        transformer.transform(source, result);
        return result.getWriter().toString();
    }

}
