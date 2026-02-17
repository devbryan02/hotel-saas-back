-- =========================
-- TENANT
-- =========================
CREATE TABLE tenant
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    business_name VARCHAR(150),
    plan VARCHAR(50),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- CLIENT
-- =========================
CREATE TABLE client
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    document VARCHAR(50),
    email VARCHAR(150),
    phone VARCHAR(30),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_client_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenant(id)
            ON DELETE CASCADE
);

-- =========================
-- ROOM
-- =========================
CREATE TABLE room
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    room_number VARCHAR(20) NOT NULL,
    room_type VARCHAR(50),
    price_per_night DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) NOT NULL,

    CONSTRAINT fk_room_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenant(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_room_per_tenant
        UNIQUE (tenant_id, room_number)
);

-- =========================
-- OCCUPATION
-- =========================
CREATE TABLE occupation
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    client_id UUID NOT NULL,
    room_id UUID NOT NULL,

    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,

    status VARCHAR(30) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_occupation_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenant(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_occupation_client
        FOREIGN KEY (client_id)
            REFERENCES client(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_occupation_room
        FOREIGN KEY (room_id)
            REFERENCES room(id)
            ON DELETE CASCADE,

    CONSTRAINT chk_dates
        CHECK (check_out_date > check_in_date)
);

-- =========================
-- APP USER
-- =========================
CREATE TABLE app_user
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,

    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_tenant
        FOREIGN KEY (tenant_id)
            REFERENCES tenant(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_user_email
        UNIQUE (email)
);
