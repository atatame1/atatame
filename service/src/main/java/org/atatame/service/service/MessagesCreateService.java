package org.atatame.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Service;

@Service
public class MessagesCreateService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean createUserMessagesTable(Long userId){
        try{
            String tableName="user_messages_"+userId;
            String sql = """
            CREATE TABLE IF NOT EXISTS %s (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                friend_id BIGINT NOT NULL COMMENT '好友ID',
                form varchar(50) NULL COMMENT '消息形式(如:text,image等)',
                self_flag TINYINT(1) NOT NULL COMMENT '是否为自己发送',
                content TEXT NOT NULL,
                deliver_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                is_deleted TINYINT DEFAULT 0 NOT NULL,
                INDEX idx_friend_time (friend_id, deliver_time DESC),
                INDEX idx_deliver_time (deliver_time DESC)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """.formatted(tableName);
            jdbcTemplate.execute(sql);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean createGroupMessagesTable(Long groupId){
        try{
            String tableName="group_messages_"+groupId;
            String sql = """
            CREATE TABLE IF NOT EXISTS %s (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                sender_id BIGINT NOT NULL COMMENT '发送者ID',
                content TEXT NOT NULL,
                form varchar(50) NULL COMMENT '消息形式(如:text,image等)',
                deliver_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                is_deleted TINYINT DEFAULT 0,
                INDEX idx_sender_time (sender_id, deliver_time DESC),
                INDEX idx_deliver_time (deliver_time DESC)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """.formatted(tableName);
            jdbcTemplate.execute(sql);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
