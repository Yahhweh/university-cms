UPDATE "user"
SET role= 'STUDENT'
where id BETWEEN 14 and 163;
UPDATE "user"
SET role= 'LECTURER'
where id BETWEEN 1 and 10;
UPDATE "user"
SET role = 'ADMIN'
where id IN (11, 12, 13);