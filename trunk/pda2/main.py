#!/usr/bin/env python
#
import logging
import pprint
import re
import os
import datetime

from google.appengine.ext import webapp
from google.appengine.ext.webapp import util
from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.api import users

class MainHandler(webapp.RequestHandler):
  def post(self):
    self.get()

  def get(self):
    user = users.get_current_user()
    self.response.out.write("""
          <!DOCTYPE html>
          <html>
          <head>
            <title>PDA2</title>
            <link rel="stylesheet" type="text/css" href="main.css"/>
            <style type="text/css">
              body {
                line-height: 1.3em;
              }
              .comments , .directions {
                font-family: monospace;
                color: #c44;
                white-space: pre;
                padding-bottom: 2em;
              }
              .directions {
                color: #444;
              }
              .tag {
                font-size: small;
              }
              .indent {
                padding-left: 2em;
              }
              .edit-link {
                font-size: small;
                background-color: #ddd;
                margin: 0.2em;
                padding: 0px 10px;
                border-radius: 5px;
                display: inline-block;
              }
              .thing {
                font-weight: bold;
                margin: 0em 0.5em 0em 0.2em;
              }
              .thing.Address {
                color: green;
              }
              .thing.Contact {
                color: blue;
              }
              .thing.Calendar {
                color: purple;
              }
              .title {
                text-decoration: none;
                font-size: 2em;
                font-weight: bold;
                padding: 0.2em 0em 0.5em;
                display: block;
                color: black;
              }
              .powered {
                color: #777;
                font-style: italic;
                margin-left: 42%%;                
              }
              .topbar {
                position: absolute;
                right: 0.5em;
                top: 0.2em;
              }
            </style>
          </head>
          <body class="pda">
          <a href="/" class="title">PDA2</a>
          <div class="topbar">%s</div>
          <form name="searchform" method="get">
          <!--
          <input type="checkbox" name="includedisabled" > Include Disabled Entries<br>
          <br>
          <input type="radio" name="format" checked value="verbose"> Verbose (regular) results<br>
          <input type="radio" name="format"  value="compact"> Compact results<br>
          -->
          Search text: <input type="text" name="q" value="%s"> <input type="submit" value="Go"><br>
          </form>
          
          <hr>
          [<a href=".?action=create&kind=Person">+Person</a>]
          <br>
          <br>
    """ % (user, self.request.get("q")))
    if user.nickname() in ("fredsa@gmail.com", "fredsa@google.com") or re.search("^Development", os.environ["SERVER_SOFTWARE"]):
      self.response.out.write("""
            {<a href="_ah/admin">Admin</a>}
            {<a href=".?action=fix">map-over-person-entities</a>}
      """)

    q = self.request.get("q")
    action = self.request.get("action")
    kind = self.request.get("kind")
    modified = self.request.get("modified")
    if q or not self.request.arguments():
      self.response.out.write("""
            <script>document.searchform.q.focus(); document.searchform.q.select();</script>
      """)
    if q:
      qlist = re.split('\W+', q.lower())
      if '' in qlist:
        qlist.remove('')
      results = None
      for qword in qlist:
        word_results = set([])
        for kind in [Person, Address, Calendar, Contact]:
          query = db.Query(kind, keys_only=True)
          query.filter("words >=", qword)
          query.filter("words <=", qword + "~")
          word_results = word_results | set([x.parent() or x for x in query])
          #self.response.out.write("word_results = %s<br><br>" % word_results)
        if results is None:
          #self.response.out.write("results is None<br>")
          results = word_results
        else:
          results = results & word_results
        #self.response.out.write("results = %s<br><br>" % results
        self.response.out.write("%s result(s) matching <code>%s</code><br>" % (len(word_results), qword))

      keys = list(results)
      if (len(qlist) > 1):
        self.response.out.write("===> %s result(s) matching <code>%s</code><br>" % (len(keys), " ".join(qlist)))
      while (keys):
        # Max 30 keys allow in IN clause
        somekeys = keys[:30]
        keys = keys[30:]
        #self.response.out.write("somekeys = %s<br><br>" % somekeys)
        query = db.Query(Person)
        query.filter("__key__ IN", somekeys)
        s = set(query)
        #self.response.out.write("s = %s<br><br>" % s)
        for person in sorted(s, key=Thing.key):
          #self.response.out.write("person = %s<br><br>" % person)
          self.personView(person)
    elif action == "create":
      if kind == "Person":
        self.personForm(Person())
      elif kind == "Contact":
        self.contactForm(Contact())
      elif kind == "Address":
        self.addressForm(Address())
      elif kind == "Calendar":
        self.calendarForm(Calendar())
    elif action == "edit":
      if kind == "Person":
        person = self.requestToPerson(self.request)
        if modified:
          self.personView(person)
        else:
          self.personForm(person)
      elif kind == "Contact":
        contact = self.requestToContact(self.request)
        if modified:
          person = db.get(contact.key().parent())
          self.personView(person)
        else:
          self.contactForm(contact)
      elif kind == "Address":
        address = self.requestToAddress(self.request)
        if modified:
          person = db.get(address.key().parent())
          self.personView(person)
        else:
          self.addressForm(address)
      elif kind == "Calendar":
        calendar = self.requestToCalendar(self.request)
        if modified:
          person = db.get(calendar.key().parent())
          self.personView(person)
        else:
          self.calendarForm(calendar)
    elif action == "fix":
      query = db.Query(Person)
      for person in query:
        person.updateWords()
        db.put(person)
      self.response.out.write("DONE<br>")

    self.response.out.write("""
          <div class="powered">powered by App Engine</div>
          </body> 
          </html> 
    """)

  def requestToPerson(self, req):
      key = req.get("key")
      if key:
        person = db.get(db.Key(encoded=key))
      else:
        person = Person()
      if not req.get("modified"):
        return person
      for (propname, prop) in Person.properties().iteritems():
        if isinstance(prop, db.BooleanProperty):
          res = propname in req.arguments()
          setattr(person, propname, res)
        elif isinstance(prop, db.StringListProperty):
          setattr(person, propname, [])
        elif isinstance(prop, db.StringProperty) or isinstance(prop, db.TextProperty):
          value = req.get(propname)
          setattr(person, propname, value)
        else:
          self.response.out.write("HMMMM " + propname)
          setattr(person, propname, req.get(propname))
      person.updateWords()
      person.put()
      return person

  def requestToContact(self, req):
      key = req.get("key")
      parent_key = req.get("parent_key")
      if key:
        contact = db.get(db.Key(encoded=key))
      elif parent_key:
        contact = Contact(parent=db.Key(encoded=parent_key))
      else:
        contact = Contact()
      if not req.get("modified"):
        return contact
      for (propname, prop) in Contact.properties().iteritems():
        if isinstance(prop, db.BooleanProperty):
          res = propname in req.arguments()
          setattr(contact, propname, res)
        elif isinstance(prop, db.StringListProperty):
          setattr(contact, propname, [])
        elif isinstance(prop, db.StringProperty) or isinstance(prop, db.TextProperty):
          value = req.get(propname)
          setattr(contact, propname, value)
        else:
          self.response.out.write("HMMMM " + propname)
          setattr(contact, propname, req.get(propname))
      contact.updateWords()
      contact.put()
      return contact

  def requestToAddress(self, req):
      key = req.get("key")
      parent_key = req.get("parent_key")
      if key:
        address = db.get(db.Key(encoded=key))
      elif parent_key:
        address = Address(parent=db.Key(encoded=parent_key))
      else:
        address = Address()
      if not req.get("modified"):
        return address
      for (propname, prop) in Address.properties().iteritems():
        if isinstance(prop, db.BooleanProperty):
          res = propname in req.arguments()
          setattr(address, propname, res)
        elif isinstance(prop, db.StringListProperty):
          setattr(address, propname, [])
        elif isinstance(prop, db.StringProperty) or isinstance(prop, db.TextProperty):
          value = req.get(propname)
          setattr(address, propname, value)
        else:
          self.response.out.write("HMMMM " + propname)
          setattr(address, propname, req.get(propname))
      address.updateWords()
      address.put()
      return address

  def requestToCalendar(self, req):
      key = req.get("key")
      parent_key = req.get("parent_key")
      if key:
        calendar = db.get(db.Key(encoded=key))
      elif parent_key:
        calendar = Calendar(parent=db.Key(encoded=parent_key))
      else:
        calendar = Calendar()
      if not req.get("modified"):
        return calendar
      for (propname, prop) in Calendar.properties().iteritems():
        if isinstance(prop, db.BooleanProperty):
          res = propname in req.arguments()
          setattr(calendar, propname, res)
        elif isinstance(prop, db.StringListProperty):
          setattr(calendar, propname, [])
        elif isinstance(prop, db.StringProperty) or isinstance(prop, db.TextProperty):
          value = req.get(propname)
          setattr(calendar, propname, value)
        elif isinstance(prop, db.DateProperty):
          value = req.get(propname)
          value = datetime.datetime.strptime(value, '%m/%d/%y')
          value = value.date()
          setattr(calendar, propname, value)
        else:
          self.response.out.write("HMMMM " + propname)
          setattr(calendar, propname, req.get(propname))
      calendar.updateWords()
      calendar.put()
      return calendar


  def personView(self, person):
      self.response.out.write("""
          <hr>
          <a href="%s" class="edit-link">Edit</a>
          <span class="thing">%s</span> <span class="tag">(%s) [%s]</span><br>
          <div class="comments">%s</div>
          <div class="indent">
      """ % (person.editUrl(),
             person.displayName(), person.category, person.enabledText(),
             person.comments))

      query = db.Query(Contact)
      query.ancestor(person.key())
      for contact in query:
        self.contactView(contact)

      query = db.Query(Address)
      query.ancestor(person.key())
      for address in query:
        self.addressView(address)

      query = db.Query(Calendar)
      query.ancestor(person.key())
      for calendar in query:
        self.calendarView(calendar)

      self.response.out.write("""
          </div>""") 


  def personForm(self, person):
      self.response.out.write("""
          <hr>
          <form name="personform" method="post" action=".">
          <input type="hidden" name="action" value="edit">
          <input type="hidden" name="kind" value="%s">
          <input type="hidden" name="modified" value="true">
          <input type="hidden" name="key" value="%s">
          <table> 
      """ % (person.kind(), person.maybeKey()))

      props = Person.properties()
      self.formFields(person)
      self.response.out.write("""<tr><td></td><td><input type="submit" name="updated" value="Save Changes" style="margin-top: 1em;"></td></tr>""")
      propname = props.keys()[0]
      self.response.out.write("""
          </table> 
          </form>
          <script>
            document.personform.%s.focus();
          </script>
          <hr>
      """ % propname)
      if person.maybeKey():
        self.response.out.write("""
            <a href="?action=create&kind=Contact&parent_key=%s">[+Contact]</a>
            &nbsp;
            <a href="?action=create&kind=Address&parent_key=%s">[+Address]</a>
            &nbsp;  
            <a href="?action=create&kind=Calendar&parent_key=%s">[+Calendar]</a>
        """ % (person.key(), person.key(), person.key()))


  def addressView(self, address):
      self.response.out.write("""
          <a href="%s" class="edit-link">Edit</a>
          <span class="thing %s">%s</span> <span class="tag">(%s) [%s]</span><br>
          <div class="directions">%s</div>
          <div class="comments">%s</div>
      """ % (address.editUrl(),
             address.kind(), address.snippet(), address.address_type, address.enabledText(),
             address.directions,
             address.comments))

  def addressForm(self, address):
      self.response.out.write("""
          <hr>
          <form name="addressform" method="post" action="."> 
          <input type="hidden" name="action" value="edit">
          <input type="hidden" name="kind" value="%s">
          <input type="hidden" name="modified" value="true">
          <input type="hidden" name="key" value="%s">
          <input type="hidden" name="parent_key" value="%s">
          <table> 
      """ % (address.kind(), address.maybeKey(), self.request.get("parent_key")))

      props = Address.properties()
      self.formFields(address)
      self.response.out.write("""<tr><td></td><td><input type="submit" name="updated" value="Save Changes" style="margin-top: 1em;"></td></tr>""")
      propname = props.keys()[0]
      self.response.out.write("""
          </table> 
          </form>
          <hr> 
      """)


  def contactView(self, contact):
      self.response.out.write("""
          <a href="%s" class="edit-link">Edit</a>
          <span class="thing %s">%s</span> <span class="tag">(%s %s) [%s]</span><br>
          <div class="comments">%s</div>
      """ % (contact.editUrl(),
             contact.kind(), contact.contact_text, contact.contact_method, contact.contact_type, contact.enabledText(),
             contact.comments))

  def contactForm(self, contact):
      self.response.out.write("""
          <hr>
          <form name="contactform" method="post" action="."> 
          <input type="hidden" name="action" value="edit">
          <input type="hidden" name="kind" value="%s">
          <input type="hidden" name="modified" value="true">
          <input type="hidden" name="key" value="%s">
          <input type="hidden" name="parent_key" value="%s">
          <table> 
      """ % (contact.kind(), contact.maybeKey(), self.request.get("parent_key")))

      props = Contact.properties()
      self.formFields(contact)
      self.response.out.write("""<tr><td></td><td><input type="submit" name="updated" value="Save Changes" style="margin-top: 1em;"></td></tr>""")
      propname = props.keys()[0]
      self.response.out.write("""
          </table> 
          </form>
          <hr> 
      """)


  def calendarView(self, calendar):
      self.response.out.write("""
          <a href="%s" class="edit-link">Edit</a>
          <span class="thing %s">%s</span> <span class="tag">(%s %s) [%s]</span><br>
          <div class="comments">%s</div>
      """ % (calendar.editUrl(),
             calendar.kind(), calendar.first_occurrence.strftime("%m/%d/%y"), calendar.frequency, calendar.occasion, calendar.enabledText(),
             calendar.comments))

  def calendarForm(self, calendar):
      self.response.out.write("""
          <hr>
          <form name="calendarform" method="post" action="."> 
          <input type="hidden" name="action" value="edit">
          <input type="hidden" name="kind" value="%s">
          <input type="hidden" name="modified" value="true">
          <input type="hidden" name="key" value="%s">
          <input type="hidden" name="parent_key" value="%s">
          <table> 
      """ % (calendar.kind(), calendar.maybeKey(), self.request.get("parent_key")))

      props = Calendar.properties()
      self.formFields(calendar)
      self.response.out.write("""<tr><td></td><td><input type="submit" name="updated" value="Save Changes" style="margin-top: 1em;"></td></tr>""")
      propname = props.keys()[0]
      self.response.out.write("""
          </table> 
          </form>
          <hr> 
      """)


  def formFields(self, thing):
    for (propname, prop) in thing.props():
      label = prop.verbose_name
      value = getattr(thing, propname)
      if isinstance(prop, SelectableStringProperty):
        values = prop.choices
        html = """<select name="%s" size="%s">""" % (propname, len(values))
        for v in values:
          selected = "selected" if value == v else ""
          html += """<option %s value="%s">%s</option>""" % (selected, v, v)
        html += """</select>"""
      elif isinstance(prop, db.BooleanProperty):
        checked = "checked" if getattr(thing, propname) else ""
        html = """<input type="checkbox" name="%s" %s> %s""" % (propname, checked, label)
        label = ""
      elif isinstance(prop, db.TextProperty):
        html = """<textarea name="%s" style="width: 50em; height: 20em; font-family: monospace;">%s</textarea>""" % (propname, value)
      elif isinstance(prop, db.StringProperty):
        html = """<input type="text" style="width: 50em;" name="%s" value="%s">""" % (propname, value)
      elif isinstance(prop, db.StringListProperty):
        #html = """<textarea name="%s" style="width: 50em; height: 4em; color: gray;">%s</textarea>""" % (propname, ", ".join(value))
        html = """<code style="color:#ddd;">%s</code>""" % " ".join(value)
      elif isinstance(prop, db.DateProperty):
        if value:
          value = value.strftime("%m/%d/%y")
        else:
          value = ""
        html = """<input type="text" style="width: 8em;" name="%s" value="%s">""" % (propname, value)
      else:
        html = """<span style="color:red;">** Unknown property type '%s' for '%s' **</span>""" % (prop.__class__.__name__, propname)
      if label == "words":
        color = "color: #ccc;"
      else:
        color = "color: blue;"
      self.response.out.write("""<tr><td style="vertical-align: top; text-align: right; %s">%s</td><td>%s</td></tr>""" % (color, label, html))


class SelectableStringProperty(db.StringProperty):
  pass

class Thing(db.Model):
  comments = db.TextProperty(verbose_name="Comments", default="")
  enabled = db.BooleanProperty(verbose_name="Enabled", required=True, default=True)
  words = db.StringListProperty(verbose_name="words", default=[])
  propnames = [
              "category",
              "send_card",
              "title",
              "mailing_name",
              "first_name",
              "last_name",
              "company_name",
              
              "address_type",
              "address_line1",
              "address_line2",
              "city",
              "state_province",
              "postal_code",
              "country",
              "directions",
              
              "contact_method",
              "contact_type",
              "contact_text",
              
              "first_occurrence",
              "frequency",
              "occasion",
                              
              "comments",
              "enabled",
              "words",
               ];

  def props(self):
    for propname in self.propnames:
      if propname in self.properties().keys():
        yield (propname, self.properties()[propname])
    
  def __str__(self):
    return "%s(key=%s)" % (self.kind(), self.key())

  def maybeKey(self):
    if self.is_saved():
      return self.key()
    else:
      return ""

  def updateWords(self):
    words = []
    for (propname, prop) in self.properties().iteritems():
      if isinstance(prop, SelectableStringProperty):
        continue
      if isinstance(prop, (db.StringProperty, db.TextProperty)):
        value = getattr(self, propname)
        words.extend(re.split('\W+', value.lower()))
    words = list(set(words))
    if '' in words:
      words.remove('')
    setattr(self, "words", words)

  def enabledText(self):
    if self.enabled:
      return "enabled"
    else:
      return "disabled"

  def editUrl(self):
    return "?action=edit&kind=%s&key=%s" % (self.kind(), self.key())
    
class Person(Thing):
  mailing_name = db.StringProperty(verbose_name="Mailing Name", default="")
  title = db.StringProperty(verbose_name="Title", default="")
  first_name = db.StringProperty(verbose_name="First Name", default="")
  last_name = db.StringProperty(verbose_name="Last Name", default="")
  company_name = db.StringProperty(verbose_name="Company Name", default="")
  category = SelectableStringProperty(verbose_name="Category", default="",
    choices=[
      "(Unspecified)",
      "Relatives",
      "Personal",
      "Hotel/Restaurant/Entertainment",
      "Services by Individuals",
      "Companies, Institutions, etc.",
      "Business Relations"
    ])
  send_card = db.BooleanProperty(verbose_name="Send Card", default=False, required=True)

  def displayName(self):
    t = ""
    if self.mailing_name:
      t += "[%s] " % self.mailing_name
    if self.company_name:
      t += "%s " % self.company_name
    if self.title:
      t += self.title + " "
    if self.first_name:
      t += self.first_name + " "
    if self.last_name:
      t += self.last_name
    return t
  

class Address(Thing):
  address_line1 = db.StringProperty(verbose_name="Address Line 1", default="")
  address_line2 = db.StringProperty(verbose_name="Address Line 2", default="")
  address_type = SelectableStringProperty(verbose_name="Address Type", default="",
    choices=[
             "(Unspecified)",
             "Home",
             "Business",
    ])
  city = db.StringProperty(verbose_name="City", default="")
  country = db.StringProperty(verbose_name="Country", default="")
  directions = db.TextProperty(verbose_name="Directions", default="")
  postal_code = db.StringProperty(verbose_name="Postal Code", default="")
  state_province = db.StringProperty(verbose_name="State/Province", default="")

  def snippet(self):
    return " / ".join([self.address_line1, self.address_line2, self.postal_code, self.city, self.state_province, self.country]).replace("/  /", "/")


class Contact(Thing):
  contact_text = db.StringProperty(verbose_name="Contact Text", default="")
  contact_method = SelectableStringProperty(verbose_name="contact_method", default="",
    choices=[
      "(Unspecified)",
      "Personal",
      "Business",
    ])
  contact_type = SelectableStringProperty(verbose_name="Contact Type", default="",
    choices=[
      "(Unspecified)",
      "Voice",
      "Data",
      "Email",
      "Mobile",
      "URL",
      "Facsimile",
    ])


class Calendar(Thing):
  first_occurrence = db.DateProperty(verbose_name="First Occurrence")
  frequency = SelectableStringProperty(verbose_name="Frequency", default="",
    choices=[
      "Annual",
    ])
  occasion = db.StringProperty(verbose_name="Occasion", default="")


def migrate(person):
  person.updateWords()

  query = db.Query(Address)
  query.ancestor(person)
  for address in query.run():
    address.updateWords()
    yield op.db.Put(address)

  query = db.Query(Contact)
  query.ancestor(person)
  for contact in query.run():
    contact.updateWords()
    yield op.db.Put(contact)

  query = db.Query(Calendar)
  query.ancestor(person)
  for calendar in query.run():
    if (calendar.first_occurrence.year < 1900):
      calendar.first_occurrence = calendar.first_occurrence.replace(year=1900)
    calendar.updateWords()
    yield op.db.Put(calendar)

  yield op.db.Put(person)


def main():
  application = webapp.WSGIApplication([('/', MainHandler)],
                                       debug=True)
  util.run_wsgi_app(application)


if __name__ == '__main__':
  main()
