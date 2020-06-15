package stage.server.room;

import java.sql.*;
import java.util.*;
import javax.annotation.PostConstruct;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import stage.common.model.Room;
import stage.server.database.SqlConnection;

import static stage.common.FileUtil.readFile;

@Log4j2
@Repository
public class RoomRepositoryH2 implements RoomRepository {
    private final String initialFill;
    private final String selectRooms;
    private final String selectRoomById;
    private final String roomInsert;
    private final String roomUpdate;
    private final String roomDelete;
    private final String generateId;

    private final SqlConnection connection;

    @Autowired
    public RoomRepositoryH2(SqlConnection connection) {
        this.connection = connection;

        initialFill = readFile("sql/initial_fill.sql");
        roomDelete = readFile("sql/room/room_delete.sql");
        roomInsert = readFile("sql/room/room_insert.sql");
        selectRooms = readFile("sql/room/room_select_all.sql");
        selectRoomById = readFile("sql/room/room_select_by_id.sql");
        roomUpdate = readFile("sql/room/room_update.sql");
        generateId = readFile("sql/room/room_generate_id.sql");
    }

    @PostConstruct
    public void initialize() {
        String createRoomTableSql = readFile("sql/room/room_table_create.sql");
        try {
            connection.update(createRoomTableSql);
            connection.update(initialFill);

            connection.commit();
        } catch (SQLException e) {
            log.error("SQL error: {}", e.getMessage());
        }
    }

    @Override
    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        try (ResultSet resultSet = connection.result(selectRooms)) {
            while (resultSet.next()) {
                rooms.add(buildRoom(resultSet));
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }

        return rooms;
    }

    @Override
    public Room getRoom(Integer id) {
        Room room = null;
        try (ResultSet resultSet = connection.result(selectRoomById, id)) {
            if (resultSet.next()) {
                room = buildRoom(resultSet);
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return room;
    }

    @Override
    public Integer addRoom(Room room) {
        Integer id = -1;
        try {
            id = generateId();
            connection.update(roomInsert, id, room.getName(),
                room.getSuitability());

            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public void updateRoom(Integer id, Room room) {
        try {
            connection.update(roomUpdate, room.getSuitability(), room.getName(),
                id);
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public void deleteRoom(Integer id) {
        try {
            connection.update(roomDelete, id);
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    private Room buildRoom(ResultSet resultSet) throws SQLException {
        Room room = new Room();
        room.setId(resultSet.getInt("ID"));
        room.setName(resultSet.getString("NAME"));
        room.setSuitability(resultSet.getString("SUITABILITY"));
        return room;
    }

    private Integer generateId() throws SQLException {
        Integer id = 0;
        try (ResultSet rs = connection.result(generateId)) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        }
        connection.commit();
        return id + 1;
    }
}
