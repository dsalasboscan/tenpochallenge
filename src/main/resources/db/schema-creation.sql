CREATE TABLE calculations
(
    id               UUID PRIMARY KEY,
    first_value      NUMERIC(20, 2) NOT NULL,
    second_value     NUMERIC(20, 2) NOT NULL,
    percentage       NUMERIC(5, 2)  NOT NULL,
    calculated_value NUMERIC(20, 2) NOT NULL,
    created_at timestamp NOT NULL DEFAULT now()
);
