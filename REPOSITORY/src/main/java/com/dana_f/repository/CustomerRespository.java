package com.dana_f.repository;

import android.app.Application;

import com.dana_f.model.Customer;
import com.dana_f.model.Customers;
import com.dana_f.repository.BASE.BaseRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;

public class CustomerRespository extends BaseRepository<Customer, Customers> {
    public CustomerRespository(Application application) {
        super(Customer.class, Customers.class, application);
    }

    @Override
    protected Query getQueryForExist(Customer entity) {
        return getCollection().whereEqualTo("email", entity.getEMAIL());
    }


    public Task<Customer> getCustomerById(String id) {
        return get(id);
        // `get(id)` is inherited from BaseRepository
    }
}
