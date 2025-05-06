package com.dana_f.repository;

import com.google.firebase.firestore.Query;

import com.dana_f.model.BASE.BaseEntity;
import com.dana_f.repository.BASE.BaseRepository;

public class Xrepository extends BaseRepository {
    @Override
    protected Query getQueryForExist(BaseEntity entity) {
        return null;
    }
}
