-- The http://localhost:8081/account URL may or may not have an acc_id in the end
-- To get to the transactions page http://localhost:8081/account URL has to have an acc_id and the /transfer
-- The http://localhost:8081/statements URL may or may not have a st_id (statement id)


-- Select user data given a username (given to authenticate/log in user)
-- http://localhost:8081/login
select * from User where username = ?;

-- Insert new entry in User table (given to register new user)
-- http://localhost:8081/register
insert into User (username, password, fname, lname) values (?, ?, ?, ?);

-- Insert new entry in Account table for a user
-- http://localhost:8081/account/create
insert into Account (name, balance, user_id, acc_id) values (?, ?, ?, ?);

-- Select the highest account id number in Account table 
-- http://localhost:8081/account/create
select max(acc_id) from Account;

-- Select the data of all accounts of the given user
-- http://localhost:8081/account
-- http://localhost:8081/statements/create
select * from Account where user_id = ?;

-- Select the data of a specific account of the given user
-- http://localhost:8081/account/create
-- http://localhost:8081/account
select * from Account where user_id = ? and acc_id = ?;

-- Select the user data and the account data for a given user
-- http://localhost:8081/account
select a.acc_id, a.name, a.balance, u.fname, u.lname from Account a, User u where a.user_id=u.user_id and not a.acc_id = ?;

-- Insert new entry in BankStatement table
-- http://localhost:8081/statements/create
insert into BankStatement (statement_id, balance_change, start_date, end_date, acc_id) values (?, ?, ?, ?, ?);

-- Find the highest statement id in BankStatement
-- http://localhost:8081/statements/create
select max(statement_id) from BankStatement;

-- Select all statement data of all accounts of the user in decending order (most recent first)
-- -- http://localhost:8081/statements
select b.statement_id, b.acc_id, b.balance_change, b.start_date, b.end_date, a.name from BankStatement b, Account a where b.acc_id=a.acc_id and a.user_id=? order by b.statement_id desc;

-- Select all statement data of a given account in decending order (most recent first)
select b.statement_id, b.balance_change, b.start_date, b.end_date, a.name from BankStatement b, Account a where b.acc_id=? and a.acc_id=b.acc_id order by statement_id desc;

-- Select statement data of a given statement id
-- http://localhost:8081/statements/create
select b.balance_change, b.start_date, b.end_date, b.acc_id, a.name from BankStatement b, Account a where b.statement_id=? and b.acc_id=a.acc_id;

-- Select account data of a given account id and check if the balance after the deposit is less than 1000000000000
-- http://localhost:8081/account
select * from Account where user_id = ? and acc_id = ? and (balance+?)<=1000000000000;

-- Insert new entry in Transactions table
-- http://localhost:8081/account
-- http://localhost:8081/account
insert into Transaction (amount, date, time, acc_id) values (?, ?, ?, ?);

-- Updates the balance in the given account
-- http://localhost:8081/account
-- http://localhost:8081/account
-- http://localhost:8081/account
update Account set balance = balance+? where acc_id = ?;

-- Updates the balance in the given account
-- http://localhost:8081/account
update Account set balance = balance-? where acc_id = ?;

-- Select account data of the given account id check if the account has more than the given amount
-- http://localhost:8081/account
-- http://localhost:8081/account
select * from Account where user_id = ? and acc_id = ? and balance>=?;

-- Select account data of a given account id and check if the balance after the transfer is less than 1000000000000
-- http://localhost:8081/account
select * from Account where acc_id = ? and (balance+?)<=1000000000000;

-- Find the highest transaction id in Transaction table
-- http://localhost:8081/account
select max(transaction_id) from Transaction;

-- Insert new entry in Transaction table with given transaction id
-- http://localhost:8081/account
insert into Transaction (amount, date, time, acc_id, transaction_id) values (?, ?, ?, ?, ?);

-- Insert new entry in Transfer table
-- http://localhost:8081/account
insert into Transfer (from_acc_id, to_acc_id, transaction_id) values (?, ?, ?)

-- Select all deposit and withdraw transaction data of a given account in decending order (most recent first)
select * from Transaction t, Account a where t.acc_id = ? and t.acc_id = a.acc_id and a.user_id = ? and t.transaction_id not in(select transaction_id from Transfer) sort by date desc;

-- Select all transactions of a given account in decending order (most recent first)
select * from Transaction t, Account a where t.acc_id = ? and t.acc_id = a.acc_id and a.user_id = ? sort by date desc;

-- Select all transfers of a given account in decending order (most recent first)
select * from Transaction t, Account a, Transfer x where (t.acc_id = ? or x.to_acc_id = ?) and a.acc_id = ? and t.transaction_id = x.transaction_id and a.user_id = ? sort by date desc;

-- Select all transaction data and account data of the given account in decending order removing any data of other accounts
-- http://localhost:8081/account
select *, null as from_acc_id, null as to_acc_id, null as transaction_id from Transaction t where t.acc_id=? and (t.transaction_id not in(select transaction_id from Transfer)) union select * from Transaction t, Transfer r where t.acc_id=? and t.transaction_id=r.transaction_id order by date desc, time desc;

-- Calculate the difference in balance between present and a date
-- http://localhost:8081/account/
-- http://localhost:8081/account
select amount from Transaction where date>=? and acc_id=?;;

-- Calculate the difference in balance between two dates
-- http://localhost:8081/statements/create
select sum(amount) from Transaction where acc_id = ? and date between ? and ?;

-- Select all transaction data and account data of the given account between two dates in decending order removing any data of other accounts
-- http://localhost:8081/statements
select *, null as from_acc_id, null as to_acc_id, null as transaction_id from Transaction t where t.acc_id=? and (t.transaction_id not in(select transaction_id from Transfer)) and t.date between ? and ? union select * from Transaction t, Transfer r where t.acc_id=? and t.transaction_id=r.transaction_id and t.date between ? and ? order by date desc, time desc;

hello