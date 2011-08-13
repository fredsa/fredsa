#!/usr/bin/env python
#
import logging
import pprint
import re

from google.appengine.ext import webapp
from google.appengine.ext.webapp import util
from google.appengine.ext import db
from google.appengine.api import users
#from mapreduce import operation as op

class MainHandler(webapp.RequestHandler):
  def get(self):
    self.response.out.write("""
          <!DOCTYPE html>
          <html>
          <head>
            <title>PDA</title>
            <link rel="stylesheet" type="text/css" href="main.css"/>
            <style type="text/css">
              body {
                line-height: 1.3em;
              }
              .comments {
                font-family: monospace;
                color: #c44;
                white-space: pre;
                padding-bottom: 2em;
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
              .title {
                text-decoration: none;
                font-size: 2em;
                font-weight: bold;
                padding: 0.2em 0em 0.5em;
                display: block;
                color: black;
              }
            </style>
          </head>
          <body class="pda">
          <a href="/" class="title">App Engine PDA2</a>
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
          [<a href="_ah/admin">Admin</a>] 
          [<a href=".?action=fix">MAP-OVER-PERSON</a>]
          <!--
          <a href=".?mailing_list=preview">[Mailing Labels Preview]</a> 
          -->
          <br>
          <br>
    """ % (self.request.get("q")))
    
    q = self.request.get("q")
    action = self.request.get("action")
    kind = self.request.get("kind")
    modified = self.request.get("modified")
    if q:
      self.response.out.write("""
            <script>document.searchform.q.focus(); document.searchform.q.select();</script>
      """)
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
    elif action == "fix":
      query = db.Query(Person)
      for person in query:
        person.updateWords()
        db.put(person)
      self.response.out.write("DONE<br>")

    self.response.out.write("""
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
      props = Person.properties()
      for propname in props:
        prop = props[propname]
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
      if key:
        contact = db.get(db.Key(encoded=key))
      else:
        contact = Contact()
      if not req.get("modified"):
        return contact
      props = Contact.properties()
      for propname in props:
        prop = props[propname]
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
      self.response.out.write("""
          </div>""") 


  def personForm(self, person):
      self.response.out.write("""
          <hr>
          <form name="personform" method="get" action=".">
          <input type="hidden" name="action" value="edit">
          <input type="hidden" name="kind" value="%s">
          <input type="hidden" name="modified" value="true">
          <input type="hidden" name="key" value="%s">
          <table> 
      """ % (person.kind(), person.maybeKey()))

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


  def contactView(self, contact):
      self.response.out.write("""
          <a href="%s" class="edit-link">Edit</a>
          <span class="thing">%s</span> <span class="tag">(%s %s) [%s]</span><br>
          <div class="comments">%s</div>
      """ % (contact.editUrl(),
             contact.contact_text, contact.contact_method, contact.contact_type, contact.enabledText(),
             contact.comments))

  def contactForm(self, contact):
      self.response.out.write("""
          <hr>
          <form name="contactform" method="get" action="."> 
          <input type="hidden" name="action" value="edit">
          <input type="hidden" name="kind" value="%s">
          <input type="hidden" name="modified" value="true">
          <input type="hidden" name="key" value="%s">
          <table> 
      """ % (contact.kind(), contact.maybeKey()))

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
        #html = """<textarea name="%s" style="width: 50em; height: 4em; color: gray;">%s</textarea>""" % (propname, ", ".join(value))
        html = """<code style="color:#ddd;">%s</code>""" % " ".join(value)
      else:
        html = """<span style="color:red;">** Unknown property type '%s' for '%s' **</span>""" % (prop.__class__.__name__, propname)
      self.response.out.write("""<tr style="color:blue;"><td style="vertical-align: top; text-align: right;">%s</td><td>%s</td></tr>""" % (label, html))


class SelectableStringProperty(db.StringProperty):
  pass

class Thing(db.Model):
  comments = db.TextProperty(verbose_name="Comments", default="")
  enabled = db.BooleanProperty(verbose_name="Enabled", required=True, default=True)
  words = db.StringListProperty(verbose_name="words", default=[])
  
  def __str__(self):
    return "%s(key=%s)" % (self.kind(), self.key())

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

  def updateWords(self):
    Thing.updateWords(self, Person.properties())
    
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
