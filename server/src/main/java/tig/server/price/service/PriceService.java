package tig.server.price.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tig.server.global.code.ErrorCode;
import tig.server.global.exception.BusinessExceptionHandler;
import tig.server.price.domain.Price;
import tig.server.price.repository.PriceRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PriceService {
    private final PriceRepository priceRepository;

    public Integer getClubPriceByClubIdAndHour(Long clubId, int hour) {
        Price price = priceRepository.findAllByClubAndHour(clubId, hour)
                .orElseThrow(() -> new BusinessExceptionHandler("price not found", ErrorCode.NOT_FOUND_ERROR));
        return price.getPrice();
    }
}
