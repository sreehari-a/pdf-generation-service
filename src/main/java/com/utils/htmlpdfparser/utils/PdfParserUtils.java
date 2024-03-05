package com.utils.htmlpdfparser.utils;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.util.XRRuntimeException;
import org.xml.sax.SAXException;

import com.utils.htmlpdfparser.controller.PdfParserController;

@Component
public class PdfParserUtils {

    private static final Logger logger = LogManager.getLogger(PdfParserController.class);

	@Autowired
	private TemplateEngine templateEngine = new TemplateEngine();

	public byte[] createPdf(String template, Map<String, Object> map) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        	String html = (String) template;
            Map<String, Object> data = (Map<String, Object>) map;

            Context context = new Context();
            context.setVariables(data);
            String processedHtml = templateEngine.process(html, context);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(outputStream, false);
            renderer.finishPDF();

            return outputStream.toByteArray();
        }  catch(TemplateInputException e) {
			logger.log(null, "Error");
			throw new Exception(e);
		}

	}
	
}
