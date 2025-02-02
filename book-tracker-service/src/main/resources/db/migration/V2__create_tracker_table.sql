CREATE TABLE IF NOT EXISTS tracker (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    status VARCHAR(255),
    checkout_time DATETIME,
    return_time DATETIME,
    FOREIGN KEY (book_id) REFERENCES book(id)
);