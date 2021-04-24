package chess.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class RoomDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public RoomDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int insertRoom() {
        String query = "INSERT INTO room(current_turn) values(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setString(1, "white");
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public String selectTurnByRoomId(int roomId) {
        String query = "SELECT current_turn from room where room_id = :room_id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("room_id", roomId);
        return namedParameterJdbcTemplate.queryForObject(query, namedParameters, String.class);
    }

    public void changeTurn(String nextTurn, String currentTurn, int roomId) {
        String query = "UPDATE room SET current_turn=? WHERE current_turn= ? AND room_id = ?";
        jdbcTemplate.update(query, nextTurn, currentTurn, roomId);
    }
}
