-- Add seating capacity to permits (run in Supabase SQL editor).
ALTER TABLE public.permits ADD COLUMN IF NOT EXISTS seating_capacity text;
