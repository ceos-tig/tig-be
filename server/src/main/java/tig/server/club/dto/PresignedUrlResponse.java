package tig.server.club.dto;

public record PresignedUrlResponse(
        String presignedUrl,
        String imageUrl,
        String objectKey
) {}
