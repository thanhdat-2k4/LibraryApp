package com.example.oop25;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;

public class QRCodeUtils {
    public static BufferedImage generateQRCode(String text, int width, int height) throws WriterException {
        BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
