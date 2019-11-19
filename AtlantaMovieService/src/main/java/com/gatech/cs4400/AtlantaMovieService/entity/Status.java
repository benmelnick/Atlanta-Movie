package com.gatech.cs4400.AtlantaMovieService.entity;

public enum Status {
    APPROVED("Approved"),
    PENDING("Pending"),
    DECLINED("Declined");

    private final String status;
    Status(String status) {
        this.status = status;
    }
}
