package com.dana_f.viewmodel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.dana_f.model.Customer;
import com.dana_f.model.Customers;
import com.dana_f.repository.BASE.BaseRepository;
import com.dana_f.repository.CustomerRespository;
import com.dana_f.viewmodel.BASE.BaseViewModel;
import com.google.firebase.firestore.Query;

public class CustomerViewModel extends BaseViewModel<Customer, Customers> {
    private final MutableLiveData<Customer> lvCustomer;
    private CustomerRespository respository;


    public CustomerViewModel(Application application) {
        super(Customer.class, Customers.class, application);
        lvCustomer = new MutableLiveData<>();
    }

    @Override
    protected BaseRepository<Customer, Customers> createRepository(Application application) {
        respository = new CustomerRespository(application);
        return respository;
    }

    public void getAll() {
        getAll(Query.Direction.ASCENDING);
    }

    public void getAll(Query.Direction direction) {
        getAll(repository.getCollection().orderBy("name", direction));
    }

    public void updateCustomerAndRefresh() {
        Customer currentCustomer = getEntity().getValue();
        if (currentCustomer != null) {
            respository.update(currentCustomer)
                    .addOnSuccessListener(isSuccess -> {
                        if (isSuccess) {
                            lvEntity.postValue(currentCustomer); // Notify observers like FirstPage
                        }
                    });
        }
    }


    public void logIn(String email, String password) {
        respository.getCollection().whereEqualTo("email", email).whereEqualTo("password", password).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.size() > 0) {
                        Customer customer = queryDocumentSnapshots.getDocuments().get(0).toObject(Customer.class);
                        lvEntity.setValue(customer);
                    }
                })
                .addOnFailureListener(e -> {
                    lvEntity.setValue(null);
                });
    }


}
