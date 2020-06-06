ALTER TABLE `timesheets`
    ADD COLUMN `cost` double NOT NULL;
ALTER TABLE `timesheets`
    ADD COLUMN `hours` varchar(255) NOT NULL;

CREATE TABLE `invoices`
(
    `id`                 bigint(20) NOT NULL,
    `created_by`         bigint(20) DEFAULT NULL,
    `creation_date`      datetime   DEFAULT NULL,
    `last_modified_by`   bigint(20) DEFAULT NULL,
    `last_modified_date` datetime   DEFAULT NULL,
    `total_cost`         double     NOT NULL,
    `client_id`          bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK325a23popo450iuywv1993` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`)

) DEFAULT CHARSET = utf8;


CREATE TABLE `invoices_timesheets`
(
    `invoice_id`   bigint(20) NOT NULL,
    `timesheet_id` bigint(20) NOT NULL,
    PRIMARY KEY (`invoice_id`, `timesheet_id`),
    CONSTRAINT `FKb7683mdu0g9JzdWIiOiRob5L3` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`),
    CONSTRAINT `FK325a23450poi456b7683mdu0g` FOREIGN KEY (`timesheet_id`) REFERENCES `timesheets` (`id`)
) DEFAULT CHARSET = utf8;