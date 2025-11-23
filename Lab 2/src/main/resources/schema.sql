CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY,
                                     nickname VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS accounts (
                                        id UUID PRIMARY KEY,
                                        user_id UUID,
                                        balance DECIMAL(20, 2),
    is_frozen BOOLEAN
    );