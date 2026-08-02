(function () {
  var toggle = document.getElementById('nav-toggle');
  var mobile = document.getElementById('mobile-nav');
  if (!toggle || !mobile) return;
  toggle.addEventListener('click', function () {
    var open = mobile.classList.toggle('open');
    toggle.setAttribute('aria-expanded', open ? 'true' : 'false');
  });
  mobile.querySelectorAll('a').forEach(function (a) {
    a.addEventListener('click', function () {
      mobile.classList.remove('open');
      toggle.setAttribute('aria-expanded', 'false');
    });
  });
})();
