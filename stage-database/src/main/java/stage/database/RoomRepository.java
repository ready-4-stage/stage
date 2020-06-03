package stage.database;

import stage.common.model.Room;

import java.util.List;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne, Tobias Tappert
 * @since 1.0.0
 */
public interface RoomRepository {
    List<Room> getRooms();

    Room getRoom(Integer id);

    Integer addRoom(Room room);

    void updateRoom(Integer id, Room room);

    void deleteRoom(Integer id);
}
