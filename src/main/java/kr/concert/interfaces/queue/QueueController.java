package kr.concert.interfaces.queue;

import kr.concert.application.queue.QueueFacade;
import kr.concert.domain.queue.QueueService;
import kr.concert.infra.config.swagger.api.SwaggerQueueApi;
import kr.concert.interfaces.presentation.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/queue")
public class QueueController implements SwaggerQueueApi {

    private final QueueFacade queueFacade;
    private final QueueService queueService;

    public QueueController(QueueFacade queueFacade, QueueService queueService) {
        this.queueFacade = queueFacade;
        this.queueService = queueService;
    }

    /**
     * 대기열 토큰 생성
     */
    @PostMapping("/token")
    public ApiResponse<String> createToken(@RequestParam Long memberId) {

        return new ApiResponse<>(true, 201, "토큰 생성 성공", queueFacade.createToken(memberId));
    }

    /**
     * 대기열 정보 조회
     */
    @PostMapping("/status")
    public ApiResponse<QueueResponse.QueueStatus> getQueue(@RequestParam String token) {

        return new ApiResponse<>(true, 200, "대기열 정보 조회 성공", queueService.getQueue(token));
    }
}
