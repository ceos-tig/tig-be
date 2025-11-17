package tig.server.packageSet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tig.server.club.domain.Club;
import tig.server.packageSet.domain.PackageSet;

import java.util.List;

public interface PackageSetRepository extends JpaRepository<PackageSet, Long> {
    @Query(value =
            "SELECT *\n"+
                    "  FROM package_set\n"+
                    " WHERE REPLACE(name,' ','')    LIKE CONCAT('%', :keyword, '%')\n"+
                    "    OR REPLACE(address,' ','') LIKE CONCAT('%', :keyword, '%')\n"+
                    " LIMIT 100",
            nativeQuery = true
    )
    List<PackageSet> searchByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM package_set ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<PackageSet> findRandomPackages(@Param("limit") int limit);

    // 방법 1: 평점 기준 인기 패키지 (평점 높은 순)
    @Query("SELECT p FROM PackageSet p WHERE p.ratingCount > 0 ORDER BY (p.ratingSum / p.ratingCount) DESC, p.ratingCount DESC")
    List<PackageSet> findPopularPackagesByRating();

    @Query(value = "SELECT * FROM package_set WHERE category = :category ORDER BY RAND() LIMIT :limit",
            nativeQuery = true)
    List<PackageSet> findRandomPackagesByCategory(@Param("category") String category, @Param("limit") int limit);

    List<PackageSet> findAll();
}
