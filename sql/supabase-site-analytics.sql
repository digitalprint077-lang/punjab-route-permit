-- Website analytics (home, verify, public permit/NOC views)
-- Run in Supabase SQL Editor once.

CREATE TABLE IF NOT EXISTS public.site_events (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  event_type text NOT NULL,
  page text,
  permit_id text,
  label text,
  vehicle_reg_no text,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS site_events_created_at_idx ON public.site_events (created_at DESC);
CREATE INDEX IF NOT EXISTS site_events_event_type_idx ON public.site_events (event_type);
CREATE INDEX IF NOT EXISTS site_events_permit_id_idx ON public.site_events (permit_id);

ALTER TABLE public.site_events ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Public insert site events" ON public.site_events;
CREATE POLICY "Public insert site events"
  ON public.site_events FOR INSERT
  TO anon, authenticated
  WITH CHECK (true);

DROP POLICY IF EXISTS "Staff read site events" ON public.site_events;
CREATE POLICY "Staff read site events"
  ON public.site_events FOR SELECT
  TO authenticated
  USING (true);
