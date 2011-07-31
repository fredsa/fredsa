#!/usr/bin/env python
#
import logging
import pprint
import re

from google.appengine.ext import webapp
from google.appengine.ext.webapp import util
from google.appengine.ext import db
from google.appengine.api import users
from mapreduce import operation as op

class MainHandler(webapp.RequestHandler):
  def get(self):
    self.response.out.write("""
          <html> 
          <head> 
            <title>PDA</title> 
            <link rel="stylesheet" type="text/css" href="main.css"/> 
          </head> 
          <body class="pda">
          <h1>App Engine PDA2</h1> 
          <form name="searchform" method="get"> 
          <!--
          <input type="checkbox" name="includedisabled" > Include Disabled Entries<br> 
          <br> 
          <input type="radio" name="format" checked value="verbose"> Verbose (regular) results<br> 
          <input type="radio" name="format"  value="compact"> Compact results<br> 
          -->
          Search text: <input type="text" name="q" value="%s"> <input type="submit" value="Go"><br> 
          </form> 
          <script>document.searchform.q.focus(); document.searchform.q.select();</script> 
          
          <hr> 
          [<a href=".?action=create_person">+Person</a>] 
          [<a href="_ah/admin">Admin</a>] 
          <!--
          <a href=".?mailing_list=preview">[Mailing Labels Preview]</a> 
          -->
    """ % (self.request.get("q")))

    q = self.request.get("q")
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
        #self.response.out.write("results = %s<br><br>" % results)

      keys = list(results)
      while (keys):
        # Max 30 keys allow in IN clause
        somekeys = keys[:30]
        keys = keys[30:]
        self.response.out.write("somekeys = %s<br><br>" % somekeys)
        query = db.Query(Person)
        query.filter("__key__ IN", somekeys)
        s = set(query)
        self.response.out.write("s = %s<br><br>" % s)
        for person in s:
          #self.response.out.write("person = %s<br><br>" % person)
          self.personForm(person)
    elif self.request.get("action") == "create_person":
      person = Person()
      self.personForm(person)
    elif self.request.get("action") == "person":
      person = self.requestToPerson(self.request)
      self.personForm(person)

    self.response.out.write("""
          </body> 
          </html> 
    """)

  def requestToPerson(self, req):
      key = req.get("key")
      if key:
        person = Person(key=db.Key(encoded=key))
      else:
        person = Person()
      props = Person.properties()
      for propname in props:
        prop = props[propname]
        if isinstance(prop, db.BooleanProperty):
          res = propname in req.arguments()
          setattr(person, propname, res)
        elif isinstance(prop, db.StringListProperty):
          setattr(person, propname, [])
        elif isinstance(prop, db.StringProperty):
          value = req.get(propname)
          setattr(person, propname, value)
        else:
          self.response.out.write("HMMMM " + propname)
          setattr(person, propname, req.get(propname))
      person.updateWords()
      if person.first_name or person.last_name or person.mailing_name:
        person.put()
      return person


  def personForm(self, person):
      self.response.out.write("""
          <hr>
          <form name="personform" method="get" action="."> 
          <input type="hidden" name="action" value="person"> 
          <input type="text" name="key" value="%s"> 
          <table> 
      """ % person.maybeKey())

      props = Person.properties()
      self.formFields(person, props)
      self.response.out.write("""<tr><td></td><td><input type="submit" name="updated" value="Save Changes" style="margin-top: 1em;"></td></tr>""")
      propname = props.keys()[0]
      self.response.out.write("""
          </table> 
          </form>
          <script>
           // document.personform.%s.focus();
          </script>
          <hr> 
      """ % propname)
      query = db.Query(Contact)
      query.ancestor(person.key())
      for contact in query:
        self.contactForm(contact)


  def contactForm(self, contact):
      self.response.out.write("""
          <hr>
          <form name="contact" method="get" action="."> 
          <input type="hidden" name="action" value="contact"> 
          <input type="text" name="key" value="%s"> 
          <table> 
      """ % contact.maybeKey())

      props = Contact.properties()
      self.formFields(contact, props)
      self.response.out.write("""<tr><td></td><td><input type="submit" name="updated" value="Save Changes" style="margin-top: 1em;"></td></tr>""")
      propname = props.keys()[0]
      self.response.out.write("""
          </table> 
          </form>
          <hr> 
      """)


  def formFields(self, thing, props):
    for propname in props:
      prop = props[propname]
      label = props[propname].verbose_name
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
        html = """<textarea name="%s" style="width: 50em; height: 4em; color: gray;">%s</textarea>""" % (propname, ", ".join(value))
      else:
        html = """<span style="color:red;">** Unknown property type '%s' for '%s' **</span>""" % (prop.__class__.__name__, propname)
      self.response.out.write("""<tr style="color:blue;"><td style="vertical-align: top; text-align: right;">%s</td><td>%s</td></tr>""" % (label, html))


class SelectableStringProperty(db.StringProperty):
  pass

class Thing(db.Model):
  comments = db.TextProperty(verbose_name="Comments", default="")
  enabled = db.BooleanProperty(verbose_name="Enabled", required=True, default=True)
  words = db.StringListProperty(verbose_name="words", default=[])

  def maybeKey(self):
    if self.is_saved():
      return self.key()
    else:
      return ""

  def updateWords(self, props):
    words = []
    for propname in props:
      prop = props[propname]
      if isinstance(prop, SelectableStringProperty):
        continue
      if isinstance(prop, db.StringProperty) or isinstance(prop, db.TextProperty):
        value = getattr(self, propname)
        words.extend(re.split('\W+', value.lower()))
    words = list(set(words))
    if '' in words:
      words.remove('')
    setattr(self, "words", words)


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

  def updateWords(self):
    Thing.updateWords(self, Person.properties())


class Address(Thing):
  address_line1 = db.StringProperty(verbose_name="Address Line 1", default="")
  address_line2 = db.StringProperty(verbose_name="Address Line 2", default="")
  address_type = SelectableStringProperty(verbose_name="Address Type", default="",
    choices=[
    ])
  city = db.StringProperty(verbose_name="City", default="")
  country = db.StringProperty(verbose_name="Country", default="")
  directions = db.TextProperty(verbose_name="Directions", default="")
  postal_code = db.StringProperty(verbose_name="Postal Code", default="")
  state_province = db.StringProperty(verbose_name="State/Province", default="")

  def updateWords(self):
    Thing.updateWords(self, Address.properties())


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

  def updateWords(self):
    Thing.updateWords(self, Contact.properties())

class Calendar(Thing):
  first_occurence = db.DateProperty()
  frequency = SelectableStringProperty(verbose_name="Frequency", default="",
    choices=[
      "Annual",
    ])
  occasion = db.StringProperty(verbose_name="Occasion", default="")

  def updateWords(self):
    Thing.updateWords(self, Calendar.properties())


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
   if (calendar.first_occurence.year < 1900):
     calendar.first_occurence = calendar.first_occurence.replace(year=1900)
   calendar.updateWords()
   yield op.db.Put(calendar)

 yield op.db.Put(person)


def main():
  application = webapp.WSGIApplication([('/', MainHandler)],
                                       debug=True)
  util.run_wsgi_app(application)


if __name__ == '__main__':
  main()
