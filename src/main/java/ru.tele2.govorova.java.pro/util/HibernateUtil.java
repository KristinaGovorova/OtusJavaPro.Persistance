package ru.tele2.govorova.java.pro.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.tele2.govorova.java.pro.entity.Customer;
import ru.tele2.govorova.java.pro.entity.Product;
import ru.tele2.govorova.java.pro.entity.Purchase;

import java.time.LocalDate;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();

            MetadataSources sources = new MetadataSources(registry)
                    .addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(Purchase.class);

            sessionFactory = sources.buildMetadata().buildSessionFactory();
        }
        return sessionFactory;
    }


   public static void initializeData(){
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Массивы
            String[] customerNames = {"Иван", "Денис", "Алексей", "Геннадий", "Мария"};
            String[] productNames = {"Телефон", "Машина", "Обувь"};
            double[] productPrices = {24600, 980000, 6200};

            // Добавление клиентов
            Customer[] customers = new Customer[customerNames.length];
            for (int i = 0; i < customers.length; i++) {
                Customer customer = new Customer();
                customer.setName(customerNames[i]);
                customers[i] = customer;
                session.save(customer);
            }

            // Добавление товаров
            Product[] products = new Product[productNames.length];
            for (int i = 0; i < products.length; i++) {
                Product product = new Product();
                product.setName(productNames[i]);
                product.setPrice(productPrices[i]);
                products[i] = product;
                session.save(product);
            }

            // Добавление покупок
            Purchase[] purchases = new Purchase[] {
                    createPurchase(customers[0], products[0], LocalDate.of(2025, 1, 12)),
                    createPurchase(customers[1], products[1], LocalDate.of(2025, 1, 15)),
                    createPurchase(customers[2], products[2], LocalDate.of(2025, 1, 16)),
                    createPurchase(customers[3], products[1], LocalDate.of(2025, 1, 17)),
                    createPurchase(customers[4], products[1], LocalDate.of(2025, 1, 19)),
                    createPurchase(customers[4], products[2], LocalDate.of(2025, 1, 8)),
                    createPurchase(customers[2], products[0], LocalDate.of(2025, 1, 4))
            };

            for (Purchase purchase : purchases) {
                session.save(purchase);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
   }

    private static Purchase createPurchase(Customer customer, Product product, LocalDate date) {
        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setProduct(product);
        purchase.setPurchaseDate(date);
        return purchase;
    }

    public static void shutdown(){
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

