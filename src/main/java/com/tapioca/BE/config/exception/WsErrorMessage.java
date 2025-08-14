package com.tapioca.BE.config.exception;

/**
 * WebSocket 표준 에러 메시지 포맷.
 * - WS 핸들러에서 error 발생 시, 항상 이 포맷으로만 클라이언트에 전송한다.
 * - REST와 달리 @RestControllerAdvice가 WS 프레임에는 개입하지 않으므로,
 *   핸들러 내부에서 직접 직렬화하여 전송해야 한다.
 *
 * 필드 규약
 * - type   : 항상 "error" (클라이언트 측에서 메시지 타입 분기용)
 * - code   : ErrorCode.code (예: "40701")
 * - message: ErrorCode.message (예: "sourceType, id 필수입니다.")
 */
public record WsErrorMessage(
        String type,
        String code,
        String message
) {
    public static WsErrorMessage of(ErrorCode ec) {
        return new WsErrorMessage("error", ec.getCode(), ec.getMessage());
    }
}