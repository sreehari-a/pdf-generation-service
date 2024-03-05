package com.utils.htmlpdfparser.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.management.modelmbean.XMLParseException;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.exceptions.TemplateInputException;
import org.xhtmlrenderer.util.XRRuntimeException;

import com.itextpdf.text.DocumentException;
import com.utils.htmlpdfparser.utils.PdfParserUtils;

@RequestMapping(path = "utils/pdf")
@CrossOrigin(origins = "http://192.168.1.11:3000", maxAge = 3600)
@RestController
public class PdfParserController {

    private static final Logger logger = LogManager.getLogger(PdfParserController.class);

	@PostMapping("/generate-pdf")
	public ResponseEntity<PdfResponse> convertHTML(@RequestBody Map<String, Object> request)
			throws Exception {
		try {
			String html = (String) request.get("html");
			Map<String, Object> data = (Map<String, Object>) request.get("data");
			return ResponseEntity.status(HttpStatus.OK).body(new PdfResponse(new PdfParserUtils().createPdf(html, data), null));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PdfResponse(null, e.getMessage()));
		} catch (DocumentException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PdfResponse(null,e.getMessage()));
		} catch (XRRuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PdfResponse(null,e.getMessage()));
		} catch(XMLParseException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PdfResponse(null,e.getMessage()));
		} 

	}
	
	public class PdfResponse {
        private SerialBlob data;
        private String error;

        public PdfResponse(byte[] data, String error) throws SerialException, SQLException {
        	this.data = data != null ? new SerialBlob(data): null;
            this.error = error;
        }

        public SerialBlob getData() {
            return data;
        }

        public String getError() {
            return error;
        }
    }

}
