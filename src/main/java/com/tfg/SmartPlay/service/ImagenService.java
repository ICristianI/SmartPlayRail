package com.tfg.SmartPlay.service;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

// Servicio para gestionar las imágenes.

@Service
public class ImagenService {

    // Guarda una imagen en la base de datos.

    public Blob saveImage(MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            return BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize());
        }
        return null;
    }

    // Devuelve la imagen por defecto de un usuario.

    public Blob getDefaultProfileImage() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/static/images/userImages/Default.png");
        if (inputStream == null) {
            throw new Exception("No se pudo encontrar la imagen por defecto.");
        }
        byte[] defaultImage = inputStream.readAllBytes();
        return new SerialBlob(defaultImage);
    }

    // Devuelve la imagen de la base de datos.

    public ResponseEntity<Object> getImageResponse(Blob image) throws SQLException {
        if (image != null) {
            Resource file = new InputStreamResource(image.getBinaryStream());
            String mimeType;
            try {
                mimeType = URLConnection.guessContentTypeFromStream(image.getBinaryStream());
            } catch (IOException e) {
                mimeType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, mimeType)
                    .contentLength(image.length())
                    .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
