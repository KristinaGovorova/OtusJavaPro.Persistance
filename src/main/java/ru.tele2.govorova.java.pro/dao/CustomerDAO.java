package ru.tele2.govorova.java.pro.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.tele2.govorova.java.pro.entity.Customer;

import java.util.List;

public class CustomerDAO {
    private final SessionFactory sessionFactory;

    public CustomerDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveCustomer(Customer customer) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(customer);
            transaction.commit();
        }
    }

    public Customer getCustomerById(Long id) {
        try (Session session = sessionFactory.openSession()){
            return session.get(Customer.class, id);
        }
    }

    public List<Customer> getAllCustomers() {
        try (Session session = sessionFactory.openSession()){
            return session.createQuery("FROM Customer", Customer.class).list();
        }
    }

    public void deleteCustomer(Long id) {
        try (Session session = sessionFactory.openSession()){
            Transaction transaction = session.beginTransaction();
            Customer customer = session.get(Customer.class, id);
            if (customer != null) {
                session.delete(customer);
            }
            transaction.commit();
        }
    }
}
