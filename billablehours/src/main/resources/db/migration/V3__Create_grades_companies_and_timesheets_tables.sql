CREATE TABLE `grades`
(
    `id`                   bigint(20)   NOT NULL,
    `created_by`           bigint(20)   DEFAULT NULL,
    `creation_date`        datetime     DEFAULT NULL,
    `last_modified_by`     bigint(20)   DEFAULT NULL,
    `last_modified_date`   datetime     DEFAULT NULL,
    `name`                  varchar(255) NOT NULL UNIQUE,
    `alias`                 varchar(255) NOT NULL,
    `rate`                  double NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_6098hjkduy723ndk12390pe5s3` (`alias`)
) DEFAULT CHARSET = utf8;

ALTER TABLE `users` ADD CONSTRAINT `FK1234567890poiuytrewasdfg` FOREIGN KEY (`grade_id`) REFERENCES `grades` (`id`);

CREATE TABLE `companies`
(
    `id`                   bigint(20)   NOT NULL,
    `created_by`           bigint(20)   DEFAULT NULL,
    `creation_date`        datetime     DEFAULT NULL,
    `last_modified_by`     bigint(20)   DEFAULT NULL,
    `last_modified_date`   datetime     DEFAULT NULL,
    `email`                varchar(255) NOT NULL UNIQUE,
    `name`                 varchar(255) NOT NULL,
    `address`              varchar(255) NOT NULL,
    `phone`                varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_6dotkFpbC5jb20iLCJpYXQiOb7` (`email`),
    UNIQUE KEY `UK_d4cCI6MTU2MzIyOTA1Nn0pe5s3` (`phone`)
) DEFAULT CHARSET = utf8;


CREATE TABLE `timesheets`
(
    `id`                   bigint(20)   NOT NULL,
    `created_by`           bigint(20)   DEFAULT NULL,
    `creation_date`        datetime     DEFAULT NULL,
    `last_modified_by`     bigint(20)   DEFAULT NULL,
    `last_modified_date`   datetime     DEFAULT NULL,
    `user_id`              bigint(20)   NOT NULL,
    `company_id`           bigint(20)  NOT NULL,
    `date`                 date     NOT NULL,
    `start_time`           time NOT NULL,
    `end_time`             time NOT NULL,
    `invoiced`             bit(1) NOT NULL,
    `rate`                 double NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK32UzUxMiJ9JzdWIiOiRob255L3` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK325a23450poi456ewasdfg1993` FOREIGN KEY (`company_id`) REFERENCES `companies` (`id`)
) DEFAULT CHARSET = utf8;