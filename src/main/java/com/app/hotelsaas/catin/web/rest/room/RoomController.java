package com.app.hotelsaas.catin.web.rest.room;

import com.app.hotelsaas.catin.application.usecase.room.CreateRoomUseCase;
import com.app.hotelsaas.catin.application.usecase.room.UpdateRoomUseCase;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.web.rest.room.mapper.RoomRestMapper;
import com.app.hotelsaas.catin.web.rest.room.request.CreateRoomRequest;
import com.app.hotelsaas.catin.web.rest.room.request.UpdateRoomRequest;
import com.app.hotelsaas.catin.web.rest.room.response.RoomDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomUseCase createRoomUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;
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
