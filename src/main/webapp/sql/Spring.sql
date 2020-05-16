create database SpringTest
go

use SpringTest
go

create table [Products] (
	[id] int primary key identity(1,1),
	[name] varchar(30) not null,
	[type] varchar(30) not null,
	[cost] int default(0)
)
go

create table [Users] (
	[id] int primary key identity(1,1),
	[login] varchar(20) not null,
	[password] varchar(30) not null
)

create table [Logs] (
	[id] int primary key identity(1,1),
	[type] varchar(10) not null,
	[query] varchar(30) not null,
	[id_user] int,
	constraint FK_Users_Logs foreign key (id_user) references Users
)

go