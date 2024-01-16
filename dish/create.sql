CREATE DATABASE Dish;
CREATE TABLE bills(
	id bigint primary key auto_increment NOT NULL,
	customerId bigint NULL,
	genTime date NULL,
	userId bigint NULL,
	status nvarchar(50) NULL
);
CREATE TABLE bills_dishes(
	billId bigint NOT NULL,
	dishId bigint NOT NULL,
	num int NULL,
    primary key(billId,dishId)
 );
CREATE TABLE categories(
	id bigint primary key auto_increment NOT NULL,
	name nvarchar(10) NOT NULL
);
CREATE TABLE comments(
	id bigint primary key NOT NULL,
	rate int NOT NULL,
	comment nvarchar(255) NULL
);
CREATE TABLE customers(
	id bigint primary key auto_increment  NOT NULL,
	description nvarchar(50) NOT NULL
);
CREATE TABLE dishComments(
	id bigint primary key auto_increment NOT NULL,
	dishId bigint NOT NULL,
	userId bigint NULL,
	rate int NULL,
	comment nvarchar(255) NULL,
	time date NULL
);
CREATE TABLE dishes(
	id bigint primary key auto_increment NOT NULL,
	dishName nvarchar(20) NULL,
	description nvarchar(50) NULL,
	unitPrice decimal(10,2) NULL,
	categoryId bigint NULL,
	cover nvarchar(255) NULL
);
CREATE TABLE permissions(
	id bigint primary key auto_increment NOT NULL,
	url nvarchar(255) NOT NULL,
	description nvarchar(255) NULL
);
CREATE TABLE roles(
	id bigint primary key auto_increment NOT NULL,
	roleName nvarchar(50) NOT NULL
);
CREATE TABLE roles_permissions(
	roleId bigint NOT NULL,
	permissionId bigint NOT NULL,
    primary key(roleId,permissionId)
);
CREATE TABLE users(
	username nvarchar(255) NOT NULL,
	password nvarchar(255) NOT NULL,
	salt nvarchar(255) NOT NULL,
	id bigint primary key auto_increment NOT NULL,
	phone nvarchar(20) NULL
);
CREATE TABLE users_roles(
	userId bigint NOT NULL,
	roleId bigint NOT NULL,
    primary key(userId,roleId)
);