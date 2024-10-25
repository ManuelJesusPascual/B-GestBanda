package com.mjpascual.gestbanda.dto.img;

import lombok.Data;

/**
 * Represents the response structure for retrieving the uploaded image from Imgur.
 * This class is used to parse and store the response from Imgur API, including
 * the success status, HTTP status code, and image data.
 */
@Data
public class ImgResponse {
    private boolean success;
    private int status;
    private DataImg data;

    /**
     * Holds image-specific details returned from Imgur, such as the image ID and direct link.
     */
    @Data
    public static class DataImg {
        private String id;
        private String link;
    }
}
