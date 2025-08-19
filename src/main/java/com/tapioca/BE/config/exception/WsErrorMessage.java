package com.tapioca.BE.config.exception;

/**
 * WebSocket 표준 에러 메시지 포맷.
 * - REST와 달리 @RestControllerAdvice가 WS 프레임에는 개입하지 않으므로,
 *   핸들러 내부에서 직접 직렬화하여 전송해야 한다.
 */
public record WsErrorMessage(
        String type,        // 항상 "error" (클라이언트 측에서 메시지 타입 분기용)
        String code,        // ErrorCode.code (예: "40701")
        String message      // ErrorCode.message (예: "sourceType, id 필수입니다.")
) {
    public static WsErrorMessage of(ErrorCode ec) {
        return new WsErrorMessage("error", ec.getCode(), ec.getMessage());
    }
}