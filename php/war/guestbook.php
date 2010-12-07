<html>
  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>
  <body>
<?php
  $user_service_factory_class =
    java_class("com.google.appengine.api.users.UserServiceFactory");
  $user_service = $user_service_factory_class->getUserService();
  $user = $user_service->getCurrentUser();
  $request_uri = $_SERVER['REQUEST_URI'];

  if ($user != null) {
?>
<p>Hello, <?= $user->getNickname() ?>! (You can
<a href="<?= $user_service->createLogoutURL($request_uri) ?>">sign out</a>.)</p>
<?php
    } else {
?>
<p>Hello!
<a href="<?= $user_service->createLoginURL($request_uri) ?>">Sign in</a>
to include your name with greetings you post.</p>
<?php
    }
?>

<?php
  $pmf_class = java_class("guestbook.PMF");
  $pm = $pmf_class->get()->getPersistenceManager();
  $query = "select from guestbook.Greeting order by date desc range 0,5";
  $greetings = $pm->newQuery($query)->execute();

  if ($greetings->isEmpty()) {
?>
<p>The guestbook has no messages.</p>
<?php
  } else {
    foreach ($greetings as $g) {
      if ($g->getAuthor() == null) {
?>
<p>An anonymous person wrote:</p>
<?php
      } else {
?>
<p><b><?= $g->getAuthor()->getNickname() ?></b> wrote:</p>
<?php
      }
?>
<blockquote><?= $g->getContent() ?></blockquote>
<?php
    }
  }
  $pm->close();
?>

    <form action="/sign" method="post">
      <div><textarea name="content" rows="3" cols="60"></textarea></div>
      <div><input type="submit" value="Post Greeting" /></div>
    </form>

  </body>
</html>