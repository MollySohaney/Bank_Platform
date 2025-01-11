-- Create the database.
create database if not exists bank_platform_db;

-- Use the created database.
use bank_platform_db;

create table if not exists User (
    user_id int auto_increment, 
    fname varchar(50) not null, 
    lname varchar(50) not null, 
    username varchar(50) not null,
    password varchar(255) not null, -- not sure on the length of the password hash
    primary key (user_id),
    unique (username) -- will be used for indexing later
);

create table if not exists Account (
    acc_id int auto_increment, 
    name varchar(50) not null, 
    balance numeric(15, 2) not null, -- no one is allowed to be more than a trillionaire 
    user_id int,
    primary key(acc_id),
    foreign key (user_id) references User(user_id)
);

create table if not exists Transaction(
    transaction_id int auto_increment, 
    amount numeric(15, 2) not null, 
    date date not null, 
    time time not null,
    acc_id int,
    primary key (transaction_id),
    foreign key (acc_id) references Account(acc_id)
);

create table if not exists BankStatement (
    statement_id int auto_increment, 
    balance_change numeric(15, 2) not null, 
    start_date date not null, 
    end_date date not null,
    acc_id int,
    primary key (statement_id),
    foreign key (acc_id) references Account(acc_id)
);

create table if not exists Transfer(
    from_acc_id int, 
    to_acc_id int,
    transaction_id int,
    primary key (from_acc_id, to_acc_id, transaction_id),
    foreign key (from_acc_id) references Account(acc_id),
    foreign key (to_acc_id) references Account(acc_id),
    foreign key (transaction_id) references Transaction(transaction_id)
);