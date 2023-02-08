package com.example.vqshki.utils;

import lombok.Getter;

@Getter
public enum ErrorCode {
    IMPOSSIBLE_TO_LOCATE_BY_ONE_BASE_STATION("Impossible to accurately locate mobile station by 1 Base Station", 5001),
    DETECTED_BY_TWO_BASE_STATIONS_WITH_ERROR("Mobile Station Detected by two Base Stations with an error", 5002);

    final String errorMessage;
    final Integer errorCode;

    ErrorCode(String errorMessage, Integer errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
