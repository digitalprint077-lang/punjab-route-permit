-- Optional: run in phpMyAdmin if you prefer SQL instead of api/install.php

CREATE TABLE IF NOT EXISTS admins (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS permits (
  id CHAR(36) NOT NULL PRIMARY KEY,
  app_no TEXT,
  route_permit_no TEXT,
  fc_no TEXT,
  full_name TEXT,
  cnic_no TEXT,
  owner_address TEXT,
  vehicle_type TEXT,
  vehicle_reg_no TEXT,
  engine_no TEXT,
  chassis_no TEXT,
  issue_authority TEXT,
  valid_from TEXT,
  valid_upto TEXT,
  fee TEXT,
  psid_no TEXT,
  counter_sig_upto TEXT,
  issuing_district TEXT,
  issuance_date TEXT,
  authority_name TEXT,
  districts TEXT,
  qr_link TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_vehicle_reg (vehicle_reg_no(64)),
  INDEX idx_app_no (app_no(64)),
  INDEX idx_full_name (full_name(64))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Default admin password: permit@2026
-- Generate a fresh hash with: php -r "echo password_hash('permit@2026', PASSWORD_DEFAULT);"
-- Or just use api/install.php which creates the admin automatically.
