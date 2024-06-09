package com.plog.demo.repository;

import com.plog.demo.model.ChatRoomTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomTableRepository extends JpaRepository<ChatRoomTable, Integer> {

    Optional<ChatRoomTable> findByUserIdAndProviderId(String userId, int providerId);

    List<ChatRoomTable> findAllByUserIdOrderByChatRoomIdDesc(String userId);

    List<ChatRoomTable> findAllByProviderIdOrderByChatRoomIdDesc(int providerId);
}
