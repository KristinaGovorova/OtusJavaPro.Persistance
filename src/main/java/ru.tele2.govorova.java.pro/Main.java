package ru.tele2.govorova.java.pro;

import ru.tele2.govorova.java.pro.util.HibernateUtil;
import ru.tele2.govorova.java.pro.dao.CustomerDAO;
import ru.tele2.govorova.java.pro.dao.ProductDAO;
import ru.tele2.govorova.java.pro.entity.Customer;
import ru.tele2.govorova.java.pro.service.PurchaseService;

import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        try {
            HibernateUtil.initializeData();


            CustomerDAO customerDAO = new CustomerDAO(HibernateUtil.getSessionFactory());
            ProductDAO productDAO = new ProductDAO(HibernateUtil.getSessionFactory());

            PurchaseService purchaseService = new PurchaseService(HibernateUtil.getSessionFactory(), customerDAO, productDAO);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println();
                System.out.println("Выберите действие: ");
                System.out.println("1. Посмотреть товары клиента");
                System.out.println("2. Найти клиентов по товару");
                System.out.println("3. Удалить товар");
                System.out.println("4. Удалить клиента");
                System.out.println("5. Выход");

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("Введите ID клиента: ");
                        Long customerId = scanner.nextLong();
                        purchaseService.viewCustomerPurchases(customerId);
                        break;
                    case 2:
                        System.out.println("Введите ID товара: ");
                        Long productId = scanner.nextLong();

                        Set<Customer> customers = purchaseService.findCustomersByProduct(productId);
                        if (!customers.isEmpty()) {
                            System.out.println("Клиенты, купившие товар:");
                            for (Customer customer : customers) {
                                System.out.println(customer.getName());
                            }
                        } else {
                            System.out.println("Нет клиентов, купивших данный товар");
                        }
                        break;
                    case 3:
                        System.out.println("Введите ID товара для удаления: ");
                        Long productToDelete = scanner.nextLong();
                        productDAO.deleteProduct(productToDelete);
                        System.out.println("Товар успешно удален!");
                        break;
                    case 4:
                        System.out.println("Введите ID клиента для удаления: ");
                        Long customerToDelete = scanner.nextLong();
                        customerDAO.deleteCustomer(customerToDelete);
                        System.out.println("Клиент успешно удален!");
                        break;
                    case 5:
                        System.out.println("Приложение закрывается!");
                        HibernateUtil.shutdown();
                        return;
                }
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}