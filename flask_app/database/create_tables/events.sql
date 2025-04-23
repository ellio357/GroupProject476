CREATE TABLE IF NOT EXISTS `events` (
  `event_id`    INT AUTO_INCREMENT PRIMARY KEY,
  `name`        VARCHAR(100) NOT NULL,
  `start_date`  DATE NOT NULL,
  `end_date`    DATE NOT NULL,
  `start_time`  TIME NOT NULL,
  `end_time`    TIME NOT NULL,
  `creator_id`  INT,
  FOREIGN KEY (`creator_id`) REFERENCES users(`user_id`)
);
