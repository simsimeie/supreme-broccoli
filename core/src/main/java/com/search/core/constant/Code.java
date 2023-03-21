package com.search.core.constant;

import lombok.Getter;

@Getter
public enum Code {
    SUCCESS("KB200", CodeCategory.SUCCESS, "성공"),
    SUCCESS_BUT_NO_DATA("KB20001", CodeCategory.SUCCESS, "조회된 결과가 없습니다."),
    BAD_REQUEST("KB400", CodeCategory.CLIENT_SIDE_ERROR, "입력하신 값을 다시 한 번 확인해주세요."),
    SEARCH_CRITERIA_VALIDATION_ERROR("KB40001", CodeCategory.CLIENT_SIDE_ERROR, "criteria 값은 A 또는 R만 가능합니다."),
    INTERNAL_SERVER_ERROR("KB500", CodeCategory.SERVER_SIDE_ERROR, "처리 중 에러가 발생했습니다."),
    API_ERROR("KB50001", CodeCategory.SERVER_SIDE_ERROR, "Open API에서 에러가 발생했습니다."),
    DB_ERROR("KB50002", CodeCategory.SERVER_SIDE_ERROR, "데이터 처리 중 에러가 발생했습니다.");

    private final String code;
    private final CodeCategory category;
    private final String message;

    Code(String code, CodeCategory category, String message) {
        this.code = code;
        this.category = category;
        this.message = message;
    }
    public boolean isSuccess() {
        return this.getCategory() == CodeCategory.SUCCESS;
    }
    public boolean isClientSideError() {
        return this.getCategory() == CodeCategory.CLIENT_SIDE_ERROR;
    }


}
