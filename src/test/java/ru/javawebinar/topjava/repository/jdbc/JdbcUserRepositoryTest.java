package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles("jdbc")
public class JdbcUserRepositoryTest extends UserServiceTest {
}