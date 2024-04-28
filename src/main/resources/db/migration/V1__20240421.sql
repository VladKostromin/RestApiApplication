CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     first_name VARCHAR(255) NOT NULL,
                                     last_name VARCHAR(255) NOT NULL,
                                     email VARCHAR(255) UNIQUE NOT NULL,
                                     username VARCHAR(255) UNIQUE NOT NULL,
                                     password VARCHAR(2048) NOT NULL,
                                     status ENUM('ACTIVE', 'BANNED', 'DELETED') NOT NULL,
                                     role ENUM('ADMIN', 'MODERATOR', 'USER') NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS files (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     file_name VARCHAR(255) NOT NULL,
                                     location VARCHAR(255) NOT NULL,
                                     status ENUM('AVALABLE', 'REMOVED') NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS events (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      user_id BIGINT,
                                      file_id BIGINT,
                                      status ENUM('UPLOADED', 'DOWNLOADED', 'DELETED') NOT NULL,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE events
    ADD CONSTRAINT fk_events_users
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE;

ALTER TABLE events
    ADD CONSTRAINT fk_events_files
        FOREIGN KEY (file_id) REFERENCES files(id)
            ON DELETE CASCADE;