package tig.server.reservation.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tig.server.reservation.PackageReservationRequest;
import tig.server.reservation.dto.*;

@Converter
public class ReservationDetailsConverter implements AttributeConverter<PackageReservationRequest, String> {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public String convertToDatabaseColumn(PackageReservationRequest attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }

    @Override
    public PackageReservationRequest convertToEntityAttribute(String dbData) {
        try {
            // 카테고리에 따라 적절한 클래스로 역직렬화
            JsonNode jsonNode = objectMapper.readTree(dbData);
            String category = jsonNode.get("category").asText();

            return switch (category) {
                case "GOLFCLUB" -> objectMapper.treeToValue(jsonNode, GolfClubReservationRequest.class);
                case "PENSION" -> objectMapper.treeToValue(jsonNode, PensionReservationRequest.class);
                case "BUS" -> objectMapper.treeToValue(jsonNode, BusReservationRequest.class);
                case "BUFFET" -> objectMapper.treeToValue(jsonNode, BuffetReservationRequest.class);
                case "LUNCH_BOX" -> objectMapper.treeToValue(jsonNode, LunchBoxReservationRequest.class);
                case "UNIFORM" -> objectMapper.treeToValue(jsonNode, UniformReservationRequest.class);
                default -> throw new IllegalArgumentException("알 수 없는 카테고리: " + category);
            };
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }
}