/* Record and read public permit/NOC page views for the staff dashboard. */
(function (root) {
  var TABLE = 'permit_views';
  var CACHE_KEY = 'pta_permit_views';
  var ABACUS = 'https://abacus.jasoncameron.dev';
  var ABACUS_NS = 'pdtg-permits';
  var SUPABASE_URL = 'https://meqkwnujbuovzzeyjywo.supabase.co';
  var SUPABASE_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1lcWt3bnVqYnVvdnp6ZXlqeXdvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODEwMDIxMTcsImV4cCI6MjA5NjU3ODExN30.1FkeN3NZnT1vmE5QRul0sgF4HMw2wx9Y2wMSdQVgJSc';

  function getClient(existing) {
    if (existing) return existing;
    if (typeof supabase === 'undefined') return null;
    return supabase.createClient(SUPABASE_URL, SUPABASE_KEY);
  }

  function abacusKey(id) {
    return String(id || '').replace(/[^A-Za-z0-9_.-]/g, '').slice(0, 64);
  }

  function readLocal() {
    try {
      var raw = localStorage.getItem(CACHE_KEY);
      var data = raw ? JSON.parse(raw) : {};
      return data && typeof data === 'object' ? data : {};
    } catch (e) {
      return {};
    }
  }

  function writeLocal(data) {
    try { localStorage.setItem(CACHE_KEY, JSON.stringify(data || {})); } catch (e) {}
  }

  function saveLocal(permitId, count, meta) {
    if (!permitId) return;
    var data = readLocal();
    var row = data[permitId] || { count: 0, last_viewed: null };
    row.count = Math.max(parseInt(row.count, 10) || 0, parseInt(count, 10) || 0);
    row.last_viewed = new Date().toISOString();
    if (meta) {
      if (meta.full_name) row.full_name = meta.full_name;
      if (meta.vehicle_reg_no) row.vehicle_reg_no = meta.vehicle_reg_no;
      if (meta.doc_type) row.doc_type = meta.doc_type;
    }
    data[permitId] = row;
    writeLocal(data);
    return row;
  }

  function recentlyRecorded(permitId) {
    try {
      var key = 'pta_view_once_' + permitId;
      var prev = parseInt(sessionStorage.getItem(key) || '0', 10);
      var now = Date.now();
      if (prev && now - prev < 120000) return true;
      sessionStorage.setItem(key, String(now));
    } catch (e) {}
    return false;
  }

  async function hitAbacus(permitId) {
    var key = abacusKey(permitId);
    if (key.length < 3) return null;
    var res = await fetch(ABACUS + '/hit/' + ABACUS_NS + '/' + encodeURIComponent(key));
    var data = await res.json();
    return typeof data.value === 'number' ? data.value : null;
  }

  async function getAbacus(permitId) {
    var key = abacusKey(permitId);
    if (key.length < 3) return 0;
    try {
      var res = await fetch(ABACUS + '/get/' + ABACUS_NS + '/' + encodeURIComponent(key));
      if (!res.ok) return 0;
      var data = await res.json();
      return parseInt(data.value, 10) || 0;
    } catch (e) {
      return 0;
    }
  }

  async function recordView(permitId, meta, existingClient) {
    if (!permitId || recentlyRecorded(permitId)) return { ok: false, skipped: true };
    var count = 0;
    try {
      var n = await hitAbacus(permitId);
      if (typeof n === 'number') count = n;
    } catch (e) {}
    if (!count) {
      var local = readLocal()[permitId];
      count = ((local && parseInt(local.count, 10)) || 0) + 1;
    }
    saveLocal(permitId, count, meta);
    var sb = getClient(existingClient);
    if (sb) {
      try {
        await sb.from(TABLE).insert([{
          permit_id: permitId,
          doc_type: (meta && meta.doc_type) || 'permit',
          full_name: (meta && meta.full_name) || null,
          vehicle_reg_no: (meta && meta.vehicle_reg_no) || null
        }]);
      } catch (e) {}
    }
    return { ok: true, count: count };
  }

  async function fetchViewStats(existingClient, permitIds) {
    var stats = { byId: {}, total: 0 };
    var local = readLocal();
    var ids = {};
    (permitIds || []).forEach(function(id) { if (id) ids[id] = true; });
    Object.keys(local).forEach(function(id) {
      ids[id] = true;
      var row = local[id] || {};
      stats.byId[id] = {
        count: parseInt(row.count, 10) || 0,
        last_viewed: row.last_viewed || null,
        full_name: row.full_name || '',
        vehicle_reg_no: row.vehicle_reg_no || '',
        doc_type: row.doc_type || 'permit'
      };
    });
    (permitIds || []).forEach(function(id) {
      if (id && !stats.byId[id]) {
        stats.byId[id] = { count: 0, last_viewed: null, full_name: '', vehicle_reg_no: '', doc_type: 'permit' };
      }
    });

    var sb = getClient(existingClient);
    if (sb) {
      try {
        var result = await sb.from(TABLE).select('permit_id,viewed_at,doc_type,full_name,vehicle_reg_no');
        if (!result.error) {
          var remoteCounts = {};
          (result.data || []).forEach(function(row) {
            var id = row.permit_id;
            if (!id) return;
            ids[id] = true;
            if (!stats.byId[id]) stats.byId[id] = { count: 0, last_viewed: null, full_name: '', vehicle_reg_no: '', doc_type: 'permit' };
            remoteCounts[id] = (remoteCounts[id] || 0) + 1;
            var item = stats.byId[id];
            if (row.viewed_at && (!item.last_viewed || row.viewed_at > item.last_viewed)) item.last_viewed = row.viewed_at;
            if (row.full_name) item.full_name = row.full_name;
            if (row.vehicle_reg_no) item.vehicle_reg_no = row.vehicle_reg_no;
            if (row.doc_type) item.doc_type = row.doc_type;
          });
          Object.keys(remoteCounts).forEach(function(id) {
            stats.byId[id].count = Math.max(stats.byId[id].count || 0, remoteCounts[id]);
          });
        }
      } catch (e) {}
    }

    var list = Object.keys(ids);
    for (var i = 0; i < list.length; i += 20) {
      var chunk = list.slice(i, i + 20);
      var values = await Promise.all(chunk.map(function(id) { return getAbacus(id); }));
      chunk.forEach(function(id, idx) {
        var n = values[idx] || 0;
        if (!stats.byId[id]) stats.byId[id] = { count: 0, last_viewed: null, full_name: '', vehicle_reg_no: '', doc_type: 'permit' };
        if (n > (stats.byId[id].count || 0)) stats.byId[id].count = n;
      });
    }

    stats.total = 0;
    Object.keys(stats.byId).forEach(function(id) { stats.total += stats.byId[id].count || 0; });
    return stats;
  }

  root.PTAPermitViews = {
    recordView: recordView,
    fetchViewStats: fetchViewStats
  };
})(window);
