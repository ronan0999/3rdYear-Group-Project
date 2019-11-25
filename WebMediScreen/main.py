# Author: Ronan Roche

import flask, pymongo, os, bcrypt, uuid
from flask import Flask, render_template, request, redirect, url_for, session, g, flash
from flask_login import LoginManager, login_user, login_required, UserMixin, logout_user, current_user
import pyrebase

app = Flask(__name__)
app.secret_key = os.urandom(24)

def noquote(s):
    return s
pyrebase.pyrebase.quote = noquote

config = {
    "apiKey": "AIzaSyBCh-kDeYZm1BhGuS1PbGXAj7985Ldt2Nw",
    "authDomain": "mediscreen-c7469.firebaseapp.com",
    "databaseURL": "https://mediscreen-c7469.firebaseio.com",
    "storageBucket": "mediscreen-c7469.appspot.com",
    "serviceAccount": "serviceAccountCredentials.json"
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()

loginManager = LoginManager()
loginManager.init_app(app)
loginManager.login_view = "login"
loginManager.login_message = "Please log in to access this page"

gpId = 0

class User(UserMixin):
    def __init__(self, email):
        self.email = email

    def get_id(self):
        return self.email
    # self.name = name

@app.route("/", methods=["GET", "POST"])
def root():
    print("In root")
    # error = []
    if 'username' in session:
        # return session['username']
        return redirect(url_for('home', email=session['username']))

    if request.method == 'POST':
        session.pop('username', None)
        accounts = db.child("professionalAccounts").get()

        loginUser = None
        for i in accounts.each():
            if i.val()['email'] == request.form['email']:
                loginUser = i.val()
                break

        if loginUser:
            print("match1")
            # print(bytes(loginUser['password']).encode('utf-8'))
            # if bcrypt.hashpw(request.form['password'].encode('utf-8'), loginUser['password']) == loginUser['password'].encode('utf-8'):
            # if bcrypt.checkpw(request.form['password'], bytes(loginUser['password'])):
            # if bcrypt.hashpw(request.form['password'], bytes(loginUser['password'])) == bytes(loginUser['password']):
            lp = bytes(loginUser['password'][2:-1].encode('utf-8'))
            p = request.form['password'].encode('utf-8')
            if bcrypt.hashpw(p, lp) == lp:
                user = User(loginUser['email'])
                login_user(user)
                session['username'] = request.form['email']
                print("match2")

                return redirect(url_for('home', email=request.form['email']))
            else:
                # error.append("An error has occured. Your email or password is incorrect")
                flash("An error has occured. Your email or password is incorrect")
        else:
            # error.append("An error has occured. Your email or password is incorrect")
            flash("An error has occured. Your email or password is incorrect")
    return redirect(url_for('login'))

@app.route("/home/<email>")
@login_required
def home(email):
    print("in home")
    return render_template("home.html", email=email)

@app.route("/login", methods=["GET", "POST"])
def login():
    print("login")
    return render_template('login.html')

@loginManager.user_loader
def load_user(email):
    proAccounts = db.child("professionalAccounts").get()
    pro = None
    for pro in proAccounts.each():
        if pro.val()['email'] == email:
            pro = pro.val()
    if pro == None:
        return None
    return User(email=pro['email'])

@app.route("/register", methods=["GET", "POST"])
def register():
    type = None
    print("register")
    if request.method == 'POST':
        if 'sendType' in request.form:
            type = request.form['profession']
            return render_template('register.html', type=type)
        else:
            # accounts = db.child("professionalAccounts").get()
            # existingUser = None
            # for i in accounts.each():
            #     if i.val()['email'] == request.form['email']:
            #         existingUser = request.form['email']
            #         break
            # ok = checkInputs(request.form)
            if checkUserExists(request.form) and checkInputs(request.form):
                # print("----------------------------------->> ", ok)
                print("creating")
                hashpass = bcrypt.hashpw(request.form['password'].encode('utf-8'), bcrypt.gensalt())
                accountDetails = {
                    'email': request.form['email'],
                    'password': str(hashpass)
                }

                db.child("professionalAccounts").push(accountDetails)

                if type == 'GP':
                    proDetail = {
                        'type': "GP",
                        # 'gpId' : str(uuid.uuid4())[:8],
                        'gpId': request.form['gpId'],
                        'name': request.form['firstName'] + " " + request.form['lastName'],
                        'email': request.form['email'],
                        'phone': request.form['phone']
                    }
                else:
                    proDetail = {
                        'type': "Insurance",
                        # 'gpId' : str(uuid.uuid4())[:8],
                        'insuranceName': request.form['insuranceName'],
                        'name': request.form['firstName'] + " " + request.form['lastName'],
                        'email': request.form['email'],
                        'phone': request.form['phone']
                    }
                db.child("professionals").push(proDetail)
                # session['username'] = request.form['email']
                return redirect(url_for('root'))
            # return 'That user already exists'
            # flash("This user already exists")
            return redirect(url_for("register"))
    else:
        if type == None:
            return render_template("register.html")
        else:
            return render_template("register.html", type=type)


def checkInputs(form):
    ok = True

    if not form["firstName"].isalpha():
        ok = False
        flash("Please enter letters for your first name")
    elif not form["lastName"].isalpha():
        ok = False
        flash("Please enter letters for your last name")
    elif not form["phone"].isnumeric():
        ok = False
        flash("Please enter numbers for your phone number")

    return ok

def checkUserExists(form):
    ok = True

    accounts = db.child("professionalAccounts").get()
    for i in accounts.each():
        if i.val()['email'] == form['email']:
            # existingUser = request.form['email']
            flash("This user already exists")
            ok = False
            break

    return ok

@app.route("/checkPatients/<email>", methods=['GET', 'POST'])
@login_required
def checkPatients(email):
    print("IN CHECK PATIENT")
    # GETTING CURRENT PRO
    # if request.method == 'POST':
    #     patientId = request.form['pId']
    #     patientData = db.child('patients').get()
    #
    #     newPatient = None
    #     for patient in patientData.each():
    #         if patient.val()['pID'] == patientId:
    #             newPatient = patient.val()
    #             break
    #     return redirect(url_for('checkPatients', email=email))
    # else:
    proData = db.child('professionals').get()

    currentPro = None
    for pro in proData.each():
        if pro.val()['email'] == email:
            currentPro = pro.val()
            break

    # GETTING LIST OF PATIENT FOR CURRENT PRO
    patientData = db.child('patients').get()
    patientList = []
    for patient in patientData.each():
        print(patient.val())
        if patient.val()['gpId'] == currentPro['gpId']:
            # patientList.append(patient.val())
            patientList.append(patient.val())
    print(patientList)
    return render_template('check_patient.html', data=patientList, email=email)


@app.route("/patientDetails/<patientId>")
@login_required
def displayPatient(patientId):
    patientData = db.child("patients").get()
    selectedPatientData = None
    for patient in patientData.each():
        if patient.val()['pID'] == patientId:
            selectedPatientData = patient.val()
            break

    return render_template("patientDetails.html", patientData=selectedPatientData)


@app.route("/logout")
@login_required
def logout():
    session.pop('username', None)
    g.user = None
    logout_user()
    return redirect(url_for("root"))

@app.before_request
def before_request():
    g.user = None
    if 'username' in session:
        g.user = session['username']


# def checkAccount(email, password):
    # accountCollection = mydb['accounts']

    # data = accountCollection.find({})

    # for doc in data:
    #     if (email == doc['email']) and (password == doc['password']):
    #         return True


if __name__ == '__main__':
    app.secret_key = 'mysecret'
    app.run(debug=True)