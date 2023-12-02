package com.alpha53.virtualteacher.repositories;

import com.alpha53.virtualteacher.exceptions.EntityNotFoundException;
import com.alpha53.virtualteacher.models.ConfirmationToken;
import com.alpha53.virtualteacher.repositories.contracts.ConfirmationTokenDao;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Repository
@Transactional
public class ConfirmationTokenDaoImpl extends NamedParameterJdbcDaoSupport implements ConfirmationTokenDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ConfirmationTokenDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DataSource dataSource) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.setDataSource(dataSource);
    }

    @Override
    public ConfirmationToken findByToken(String token) {
        String query = "SELECT id, token, created_at as createdAt, expires_at as expiresAt, confirmed_at as confirmedAt, " +
                       "user_email as userEmail  " +
                       "FROM tokens " +
                       "WHERE tokens.token like :token;";

        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("token", token);

        try {
            return namedParameterJdbcTemplate.queryForObject(query, in, new BeanPropertyRowMapper<>(ConfirmationToken.class));
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityNotFoundException(String.format("No token %s found.", token));
        }
    }

    @Override
    public void save(ConfirmationToken token) {
        String sql = "INSERT INTO tokens (token, created_at, expires_at, confirmed_at, user_email) " +
                     "VALUES (:token, :createdAt , :expiresAt, :confirmedAt, :userEmail)";

        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("token", token.getToken());
        in.addValue("createdAt", token.getCreatedAt());
        in.addValue("expiresAt", token.getExpiresAt());
        in.addValue("confirmedAt", token.getConfirmedAt());
        in.addValue("userEmail", token.getUserEmail());

        namedParameterJdbcTemplate.update(sql, in);
    }

    @Override
    public void updateConfirmedAt(String token, LocalDateTime time) {
        String sql = "UPDATE tokens SET confirmed_at = :confirmedAt " +
                     "WHERE tokens.token like :token ";

        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("confirmedAt", time);
        in.addValue("token", token);

        namedParameterJdbcTemplate.update(sql, in);
    }

    @Override
    public void delete(String token) {
        String sql = "DELETE FROM tokens " +
                     "WHERE token like :token";
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("token", token);
        namedParameterJdbcTemplate.update(sql,in);
    }
}
