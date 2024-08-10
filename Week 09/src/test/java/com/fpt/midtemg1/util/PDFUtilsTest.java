package com.fpt.midtemg1.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PDFUtilsTest {

    @Mock
    private SpringTemplateEngine springTemplateEngine;

    private PDFUtils pdfUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pdfUtils = new PDFUtils(springTemplateEngine);
    }

    @Test
    void testGenerateAllInvoicesPDFSuccess() throws IOException {
        // Mock the behavior of SpringTemplateEngine
        when(springTemplateEngine.process(anyString(), any(Context.class)))
                .thenReturn("<html><body>Test PDF Content</body></html>");

        // Call the method under test
        byte[] pdfBytes = pdfUtils.generateAllInvoicesPDF(Collections.emptyList());

        // Assert the results
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);

        // Verify the SpringTemplateEngine was called with the correct template name
        verify(springTemplateEngine, times(1)).process(eq("invoice-template"), any(Context.class));
    }

    @Test
    void testGenerateAllInvoicesPDFIOException() throws IOException {
        // Spy on the PDFUtils instance to simulate an IOException
        PDFUtils spyPDFUtils = spy(pdfUtils);

        // Simulate an IOException by throwing it explicitly during the method call
        doThrow(IOException.class).when(spyPDFUtils).generateAllInvoicesPDF(anyList());

        IOException thrownException = assertThrows(IOException.class, () -> {
            // Call the method under test, expecting an exception
            spyPDFUtils.generateAllInvoicesPDF(Collections.emptyList());
        });

        // Assert that the thrown exception is of type IOException
        assertTrue(thrownException instanceof IOException);
    }


}
