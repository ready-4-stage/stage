package stage.server.room;

import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stage.common.model.Room;
import stage.server.authentication.aop.*;

@Log4j2
@Service
public class RoomService {
    private final RoomRepository repository;

    @Autowired
    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    @RequireAdminOrTeacher
    public List<Room> getRooms() {
        return repository.getRooms();
    }

    @RequireAdminOrTeacher
    public Room getRoom(Integer id) {
        Room room = repository.getRoom(id);
        if (room == null) {
            throw new RoomNotFoundException();
        }
        return room;
    }

    @RequireAdmin
    public Integer addRoom(Room room) {
        room.setId(null);
        return repository.addRoom(room);
    }

    @RequireAdmin
    public void updateRoom(Integer id, Room room) {
        Room oldRoom = getRoom(id);
        room.setId(oldRoom.getId());
        updateRoomById(oldRoom, room);
    }

    @RequireAdmin
    public void deleteRoom(Integer id) {
        Room room = getRoom(id);
        if (room == null) {
            throw new RoomNotFoundException();
        }
        repository.deleteRoom(id);
    }

    private void updateRoomById(Room oldRoom, Room newRoom) {
        Integer id = newRoom.getId();
        if (newRoom.getSuitability() == null) {
            newRoom.setSuitability(oldRoom.getSuitability());
        }
        if (newRoom.getName() == null) {
            newRoom.setName(oldRoom.getName());
        }
        repository.updateRoom(id, newRoom);
    }
}
