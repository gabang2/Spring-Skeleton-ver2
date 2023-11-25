package com.project.global.error.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    INVALID_REQUEST(400, "C007", "요청이 잘못되었습니다."),
    MONTH_FORMAT_BAD_REQUEST(400, "C008", "월별 조회는 yyyy-mm 형식으로 맞춰야 합니다."),
    LOCAL_DATE_TIME_VALIDATOR(400, "C009", "날짜 입력 형식을 확인해주세요. ex) 2023-11-01T00:00:00"),


    // S3
    IMAGE_UPLOAD_FAIL(400, "S001", "이미지를 업로드할 수 없습니다."),

    // Member
    AUTHORIZATION_HEADER_NOT_VALID(400, "M001", "Authorization 헤더가 유효하지 않습니다."),
    MISSING_REQUEST(400, "M003", "필수 요청 정보기 누락되었습니다."),
    KAKAO_CODE_NOT_VALID(400, "M004", "카카오 코드가 유효하지 않습니다."),
    TOKEN_NOT_VALID(400, "M005", "AccessToken이 유효하지 않습니다."),
    MEMBER_NOT_FOUND(400, "M006", "사용자를 찾을 수 없습니다."),
    NICKNAME_DUPLICATE(400, "M007", "닉네임이 중복되었습니다."),
    MEMBER_LOGOUT(400, "M008", "이미 로그아웃한 유저입니다."),
    MEMBER_ALREADY(400, "M009", "이미 가입한 유저입니다."),
    EMAIL_DUPLICATE(400, "M010", "이메일이 중복되었습니다."),
    EMAIL_NOT_EXIST(400, "M012", "존재하지 않는 이메일입니다."),
    PASSWORD_NOT_MATCH(400, "M013", "비밀번호가 잘못되었습니다."),

    // Ticket
    TICKET_NOT_FOUND(400, "T001", "티켓을 찾을 수 없습니다."),
    TICKET_DELETE_FAIL(400, "T002", "내 티켓만 삭제할 수 있습니다."),
    TICKET_VIEW_FAIL(400, "T003", "내 티켓 또는 전체 공개 티켓만 조회할 수 있습니다."),
    TICKET_PATCH_FAIL(400, "T004", "내 티켓만 수정할 수 있습니다."),

    // Feed
    CATEGORY_NOT_FOUND(400, "CA001", "카테고리를 찾을 수 없습니다.")

    ;

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
