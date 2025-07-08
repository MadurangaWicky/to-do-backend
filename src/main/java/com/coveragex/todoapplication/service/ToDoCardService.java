package com.coveragex.todoapplication.service;


import com.coveragex.todoapplication.dto.request.ToDoCardCreateRequestDTO;
import com.coveragex.todoapplication.dto.response.ToDoCardResponseDTO;
import com.coveragex.todoapplication.entity.ToDoCard;
import com.coveragex.todoapplication.entity.User;
import com.coveragex.todoapplication.exception.CustomException;
import com.coveragex.todoapplication.repository.ToDoCardRepository;
import com.coveragex.todoapplication.repository.UserRepository;
import com.coveragex.todoapplication.utility.errorcode.CommonErrorCodes;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoCardService {
    private final UserRepository userRepository;
    private final ToDoCardRepository toDoCardRepository;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    public ToDoCardService(UserRepository userRepository, ToDoCardRepository toDoCardRepository) {
        this.userRepository = userRepository;
        this.toDoCardRepository = toDoCardRepository;
    }

    public ToDoCardResponseDTO createToDoCard(ToDoCardCreateRequestDTO requestDTO, User user) {
        try {
            logger.info("Create ToDoCard for the user: {}", requestDTO.getTitle());
            ToDoCard toDoCard = new ToDoCard();
            toDoCard.setTitle(requestDTO.getTitle());
            toDoCard.setDescription(requestDTO.getDescription());
            toDoCard.setUser(user);
            toDoCardRepository.save(toDoCard);
            ToDoCardResponseDTO response = new ToDoCardResponseDTO();
            response.setId(toDoCard.getId());
            response.setTitle(toDoCard.getTitle());
            response.setDescription(toDoCard.getDescription());
            response.setDone(toDoCard.isDone());
            response.setDeleted(toDoCard.isDeleted());
            response.setCreatedAt(toDoCard.getCreatedAt());
            response.setDoneAt(toDoCard.getDoneAt());
            response.setDeletedAt(toDoCard.getDeletedAt());
            response.setUserId(user.getId());
            logger.info("Successfully created ToDoCard for the user: {}", requestDTO.getTitle());
            return response;
        }
        catch (CustomException e) {
            logger.error("Failed to create ToDoCard for the user: {}", requestDTO.getTitle());
            throw e;
        }
        catch (Exception e){
            logger.error("Failed to create ToDoCard for the user: {}", requestDTO.getTitle());
            throw new CustomException(CommonErrorCodes.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

    }


    public ToDoCardResponseDTO updateToDoCard(Long cardId, ToDoCardCreateRequestDTO requestDTO, User user) {
        try {
            logger.info("Update request for ToDoCard id={} by user={}", cardId, user.getId());

            ToDoCard toDoCard = toDoCardRepository.findById(cardId)
                    .orElseThrow(() -> {
                        logger.warn("ToDoCard not found: id={}", cardId);
                        return new CustomException(CommonErrorCodes.NOT_FOUND, "Card not found");
                    });

            if (!toDoCard.getUser().getId().equals(user.getId())) {
                logger.warn("Unauthorized update attempt by user={}, cardUser={}", user.getId(), toDoCard.getUser().getId());
                throw new CustomException(CommonErrorCodes.UNAUTHORIZED, "Not allowed to update this card");
            }

            if (requestDTO.getTitle() != null && !requestDTO.getTitle().isBlank()) {
                toDoCard.setTitle(requestDTO.getTitle());
            }

            if (requestDTO.getDescription() != null && !requestDTO.getDescription().isBlank()) {
                toDoCard.setDescription(requestDTO.getDescription());
            }

            toDoCardRepository.save(toDoCard);

            ToDoCardResponseDTO response = new ToDoCardResponseDTO();
            response.setId(toDoCard.getId());
            response.setTitle(toDoCard.getTitle());
            response.setDescription(toDoCard.getDescription());
            response.setDone(toDoCard.isDone());
            response.setDeleted(toDoCard.isDeleted());
            response.setCreatedAt(toDoCard.getCreatedAt());
            response.setDoneAt(toDoCard.getDoneAt());
            response.setDeletedAt(toDoCard.getDeletedAt());
            response.setUserId(user.getId());

            logger.info("Successfully updated ToDoCard id={} for user={}", cardId, user.getId());

            return response;

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Internal error while updating ToDoCard id={}", cardId, e);
            throw new CustomException(CommonErrorCodes.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }


    public ToDoCardResponseDTO deleteToDoCard(Long cardId, User user) {
        try {
            logger.info("Request to delete ToDoCard id={} by user={}", cardId, user.getId());

            ToDoCard toDoCard = toDoCardRepository.findById(cardId)
                    .orElseThrow(() -> {
                        logger.warn("ToDoCard not found: id={}", cardId);
                        return new CustomException(CommonErrorCodes.NOT_FOUND, "Card not found");
                    });

            if (!toDoCard.getUser().getId().equals(user.getId())) {
                logger.warn("Unauthorized delete attempt by user={}, cardUser={}", user.getId(), toDoCard.getUser().getId());
                throw new CustomException(CommonErrorCodes.UNAUTHORIZED, "Not allowed to delete this card");
            }

            if (toDoCard.isDeleted()) {
                logger.warn("Card already deleted: id={}", cardId);
                throw new CustomException(CommonErrorCodes.BAD_REQUEST, "Card already deleted");
            }

            toDoCard.setDeleted(true);
            toDoCard.setDeletedAt(Instant.now());
            toDoCardRepository.save(toDoCard);

            ToDoCardResponseDTO response = new ToDoCardResponseDTO();
            response.setId(toDoCard.getId());
            response.setTitle(toDoCard.getTitle());
            response.setDescription(toDoCard.getDescription());
            response.setDeleted(toDoCard.isDeleted());
            response.setCreatedAt(toDoCard.getCreatedAt());
            response.setDoneAt(toDoCard.getDoneAt());
            response.setDeletedAt(toDoCard.getDeletedAt());
            response.setUserId(user.getId());

            logger.info("Successfully marked ToDoCard id={} as deleted", cardId);
            return response;

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Internal error while deleting ToDoCard id={}", cardId, e);
            throw new CustomException(CommonErrorCodes.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }


    public ToDoCardResponseDTO markCardAsDone(Long cardId, User user) {
        try {
            logger.info("Request to mark ToDoCard id={} as done by user={}", cardId, user.getId());

            ToDoCard card = toDoCardRepository.findById(cardId)
                    .orElseThrow(() -> new CustomException(CommonErrorCodes.NOT_FOUND, "Card not found"));

            if (!card.getUser().getId().equals(user.getId())) {
                throw new CustomException(CommonErrorCodes.UNAUTHORIZED, "Unauthorized access");
            }

            if(card.isDeleted()) {
                logger.warn("Card already deleted: id={}", cardId);
                throw new CustomException(CommonErrorCodes.BAD_REQUEST, "Card already deleted");
            }

            if (card.isDone()) {
                logger.warn("Card already marked as done: id={}", cardId);
                throw new CustomException(CommonErrorCodes.BAD_REQUEST, "Card already marked as done");
            }

            card.setDone(true);
            card.setDoneAt(Instant.now());
            toDoCardRepository.save(card);

            ToDoCardResponseDTO response = new ToDoCardResponseDTO();
            response.setId(card.getId());
            response.setTitle(card.getTitle());
            response.setDescription(card.getDescription());
            response.setDone(card.isDone());
            response.setDoneAt(card.getDoneAt());
            response.setCreatedAt(card.getCreatedAt());
            response.setUserId(user.getId());

            return response;

        } catch (CustomException e) {
            logger.error("Failed to mark ToDoCard as done", e);
            logger.error(e.toString());
            throw e;
        } catch (Exception e) {
            logger.error("Failed to mark ToDoCard as done", e);
            logger.error(e.toString());
            throw new CustomException(CommonErrorCodes.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }


    public List<ToDoCardResponseDTO> getTopActiveCardList(User user) {
        try{
            logger.info("Request to get top active card list for user={} and limit={}", user.getUsername(), user.getCardListLimit());
            int pageNumber = 0;
            int pageSize = user.getCardListLimit();
            Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<ToDoCard> toDoCards = toDoCardRepository.findByUserAndIsDeletedFalseAndIsDoneFalse(user, pageable);
            List<ToDoCardResponseDTO> response = new ArrayList<>();
            for (ToDoCard card : toDoCards) {
                ToDoCardResponseDTO cardResponse = new ToDoCardResponseDTO();
                cardResponse.setId(card.getId());
                cardResponse.setTitle(card.getTitle());
                cardResponse.setDescription(card.getDescription());
                cardResponse.setDone(card.isDone());
                cardResponse.setDoneAt(card.getDoneAt());
                cardResponse.setCreatedAt(card.getCreatedAt());
                cardResponse.setUserId(user.getId());
                response.add(cardResponse);
            }
            logger.info("Successfully got top active card list for user={}", user.getUsername());
            return response;
        }
        catch (CustomException e){
            logger.error(e.toString());
            logger.error("Failed to get top active card list for user={}", user.getUsername());
            throw e;
        }
        catch (Exception e){
            logger.error(e.toString());
            logger.error("Failed to get top active card list for user={}", user.getUsername());
            throw new CustomException(CommonErrorCodes.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

    }

    public List<ToDoCardResponseDTO> getTopDoneCardList(User user) {
        try{
            logger.info("Request to get top done card list for user={} and limit={}", user.getUsername(), user.getCardListLimit());
            int pageNumber = 0;
            int pageSize = user.getCardListLimit();
            Sort sort = Sort.by(Sort.Direction.DESC, "doneAt");
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<ToDoCard> toDoCards = toDoCardRepository.findByUserAndIsDeletedFalseAndIsDoneTrue(user, pageable);
            List<ToDoCardResponseDTO> response = new ArrayList<>();
            for (ToDoCard card : toDoCards) {
                ToDoCardResponseDTO cardResponse = new ToDoCardResponseDTO();
                cardResponse.setId(card.getId());
                cardResponse.setTitle(card.getTitle());
                cardResponse.setDescription(card.getDescription());
                cardResponse.setDone(card.isDone());
                cardResponse.setDoneAt(card.getDoneAt());
                cardResponse.setCreatedAt(card.getCreatedAt());
                cardResponse.setUserId(user.getId());
                response.add(cardResponse);
            }
            logger.info("Successfully got top done card list for user={}", user.getUsername());
            return response;
        }
        catch (CustomException e){
            logger.error(e.toString());
            logger.error("Failed to get top done card list for user={}", user.getUsername());
            throw e;
        }
        catch (Exception e){
            logger.error(e.toString());
            logger.error("Failed to get top done card list for user={}", user.getUsername());
            throw new CustomException(CommonErrorCodes.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

public List<ToDoCardResponseDTO> getAllActiveCardList(User user, int pageNumber, int pageSize) {
    try{
        logger.info("Request to get all active card list for user={} and pageNumber={} and pageSize={}", user.getUsername(), pageNumber, pageSize);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<ToDoCard> toDoCards = toDoCardRepository.findByUserAndIsDeletedFalseAndIsDoneFalse(user, pageable);
        List<ToDoCardResponseDTO> response = new ArrayList<>();
        for (ToDoCard card : toDoCards) {
            ToDoCardResponseDTO cardResponse = new ToDoCardResponseDTO();
            cardResponse.setId(card.getId());
            cardResponse.setTitle(card.getTitle());
            cardResponse.setDescription(card.getDescription());
            cardResponse.setDone(card.isDone());
            cardResponse.setDoneAt(card.getDoneAt());
            cardResponse.setCreatedAt(card.getCreatedAt());
            cardResponse.setUserId(user.getId());
            response.add(cardResponse);
        }
        logger.info("Successfully got all active card list for user={}", user.getUsername());
        return response;
    }
    catch (CustomException e){
        logger.error(e.toString());
        logger.error("Failed to get all active card list for user={}", user.getUsername());
        throw e;
    }
    catch (Exception e){
        logger.error(e.toString());
        logger.error("Failed to get all active card list for user={}", user.getUsername());
        throw new CustomException(CommonErrorCodes.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
}

public List<ToDoCardResponseDTO> getAllDoneCardList(User user, int pageNumber, int pageSize) {
    try{
        logger.info("Request to get all done card list for user={} and pageNumber={} and pageSize={}", user.getUsername(), pageNumber, pageSize);
        Sort sort = Sort.by(Sort.Direction.DESC, "doneAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<ToDoCard> toDoCards = toDoCardRepository.findByUserAndIsDeletedFalseAndIsDoneTrue(user, pageable);
        List<ToDoCardResponseDTO> response = new ArrayList<>();
        for (ToDoCard card : toDoCards) {
            ToDoCardResponseDTO cardResponse = new ToDoCardResponseDTO();
            cardResponse.setId(card.getId());
            cardResponse.setTitle(card.getTitle());
            cardResponse.setDescription(card.getDescription());
            cardResponse.setDone(card.isDone());
            cardResponse.setDoneAt(card.getDoneAt());
            cardResponse.setCreatedAt(card.getCreatedAt());
            cardResponse.setUserId(user.getId());
            response.add(cardResponse);
        }
        logger.info("Successfully got all done card list for user={}", user.getUsername());
        return response;
    }
    catch (CustomException e){
        logger.error(e.toString());
        logger.error("Failed to get all done card list for user={}", user.getUsername());
        throw e;
    }
    catch (Exception e){
        logger.error(e.toString());
        logger.error("Failed to get all done card list for user={}", user.getUsername());
        throw new CustomException(CommonErrorCodes.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
}

public List<ToDoCardResponseDTO> getAllDeletedCardList(User user, int pageNumber, int pageSize) {
    try{
        logger.info("Request to get all deleted card list for user={} and pageNumber={} and pageSize={}", user.getUsername(), pageNumber, pageSize);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<ToDoCard> toDoCards = toDoCardRepository.findByUserAndIsDeletedTrue(user, pageable);
        List<ToDoCardResponseDTO> response = new ArrayList<>();
        for (ToDoCard card : toDoCards) {
            ToDoCardResponseDTO cardResponse = new ToDoCardResponseDTO();
            cardResponse.setId(card.getId());
            cardResponse.setTitle(card.getTitle());
            cardResponse.setDescription(card.getDescription());
            cardResponse.setDone(card.isDone());
            cardResponse.setDoneAt(card.getDoneAt());
            cardResponse.setCreatedAt(card.getCreatedAt());
            cardResponse.setUserId(user.getId());
            response.add(cardResponse);
        }
        logger.info("Successfully got all deleted card list for user={}", user.getUsername());
        return response;
    }
    catch (CustomException e){
        logger.error(e.toString());
        logger.error("Failed to get all deleted card list for user={}", user.getUsername());
        throw e;
    }
    catch (Exception e){
        logger.error(e.toString());
        logger.error("Failed to get all deleted card list for user={}", user.getUsername());
        throw new CustomException(CommonErrorCodes.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }
}


}
