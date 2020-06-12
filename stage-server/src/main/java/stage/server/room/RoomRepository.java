package stage.server.room;

import java.util.List;
import stage.common.model.Room;
import stage.server.Repository;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
public interface RoomRepository extends Repository {
    List<Room> getRooms();

    Room getRoom(Integer id);

    Integer addRoom(Room room);

    void updateRoom(Integer id, Room room);

    void deleteRoom(Integer id);
}
