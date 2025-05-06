package com.dana_f.viewmodel;

import android.app.Application;

import com.dana_f.repository.BASE.BaseRepository;
import com.dana_f.viewmodel.BASE.BaseViewModel;

public class XviewModel extends BaseViewModel {
    @Override
    protected BaseRepository createRepository(Application application) {
        return null;
    }

    public XviewModel(Class tEntity, Class tCollection, Application application) {
        super(tEntity, tCollection, application);
    }
}
