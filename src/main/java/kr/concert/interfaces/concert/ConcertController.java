package kr.concert.interfaces.concert;

import kr.concert.application.queue.QueueFacade;
import kr.concert.domain.concert.entity.Concert;
import kr.concert.domain.concert.service.ConcertService;
import kr.concert.domain.queue.service.QueueService;
import kr.concert.infra.config.swagger.api.SwaggerConcertApi;
import kr.concert.interfaces.presentation.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/concerts")
public class ConcertController implements SwaggerConcertApi {

    private final ConcertService concertService;

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    /**
     * 콘서트 추가
     */
    @PostMapping()
    public ApiResponse<ConcertResponse.CreateConcert> createConcerts(@RequestBody ConcertRequest.CreateConcert createConcert){
        return new ApiResponse<>(true, 201, "콘서트를 생성하였습니다.", concertService.createConcert(createConcert.concertName()));
    }

    /**
     * 빠른 매진 랭킹 순위 (최근 7일)
     */
    @GetMapping("/rank")
    public ApiResponse<List<ConcertResponse.Rank>> getConcertRanks(){

        return new ApiResponse<>(true, 200, "빠른 매진 랭킹 순위를 조회하였습니다.", concertService.getConcertRanks());
    }
}
