package ex.springsecurity_1805.Models;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        Long[] newId = {0L}; // Используем массив для изменения значения в лямбде

        session.doWork(connection -> {
            // Выполняем запрос для получения максимального id из таблицы
            String query = "SELECT MAX(id) FROM audio"; // Замените my_table на вашу таблицу

            try (PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long maxId = rs.getLong(1);
                    if (rs.wasNull()) {
                        // Если таблица пустая, начинаем с 1
                        newId[0] = 152L;
                    } else {
                        // Увеличиваем максимальный id на 1 для нового идентификатора
                        newId[0] = maxId + 50L;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка при генерации id", e);
            }
        });

        return newId[0];
    }
}

