package com.plog.demo.repository;

import com.plog.demo.model.ChatMessageTable;
import com.plog.demo.model.ChatRoomTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageTableRepository extends JpaRepository<ChatMessageTable, Integer> {

    List<ChatMessageTable> findAllByChatRoomTable(ChatRoomTable chatRoomTable);
}
