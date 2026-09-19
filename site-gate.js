/* Shared public-site on/off switch for Punjab Route Permit pages. */
(function (root) {
  var SITE_STATUS_ID = 'ffffffff-ffff-4fff-8fff-ffffffffffff';
  var SITE_STATUS_APP = '__SITE_STATUS__';
  var CACHE_KEY = 'pta_site_enabled';
  var SUPABASE_URL = 'https://meqkwnujbuovzzeyjywo.supabase.co';
  var SUPABASE_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1lcWt3bnVqYnVvdnp6ZXlqeXdvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODEwMDIxMTcsImV4cCI6MjA5NjU3ODExN30.1FkeN3NZnT1vmE5QRul0sgF4HMw2wx9Y2wMSdQVgJSc';

  function isStatusRecord(r) {
    if (!r) return false;
    return r.id === SITE_STATUS_ID || r.doc_type === 'site_status' || r.app_no === SITE_STATUS_APP;
  }

  function cacheSet(enabled) {
    try { localStorage.setItem(CACHE_KEY, enabled ? '1' : '0'); } catch (e) {}
  }

  function cacheGet() {
    try {
      var v = localStorage.getItem(CACHE_KEY);
      if (v === '0') return false;
      if (v === '1') return true;
    } catch (e) {}
    return null;
  }

  function getClient(existing) {
    if (existing) return existing;
    if (typeof supabase === 'undefined') return null;
    return supabase.createClient(SUPABASE_URL, SUPABASE_KEY);
  }

  function rowEnabled(row) {
    return String((row && row.qr_link) || '').toLowerCase() !== 'disabled';
  }

  async function loadStatusRow(sb) {
    var byId = await sb.from('permits').select('id,app_no,doc_type,qr_link').eq('id', SITE_STATUS_ID).maybeSingle();
    if (byId && byId.data) return byId.data;
    var byApp = await sb.from('permits').select('id,app_no,doc_type,qr_link').eq('app_no', SITE_STATUS_APP).limit(1);
    if (byApp && byApp.data && byApp.data[0]) return byApp.data[0];
    if (byId && byId.error && !/multiple|0 rows|no rows|json object|PGRST116/i.test(String(byId.error.message || ''))) {
      throw byId.error;
    }
    return null;
  }

  async function fetchSiteEnabled(existingClient) {
    var cached = cacheGet();
    var sb = getClient(existingClient);
    if (!sb) return cached == null ? true : cached;
    try {
      var row = await loadStatusRow(sb);
      if (row) {
        var enabled = rowEnabled(row);
        cacheSet(enabled);
        return enabled;
      }
    } catch (e) {}
    return cached == null ? true : cached;
  }

  async function setSiteEnabled(enabled, existingClient) {
    enabled = !!enabled;
    cacheSet(enabled);
    var sb = getClient(existingClient);
    if (!sb) return { ok: false, localOnly: true, error: 'Not connected' };
    var payload = {
      id: SITE_STATUS_ID,
      app_no: SITE_STATUS_APP,
      full_name: 'SITE STATUS',
      doc_type: 'site_status',
      qr_link: enabled ? 'enabled' : 'disabled'
    };
    var result = await sb.from('permits').upsert(payload, { onConflict: 'id' }).select();
    if (result.error && /doc_type|schema cache|column/i.test(String(result.error.message || ''))) {
      var fallback = {
        id: SITE_STATUS_ID,
        app_no: SITE_STATUS_APP,
        full_name: 'SITE STATUS',
        qr_link: enabled ? 'enabled' : 'disabled'
      };
      result = await sb.from('permits').upsert(fallback, { onConflict: 'id' }).select();
    }
    if (result.error) {
      return { ok: false, localOnly: true, error: result.error.message || 'Save failed' };
    }
    return { ok: true, localOnly: false };
  }

  function overlayStyleText() {
    return '#site-offline-overlay{position:fixed;inset:0;z-index:2147483000;display:flex;align-items:center;justify-content:center;padding:24px;background:linear-gradient(180deg,#f7fbf9,#eef4f1);font-family:"Outfit",system-ui,sans-serif;color:#102033;}' +
      '#site-offline-overlay .box{width:min(460px,100%);background:#fff;border:1px solid rgba(16,32,51,.12);border-radius:18px;padding:28px 24px;box-shadow:0 18px 40px rgba(16,32,51,.12);text-align:center;}' +
      '#site-offline-overlay .kicker{margin:0 0 10px;font-size:12px;letter-spacing:.12em;text-transform:uppercase;color:#0d6b66;font-weight:700;}' +
      '#site-offline-overlay h1{margin:0 0 10px;font-size:1.55rem;line-height:1.2;}' +
      '#site-offline-overlay p{margin:0;color:#5a6b68;line-height:1.55;font-size:.95rem;}';
  }

  function showOfflineOverlay() {
    if (document.getElementById('site-offline-overlay')) return;
    var style = document.createElement('style');
    style.id = 'site-offline-style';
    style.textContent = overlayStyleText();
    var wrap = document.createElement('div');
    wrap.id = 'site-offline-overlay';
    wrap.setAttribute('role', 'alertdialog');
    wrap.setAttribute('aria-labelledby', 'site-offline-title');
    wrap.innerHTML =
      '<div class="box">' +
        '<p class="kicker">Punjab Provincial Transport Authority</p>' +
        '<h1 id="site-offline-title">Website temporarily unavailable</h1>' +
        '<p>Public permit services are currently switched off. Please try again later.</p>' +
        '<p style="margin-top:16px;font-size:12px"><a href="admin.html" style="color:#5a6b68;text-decoration:none;font-weight:650">Staff login</a></p>' +
      '</div>';
    function mount() {
      if (!document.body || document.getElementById('site-offline-overlay')) return;
      document.body.appendChild(style);
      document.body.appendChild(wrap);
      document.documentElement.style.overflow = 'hidden';
    }
    if (document.body) mount();
    else document.addEventListener('DOMContentLoaded', mount);
  }

  function hideOfflineOverlay() {
    var wrap = document.getElementById('site-offline-overlay');
    var style = document.getElementById('site-offline-style');
    if (wrap && wrap.parentNode) wrap.parentNode.removeChild(wrap);
    if (style && style.parentNode) style.parentNode.removeChild(style);
    document.documentElement.style.overflow = '';
  }

  function applyEnabled(enabled) {
    if (enabled) hideOfflineOverlay();
    else showOfflineOverlay();
  }

  function markChecking(on) {
    try {
      document.documentElement.classList.toggle('pta-site-checking', !!on);
    } catch (e) {}
  }

  async function enforcePublicGate() {
    var cached = cacheGet();
    if (cached !== null) applyEnabled(cached);
    var enabled = await fetchSiteEnabled();
    applyEnabled(enabled);
    markChecking(false);
    return enabled;
  }

  var styleGate = document.createElement('style');
  styleGate.textContent = 'html.pta-site-checking body{visibility:hidden !important;}html.pta-site-checking #site-offline-overlay,html.pta-site-checking #site-offline-overlay *{visibility:visible !important;}';
  (document.head || document.documentElement).appendChild(styleGate);

  root.PTASiteGate = {
    SITE_STATUS_ID: SITE_STATUS_ID,
    isStatusRecord: isStatusRecord,
    fetchSiteEnabled: fetchSiteEnabled,
    setSiteEnabled: setSiteEnabled,
    showOfflineOverlay: showOfflineOverlay,
    hideOfflineOverlay: hideOfflineOverlay,
    enforcePublicGate: enforcePublicGate
  };

  try {
    window.addEventListener('storage', function(e) {
      if (e.key !== CACHE_KEY) return;
      if (e.newValue === '0') applyEnabled(false);
      else if (e.newValue === '1') applyEnabled(true);
    });
  } catch (e) {}

  var script = document.currentScript;
  var shouldEnforce = !script || script.getAttribute('data-enforce') !== 'false';
  if (shouldEnforce) {
    markChecking(true);
    setTimeout(function(){ markChecking(false); }, 4000);
    enforcePublicGate();
    window.addEventListener('pageshow', function() { enforcePublicGate(); });
  }
})(window);
