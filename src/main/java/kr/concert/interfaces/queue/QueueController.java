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

    public QueueController(QueueFacade queueFacade) {
        this.queueFacade = queueFacade;
    }

    /**
     * 대기열 토큰 생성
     */
    @PostMapping("/token")
    public ApiResponse<String> createToken(@RequestParam Long memberId) {

        return new ApiResponse<>(true, 201, "토큰 생성 성공", queueFacade.createToken(memberId));
    }

    /**
     *
     */
}
