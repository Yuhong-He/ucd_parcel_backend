package com.example.entry.utils;

public class PermissionHelper {

    public static String[] unregistered() {
        return new String[]{
            "/user/login",
            "/user/sendRegisterEmail",
            "/user/register",
            "/",
            "/api-docs",
            "/swagger-ui/*"
        };
    }

    public static String[] anyRegistered() {
        return new String[]{
            "/parcel/list",
            "/parcel/tracks"
        };
    }

    public static String[] student() {
        return new String[]{
            "/parcel/confirmAddress"
        };
    }

    public static String[] postman() {
        return new String[]{
            "/parcel/deliver"
        };
    }

    public static String[] mervilleStaff() {
        return new String[]{
            "/parcel/confirmCollected"
        };
    }

    public static String[] estateServiceStaff() {
        return new String[]{
            "/user/searchStudentByName",
            "/parcel/create"
        };
    }

}
