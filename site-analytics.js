/* Public website analytics — events stored in Supabase site_events. */
(function (root) {
  var TABLE = 'site_events';
  var SUPABASE_URL = 'https://meqkwnujbuovzzeyjywo.supabase.co';
  var SUPABASE_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1lcWt3bnVqYnVvdnp6ZXlqeXdvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODEwMDIxMTcsImV4cCI6MjA5NjU3ODExN30.1FkeN3NZnT1vmE5QRul0sgF4HMw2wx9Y2wMSdQVgJSc';

  function getClient(existing) {
    if (existing) return existing;
    if (typeof supabase === 'undefined') return null;
    return supabase.createClient(SUPABASE_URL, SUPABASE_KEY);
  }

  function shouldSkipDedupe(eventType, key) {
    try {
      var storageKey = 'pta_an_' + eventType + '_' + (key || 'global');
      var prev = parseInt(sessionStorage.getItem(storageKey) || '0', 10);
      var now = Date.now();
      if (prev && now - prev < 90000) return true;
      sessionStorage.setItem(storageKey, String(now));
    } catch (e) {}
    return false;
  }

  async function track(eventType, payload, existingClient) {
    payload = payload || {};
    if (payload.dedupe !== false && shouldSkipDedupe(eventType, payload.dedupeKey || payload.permit_id || '')) {
      return { ok: false, skipped: true };
    }
    var sb = getClient(existingClient);
    if (!sb) return { ok: false, error: 'no_client' };
    try {
      var row = {
        event_type: eventType,
        page: payload.page || null,
        permit_id: payload.permit_id || null,
        label: payload.label || null,
        vehicle_reg_no: payload.vehicle_reg_no || null
      };
      var result = await sb.from(TABLE).insert([row]);
      if (result.error) throw result.error;
      return { ok: true };
    } catch (e) {
      return { ok: false, error: e };
    }
  }

  function dayKey(iso) {
    return String(iso || '').substring(0, 10);
  }

  function emptyDayMap(days) {
    var map = {};
    var d = new Date();
    d.setHours(0, 0, 0, 0);
    for (var i = days - 1; i >= 0; i--) {
      var x = new Date(d);
      x.setDate(x.getDate() - i);
      map[x.toISOString().substring(0, 10)] = 0;
    }
    return map;
  }

  async function fetchDashboardStats(existingClient, days) {
    days = days || 7;
    var sb = getClient(existingClient);
    var out = {
      ok: false,
      days: days,
      today: 0,
      weekTotal: 0,
      verifies: 0,
      docViews: 0,
      homeViews: 0,
      byDay: emptyDayMap(days),
      topDocs: [],
      error: null
    };
    if (!sb) {
      out.error = 'Not connected';
      return out;
    }
    var since = new Date();
    since.setDate(since.getDate() - (days - 1));
    since.setHours(0, 0, 0, 0);
    try {
      var result = await sb
        .from(TABLE)
        .select('event_type,permit_id,label,vehicle_reg_no,created_at')
        .gte('created_at', since.toISOString())
        .order('created_at', { ascending: false })
        .limit(8000);
      if (result.error) throw result.error;
      var todayStr = new Date().toISOString().substring(0, 10);
      var topMap = {};
      (result.data || []).forEach(function(row) {
        var dk = dayKey(row.created_at);
        if (out.byDay[dk] != null) out.byDay[dk]++;
        out.weekTotal++;
        if (dk === todayStr) out.today++;
        var et = row.event_type || '';
        if (et === 'verify') out.verifies++;
        if (et === 'home_view') out.homeViews++;
        if (et === 'permit_view' || et === 'noc_view') {
          out.docViews++;
          var pid = row.permit_id;
          if (pid) {
            if (!topMap[pid]) {
              topMap[pid] = {
                permit_id: pid,
                count: 0,
                label: row.label || row.vehicle_reg_no || pid,
                event_type: et
              };
            }
            topMap[pid].count++;
            if (row.label) topMap[pid].label = row.label;
          }
        }
      });
      out.topDocs = Object.keys(topMap).map(function(k) { return topMap[k]; })
        .sort(function(a, b) { return b.count - a.count; })
        .slice(0, 8);
      out.ok = true;
    } catch (e) {
      out.error = (e && e.message) || 'Could not load analytics';
    }
    return out;
  }

  root.PTASiteAnalytics = {
    track: track,
    fetchDashboardStats: fetchDashboardStats
  };
})(window);
