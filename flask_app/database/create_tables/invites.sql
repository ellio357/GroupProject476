CREATE TABLE IF NOT EXISTS `invites` (
  `invite_id` INT AUTO_INCREMENT PRIMARY KEY,
  `event_id` INT NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `user_id` INT,
  FOREIGN KEY (`event_id`) REFERENCES events(`event_id`),
  FOREIGN KEY (`user_id`) REFERENCES users(`user_id`)
);
