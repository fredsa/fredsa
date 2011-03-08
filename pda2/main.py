#!/usr/bin/env python
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
from google.appengine.ext import webapp
from google.appengine.ext.webapp import util
from google.appengine.ext import db
from google.appengine.api import users


class MainHandler(webapp.RequestHandler):
  def get(self):
    self.response.out.write("""
<html> 
<head> 
  <title>PDA</title> 
  <link rel="stylesheet" type="text/css" href="/site/sitelook.css"/> 
</head> 
<body class="pda">
<h1>App Engine PDA2</h1> 
<form name="searchform" method="post"> 
<!--
<input type="checkbox" name="includedisabled" > Include Disabled Entries<br> 
<br> 
<input type="radio" name="format" checked value="verbose"> Verbose (regular) results<br> 
<input type="radio" name="format"  value="compact"> Compact results<br> 
<input type="radio" name="format"  value="json"> JSON results<br> 
-->
Search text: <input type="text" name="search" value=""> <input type="submit" value="Go"><br> 
</form> 
<script>document.searchform.search.focus(); document.searchform.search.select();</script> 

<hr> 
[<a href=".?action=person">+Person</a>] 
[<a href="_ah/admin">Admin</a>] 
<!--
<a href=".?mailing_list=preview">[Mailing Labels Preview]</a> 
-->
    """)

    if self.request.get("action") == "person":
      key = self.request.get("key")
      if key:
        person = db.get(key)
      else:
        person = Person()
      self.personForm(person)

    self.response.out.write("""
</body> 
</html> 
    """)

  def personForm(self, person):
      props = Person.properties()
      for propname in props:
        prop = props[propname]
        if isinstance(prop, db.BooleanProperty):
          res = propname in self.request.arguments()
          setattr(person, propname, res)
        elif isinstance(prop, db.StringListProperty):
          setattr(person, propname, [])
        else:
          setattr(person, propname, self.request.get(propname))

      if person.first_name or person.last_name or person.mailing_name:
        person.put()

      self.response.out.write("""
<hr>
<form name="personform" method="get" action="."> 
<input type="hidden" name="action" value="%s"> 
<input type="text" name="key" value="%s"> 
<table> 
      """ % (self.request.get("action"), person.maybeKey()))

      for propname in props:
        prop = props[propname]
	label = props[propname].verbose_name
        value = getattr(person, propname)
        if isinstance(prop, SelectableStringProperty):
          values=prop.choices
          html="""<select name="%s" size="%s">""" % (propname, len(values))
          for v in values:
            selected="selected" if value == v else ""
            html+="""<option %s value="%s">%s</option>""" % (selected, v, v)
          html+="""</select>"""
        elif isinstance(prop, db.BooleanProperty):
          checked = "checked" if getattr(person, propname) else ""
          html = """<input type="checkbox" name="%s" %s> %s""" % (checked, propname, label)
          label = ""
        elif isinstance(prop, db.StringProperty):
          html = """<input type="text" name="%s" value="%s">""" % (propname, value)
        else:
          html = """xxxxx"""
        self.response.out.write("""<tr style="color:red;"><td style="vertical-align: top; text-align: right;">%s</td><td>%s</td></tr>""" % (label, html))

      self.response.out.write("""<tr><td></td><td><input type="submit" name="updated" value="Save Changes" style="margin-top: 1em;"></td></tr>""")
      propname = props.keys()[0]
      self.response.out.write("""
</table> 
</form>
<script>document.personform.%s.focus();</script>
<hr> 
      """ % propname)

class SelectableStringProperty(db.StringProperty):
  pass

class Person(db.Model):
  mailing_name = db.StringProperty(verbose_name="Mailing Name", default="")
  title = db.StringProperty(verbose_name="Title", default="")
  first_name = db.StringProperty(verbose_name="First Name", default="")
  last_name = db.StringProperty(verbose_name="Last Name", default="")
  company_name = db.StringProperty(verbose_name="Company Name", default="")
  comments = db.StringProperty(verbose_name="Comments", default="")
  category = SelectableStringProperty(verbose_name="Category", default="",
    choices=[
      "Relatives",
      "Personal",
      "Hotel/Restaurant/Entertainment",
      "Services by Individuals",
      "Companies, Institutions, etc.",
      "Business Relations"
    ])
  enabled = db.BooleanProperty(verbose_name="Enabled", required=True, default=True)
  send_card = db.BooleanProperty(verbose_name="Send Card", default=False, required=True)
  words = db.StringListProperty(verbose_name="Words", default=[])

  def maybeKey(self):
    if self.is_saved():
      return self.key()
    else:
      return ""


def main():
  application = webapp.WSGIApplication([('/', MainHandler)],
                                       debug=True)
  util.run_wsgi_app(application)


if __name__ == '__main__':
  main()
