-- Punjab Route Permit — Supabase (PostgreSQL) migration
-- Run in: Supabase Dashboard → SQL Editor → New query → Paste → Run
--
-- Safe to run more than once (uses IF NOT EXISTS / ADD COLUMN IF NOT EXISTS).

-- ── Optional: full table (new projects only) ─────────────────────────────
CREATE TABLE IF NOT EXISTS public.permits (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  app_no text,
  route_permit_no text,
  fc_no text,
  full_name text,
  cnic_no text,
  owner_address text,
  vehicle_type text,
  seating_capacity text,
  vehicle_make text,
  vehicle_reg_no text,
  engine_no text,
  chassis_no text,
  issue_authority text,
  valid_from text,
  valid_upto text,
  fee text,
  psid_no text,
  counter_sig_upto text,
  guarantee_expiry text,
  third_party_insurance_expiry text,
  issuing_district text,
  issuance_date text,
  authority_name text,
  districts text,
  qr_link text,
  doc_type text DEFAULT 'permit',
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- ── Existing table: add missing columns (fields 13 & 14 + doc type) ───────
ALTER TABLE public.permits
  ADD COLUMN IF NOT EXISTS vehicle_make text;

ALTER TABLE public.permits
  ADD COLUMN IF NOT EXISTS seating_capacity text;

ALTER TABLE public.permits
  ADD COLUMN IF NOT EXISTS guarantee_expiry text;

ALTER TABLE public.permits
  ADD COLUMN IF NOT EXISTS third_party_insurance_expiry text;

ALTER TABLE public.permits
  ADD COLUMN IF NOT EXISTS doc_type text DEFAULT 'permit';

ALTER TABLE public.permits
  ADD COLUMN IF NOT EXISTS districts text;

ALTER TABLE public.permits
  ADD COLUMN IF NOT EXISTS qr_link text;

-- ── Helpful indexes (optional) ───────────────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_permits_vehicle_reg ON public.permits (vehicle_reg_no);
CREATE INDEX IF NOT EXISTS idx_permits_app_no ON public.permits (app_no);
CREATE INDEX IF NOT EXISTS idx_permits_created_at ON public.permits (created_at DESC);

-- ── Row Level Security (adjust policies for your auth setup) ─────────────
ALTER TABLE public.permits ENABLE ROW LEVEL SECURITY;

-- Example: allow anon read for public permit page (customize as needed)
-- DROP POLICY IF EXISTS "Public read permits" ON public.permits;
-- CREATE POLICY "Public read permits" ON public.permits
--   FOR SELECT TO anon USING (true);
