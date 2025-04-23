CREATE TABLE IF NOT EXISTS `participants` (
  `participant_id` INT AUTO_INCREMENT PRIMARY KEY,
  `event_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  FOREIGN KEY (`event_id`) REFERENCES events(`event_id`),
  FOREIGN KEY (`user_id`) REFERENCES users(`user_id`)
);
