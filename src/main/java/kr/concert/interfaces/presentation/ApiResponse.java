package kr.concert.interfaces.presentation;

import lombok.Builder;
import lombok.Getter;


// [record] private final, equals(), toString(), hashCode(), getter 자동 생성
public record ApiResponse<T>(boolean success, int code, String message, T data) {

}
