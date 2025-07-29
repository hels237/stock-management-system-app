package com.k48.stock_management_system.services;

import java.io.InputStream;

public interface FlickrService {
    String savePhoto(InputStream photo, String title);
}
