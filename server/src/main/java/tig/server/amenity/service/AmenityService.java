package tig.server.amenity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.amenity.domain.Amenity;
import tig.server.amenity.dto.AmenityResponseDto;
import tig.server.amenity.repository.AmenityRepository;
import tig.server.enums.Facility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AmenityService {
    private final AmenityRepository amenityRepository;

    public List<Facility> getAmenitiesByClubId(Long clubId) {
        Optional<List<Amenity>> amenities = amenityRepository.findByClubId(clubId);
        if (amenities.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        } else {
            List<Amenity> amenityList = amenities.get();
            List<AmenityResponseDto> amenityResponseDtoList = new ArrayList<>();
            for (Amenity amenity : amenityList) {
                AmenityResponseDto amenityResponseDto = AmenityResponseDto.fromAmenity(amenity.getName());
                amenityResponseDtoList.add(amenityResponseDto);
            }
            return amenityResponseDtoList.stream()
                    .map(AmenityResponseDto::getFacility)
                    .collect(Collectors.toList());
        }
    }
}
