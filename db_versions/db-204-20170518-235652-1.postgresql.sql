--liquibase formatted sql

--changeset CarlosEduardo:1495162618894-1
ALTER TABLE entidades ADD patern VARCHAR(200) DEFAULT '#.##0,00';

