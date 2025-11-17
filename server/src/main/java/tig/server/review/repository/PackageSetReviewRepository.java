package tig.server.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tig.server.review.domain.PackageSetReview;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageSetReviewRepository extends JpaRepository<PackageSetReview, Long> {

    @Query("SELECT psr FROM PackageSetReview psr " +
           "JOIN FETCH psr.packageReservation pr " +
           "JOIN FETCH pr.packageSet ps " +
           "WHERE ps.id = :packageSetId " +
           "ORDER BY psr.createdAt DESC")
    List<PackageSetReview> findByPackageSetId(@Param("packageSetId") Long packageSetId);

    @Query("SELECT psr FROM PackageSetReview psr " +
           "JOIN FETCH psr.packageReservation pr " +
           "WHERE pr.id = :packageReservationId")
    Optional<PackageSetReview> findByPackageReservationId(@Param("packageReservationId") Long packageReservationId);

    @Query("SELECT psr FROM PackageSetReview psr " +
           "JOIN FETCH psr.member m " +
           "WHERE m.id = :memberId " +
           "ORDER BY psr.createdAt DESC")
    List<PackageSetReview> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT AVG(psr.rating) FROM PackageSetReview psr " +
           "JOIN psr.packageReservation pr " +
           "JOIN pr.packageSet ps " +
           "WHERE ps.id = :packageSetId")
    Double findAverageRatingByPackageSetId(@Param("packageSetId") Long packageSetId);

    @Query("SELECT COUNT(psr) FROM PackageSetReview psr " +
           "JOIN psr.packageReservation pr " +
           "JOIN pr.packageSet ps " +
           "WHERE ps.id = :packageSetId")
    Long countByPackageSetId(@Param("packageSetId") Long packageSetId);
}