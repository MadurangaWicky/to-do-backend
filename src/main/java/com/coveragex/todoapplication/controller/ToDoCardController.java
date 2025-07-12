package com.coveragex.todoapplication.controller;


import com.coveragex.todoapplication.dto.request.ToDoCardCreateRequestDTO;
import com.coveragex.todoapplication.dto.response.StandardResponse;
import com.coveragex.todoapplication.dto.response.ToDoCardResponseDTO;
import com.coveragex.todoapplication.entity.User;
import com.coveragex.todoapplication.service.ToDoCardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        originPatterns = "*",
        allowCredentials = "true"
)

@RestController
@RequestMapping(path = "api/todo-card")
public class ToDoCardController {

    private final ToDoCardService toDoCardService;

    public ToDoCardController(ToDoCardService toDoCardService) {
        this.toDoCardService = toDoCardService;
    }

    @PostMapping("/create")
    public ResponseEntity<StandardResponse> createToDoCard(@Valid @RequestBody ToDoCardCreateRequestDTO createRequestDTO, @AuthenticationPrincipal User user) {
        StandardResponse response = new StandardResponse(true, toDoCardService.createToDoCard(createRequestDTO, user));
        return ResponseEntity.ok().body(response);
    }


    @PutMapping("/update/{cardId}")
    public ResponseEntity<StandardResponse> updateCard(
            @PathVariable Long cardId,
            @RequestBody @Valid ToDoCardCreateRequestDTO request,
            @AuthenticationPrincipal User user
    ) {
        StandardResponse response = new StandardResponse(true, toDoCardService.updateToDoCard(cardId, request, user));
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<StandardResponse> deleteCard(
            @PathVariable Long cardId,
            @AuthenticationPrincipal User user
    ) {
        ToDoCardResponseDTO result = toDoCardService.deleteToDoCard(cardId, user);
        StandardResponse response = new StandardResponse(true, result);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/mark-done/{cardId}")
    public ResponseEntity<StandardResponse> markCardAsDone(
            @PathVariable Long cardId,
            @AuthenticationPrincipal User user
    ) {
        ToDoCardResponseDTO updatedCard = toDoCardService.markCardAsDone(cardId, user);
        StandardResponse response = new StandardResponse(true, updatedCard);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-active-card-list")
    public ResponseEntity<List<ToDoCardResponseDTO>> getTopCardList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(toDoCardService.getTopActiveCardList(user));
    }

    @GetMapping("/top-done-card-list")
    public ResponseEntity<List<ToDoCardResponseDTO>> getTopDoneCardList(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(toDoCardService.getTopDoneCardList(user));
    }

    @GetMapping("/all-active-card-list/{page}/{size}")
    public ResponseEntity<List<ToDoCardResponseDTO>> getAllActiveCardList(@AuthenticationPrincipal User user, @PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok().body(toDoCardService.getAllActiveCardList(user, page, size));
    }

    @GetMapping("/all-done-card-list/{page}/{size}")
    public ResponseEntity<List<ToDoCardResponseDTO>> getAllDoneCardList(@AuthenticationPrincipal User user, @PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok().body(toDoCardService.getAllDoneCardList(user, page, size));
    }

    @GetMapping("/all-deleted-card-list/{page}/{size}")
    public ResponseEntity<List<ToDoCardResponseDTO>> getAllDeletedCardList(@AuthenticationPrincipal User user, @PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok().body(toDoCardService.getAllDeletedCardList(user, page, size));
    }

}
