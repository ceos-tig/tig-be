package tig.server.club.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.club.domain.Club;
import tig.server.club.dto.ClubDTO;
import tig.server.club.mapper.ClubMapper;
import tig.server.club.repository.ClubRepository;
import tig.server.config.S3Uploader;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper = ClubMapper.INSTANCE;

    private final S3Uploader s3Uploader;

    public List<ClubDTO.Response> getAllClubs() {
        return clubRepository.findAll().stream()
                .map(clubMapper::entityToResponse)
                .collect(Collectors.toList());
    }

    public ClubDTO.Response getClubById(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("club not found"));
        List<String> CloudFrontImageUrl = s3Uploader.getImageUrls(club.getImageUrls());
        club.setImageUrls(CloudFrontImageUrl);
        return clubMapper.entityToResponse(club);
    }


}
