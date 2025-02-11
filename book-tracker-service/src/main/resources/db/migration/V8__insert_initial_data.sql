INSERT INTO tracker (book_id, status, checkout_time, return_time)
SELECT id, 'available', NULL, NULL
FROM book;