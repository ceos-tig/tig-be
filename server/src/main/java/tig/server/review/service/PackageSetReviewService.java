package tig.server.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.global.code.ErrorCode;
import tig.server.member.domain.Member;
import tig.server.member.repository.MemberRepository;
import tig.server.openai.service.OpenAIService;
import tig.server.packageSet.domain.PackageSet;
import tig.server.packageSet.repository.PackageSetRepository;
import tig.server.reservation.domain.PackageReservation;
import tig.server.reservation.repository.PackageReservationRepository;
import tig.server.review.domain.PackageSetReview;
import tig.server.review.dto.PackageSetReviewRequest;
import tig.server.review.dto.PackageSetReviewResponse;
import tig.server.review.dto.PackageSetReviewWithSummaryResponse;
import tig.server.review.repository.PackageSetReviewRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PackageSetReviewService {
    private final PackageSetReviewRepository packageSetReviewRepository;
    private final PackageReservationRepository packageReservationRepository;
    private final PackageSetRepository packageSetRepository;
    private final MemberRepository memberRepository;
    private final OpenAIService openAIService;
    private final RedisTemplate<String, String> redisTemplateAI;

    private static final String REDIS_PACKAGE_REVIEW_SUMMARY_KEY_PREFIX = "PackageReviewSummary:";

    @Transactional
    public PackageSetReviewResponse createPackageSetReview(Long memberId, PackageSetReviewRequest request) {
        try {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new BusinessExceptionHandler("Member not found", ErrorCode.NOT_FOUND_ERROR));

            PackageReservation packageReservation = packageReservationRepository.findById(request.getPackageReservationId())
                    .orElseThrow(() -> new BusinessExceptionHandler("Package reservation not found", ErrorCode.NOT_FOUND_ERROR));

            // 이미 리뷰가 작성된 예약인지 확인
            if (packageSetReviewRepository.findByPackageReservationId(request.getPackageReservationId()).isPresent()) {
                throw new BusinessExceptionHandler("Review already exists for this package reservation", ErrorCode.BAD_REQUEST_ERROR);
            }

            // 예약한 사용자와 리뷰 작성자가 같은지 확인
            if (!packageReservation.getMember().getId().equals(memberId)) {
                throw new BusinessExceptionHandler("Unauthorized to write review for this reservation", ErrorCode.FORBIDDEN_ERROR);
            }

            PackageSetReview packageSetReview = PackageSetReview.builder()
                    .packageReservation(packageReservation)
                    .member(member)
                    .rating(request.getRating())
                    .contents(request.getContents())
                    .build();

            packageSetReviewRepository.save(packageSetReview);

            // PackageSet의 평점 업데이트
            updatePackageSetRating(packageReservation.getPackageSet().getId());

            return mapToResponse(packageSetReview);

        } catch (Exception e) {
            throw new BusinessExceptionHandler("Package set review creation failed: " + e.getMessage(), ErrorCode.IO_ERROR);
        }
    }

    @Transactional
    public PackageSetReviewResponse modifyPackageSetReview(Long reviewId, PackageSetReviewRequest request) {
        try {
            PackageSetReview existingReview = packageSetReviewRepository.findById(reviewId)
                    .orElseThrow(() -> new BusinessExceptionHandler("Package set review not found", ErrorCode.NOT_FOUND_ERROR));

            existingReview.setRating(request.getRating());
            existingReview.setContents(request.getContents());

            packageSetReviewRepository.save(existingReview);

            // PackageSet의 평점 업데이트
            updatePackageSetRating(existingReview.getPackageReservation().getPackageSet().getId());

            return mapToResponse(existingReview);

        } catch (Exception e) {
            throw new BusinessExceptionHandler("Package set review modification failed: " + e.getMessage(), ErrorCode.IO_ERROR);
        }
    }

    public PackageSetReviewResponse getPackageSetReviewById(Long reviewId) {
        PackageSetReview review = packageSetReviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessExceptionHandler("Package set review not found", ErrorCode.NOT_FOUND_ERROR));

        return mapToResponse(review);
    }

    public PackageSetReviewWithSummaryResponse getPackageSetReviewsByPackageSetId(Long packageSetId) {
        PackageSet packageSet = packageSetRepository.findById(packageSetId)
                .orElseThrow(() -> new BusinessExceptionHandler("Package set not found", ErrorCode.NOT_FOUND_ERROR));

        List<PackageSetReview> reviews = packageSetReviewRepository.findByPackageSetId(packageSetId);
        Double averageRating = packageSetReviewRepository.findAverageRatingByPackageSetId(packageSetId);
        Long totalReviewCount = packageSetReviewRepository.countByPackageSetId(packageSetId);

        // AI 요약 처리
        String redisKey = REDIS_PACKAGE_REVIEW_SUMMARY_KEY_PREFIX + packageSetId;
        String cachedSummary = redisTemplateAI.opsForValue().get(redisKey);
        String aiSummary = cachedSummary;

        if (totalReviewCount >= 3 && (totalReviewCount % 10 == 0 || aiSummary == null)) {
            StringBuilder prompt = new StringBuilder();
            reviews.stream()
                    .filter(review -> Objects.nonNull(review.getContents()))
                    .limit(50)
                    .forEach(review -> prompt.append(review.getContents()).append(" "));

            if (prompt.length() > 0) {
                aiSummary = openAIService.reviewSummary(prompt.toString()).getChoices().get(0).getMessage().getContent();
                redisTemplateAI.opsForValue().set(redisKey, aiSummary);
            }
        }

        List<PackageSetReviewResponse> reviewResponses = reviews.stream()
                .limit(50)
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PackageSetReviewWithSummaryResponse.builder()
                .packageSetId(packageSetId)
                .packageSetName(packageSet.getName())
                .averageRating(averageRating != null ? averageRating : 0.0)
                .totalReviewCount(totalReviewCount)
                .reviews(reviewResponses)
                .reviewSummary(aiSummary)
                .build();
    }

    public List<PackageSetReviewResponse> getPackageSetReviewsByMemberId(Long memberId) {
        List<PackageSetReview> reviews = packageSetReviewRepository.findByMemberId(memberId);
        return reviews.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePackageSetReview(Long reviewId) {
        try {
            PackageSetReview review = packageSetReviewRepository.findById(reviewId)
                    .orElseThrow(() -> new BusinessExceptionHandler("Package set review not found", ErrorCode.NOT_FOUND_ERROR));

            Long packageSetId = review.getPackageReservation().getPackageSet().getId();

            review.setDeleted(true);
            packageSetReviewRepository.save(review);

            // PackageSet의 평점 업데이트
            updatePackageSetRating(packageSetId);

        } catch (Exception e) {
            throw new BusinessExceptionHandler("Package set review deletion failed: " + e.getMessage(), ErrorCode.IO_ERROR);
        }
    }

    @Transactional
    protected void updatePackageSetRating(Long packageSetId) {
        Double averageRating = packageSetReviewRepository.findAverageRatingByPackageSetId(packageSetId);
        Long reviewCount = packageSetReviewRepository.countByPackageSetId(packageSetId);

        PackageSet packageSet = packageSetRepository.findById(packageSetId)
                .orElseThrow(() -> new BusinessExceptionHandler("Package set not found", ErrorCode.NOT_FOUND_ERROR));

        // PackageSet에 rating 필드가 있다면 업데이트
        // 현재 PackageSet 엔티티에는 ratingSum과 ratingCount가 있으므로 이를 활용
        if (averageRating != null && reviewCount != null) {
            // 실제 구현시에는 PackageSet 엔티티를 수정하여 rating 필드를 추가하거나
            // 기존 ratingSum, ratingCount를 활용하여 계산
        }
    }

    private PackageSetReviewResponse mapToResponse(PackageSetReview review) {
        return PackageSetReviewResponse.builder()
                .reviewId(review.getId())
                .packageReservationId(review.getPackageReservation().getId())
                .packageSetId(review.getPackageReservation().getPackageSet().getId())
                .packageSetName(review.getPackageReservation().getPackageSet().getName())
                .category(review.getPackageReservation().getCategory())
                .rating(review.getRating())
                .contents(review.getContents())
                .userName(review.getMember().getName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}