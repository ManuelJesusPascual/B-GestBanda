package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.img.ImgResponse;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

/**
 * Service class responsible for handling image upload operations, specifically to the Imgur service.
 * Encodes images, prepares HTTP requests, and handles responses for the upload process.
 */
@Service
public class ImgService {

    /**
     * Client ID for Imgur, retrieved from application properties.
     */
    @Value("${imgur.clientID}")
    private String clientId;

    /**
     * URL for uploading images to Imgur, retrieved from application properties.
     */
    @Value("${imgur.uploadUrl}")
    private String uploadUrl;

    /**
     * HTTP client used to call the Imgur API.
     */
    private final RestClient restClient = RestClient.create();

    /**
     * Uploads an image to Imgur by encoding it and sending it in a multipart/form-data request.
     * Handles exceptions and error responses from the upload process.
     *
     * @param img MultipartFile object representing the image to be uploaded.
     * @return ImgResponse containing details of the uploaded image.
     */
    public ImgResponse uploadImage(MultipartFile img) {
        ResponseEntity<ImgResponse> response;
        try {
            String encodedImage = getEncodedImg(img);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("image", encodedImage);

            response = restClient.post()
                    .uri(uploadUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Client-ID " + clientId)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .toEntity(ImgResponse.class);

        } catch (Exception e) {
            throw new GestBandaException(new Exception(), "Error al intentar guardar la imagen", ErrorCodes.UPLOAD_IMAGE_ERROR);
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new GestBandaException(new Exception(), "Error al intentar guardar la imagen", ErrorCodes.UPLOAD_IMAGE_ERROR);
        }
        return response.getBody();
    }

    /**
     * Encodes the given image in Base64 format to prepare it for transmission in a multipart form request.
     *
     * @param img MultipartFile containing the image to be encoded.
     * @return A Base64-encoded string of the image.
     * @throws IOException if an error occurs while reading the image file.
     */
    private String getEncodedImg(MultipartFile img) throws IOException {
        byte[] imgBytes = img.getBytes();
        return Base64.getEncoder().encodeToString(imgBytes);
    }
}
