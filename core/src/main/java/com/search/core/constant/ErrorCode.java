package com.search.core.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS("KB200", ErrorCategory.SUCCESS, "성공"),
    BAD_REQUEST("KB400", ErrorCategory.CLIENT_SIDE, "입력하신 값을 다시 한 번 확인해주세요."),
    INTERNAL_SERVER_ERROR("KB500", ErrorCategory.SERVER_SIDE, "처리 중 에러가 발생했습니다."),
    API_ERROR("KB50001", ErrorCategory.SERVER_SIDE, "Open API에서 에러가 발생했습니다."),
    DB_ERROR("KB50002", ErrorCategory.SERVER_SIDE, "데이터 처리 중 에러가 발생했습니다.");

    private final String code;
    private final ErrorCategory category;
    private final String message;

    ErrorCode(String code, ErrorCategory category, String message) {
        this.code = code;
        this.category = category;
        this.message = message;
    }
    public boolean isClientSideError() {
        return this.getCategory() == ErrorCategory.CLIENT_SIDE;
    }
    enum ErrorCategory {
        SUCCESS, CLIENT_SIDE, SERVER_SIDE
    }
}
