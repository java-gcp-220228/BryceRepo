# Project 0 Checklist

1. - [ ] `POST /clients` 
        - Creates a new client
2. - [ ] `GET /clients`
        - Gets all clients
3. - [ ] `GET /clients/{client_id}`: 
      - Get client with an id of X (if the client exists)
4. - [ ] `PUT /clients/{client_id}` 
       - Update client with an id of X (if the client exists)
5. - [ ] `DELETE /clients/{client_id}` 
      - Delete client with an id of X (if the client exists)
6. - [ ] `POST /clients/{client_id}/accounts`: 
      - Create a new account for a client with id of X (if client exists)

7. - [ ] `GET /clients/{client_id}/accounts` 
       - Get all accounts for client with id of X (if client exists)
8. - [ ] `GET /clients/{client_id}/accounts?amountLessThan=2000&amountGreaterThan=400` 
       - Get all accounts for client id of X with balances between 400 and 2000 (if client exists)
7. - [ ] `GET /clients/{client_id}/accounts/{account_id}` 
      - Get account with id of Y belonging to client with id of X (if client and account exist AND if account belongs to client)
8.  - [ ] `PUT /clients/{client_id}/accounts/{account_id}`  
        - Update account with id of Y belonging to client with id of X (if client and account exist AND if account belongs to client)
9.  - [ ] `DELETE /clients/{client_id}/accounts/{account_id}` 
        - Delete account with id of Y belonging to client with id of X (if client and account exist AND if account belongs to client)

#### ***7 and 8 should be the same endpoint (check for query parameters using ctx.queryParam("amountLessThan") / ctx.queryParam("amountGreaterThan))***

****

Tables:
- client_data

****

Fields:

- client_id
- account_id
- account_type
- balance
- account_status
- create_date
- update_date

****

## SQL Script for data upload:

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS client_data;

CREATE TABLE client_data (
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    client_age INTEGER,
    city VARCHAR(50),
    state VARCHAR(50),
    account_type VARCHAR(50),
    balance INTEGER,
    account_status VARCHAR(5),
    create_date DATE,
    update_date DATE,
    account_id uuid default uuid_generate_v4() NOT NULL,
    client_id uuid default uuid_generate_v4(),
	PRIMARY KEY (client_id)
);

INSERT INTO client_data (first_name, last_name, client_age, city, state, account_type, balance, account_status, create_date, update_date)	 
VALUES 
('Bob', 'Smith', 25, 'City1', 'State1', 'check', 10000, 'A', '2022-03-03', '2022-03-10'),
('John', 'Doe', 32, 'City2', 'State1', 'check', 1000, 'B', '2001-01-06', '2020-05-08'),
('Jane', 'Doe', 76, 'City3', 'State2', 'save',  150, 'C', '1990-01-06', '1995-08-10');


select * from client_data;
```








George's project : https://github.com/polydin/project-zero
