SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`    int NOT NULL AUTO_INCREMENT,
    `name`  varchar(255) DEFAULT NULL,
    `email` varchar(255) DEFAULT NULL,
    `age`   int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `index_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET
FOREIGN_KEY_CHECKS = 1;
