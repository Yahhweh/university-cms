CREATE TABLE user_roles(
    user_id BIGINT NOT NULL REFERENCES "user"(id),
    role VARCHAR(50) NOT NULL
);

INSERT INTO user_roles (user_id, role)
SELECT id, role FROM "user";

ALTER TABLE "user" DROP COLUMN role;