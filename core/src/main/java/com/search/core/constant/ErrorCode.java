package com.search.core.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS("KB200", ErrorCategory.SUCCESS, "성공"),
    SUCCESS_BUT_NO_DATA("KB20001", ErrorCategory.SUCCESS, "조회된 결과가 없습니다."),
    BAD_REQUEST("KB400", ErrorCategory.CLIENT_SIDE, "입력하신 값을 다시 한 번 확인해주세요."),
    SEARCH_CRITERIA_VALIDATION_ERROR("KB40001", ErrorCategory.CLIENT_SIDE, "criteria 값은 A 또는 R만 가능합니다."),
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
    public boolean isSuccess() {
        return this.getCategory() == ErrorCategory.SUCCESS;
    }
    public boolean isClientSideError() {
        return this.getCategory() == ErrorCategory.CLIENT_SIDE;
    }

    public enum ErrorCategory {
        SUCCESS, CLIENT_SIDE, SERVER_SIDE
    }
}
