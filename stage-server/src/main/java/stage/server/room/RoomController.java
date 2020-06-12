package stage.server.room;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import stage.common.model.Room;

import java.util.List;


@RestController
@RequestMapping("/v1/room")
public class RoomController {
    private final RoomService service;

    @Autowired
    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping
    public List<Room> get() {
        return service.getRooms();
    }

    @GetMapping("/{id}")
    public Room get(@PathVariable("id") Integer id) {
        return service.getRoom(id);
    }

    @PutMapping
    public RoomPutResponse put(@RequestBody Room room) {
        return new RoomPutResponse(service.addRoom(room));
    }

    @PatchMapping("/{id}")
    public void patch(@PathVariable("id") Integer id,
                      @RequestBody Room room) {
        service.updateRoom(id, room);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        service.delteRoom(id);
    }
}
