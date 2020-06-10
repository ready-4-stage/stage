package stage.server.room;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stage.common.model.Room;

import java.util.List;

@Log4j2
@Service
public class RoomService {
    private final RoomRepository repository;

    @Autowired
    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public List<Room> getRooms() {
        return (repository.getRooms());
    }

    public Room getRoom(Integer id) {
        Room room = repository.getRoom(id);
        if (room == null) {
            throw new RoomNotFoundException();
        }
        return room;
    }

    public Integer addRoom(Room room) {
        room.setId(null);
        return repository.addRoom(room);
    }

    public void updateRoom(Integer id, Room room) {
        Room oldRoom = getRoom(id);
        room.setId(oldRoom.getId());
        updateRoomById(oldRoom, room);
    }

    public void delteRoom(Integer id) {
        Room room = getRoom(id);
        if (room == null) {
            throw new RoomNotFoundException();
        }
        delteRoom(id);
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
