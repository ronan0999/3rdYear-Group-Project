import flask, pymongo
from flask import Flask, render_template, request, redirect, url_for, session
import bcrypt


app = Flask(__name__)

# myclient = pymongo.MongoClient("mongodb://localhost:27017/")  # connection url
# print("connected to mongo")
# mydb = myclient['medicare'] # connection to db
# print("connected to db")


@app.route("/", methods=["GET", "POST"])
def root():
    print("In root")
    if 'username' in session:
        return 'You are logged in as ' + session['username']
    return render_template('home.html')
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

@app.route("/login", methods=["POST"])
def login():
    print("login")
    # accounts = mydb['accounts']
    # loginUser = accounts.find_one({'name': request.form['email']})
    return render_template('login.html')

@app.route("/register", methods=["GET", "POST"])
def register():
    print("register")
    if request.method == 'POST':
        accounts = mydb['accounts']
        existingUser = accounts.find_one({'email': request.form['email']})
        print(existingUser)
        if existingUser is None:
            print("creating")
            professionals = mydb['professionals']
            hashpass = bcrypt.hashpw(request.form['password'].encode('utf-8'), bcrypt.gensalt())
            accounts.insert_one({
                'email': request.form['email'],
                'password': hashpass
            })

            professionals.insert_one({
                'type': request.form['profession'],
                'name': request.form['firstName'] + " " + request.form['lastName'],
                'email': request.form['email'],
                'phone': request.form['phone']
            })
            session['username'] = request.form['email']
            return redirect(url_for('root'))
        return 'That username already exists'

    return render_template("register.html")

def checkAccount(email, password):
    accountCollection = mydb['accounts']

    data = accountCollection.find({})

    for doc in data:
        if (email == doc['email']) and (password == doc['password']):
            return True


if __name__ == '__main__':
    app.secret_key = 'mysecret'
    app.run(debug=True)