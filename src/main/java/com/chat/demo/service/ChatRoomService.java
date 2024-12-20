package com.chat.demo.service;

import com.chat.demo.entity.ChatRoom;
import com.chat.demo.entity.DTO.ChatRoomDto;
import com.chat.demo.repository.ChatRoomRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    // 获取所有聊天室
    public List<ChatRoomDto> getAllChatRooms() {
        return chatRoomRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 根据 ID 获取聊天室
    public ChatRoomDto getChatRoomById(Long id) {
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(id);
        return chatRoomOptional.map(this::convertToDto).orElse(null);
    }

    // 创建聊天室
    public ChatRoomDto createChatRoom(ChatRoomDto chatRoomDto) {
        ChatRoom chatRoom = convertToEntity(chatRoomDto);
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoom.setUpdatedAt(LocalDateTime.now());
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return convertToDto(savedChatRoom);
    }

    // 删除聊天室
    public boolean deleteChatRoom(Long id) {
        if (chatRoomRepository.existsById(id)) {
            chatRoomRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 更新聊天室
    public ChatRoomDto updateChatRoom(Long id, ChatRoomDto chatRoomDto) {
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(id);
        if (chatRoomOptional.isEmpty()) {
            return null;
        }
        ChatRoom chatRoom = chatRoomOptional.get();
        BeanUtils.copyProperties(chatRoomDto, chatRoom, "id", "createdAt");
        chatRoom.setUpdatedAt(LocalDateTime.now());
        ChatRoom updatedChatRoom = chatRoomRepository.save(chatRoom);
        return convertToDto(updatedChatRoom);
    }

    // 将实体转换为 DTO
    private ChatRoomDto convertToDto(ChatRoom chatRoom) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        BeanUtils.copyProperties(chatRoom, chatRoomDto);
        return chatRoomDto;
    }

    // 将 DTO 转换为实体
    private ChatRoom convertToEntity(ChatRoomDto chatRoomDto) {
        ChatRoom chatRoom = new ChatRoom();
        BeanUtils.copyProperties(chatRoomDto, chatRoom);
        return chatRoom;
    }
}
