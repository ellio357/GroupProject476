# Author: Prof. MM Ghassemi <ghassem3@msu.edu>
from flask import current_app as app
from flask import render_template, redirect, request, session, url_for, copy_current_request_context
from flask_socketio import SocketIO, emit, join_room, leave_room, close_room, rooms, disconnect
from .utils.database.database  import database
from werkzeug.datastructures   import ImmutableMultiDict
from pprint import pprint
import json
import random
import functools
from . import socketio
db = database()


#######################################################################################
# AUTHENTICATION RELATED
#######################################################################################
def login_required(func):
    @functools.wraps(func)
    def secure_function(*args, **kwargs):
        if "email" not in session:
            return redirect(url_for("login", next=request.url))
        return func(*args, **kwargs)
    return secure_function

def getUser():
	return session['email'] if 'email' in session else 'Unknown'

@app.route('/login')
def login():
    session.pop('email', default=None)
    return render_template('login.html', user="Unknown")

@app.route('/logout')
def logout():
	session.pop('email', default=None)
	return redirect('/')

@app.route('/processlogin', methods=['POST'])
def process_login():
    email = request.form.get('email')
    password = request.form.get('password')
    new_user = request.form.get('new_user') == 'true' 

    if new_user:
        result = db.createUser(email=email, password=password)
        if result.get('success') == 0:
            return json.dumps({'success': 0, 'message': result.get('message', 'Failed to create user')})
    else:
        encrypted = db.onewayEncrypt(password)
        user = db.query("SELECT * FROM users WHERE email = %s AND password = %s", (email, encrypted))

        if not user:
            return json.dumps({'success': 0, 'message': 'Invalid email or password'})
    
    session['email'] = email
    return json.dumps({'success': 1})



#######################################################################################
# OTHER
#######################################################################################
@app.route('/')
def root():
	return redirect('/home')

@app.route('/home')
def home():
	print(db.query('SELECT * FROM users'))
	return render_template('home.html', user=getUser())

@app.route('/create_event', methods=['GET', 'POST'])
@login_required
def create_event():
    user_email = getUser()

    if request.method == 'POST':
        form = request.form
        name = form.get('name')
        start_date = form.get('start_date')
        end_date = form.get('end_date')
        start_time = form.get('start_time')
        end_time = form.get('end_time')
        invitees_raw = form.get('invitees', '')
        invitees = [email.strip() for email in invitees_raw.split(',') if email.strip()]


    return render_template('create_event.html', user=user_email)


@app.route('/join_event')
@login_required
def join_event():
    user_email = getUser()
    return render_template('join_event.html', user=user_email)

@app.route('/events')
@login_required
def events():
    user_email = getUser()
    user = user_email if user_email else 'Unknown'
    user_events = db.get_user_events(user_email)
    return render_template('events.html', user=user)


@app.after_request
def add_header(r):
    r.headers["Cache-Control"] = "no-cache, no-store, must-revalidate, public, max-age=0"
    r.headers["Pragma"] = "no-cache"
    r.headers["Expires"] = "0"
    return r

@app.route('/users')
def show_users():
    users = db.query("SELECT email FROM users")
    return render_template('users.html', users=users)


