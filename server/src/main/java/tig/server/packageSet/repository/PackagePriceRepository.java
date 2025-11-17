package tig.server.packageSet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tig.server.packageSet.domain.PackagePrice;
import tig.server.packageSet.domain.PackageSet;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackagePriceRepository extends JpaRepository<PackagePrice, Long> {

    List<PackagePrice> findByPackageSetId(Long packageSetId);

    @Query("SELECT pp FROM PackagePrice pp WHERE pp.packageSet.id = :packageSetId AND pp.isDefault = true")
    Optional<PackagePrice> findDefaultPriceByPackageSetId(@Param("packageSetId") Long packageSetId);

    @Query("SELECT pp FROM PackagePrice pp WHERE pp.packageSet.id = :packageSetId AND pp.optionType = :optionType AND pp.optionValue = :optionValue")
    Optional<PackagePrice> findByPackageSetIdAndOptionTypeAndOptionValue(
            @Param("packageSetId") Long packageSetId,
            @Param("optionType") String optionType,
            @Param("optionValue") String optionValue
    );

    @Query("SELECT pp FROM PackagePrice pp WHERE pp.packageSet = :packageSet")
    List<PackagePrice> findByPackageSet(@Param("packageSet") PackageSet packageSet);

    void deleteByPackageSetId(Long packageSetId);
}