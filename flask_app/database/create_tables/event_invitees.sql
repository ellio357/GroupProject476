CREATE TABLE IF NOT EXISTS `event_invitees` (
  `event_id` INT(11) NOT NULL,
  `user_id`  INT(11) NOT NULL,
  PRIMARY KEY (`event_id`, `user_id`),
  FOREIGN KEY (`event_id`) REFERENCES events(`event_id`) ON DELETE CASCADE,
  FOREIGN KEY (`user_id`)  REFERENCES users(`user_id`)  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Mapping of users to events they are invited to';
