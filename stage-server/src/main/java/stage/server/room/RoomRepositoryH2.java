package stage.server.room;

import java.sql.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.log4j.Log4j2;
import stage.common.model.Room;
import stage.server.database.SqlConnection;

import static stage.common.FileUtil.readFile;

@Log4j2
@Repository
class RoomRepositoryH2 implements RoomRepository {
    private final String select;
    private final String selectById;
    private final String insert;
    private final String update;
    private final String delete;
    private final String sequence;

    private final SqlConnection connection;

    @Autowired
    RoomRepositoryH2(SqlConnection connection) {
        this.connection = connection;
        delete = readFile("sql/room/delete.sql");
        insert = readFile("sql/room/insert.sql");
        select = readFile("sql/room/select.sql");
        selectById = readFile("sql/room/select_by_id.sql");
        update = readFile("sql/room/update.sql");
        sequence = readFile("sql/room/sequence.sql");
    }

    @Override
    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        try (ResultSet resultSet = connection.result(select)) {
            while (resultSet.next()) {
                rooms.add(buildRoom(resultSet));
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return rooms;
    }

    @Override
    public Room getRoom(Integer id) {
        Room room = null;
        try (ResultSet resultSet = connection.result(selectById, id)) {
            if (resultSet.next()) {
                room = buildRoom(resultSet);
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return room;
    }

    @Override
    public Integer addRoom(Room room) {
        Integer id = -1;
        try (ResultSet res = connection.result(sequence)) {
            if (res.next()) {
                connection.update(insert, //
                    id, //
                    room.getName(), //
                    room.getSuitability());
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public void updateRoom(Integer id, Room room) {
        try {
            connection.update(update, //
                room.getSuitability(), //
                room.getName(), //
                id);
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public void deleteRoom(Integer id) {
        try {
            connection.update(delete, id);
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
}
