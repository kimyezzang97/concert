package kr.concert.infra.config.swagger.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.concert.interfaces.concert.ConcertRequest;
import kr.concert.interfaces.concert.ConcertResponse;
import kr.concert.interfaces.presentation.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Concert", description = "콘서트 관련 API")
public interface SwaggerConcertApi {

    @Operation(summary = "콘서트 생성", description = "콘서트를 생성합니다.")
    public ApiResponse<ConcertResponse.CreateConcert> createConcerts(ConcertRequest.CreateConcert createConcert);

    @Operation(summary = "콘서트 랭킹 조회", description = "최근 7일간 콘서트들의 빠른 매진 랭킹을 조회합니다.")
    ApiResponse<List<ConcertResponse.Rank>> getConcertRanks();
}
