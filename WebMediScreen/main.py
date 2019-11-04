import flask, pymongo, os, bcrypt, uuid
from flask import Flask, render_template, request, redirect, url_for, session, g
import pyrebase

app = Flask(__name__)
app.secret_key = os.urandom(24)

# myclient = pymongo.MongoClient("mongodb://localhost:27017/")  # connection url
# print("connected to mongo")
# mydb = myclient['medicare'] # connection to db
# print("connected to db")

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


gpId = 0

@app.route("/", methods=["GET", "POST"])
def root():
    print("In root")
    if 'username' in session:
        # return session['username']
        return redirect(url_for('home', email=session['username']))
    if request.method == 'POST':
        session.pop('username', None)
        # accounts = mydb['accounts']
        accounts = db.child("professionalAccounts").get()
        # loginUser = accounts.find_one({'email': request.form['email']})

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
            print(lp)
            p = request.form['password'].encode('utf-8')
            print(bcrypt.hashpw(p, lp))
            if bcrypt.hashpw(p, lp) == lp:
                session['username'] = request.form['email']
                print("match2")
                print(session['username'])
                # return redirect(url_for('root'))
                return redirect(url_for('home', email=request.form['email']))
    return redirect(url_for('login'))

    # if 'username' in session:
    #     return 'You are logged in as ' + session['username']
    # return render_template('home.html')
    # if request.method == 'POST':
    #     email = request.form.get('email')
    #     password = request.form.get('password')
    #
    #     if checkAccount(email, password):
    #         print("EXISTS")
    #         return redirect(url_for('root'))
    #     else:
    #         print("NOT EXISTS")
    #         return redirect(url_for('login'))
    # else:
    #     return render_template('home.html')

@app.route("/home/<email>")
def home(email):
    print("in home")
    return render_template("home.html", email=email)

@app.route("/login", methods=["GET", "POST"])
def login():
    print("login")
    # if request.method == "POST":
    #     accounts = mydb['accounts']
    #     loginUser = accounts.find_one({'email': request.form['email']})
    #
    #     if loginUser:
    #         print("match1")
    #         if bcrypt.hashpw(request.form['password'].encode('utf-8'), loginUser['password']) == loginUser['password']:
    #             session['username'] = request.form['email']
    #             print("match2")
    #             # return redirect(url_for('root'))
    #             return "You are logged in as " + request.form['email']
    #     return "invalid email or password"
    return render_template('login.html')

@app.route("/register", methods=["GET", "POST"])
def register():
    print("register")
    if request.method == 'POST':
        # accounts = mydb['accounts']
        accounts = db.child("professionalAccounts").get()
        # existingUser = accounts.find_one({'email': request.form['email']})
        existingUser = None
        for i in accounts.each():
            if i.val()['email'] == request.form['email']:
                existingUser = request.form['email']
                break
        print(existingUser)
        if existingUser is None:
            print("creating")
            # professionals = mydb['professionals']
            hashpass = bcrypt.hashpw(request.form['password'].encode('utf-8'), bcrypt.gensalt())
            print(hashpass)
            print(str(hashpass))
            # accounts.insert_one({
            #     'email': request.form['email'],
            #     'password': hashpass
            # })
            accountDetails = {
                'email': request.form['email'],
                'password': str(hashpass)
            }

            db.child("professionalAccounts").push(accountDetails)

            # USE FUNCTIONS TO ADD EITHER GP OR INSURANCE   <====
            # professionals.insert_one({
            #     'type': request.form['profession'],
            #     'gp' : str(uuid.uuid4())[:8],
            #     'name': request.form['firstName'] + " " + request.form['lastName'],
            #     'email': request.form['email'],
            #     'phone': request.form['phone']
            # })

            proDetail = {
                'type': request.form['profession'],
                'gpId' : str(uuid.uuid4())[:8],
                'name': request.form['firstName'] + " " + request.form['lastName'],
                'email': request.form['email'],
                'phone': request.form['phone']
            }
            db.child("professionals").push(proDetail)
            session['username'] = request.form['email']
            return redirect(url_for('root'))
        return 'That username already exists'

    return render_template("register.html")

@app.route("/checkPatients/<email>")
def checkPatients(email):
    print("IN CHECK PATIENT")
    # GETTING CURRENT PRO
    proData = db.child('professionals').get()

    currentPro = None
    for pro in proData.each():
        if pro.val()['email'] == email:
            currentPro = pro.val()
            break
    # print(currentPro)

    # GETTING LIST OF PATIENT FOR CURRENT PRO
    patientData = db.child('patients').get()
    patientList = []
    for patient in patientData.each():
        print(patient.val())
        if patient.val()['gpname'] == currentPro['name']:
            # patientList.append(patient.val())
            patientList.append(patient.val())
    print(patientList)
    return render_template('check_patient.html', data=patientList)
    

@app.route("/patientDetails/<patientId>")
def displayPatient(patientId):
    patientData = db.child("patients").get()
    selectedPatientData = None
    for patient in patientData.each():
        if patient.val()['pID'] == patientId:
            selectedPatientData = patient.val()
            break

    return render_template("patientDetails.html", patientData=selectedPatientData)


@app.route("/logout")
def logout():
    session.pop('username', None)
    g.user = None
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