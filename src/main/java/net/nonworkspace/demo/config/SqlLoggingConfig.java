package net.nonworkspace.demo.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import java.util.Locale;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqlLoggingConfig implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
        String prepared, String sql, String url) {
        return String.format("[%s] | %d ms | %s", category, elapsed, formatSql(category, sql));
    }

    private String formatSql(String category, String sql) {
        String formattedSql = sql;
        if (sql != null && !sql.isEmpty() && sql.trim().length() > 0 && Category.STATEMENT.getName()
            .equalsIgnoreCase(category)) {
            String trimmedSql = sql.trim().toLowerCase(Locale.ROOT);
            if (trimmedSql.startsWith("create ") || trimmedSql.startsWith("drop ")
                || trimmedSql.startsWith("alter ") || trimmedSql.startsWith("alter ")
                || trimmedSql.startsWith("comment ")) {
                formattedSql = FormatStyle.DDL.getFormatter().format(sql.trim());
            } else {
                formattedSql = FormatStyle.BASIC.getFormatter().format(sql.trim());
            }
        }
        return formattedSql;
    }

    @PostConstruct
    public void init() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
    }
}
