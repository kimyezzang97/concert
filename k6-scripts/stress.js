import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    stages: [
        { duration: '1m', target: 24000 },
    ]
}

export default function () {
    http.get('http://concert-service:8080/api/v1/reservations/concerts');

    // 응답 상태 체크
    check(res, {
        "GET status is 200": (r) => r.status === 200,
    });

    sleep(1);
}