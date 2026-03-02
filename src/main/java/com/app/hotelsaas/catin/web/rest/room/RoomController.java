package com.app.hotelsaas.catin.web.rest.room;

import com.app.hotelsaas.catin.application.usecase.room.CreateRoomUseCase;
import com.app.hotelsaas.catin.application.usecase.room.GetRoomUseCase;
import com.app.hotelsaas.catin.application.usecase.room.RoomWithOccupation;
import com.app.hotelsaas.catin.application.usecase.room.UpdateRoomUseCase;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.web.rest.room.mapper.RoomRestMapper;
import com.app.hotelsaas.catin.web.rest.room.request.CreateRoomRequest;
import com.app.hotelsaas.catin.web.rest.room.request.UpdateRoomRequest;
import com.app.hotelsaas.catin.web.rest.room.response.RoomDetailResponse;
import com.app.hotelsaas.catin.web.rest.room.response.RoomListItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomUseCase createRoomUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;
    private final GetRoomUseCase getRoomUseCase;
    private final RoomRestMapper mapper;

    @PostMapping
    public ResponseEntity<RoomDetailResponse> create(
            @PathVariable UUID tenantId,
            @Valid @RequestBody CreateRoomRequest request
    ){
        Room saved = createRoomUseCase.execute(tenantId, request);
        return ResponseEntity.created(
                URI.create("/tenants/"+tenantId+"/rooms/"+saved.getId())
        ).body(mapper.toRoomDetailResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<RoomListItemResponse>> findAll(
            @PathVariable UUID tenantId
    ) {
        List<RoomWithOccupation> rooms = getRoomUseCase.findAllByTenantId(tenantId);
        return ResponseEntity.ok(mapper.toListItemResponses(rooms));
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDetailResponse> update(
            @PathVariable UUID roomId,
            @PathVariable UUID tenantId,
            @Valid @RequestBody UpdateRoomRequest request
    ){
        Room updated = updateRoomUseCase.execute(roomId, tenantId, request);
        return ResponseEntity.ok(mapper.toRoomDetailResponse(updated));
    }
}
