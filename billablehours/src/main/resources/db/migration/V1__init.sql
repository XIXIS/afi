CREATE TABLE `hibernate_sequence`
(
    `next_val` bigint(20) DEFAULT NULL
) DEFAULT CHARSET = utf8;

INSERT INTO `hibernate_sequence` VALUES (1);

CREATE TABLE `users`
(
    `id`                   bigint(20)   NOT NULL,
    `created_by`           bigint(20)   DEFAULT NULL,
    `creation_date`        datetime     DEFAULT NULL,
    `last_modified_by`     bigint(20)   DEFAULT NULL,
    `last_modified_date`   datetime     DEFAULT NULL,
    `email`                varchar(255) NOT NULL,
    `enabled`              bit(1)       NOT NULL,
    `first_name`           varchar(255) NOT NULL,
    `last_name`            varchar(255) NOT NULL,
    `password`             varchar(150) DEFAULT NULL,
    `phone`                varchar(255) NOT NULL,
    `user_type_id`         bigint(20)   DEFAULT NULL,
    `has_changed_password` bit(1)       NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
    UNIQUE KEY `UK_du5v5sr43g5bfnji4vb8hg5s3` (`phone`)
) DEFAULT CHARSET = utf8;


CREATE TABLE `user_types`
(
    `id`   bigint(20)   NOT NULL,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;