package com.bernardo.moneywise.seed;

import com.bernardo.moneywise.enums.Role;
import com.bernardo.moneywise.enums.TransactionType;
import com.bernardo.moneywise.model.Category;
import com.bernardo.moneywise.model.Transaction;
import com.bernardo.moneywise.model.User;
import com.bernardo.moneywise.repository.CategoryRepository;
import com.bernardo.moneywise.repository.TransactionRepository;
import com.bernardo.moneywise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedCategoriesAndTransactions();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
    }

    private void seedCategoriesAndTransactions() {
        if (categoryRepository.count() > 0) return;

        Category food = categoryRepository.save(new Category(null, "Food"));
        Category transport = categoryRepository.save(new Category(null, "Transport"));
        Category salary = categoryRepository.save(new Category(null, "Salary"));
        Category leisure = categoryRepository.save(new Category(null, "Leisure"));
        Category health = categoryRepository.save(new Category(null, "Health"));

        List<Transaction> transactions = List.of(
                buildTransaction("Grocery shopping", "89.90", TransactionType.EXPENSE, LocalDate.now().minusDays(1), food),
                buildTransaction("Bus pass", "120.00", TransactionType.EXPENSE, LocalDate.now().minusDays(2), transport),
                buildTransaction("Monthly salary", "3500.00", TransactionType.INCOME, LocalDate.now().minusDays(3), salary),
                buildTransaction("Movie night", "45.00", TransactionType.EXPENSE, LocalDate.now().minusDays(4), leisure),
                buildTransaction("Pharmacy", "60.50", TransactionType.EXPENSE, LocalDate.now().minusDays(5), health),
                buildTransaction("Freelance project", "800.00", TransactionType.INCOME, LocalDate.now().minusDays(6), salary),
                buildTransaction("Restaurant dinner", "150.00", TransactionType.EXPENSE, LocalDate.now().minusDays(7), food),
                buildTransaction("Uber ride", "32.00", TransactionType.EXPENSE, LocalDate.now().minusDays(8), transport),
                buildTransaction("Gym membership", "99.90", TransactionType.EXPENSE, LocalDate.now().minusDays(9), health),
                buildTransaction("Concert ticket", "200.00", TransactionType.EXPENSE, LocalDate.now().minusDays(10), leisure),
                buildTransaction("Bonus payment", "500.00", TransactionType.INCOME, LocalDate.now().minusDays(11), salary),
                buildTransaction("Supermarket", "210.30", TransactionType.EXPENSE, LocalDate.now().minusDays(12), food)
        );

        transactionRepository.saveAll(transactions);
    }

    private Transaction buildTransaction(String description, String amount, TransactionType type, LocalDate date, Category category) {
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setType(type);
        transaction.setTransactionDate(date);
        transaction.setCategory(category);
        return transaction;
    }
}
