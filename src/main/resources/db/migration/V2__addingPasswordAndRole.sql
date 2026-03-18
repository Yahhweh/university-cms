DO
$$
BEGIN
    IF
NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'role_type') THEN
CREATE TYPE role_type AS ENUM ('LECTURER', 'STUDENT', 'ADMIN');
END IF;
END $$;

ALTER TABLE "user"
    ADD COLUMN role role_type NOT NULL DEFAULT 'STUDENT';